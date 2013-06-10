package amp.examples.streaming.subscriber;

import amp.eventing.streaming.DefaultStreamingBus;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingEventBus;
import cmf.eventing.patterns.streaming.IStreamingProgressUpdater;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StreamingCollectionSubscriber {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext injector = new ClassPathXmlApplicationContext("classpath:META-INF/spring/eventBusContext.xml");

        IStreamingEventBus streamingEventBus = injector.getBean("eventBus", DefaultStreamingBus.class);

        IStreamingCollectionHandler<String> handler = new CollectionHandler();
        IStreamingProgressUpdater notifier = new IStreamingProgressUpdater() {
            @Override
            public void updateProgress(String sequenceId, int numEventsProcessed) {
                System.out.println("SequenceId: " + sequenceId + " has processed " + numEventsProcessed + " event(s).");
            }
        };
        handler.setStreamingProgressUpdater(notifier);

        streamingEventBus.subscribeToCollection(handler);

        System.in.read();
        System.exit(0);
    }
}
