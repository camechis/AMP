package pegasus.eventbus.client;

import cmf.bus.Envelope;

public class WrappedEnvelope {

	Envelope envelope = null;

	public Envelope getEnvelope() {
        return envelope;
    }

    // Constructor for testing
	public WrappedEnvelope() {
		super();
		envelope = new Envelope();
	}

	public WrappedEnvelope(Envelope cmfenv) {
		super();
		this.envelope = cmfenv;
	}

}
