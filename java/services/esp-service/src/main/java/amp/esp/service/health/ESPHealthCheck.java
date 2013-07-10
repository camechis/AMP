package amp.esp.service.health;

import com.yammer.metrics.core.HealthCheck;

public class ESPHealthCheck extends HealthCheck {

	public ESPHealthCheck() {
		
		super("ESP");
	}

	@Override
	protected Result check() throws Exception {
		
		return Result.healthy();
	}
}
