package pegasus.eventbus.client;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class EventManager {

	cmf.bus.IEnvelopeBus bus = null;

	public SubscriptionToken subscribe(Subscription subscription) {
		IRegistration registration = subscription.getRegistration();
		try {
			bus.register(registration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new SubscriptionToken(subscription);
	}

	public void unsubscribe(SubscriptionToken token) {
		IRegistration registration = token.getSubscription().getRegistration();
		try {
			bus.unregister(registration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void publish(Object message) {
		Envelope envelope = (Envelope) message;
		try {
			bus.send(envelope);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
