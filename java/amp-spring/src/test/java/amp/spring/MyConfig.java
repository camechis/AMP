package amp.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import amp.spring.annotations.AmpereConnectionMode;
import amp.spring.annotations.EnableAmpere;
import amp.spring.configuration.AmpereConfig;

@Configuration
@EnableAmpere()
@Import(AmpereConfig.class)
public class MyConfig {
	
	
	
}
