package amp.gel.managed;

import amp.gel.service.TopicLogger;

import com.yammer.dropwizard.lifecycle.Managed;

public class TopicLoggerManager implements Managed {

	private TopicLogger topicLogger;

	public void setTopicLogger(TopicLogger topicLogger) {
		this.topicLogger = topicLogger;
	}

	public void start() throws Exception {
		topicLogger.start();
	}

	public void stop() throws Exception {
		topicLogger.stop();
	}
}