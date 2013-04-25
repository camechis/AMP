package amp.esp.monitors;

import org.junit.Test;

import amp.esp.InferredEvent;
import amp.esp.TestUtils;
import amp.esp.monitors.CorrelateRequestResponsesEventDetector;

import pegasus.eventbus.client.WrappedEnvelope;

public class CorrelateRequestResponsesEventDetectorTest extends AbstractDetectorTest {

    private static final String REQUEST_RESPONSE = "RequestResponse";

    @Test
    public void testReqResponseEventStream() {
        testRepository.addMonitor(new CorrelateRequestResponsesEventDetector());

        WrappedEnvelope e1 = TestUtils.makeRequest("red");
        sendAndExpectNo(e1, REQUEST_RESPONSE);

        WrappedEnvelope e2 = TestUtils.makeResponse("yellow");
        sendAndExpectNo(e2, REQUEST_RESPONSE);

        WrappedEnvelope e3 = TestUtils.makeResponse("red");
        InferredEvent found = sendAndExpect(e3, 1, REQUEST_RESPONSE).get(0);
        assertInferredEventReferences(found, e1);
        assertInferredEventReferences(found, e3);

        WrappedEnvelope e4 = TestUtils.makeRequest("green");
        sendAndExpectNo(e4, REQUEST_RESPONSE);

        WrappedEnvelope e5 = TestUtils.makeRequest("chartreuse");
        sendAndExpectNo(e5, REQUEST_RESPONSE);

        WrappedEnvelope e6 = TestUtils.makeResponse("purple");
        sendAndExpectNo(e6, REQUEST_RESPONSE);

        WrappedEnvelope e7 = TestUtils.makeResponse("green");
        InferredEvent found1 = sendAndExpect(e7, 1, REQUEST_RESPONSE).get(0);
        assertInferredEventReferences(found1, e4);
        assertInferredEventReferences(found1, e7);

        WrappedEnvelope e8 = TestUtils.makeResponse("blue");
        sendAndExpectNo(e8, REQUEST_RESPONSE);

        WrappedEnvelope e9 = TestUtils.makeRequest("orange");
        sendAndExpectNo(e9, REQUEST_RESPONSE);

        WrappedEnvelope e10 = TestUtils.makeRequest("pink");
        sendAndExpectNo(e10, REQUEST_RESPONSE);

        WrappedEnvelope e11 = TestUtils.makeResponse("orange");
        InferredEvent found2 = sendAndExpect(e11, 1, REQUEST_RESPONSE).get(0);
        assertInferredEventReferences(found2, e9);
        assertInferredEventReferences(found2, e11);
    }
}
