package amp.tests.integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.rabbitmq.client.ConnectionFactory;

import amp.rabbit.connection.TokenConnectionFactory;
import amp.rabbit.topology.Exchange;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

public class TokenChannelFactoryTests {
    
	protected static FileSystemXmlApplicationContext context;
	protected static TokenConnectionFactory factory;
	
	@BeforeClass
	public static void BeforeAllTests(){
		context = new FileSystemXmlApplicationContext(Config.Authorization.AnubisBasic, Config.Topology.Simple);
		factory = (TokenConnectionFactory) context.getBean("channelFactory");
	}
	
	@AfterClass
	public static void AfterAllTests(){
		context.close();
	}
	
    @Test
    public void Should_be_able_to_get_token_from_Anubis() throws Exception
    {
    	ConnectionFactory rabbitFactory = mock(ConnectionFactory.class);
    	factory.configureConnectionFactory(rabbitFactory, new Exchange());
    	
    	verify(rabbitFactory).setUsername((String)isNotNull());
    	verify(rabbitFactory).setPassword((String)isNotNull());
    }
}
