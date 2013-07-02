package amp.examples.streaming.publisher;

import amp.eventing.streaming.DefaultStreamingBus;
import cmf.eventing.patterns.streaming.IEventStream;
import cmf.eventing.patterns.streaming.IStreamingEventBus;
import cmf.eventing.patterns.streaming.IStreamingMapperCallback;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

public class ChunkedSequencePublisher {

    /**
     * Demonstrates how to publish a chunked sequence of messages or using the eventStream to publish for you.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext injector = new ClassPathXmlApplicationContext("classpath:META-INF/spring/eventBusContext.xml");

        IStreamingEventBus streamingEventBus = injector.getBean("eventBus", DefaultStreamingBus.class);

        ArrayList<Object> streamMessages = new ArrayList<Object>();
        streamMessages.add("I am ");
        streamMessages.add("the very ");
        streamMessages.add("model of ");
        streamMessages.add("a Modern ");
        streamMessages.add("Major-General, ");
        streamMessages.add("I've information ");
        streamMessages.add("vegetable, animal, ");
        streamMessages.add("and mineral, ");
        streamMessages.add("I know the kings of England, ");
        streamMessages.add("and I quote the fights historical, ");
        streamMessages.add("From Marathon to Waterloo, ");
        streamMessages.add("in order categorical; ");

        IStreamingMapperCallback<String> mapper = new IStreamingMapperCallback<String>() {
            @Override
            public String map(Object element) {
                return (String) element;
            }
        };

        streamingEventBus.setBatchLimit(2);
        streamingEventBus.publishChunkedSequence(streamMessages.iterator(), mapper);
        System.exit(0);
    }
}
