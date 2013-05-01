
package amp.esp.monitors;

import amp.esp.EventMatcher;
import amp.esp.EventMonitor;
import amp.esp.EventStreamProcessor;
import amp.esp.InferredEvent;
import amp.esp.publish.Publisher;

import java.util.Collection;
import java.util.HashSet;

import com.espertech.esper.client.EventBean;

public class UnauthorizedAccessAttemptsDetector extends EventMonitor {

    public static final String INFERRED_TYPE = "UnauthorizedAccessAttempts";

    private int minFreq;
    private String timeLimit;

    public UnauthorizedAccessAttemptsDetector(int minFreq, String timeLimit) {
        super();
        this.minFreq = minFreq;
        this.timeLimit = timeLimit;
    }

    @Override
    public InferredEvent receive(EventBean eventBean) {
        String userid = (String) eventBean.get("userid");
        long freq = (Long) eventBean.get("freq");
        InferredEvent resultingEvent = makeInferredEvent();
        resultingEvent.putData("User", userid);
        resultingEvent.putData("Occurrences", "" + freq);
        resultingEvent.putData("Period", "" + timeLimit);
        return resultingEvent;
    }

    @Override
    public Collection<Publisher> registerPatterns(EventStreamProcessor esp) {

        String env1 = "request";
        String env2 = "response";
        String type1 = "Request";
        String type2 = "Response";
        String ipattern2 = EventMatcher.everyEnvelope(env1) .matching("EventType", type1 )
                .followedBy(EventMatcher.everyEnvelope(env2).matching("EventType", type2)
                        .matchingRef("CorrelationId", env1, "Id")
                        .matching("Topic", "Unauthorized Access")
                        ).getPattern();

        String createUA = "insert into UnauthorizedAccesses " +
                "select request.getHeader(" + quote("ReplyTo") + ") as userid, request, response " +
                "from pattern [ " + ipattern2 +  " ]";

        String createUAF = "insert into UnauthorizedAccessesFreq " +
                "select userid, count(*) as freq from UnauthorizedAccesses.win:time(" +
                timeLimit + ") group by userid";
        String getUAF = "select * from UnauthorizedAccessesFreq.win:time(" + timeLimit +
                ") where freq >= " + minFreq;

        esp.monitor(EventMatcher.matcher(true, createUA), null);
        esp.monitor(EventMatcher.matcher(true, createUAF), null);
        esp.monitor(EventMatcher.matcher(true, getUAF), this);

        // @todo = this needs to be integrated
        return new HashSet<Publisher>();
    }

    private String quote(String str) {
        return String.format("\"%s\"", str);
    }

    @Override
    public String getInferredType() {
        return INFERRED_TYPE;
    }

}
