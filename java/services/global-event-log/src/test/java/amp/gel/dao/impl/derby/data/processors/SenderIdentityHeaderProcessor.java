package amp.gel.dao.impl.derby.data.processors;

import java.util.Random;

import amp.eventing.EnvelopeHelper;
import amp.eventing.EventContext;
import amp.eventing.EventContext.Directions;
import amp.eventing.IContinuationCallback;

/**
 * Processes event sequences and sets the sender identity in the envelope header
 * to either "SYSTEM" for service responses or a person for user requests. The
 * same person is set for all events within the event sequence.
 * 
 */
public class SenderIdentityHeaderProcessor implements EventSequenceProcessor {

	private static final String[] HUMAN_USERS = { "gwashington", "jadams",
			"tjefferson", "jmadison", "jmonroe", "jqadams", "ajackson" };

	private static final String SYSTEM_USER = "SYSTEM";

	private String humanUser = null;

	private Random random = new Random();

	public SenderIdentityHeaderProcessor() {
		randomHumanUser();
	}

	public void processEvent(EventContext eventContext,
			IContinuationCallback continuationCallback) throws Exception {
		if (Directions.Out == eventContext.getDirection()) {
			EnvelopeHelper env = new EnvelopeHelper(eventContext.getEnvelope());

			String messageType = env.getMessageType();
			String senderIdentity = generateSenderIdentity(messageType);

			env.setSenderIdentity(senderIdentity);
		}
	}

	private String generateSenderIdentity(String messageType) {
		String senderIdentity;
		if (messageType.endsWith("ResponseEvent")
				|| messageType.endsWith("ResultEvent")) {
			senderIdentity = SYSTEM_USER;
		} else {
			senderIdentity = humanUser;
		}

		return senderIdentity;
	}

	public void restartEventSequence() {
		randomHumanUser();
	}

	private void randomHumanUser() {
		int randomIndex = random.nextInt(HUMAN_USERS.length);
		humanUser = HUMAN_USERS[randomIndex];
	}
}
