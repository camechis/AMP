package amp.esp;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.google.common.annotations.VisibleForTesting;

public class EventStreamProcessor {

    // TODO: consider making this a constructor parameter to allow for multiple instances
    public static final String engineURI = "EventStreamProcessor";
    private static final Logger LOG = LoggerFactory.getLogger(EventStreamProcessor.class);

    private EPServiceProvider epService;

    private final String espKey = this.getClass().getCanonicalName();
    private IRegistration registration;

    IEnvelopeBus bus;

    /**
     * This is a wrapper class around an EventMonitor to allow it to receive events
     * from the Esper runtime.  It simplifies the API so that an EventMonitor registers its
     * patterns with the ESP and has a 'receive' method that is called for each new
     * Event Bean which may return an Inferred Event.
     *
     * @author israel
     *
     */
    class EnvelopeListener implements UpdateListener {

        public EnvelopeListener(EventMonitor monitor) {
            super();
            this.monitor = monitor;
        }

        EventMonitor monitor;

        @Override
        public void update(EventBean[] newEvents, EventBean[] oldEvents) {
            try {
                for (EventBean eventBean : newEvents) {
                    LOG.debug(Utils.beanString(eventBean) + "==> " + monitor + ".receive(" + ")");
                    processResult(monitor.receive(eventBean));
                }
                // resultingEvent = monitor.handle(newEvents, oldEvents);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

        private void processResult(InferredEvent resultingEvent) {
            if (resultingEvent == null) {
                return;
            }

            EPRuntime epRuntime = epService.getEPRuntime();
            if (resultingEvent instanceof InferredEventList) {
                for (InferredEvent ev : ((InferredEventList) resultingEvent).getList()) {
                    LOG.debug(monitor + " SENDS* " + ev);
                    epRuntime.sendEvent(ev);
                }
            } else {
                LOG.debug(monitor + " SENDS " + resultingEvent);
                epRuntime.sendEvent(resultingEvent);
            }
        }
    }

    public EventStreamProcessor() {
        epService = createEventProcessor();
    }

    public EventStreamProcessor(EventMonitorRepository repository) {
        this();
        setRepository(repository);
    }

    public void setRepository(EventMonitorRepository repository) {
        repository.registerWith(this);
    }

    private EPServiceProvider createEventProcessor() {

        //@todo - workaround for classloader issue
        // save off context class loader
        ClassLoader contextClassloader = Thread.currentThread().getContextClassLoader();

        // set new classloader
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        Configuration configuration = new Configuration();
        configuration.addEventType("Envelope", Envelope.class);
        configuration.addEventType("InferredEvent", InferredEvent.class);
        EPServiceProvider epService = EPServiceProviderManager.getProvider(engineURI, configuration);
        epService.initialize();

        // reset old classloader
        Thread.currentThread().setContextClassLoader(contextClassloader);

        return epService;
    }

    public void attachToEventBus(IEnvelopeBus bus) throws Exception {
        if (this.bus != null) {
            try {
                detachFromEventBus();
            } catch (Exception e) {
                // ignore
            }
        }
        this.bus = bus;
        registration = new ESPRegistration(this);
        bus.register(registration);
    }

    public void detachFromEventBus() throws Exception {
        if (bus != null) {
            bus.unregister(registration);
            this.bus = null;
            this.registration = null;
        }
    }

    public void watchFor(EventMonitor monitor) {
        monitor.registerPatterns(this);
    }

    @VisibleForTesting
    public void sendEvent(Envelope envelope) {
        LOG.debug(" --> Event: " + envelope);
        epService.getEPRuntime().sendEvent(envelope);

    }

    public void monitor(EventMatcher matcher, EventMonitor monitor) {
        monitor(matcher.isEPL(), matcher.getPattern(), monitor);
    }

    private
    void monitor(boolean isEPL, String pattern, EventMonitor monitor) {
        EPAdministrator administrator = epService.getEPAdministrator();
        EPStatement stmt;
        LOG.debug("Creating " + (isEPL ? "EPL" : "pattern") + ": " + pattern);
        dbg("Creating " + (isEPL ? "EPL" : "pattern") + ": " + pattern);
        try {
            if (isEPL) {
                stmt = administrator.createEPL(pattern);
            } else {
                stmt = administrator.createPattern(pattern);
            }
        } catch (EPException e) {
            System.err.println("Error creating " + (isEPL ? "EPL" : "Pattern") + " statement: " + pattern);
            dbg("\nError creating " + (isEPL ? "EPL" : "Pattern") + " statement: " + pattern + "\n" + e);
            e.printStackTrace();
            throw e;
        }
        if (monitor != null) {
            stmt.addListener(new EnvelopeListener(monitor));
        }
        if (monitor == null) {
            UpdateListener intermediatePrinter = createIntermediatePrinter(pattern);
            if (intermediatePrinter != null) {
                stmt.addListener(intermediatePrinter);
            }
        }
    }

    private UpdateListener createIntermediatePrinter(final String pattern) {
        boolean watchInt = false;
        if (!watchInt) {
            return null;
        }

        String displ = pattern;
        String insert = "insert into ";
        if (pattern.startsWith(insert)) {
            displ = pattern.substring(insert.length()).split("\\W")[0];
        }
        final String fdispl = displ;
        UpdateListener ip = new UpdateListener() {
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for (EventBean eventBean : newEvents) {
                    LOG.debug("!!!! " + fdispl + ": " + Utils.beanString(eventBean));
                }
            }
        };

        return ip;
    }

    public static void dbg(String str) { fprint("/tmp/DEBUG", str); }

    /**
     *  Write debugging output to a file
     *
     * @param fname the file to be written to
     * @param data The debug string to be written
     **/

    public static void fprint(String fname, String data) {
      java.io.PrintWriter file;
      boolean append = true;
      try {
        file = new java.io.PrintWriter(new java.io.FileOutputStream(fname, append));
      } catch (Exception exc) {
        return;
      }
      file.print(data + "\n");
      file.close();
    }
}
