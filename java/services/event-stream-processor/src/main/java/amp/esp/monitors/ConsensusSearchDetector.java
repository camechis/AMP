package amp.esp.monitors;

import amp.esp.EnvelopeUtils;
import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.InferredEventList;
import amp.esp.WEUtils;
import amp.esp.publish.Publisher;
import cmf.bus.Envelope;

import java.util.Collection;
import java.util.HashSet;

import com.espertech.esper.client.EventBean;

public class ConsensusSearchDetector extends EventMonitor {

//    private static final String SEARCH = "Search";
    private static final String SEARCH = "pegasus.core.search.event.TextSearchEvent";
    public static final String INFERRED_TYPE = "ConsensusSearchEvent";
    private static final String TERM_KEY = "term";
    private static final String USER_KEY = "user";
    private static final String FREQ_KEY = "frequency";

    /**
     * Catch a search event and split it into multiple search term events. This normalizes case to lower-case and removes stop words.
     *
     * e.g. when there's a event like Search("JackRyan", "Europe Satellite Imagery For Sunday") this class will generate the events SearchTerm("JackRyan", 'europe') SearchTerm("JackRyan", 'satellite')
     * SearchTerm("JackRyan", 'imagery') SearchTerm("JackRyan", 'sunday')
     *
     * @author israel
     *
     */
    class TermSplitter extends EventMonitor {

//        private EventMatcher matcher = EventMatcher.selectEnvelope("search").matching("EventType", SEARCH);
        private EventMatcher matcher = EventMatcher.everyEnvelope("search").matching("EventType", SEARCH);

        private String fieldToSplit = "search";
        private String inferredType = "Search Term";

        public TermSplitter() {
            super();
        }

        @Override
        public InferredEventList receive(EventBean eventBean) {
            Envelope env = getEnvelopeFromBean(eventBean, fieldToSplit);
            InferredEventList events = new InferredEventList();
            String userid = WEUtils.getReplyTo(env);
            String topic = WEUtils.getTopic(env);
            Iterable<String> searchTerms = EnvelopeUtils.getSearchTerms(topic);
            for (String term : searchTerms) {
                InferredEvent event = makeInferredEvent();
                event.putData(USER_KEY, userid).putData(TERM_KEY, term);
                events.addInferredEvent(event);
            }
            return events;
        }


        @Override
        public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {
            esp.monitor(matcher, this);

            // @todo = this needs to be integrated
            return new HashSet<Publisher>();
        }

        @Override
        public String getInferredType() {
            return inferredType;
        }
    }

    private int minFreq;
    private String timeLimit;

    public ConsensusSearchDetector(int minFreq, String timeLimit) {
        super();
        this.minFreq = minFreq;
        this.timeLimit = timeLimit;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        InferredEvent event = makeInferredEvent();
        String term = (String) eventBean.get("term");
        Long freq = (Long) eventBean.get("freq");
        event.putData(FREQ_KEY, "" + freq).putData(TERM_KEY, term);
        return event;
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {

        // To calculate search term frequency, we need to do the following:
        //
        // 1. split the search string into individual terms and add them individually
        // into the event stream (the TermSplitter class does this).
        // 2. extract the term and user strings from the search so that they can be aggregated
        // and filtered.
        // 3. calculate a frequency count aggregating by the term.
        // 4. filter the terms with q frequency count at least the minimumn specified
        // frequency count.

        // extract search terms and add multiple inferred events (one for each)
        EventMonitor termSplitter = new TermSplitter();
        termSplitter.registerPatterns(esp);

        // extract out the search term and and user from the search term stream within the
        // time period
        String createUT = "insert into UserTerms " +
                "select event.getData('" + TERM_KEY + "')  as term," +
                " event.getData('" + USER_KEY + "') as user" +
                " from InferredEvent.win:time(" + timeLimit + ") as event" +
                " where type='Search Term'";
//        esp.monitor(true, createUT, null);
        esp.monitor(EventMatcher.matcher(true, createUT), null);

        // TODO: have frequency counts based on unique user requests

        // extract frequency count by search term
        String createSTF = "insert into SearchTermFreq select term, count(*) as freq " + "from UserTerms.win:time(" + timeLimit + ") group by term";
        esp.monitor(EventMatcher.matcher(true, createSTF), null);

        // filter out search terms with at least the desired minimum frequency
        String getSTF = "select * from SearchTermFreq.win:time(" + timeLimit + ") where freq >= " + minFreq;
        esp.monitor(EventMatcher.matcher(true, getSTF), this);

        // TODO: enable for multiple instantiations
        // this will run into problems if multiple CSDs are created; e.g.
        // CSD(20, 30min), CSD(30, 60min) since there will be multiple intermediate
        // events created (Search Terms, UserTerms, SearchTermFreq)

        // @todo = this needs to be integrated
        return new HashSet<Publisher>();
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
