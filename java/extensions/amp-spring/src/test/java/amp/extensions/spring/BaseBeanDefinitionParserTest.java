package amp.extensions.spring;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class BaseBeanDefinitionParserTest {

		private Logger logger;
	
		private static final String SPRINGCONTEXT = 
			"src/test/resources/beanDefinitionParserTestContext.xml";
		
		public BaseBeanDefinitionParserTest(){
			
			this.logger = LoggerFactory.getLogger(this.getClass());
		}
		
		FileSystemXmlApplicationContext context;
		
		@Before
		public void setUp() throws Exception {
			
			context = new FileSystemXmlApplicationContext(SPRINGCONTEXT);
		}

		@After
		public void tearDown() throws Exception {
			
			context.close();
		}
}
