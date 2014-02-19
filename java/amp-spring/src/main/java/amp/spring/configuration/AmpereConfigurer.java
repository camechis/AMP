package amp.spring.configuration;

import java.util.List;

import amp.messaging.IMessageProcessor;
import amp.utility.http.HttpClientProvider;

public interface AmpereConfigurer {

	public HttpClientProvider configureGtsConnection( );
	
	public HttpClientProvider configureAnubisConnection( );
	
	public void configureInBoundProcessors( List<IMessageProcessor> inboundProcessors );
	
	public void configureOutBoundProcessors( List<IMessageProcessor> outboundProcessors );
	
	
}
