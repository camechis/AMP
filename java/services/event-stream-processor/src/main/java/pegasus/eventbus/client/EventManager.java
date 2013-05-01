package pegasus.eventbus.client;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class EventManager {

	cmf.bus.IEnvelopeBus bus = null;

	public IRegistration subscribe(IRegistration registration) {
		try {
			bus.register(registration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return registration;
	}

	public void unsubscribe(IRegistration registration) {
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
