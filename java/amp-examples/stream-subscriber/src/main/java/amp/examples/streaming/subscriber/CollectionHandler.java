package amp.examples.streaming.subscriber;

import cmf.bus.Envelope;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingProgressUpdater;

import java.util.Collection;
import java.util.Map;

public class CollectionHandler implements IStreamingCollectionHandler<String> {
    private IStreamingProgressUpdater notifier;

    @Override
    public Object handleCollection(Collection<IStreamingEventItem<String>> events, Map<String, String> headers) {
        System.out.println("Received a collection from AMPere of size: " + events.size());

        for(IStreamingEventItem<String> eventItem : events) {
            System.out.println("Event Item: SequenceId => " + eventItem.getSequenceId() +
                    ", Position => " + eventItem.getPosition() +
                    ", IsLast => " + eventItem.isLast() +
                    "\n\t Content => " + eventItem.getEvent());
        }

        System.out.println("Processing complete.");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public IStreamingProgressUpdater getProgress() {
        return this.notifier;
    }

    @Override
    public void setStreamingProgressUpdater(IStreamingProgressUpdater updater) {
        this.notifier = updater;
    }

    @Override
    public Class<String> getEventType() {
        return String.class;
    }

    @Override
    public Object handle(String event, Map<String, String> headers) {
        System.out.println("handle called");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception e) {
        System.out.println("handleFailed called");
        return null;
    }
}
