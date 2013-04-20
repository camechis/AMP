package pegasus.eventbus.client;

public class SubscriptionToken {

	private Subscription subscription;

	public SubscriptionToken(Subscription subscription) {
		this.subscription = subscription;
	}

	public Subscription getSubscription() {
		return subscription;
	}

}
