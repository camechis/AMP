package amp.gel.dao.impl.derby;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import amp.gel.dao.BaseTopicLoggerIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-cmf-context.xml",
		"classpath:test-derby-context.xml" })
public class DerbyTopicLoggerIntegrationTest extends
		BaseTopicLoggerIntegrationTest {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() {
		super.test();
	}
}