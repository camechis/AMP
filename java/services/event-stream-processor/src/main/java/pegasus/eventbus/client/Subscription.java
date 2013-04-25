package pegasus.eventbus.client;

import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;

public class Subscription {

	IRegistration registration;

	public Subscription(final EnvelopeHandler envelopeHandler) {
		registration = new IRegistration() {

			@Override
			public Object handleFailed(Envelope env, Exception ex) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object handle(Envelope env) throws Exception {
				pegasus.eventbus.client.WrappedEnvelope pegenv = new pegasus.eventbus.client.WrappedEnvelope(env);
				return envelopeHandler.handleEnvelope(pegenv);
			}

			@Override
			public Map<String, String> getRegistrationInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IEnvelopeFilterPredicate getFilterPredicate() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public IRegistration getRegistration() {
		return registration;
	}

}
