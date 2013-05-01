package amp.esp;


import java.util.Date;
import java.util.Map;
import java.util.UUID;

import pegasus.eventbus.client.WrappedEnvelope;

import com.google.common.collect.Maps;


public class TestUtils {

	private static final String SEARCH_EVENT = "pegasus.core.search.event.TextSearchEvent";

    public static WrappedEnvelope makeRequest(String requestReferenceID) {
        return TestUtils.makeEnvelope("Request", requestReferenceID, null,
                "Request topic for " + requestReferenceID, "requester");
    }

    public static WrappedEnvelope makeResponse(String responseReferenceID) {
        return TestUtils.makeEnvelope("Response", null, responseReferenceID,
                "Response topic for " + responseReferenceID, "responder");
    }

    public static WrappedEnvelope makeAuthRequest(String user, String resource, String reqID) {
        WrappedEnvelope env = TestUtils.makeEnvelope("Request", reqID, null, resource, user);
        return env;
    }

    public static WrappedEnvelope makeAuthResponse(boolean allowed, String reqID) {
        String approval = allowed ? "APPROVED" : "Unauthorized Access";
        WrappedEnvelope env = TestUtils.makeEnvelope("Response", null, reqID, approval,
                "Resource Allocation Server");
        return env;
    }

    public static WrappedEnvelope makeSearchRequest(String user, String resource, String reqID) {
        WrappedEnvelope env = TestUtils.makeEnvelope(SEARCH_EVENT, reqID, null, resource, user);
        return env;
    }

    public static WrappedEnvelope createDocumentCollection(String reqID) {
        return TestUtils.makeEnvelope("DocumentCollectionSearchResult", null, reqID,
                reqID + " documents", "librarian");
    }

    public static WrappedEnvelope createHitFrequency(String reqID) {
        return TestUtils.makeEnvelope("HitFrequencySearchResult", null, reqID,
                reqID + " hit frequency", "hit freq counter");
    }



    private static long testTime = 12104;
    private static long timeIncr = 2000;

    public static WrappedEnvelope makeEnvelope(String type, String idsymbol, String correlationIdsymbol,
            String topic, String replyTo) {
        WrappedEnvelope e = new WrappedEnvelope();
        e.setEventType(type);
        e.setId(symIdToRealId(idsymbol));
        if (correlationIdsymbol != null) {
            e.setCorrelationId(symIdToRealId(correlationIdsymbol));
        }
        e.setTopic(topic);
        e.setReplyTo(replyTo);
        Date timestamp = new Date(testTime);
        testTime += timeIncr;
        e.setTimestamp(timestamp);
        e.setBody((type + topic).getBytes());
        return e;
    }

    public static Map<String, UUID> idMappings = Maps.newHashMap();

    /**
     * Translate between human readable IDs and internal UUIDs.  This method finds the UUID
     * that corresponds to the specified string and returns it.  If there isn't a UUID for
     * that string already, then a new UUID is allocated and returned, caching the correspondence
     * for future references of that string.
     *
     * @param idsymbol a string representing a unique ID, or null to create a new unique UUID
     * @return the UUID that permanently corresponds to the string
     */
    public static UUID symIdToRealId(String idsymbol) {
        UUID id = null;

        if (idsymbol != null) {
            id = idMappings.get(idsymbol);
        }

        if (id == null) {
            id = UUID.randomUUID();
            idMappings.put(idsymbol, id);
        }
        return id;
    }
}

