package amp.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import amp.spring.configuration.AmpereConfig;
import amp.spring.configuration.CommandableRoutingInfoCache;
import amp.spring.configuration.GlobalTopologyConfig;
import amp.spring.configuration.SSLHttpClientProviderConfig;
import amp.spring.configuration.TokenAmpereConnectionConfig;

public class ConfigurationTest {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(MyConfig.class);
		ctx.refresh();

	}

}
