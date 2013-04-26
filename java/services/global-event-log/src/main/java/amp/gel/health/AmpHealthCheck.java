package amp.gel.health;

import com.yammer.metrics.core.HealthCheck;

public class AmpHealthCheck extends HealthCheck {

	public AmpHealthCheck() {
		super("amp");
	}

	@Override
	protected Result check() throws Exception {
		// TODO: real health check
		return Result.healthy();
	}
}
