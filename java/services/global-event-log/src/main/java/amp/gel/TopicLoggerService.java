package amp.gel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berico.fallwizard.SpringService;
import com.yammer.dropwizard.config.Bootstrap;

public class TopicLoggerService extends SpringService<TopicLoggerConfiguration> {
	private static final Logger logger = LoggerFactory
			.getLogger(TopicLoggerService.class);

	public static void main(String[] args) throws Exception {
		new TopicLoggerService().run(args);
	}

	@Override
	public void initialize(Bootstrap<TopicLoggerConfiguration> bootstrap) {
		super.initialize(bootstrap);
		logger.info("Initializing Topic Logger Service!");
	}
}