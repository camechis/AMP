package pegasus.eventbus.client;

public interface EventManager extends cmf.bus.IEnvelopeBus {

	SubscriptionToken subscribe(Subscription subscription);

	void unsubscribe(SubscriptionToken token);

	void publish(Object message);

}
