package amp.esp.monitors;

import amp.esp.InferredEvent;
import amp.esp.TestUtils;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import pegasus.eventbus.client.WrappedEnvelope;

public class JsEnvelopeCounterTest extends AbstractDetectorTest {

	private static final String COUNT_EVENT = "BusEvents";

	@Test
	public void testCounter() throws ScriptException {
		JsEnvelopeCounter counter;
		counter = new JsEnvelopeCounter();
		testRepository.addMonitor(counter);

		int envCount = 1;

		WrappedEnvelope signoff = TestUtils.makeEnvelope("Signoff", null, null, "Say goodnight Gracie", "Gracie Allen");
		sendAndCheckCount(signoff, envCount ++);

		WrappedEnvelope reqMS1 = TestUtils.makeAuthRequest("Maxwell Smart", "Shoe Phone", "MS1");
		sendAndCheckCount(reqMS1, envCount++);

		WrappedEnvelope reqPW1 = TestUtils.makeAuthRequest("Peewee Herman", "sat 1 imagery", "PW1");
		sendAndCheckCount(reqPW1, envCount++);

		WrappedEnvelope responseMS1 = TestUtils.makeAuthResponse(true, "MS1");
		sendAndCheckCount(responseMS1, envCount++);

		WrappedEnvelope responsePW1 = TestUtils.makeAuthResponse(false, "PW1");
		sendAndCheckCount(responsePW1, envCount++);

		WrappedEnvelope reqPW2 = TestUtils.makeAuthRequest("Peewee Herman", "sat 2 imagery", "PW2");
		sendAndCheckCount(reqPW2, envCount++);

		WrappedEnvelope reqJB1 = TestUtils.makeAuthRequest("James Bond", "Dr No personnel file", "JB1");
		sendAndCheckCount(reqJB1, envCount++);

		WrappedEnvelope reqJB2 = TestUtils.makeAuthRequest("James Bond", "Kill Authorization", "JB2");
		sendAndCheckCount(reqJB2, envCount++);

		WrappedEnvelope responsePW2 = TestUtils.makeAuthResponse(false, "PW2");
		sendAndCheckCount(responsePW2, envCount++);

		WrappedEnvelope reqPW3 = TestUtils.makeAuthRequest("Peewee Herman", "sat 3 imagery", "PW3");
		sendAndCheckCount(reqPW3, envCount++);

		WrappedEnvelope responsePW3 = TestUtils.makeAuthResponse(false, "PW3");
		sendAndCheckCount(responsePW3, envCount++);

		WrappedEnvelope responseJB1 = TestUtils.makeAuthResponse(true, "JB1");
		sendAndCheckCount(responseJB1, envCount++);

		WrappedEnvelope reqJB3 = TestUtils.makeAuthRequest("James Bond", "Underwater car", "JB3");
		sendAndCheckCount(reqJB3, envCount++);

		WrappedEnvelope responseJB2 = TestUtils.makeAuthResponse(true, "JB2");
		sendAndCheckCount(responseJB2, envCount++);

		WrappedEnvelope responseJB3 = TestUtils.makeAuthResponse(true, "JB3");
		sendAndCheckCount(responseJB3, envCount++);
	}

	private void sendAndCheckCount(WrappedEnvelope env, int expectedCount) {
		InferredEvent ie = sendAndExpect(env, 1, COUNT_EVENT).get(0);
		String countStr = ie.getData("Count");
		int count = Integer.parseInt(countStr);
		Assert.assertEquals(expectedCount, count);
	}
}
