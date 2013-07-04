package amp.examples.streaming.subscriber;

import amp.eventing.streaming.DefaultStreamingBus;
import amp.eventing.streaming.IStandardStreamingEventBus;
import amp.examples.streaming.common.ModernMajorGeneralMessage;
import cmf.bus.Envelope;
import cmf.eventing.patterns.streaming.IStreamingEventBus;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingReaderHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class StreamingReaderSubscriber {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext injector = new ClassPathXmlApplicationContext("classpath:META-INF/spring/eventBusContext.xml");

        IStandardStreamingEventBus streamingEventBus = injector.getBean("eventBus", DefaultStreamingBus.class);
        IStreamingReaderHandler<ModernMajorGeneralMessage> handler = new IStreamingReaderHandler<ModernMajorGeneralMessage>() {
            @Override
            public Object onEventRead(IStreamingEventItem<ModernMajorGeneralMessage> eventItem) {
                System.out.println("Message received: (sequenceId: " + eventItem.getSequenceId().toString() +
                        "), (position: " + eventItem.getPosition() +
                        "), (isLast: " + Boolean.toString(eventItem.isLast()) + "), \nEvent Value: " + eventItem.getEvent().getContent() );
                return null;
            }

            @Override
            public void dispose() {
                System.out.println("Last event was received from publisher. Event Stream has been closed.");
                isDone = true;
            }

            @Override
            public Class<ModernMajorGeneralMessage> getEventType() {
                return ModernMajorGeneralMessage.class;
            }

            @Override
            public Object handle(ModernMajorGeneralMessage event, Map<String, String> headers) {
                System.out.println("Handle called");
                return null;
            }

            @Override
            public Object handleFailed(Envelope envelope, Exception e) {
                System.out.println("Handle Failed called");
                return null;
            }
        };

        boolean allEventsReceived = false;

        streamingEventBus.subscribeToReader(handler);


        while (allEventsReceived == false) {

            allEventsReceived = StreamingReaderSubscriber.isDone;

            if (allEventsReceived) {
                System.out.println("All events have been received. Processing is complete.");
            } else {
                System.out.println("Still waiting for more events to be processed.");
            }

            Thread.sleep(200);
        }
        System.exit(0);
    }


    public static boolean isDone;
}
