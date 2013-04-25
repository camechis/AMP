package amp.esp.monitors;

import org.junit.Test;

import amp.esp.InferredEvent;
import amp.esp.TestUtils;
import amp.esp.monitors.DocumentCollectionWithHitFrequencySearchResultsDetector;

import pegasus.eventbus.client.WrappedEnvelope;

public class DocumentCollectionWithHitFrequencySearchResultDetectorTest extends AbstractDetectorTest {

    private static final String DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT =
            "DocumentCollectionWithHitFrequencySearchResult";



    @Test
    public void testMatchingDocCollAndHitFreq() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("red");
        sendAndExpectNo(dc1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("red");
        InferredEvent event =
                sendAndExpect(hf1, 1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT).get(0);
        assertInferredEventReferences(event, dc1);
        assertInferredEventReferences(event, hf1);
    }

    @Test
    public void testMatchingHitFreqAndDocColl() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("red");
        sendAndExpectNo(hf1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("red");
        InferredEvent event =
                sendAndExpect(dc1, 1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT).get(0);
        assertInferredEventReferences(event, dc1);
        assertInferredEventReferences(event, hf1);
    }

    @Test
    public void testNonMatchingDocCollAndHitFreq() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("red");
        sendAndExpectNo(dc1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("green");
        sendAndExpectNo(hf1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);
    }

    @Test
    public void testNonMatchingHitFreqAndDocColl() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("red");
        sendAndExpectNo(hf1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("green");
        sendAndExpectNo(dc1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);
    }

    @Test
    public void testNonAndMatchingDocCollAndHitFreq() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("red");
        sendAndExpectNo(dc1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("green");
        sendAndExpectNo(hf1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope hf2 = TestUtils.createHitFrequency("red");
        InferredEvent event =
                sendAndExpect(hf2, 1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT).get(0);
        assertInferredEventReferences(event, dc1);
        assertInferredEventReferences(event, hf2);
    }

    @Test
    public void testNonAndMatchingHitFreqAndDocColl() {
        testRepository.addMonitor(new DocumentCollectionWithHitFrequencySearchResultsDetector());

        WrappedEnvelope hf1 = TestUtils.createHitFrequency("red");
        sendAndExpectNo(hf1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope dc1 = TestUtils.createDocumentCollection("green");
        sendAndExpectNo(dc1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT);

        WrappedEnvelope dc2 = TestUtils.createDocumentCollection("red");
        InferredEvent event =
                sendAndExpect(dc2, 1, DOCUMENT_COLLECTION_WITH_HIT_FREQUENCY_SEARCH_RESULT).get(0);
        assertInferredEventReferences(event, dc2);
        assertInferredEventReferences(event, hf1);
    }
}
