package pegasus.eventbus.client;

import cmf.bus.Envelope;


public interface EnvelopeHandler {

	EventResult handleEnvelope(Envelope envelope);
}
