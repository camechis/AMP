package pegasus.eventbus.client;

public interface EnvelopeHandler {

	EventResult handleEnvelope(Envelope envelope);

	String getEventSetName();

}
