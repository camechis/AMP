package amp.examples.adaptor.health;

import com.yammer.metrics.core.HealthCheck;

public class ExampleHealthCheck extends HealthCheck {

	public ExampleHealthCheck() {
		
		super("example");
	}

	@Override
	protected Result check() throws Exception {
		
		return Result.healthy();
	}
}
