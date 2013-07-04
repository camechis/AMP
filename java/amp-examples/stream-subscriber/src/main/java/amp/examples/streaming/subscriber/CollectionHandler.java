package amp.examples.streaming.subscriber;

import amp.examples.streaming.common.ModernMajorGeneralMessage;
import cmf.bus.Envelope;
import cmf.eventing.patterns.streaming.IStreamingCollectionHandler;
import cmf.eventing.patterns.streaming.IStreamingEventItem;
import cmf.eventing.patterns.streaming.IStreamingProgressUpdater;

import java.util.Collection;
import java.util.Map;

public class CollectionHandler implements IStreamingCollectionHandler<ModernMajorGeneralMessage> {
    private IStreamingProgressUpdater notifier;

    @Override
    public Object handleCollection(Collection<IStreamingEventItem<ModernMajorGeneralMessage>> events, Map<String, String> headers) {
        System.out.println("Received a collection from AMPere of size: " + events.size());

        for(IStreamingEventItem<ModernMajorGeneralMessage> eventItem : events) {
            System.out.println("Event Item: SequenceId => " + eventItem.getSequenceId() +
                    ", Position => " + eventItem.getPosition() +
                    ", IsLast => " + eventItem.isLast() +
                    "\n\t Content => " + eventItem.getEvent().getContent());
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
    public Class<ModernMajorGeneralMessage> getEventType() {
        return ModernMajorGeneralMessage.class;
    }

    @Override
    public Object handle(ModernMajorGeneralMessage event, Map<String, String> headers) {
        System.out.println("handle called");
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception e) {
        System.out.println("handleFailed called");
        return null;
    }
}
