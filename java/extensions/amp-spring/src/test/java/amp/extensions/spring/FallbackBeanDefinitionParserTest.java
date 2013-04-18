package amp.extensions.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import amp.topology.client.DefaultApplicationExchangeProvider;

public class FallbackBeanDefinitionParserTest extends BaseBeanDefinitionParserTest {

	@Test
	public void fallback_provider_is_correctly_parsed_supply_defaults_for_nonspecified_values() {
		
		DefaultApplicationExchangeProvider providerWithDefaults = new DefaultApplicationExchangeProvider();
		
		DefaultApplicationExchangeProvider provider = 
			context.getBean("fallbackTest1", DefaultApplicationExchangeProvider.class);
		
		assertEquals("devexample.com", provider.getHostname());
		assertEquals("test-exchange", provider.getExchangeName());
		assertEquals(true, provider.isDurable());
		
		assertEquals(providerWithDefaults.getExchangeType(), provider.getExchangeType());
		assertEquals(providerWithDefaults.getPort(), provider.getPort());
		assertEquals(providerWithDefaults.getQueueName(), provider.getQueueName());
		assertEquals(providerWithDefaults.getVhost(), provider.getVhost());
		assertEquals(providerWithDefaults.isAutoDelete(), provider.isAutoDelete());
	}
	
	@Test
	public void fallback_provider_is_correctly_parsed_for_all_values____no_defaults() {
		
		DefaultApplicationExchangeProvider provider = 
			context.getBean("fallbackTest2", DefaultApplicationExchangeProvider.class);
		
		assertEquals("devexample.com", provider.getHostname());
		assertEquals("test-exchange", provider.getExchangeName());
		assertEquals(true, provider.isDurable());
		assertEquals(true, provider.isAutoDelete());
		assertEquals("topic", provider.getExchangeType());
		assertEquals(12345, provider.getPort());
		assertEquals("myqueue", provider.getQueueName());
		assertEquals("test", provider.getVhost());
		assertEquals("me", provider.getClientName());
	}
	
}
