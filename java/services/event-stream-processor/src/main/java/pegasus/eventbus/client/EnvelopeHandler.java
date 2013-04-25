package pegasus.eventbus.client;

public interface EnvelopeHandler {

	EventResult handleEnvelope(WrappedEnvelope envelope);

	String getEventSetName();

}
