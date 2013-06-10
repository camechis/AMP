package amp.examples.streaming.subscriber;

import amp.eventing.streaming.DefaultStreamingBus;
import cmf.bus.Envelope;
import cmf.eventing.patterns.streaming.IStreamingEventBus;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingReaderHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jholmberg
 * Date: 6/7/13
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamingSubscriber {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext injector = new ClassPathXmlApplicationContext("classpath:META-INF/spring/eventBusContext.xml");

        IStreamingEventBus streamingEventBus = injector.getBean("eventBus", DefaultStreamingBus.class);

        final Boolean isDone = new Boolean(false);


        IStreamingReaderHandler<String> handler = new IStreamingReaderHandler<String>() {
            @Override
            public Object onSequenceEventRead(IStreamingEventItem<String> eventItem) {
                System.out.println("Message received: (sequenceId: " + eventItem.getSequenceId().toString() +
                        "), (position: " + eventItem.getPosition() +
                        "), (isLast: " + Boolean.toString(eventItem.isLast()) + "), \nEvent Value: " + eventItem.getEvent() );
                return null;
            }

            @Override
            public Object onSequenceFinished(IStreamingEventItem<String> eventItem) {
                System.out.println("Last Message In Sequence received: (sequenceId: " + eventItem.getSequenceId().toString() +
                        "), (position: " + eventItem.getPosition() +
                        "), (isLast: " + Boolean.toString(eventItem.isLast()) + "), \nEvent Value: " + eventItem.getEvent() );
                StreamingSubscriber.isDone = true;
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Class getEventType() {
                return String.class;
            }

            @Override
            public Object handle(String event, Map<String, String> headers) {
                System.out.println("Handle called.");
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object handleFailed(Envelope envelope, Exception e) {
                System.out.println("Handle Failed called");
                return null;
            }
        };

        boolean allEventsReceived = false;

        streamingEventBus.subscribeToNotifier(handler);


        while (allEventsReceived == false) {

            allEventsReceived = isDone;

            Thread.sleep(200);
        }
    }


    public static boolean isDone;
}
