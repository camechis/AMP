package amp.eventing.streaming;

import cmf.eventing.patterns.streaming.IStreamingEventItem;

import java.util.Map;
import java.util.UUID;

import static cmf.eventing.patterns.streaming.StreamingEnvelopeConstants.*;

/**
 * Contains the elements needed to receive an event from an event stream.
 * This allows each discrete event pulled from a subscribed event stream to
 * contain the necessary state that a subscriber would need including the
 * sequenceId, position, and whether this event is the last in the published
 * sequence.
 * User: jholmberg
 * Date: 6/7/13
 */
public class StreamingEventItem<TEVENT> implements IStreamingEventItem<TEVENT> {
    private final TEVENT event;
    private final Map<String, String> eventHeaders;

    public StreamingEventItem(TEVENT event, Map<String, String> eventHeaders) {
        this.event = event;
        this.eventHeaders = eventHeaders;
    }

    /**
     * Get the event received from the published stream
     * @return
     */
    public TEVENT getEvent() {
        return event;
    }

    /**
     * Helper method that extracts the unique sequenceId from the event headers
     * @return
     */
    public UUID getSequenceId() {
        return UUID.fromString(eventHeaders.get(SEQUENCE_ID));
    }

    /**
     * Helper method that returns the position of this event in the event sequence
     * @return
     */
    public int getPosition() {
        return Integer.parseInt(eventHeaders.get(POSITION));
    }

    /**
     * Helper method that returns whether this event is the last in the sequence
     * @return
     */
    public boolean isLast() {
        return Boolean.parseBoolean(eventHeaders.get(IS_LAST));
    }

    /**
     * Returns all headers associated with the event
     * @return
     */
    public Map<String, String> getEventHeaders() {
        return eventHeaders;
    }
}
