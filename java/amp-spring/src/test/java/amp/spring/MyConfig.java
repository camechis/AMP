package amp.spring;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import amp.messaging.IMessageProcessor;
import amp.spring.annotations.AmpereConnectionMode;
import amp.spring.annotations.EnableAmpere;
import amp.spring.configuration.AmpereConfig;
import amp.spring.configuration.AmpereConfigurer;
import amp.utility.http.HttpClientProvider;

@Configuration
@EnableAmpere()
public class MyConfig implements AmpereConfigurer {

	@Override
	public HttpClientProvider configureGtsConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpClientProvider configureAnubisConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configureInBoundProcessors(
			List<IMessageProcessor> inboundProcessors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureOutBoundProcessors(
			List<IMessageProcessor> outboundProcessors) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
