package amp.gel.dao.impl.derby.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.eventing.EventContext;
import amp.eventing.EventContext.Directions;
import amp.eventing.IContinuationCallback;
import amp.eventing.IEventProcessor;
import amp.gel.dao.impl.derby.data.events.core.Event;
import amp.gel.dao.impl.derby.data.generators.EventSequenceGenerator;
import amp.gel.dao.impl.derby.data.processors.EventSequenceProcessor;
import cmf.bus.Envelope;

public class EnvelopeGenerator implements Iterable<Envelope>,
		Iterator<Envelope> {
	private static final Logger logger = LoggerFactory
			.getLogger(EnvelopeGenerator.class);

	private static final IContinuationCallback DEFAULT_CONTINUATION_CALLBACK = new IContinuationCallback() {
		public void continueProcessing() throws Exception {
		}
	};

	private List<EventSequenceProcessor> outboundProcessors;

	private List<EventSequenceGenerator> eventSequenceGenerators;

	private Queue<Envelope> envelopes = new LinkedBlockingQueue<Envelope>();

	private Random random = new Random();

	public EnvelopeGenerator(List<EventSequenceProcessor> outboundProcessors,
			List<EventSequenceGenerator> eventSequenceGenerators) {
		this.outboundProcessors = outboundProcessors;
		this.eventSequenceGenerators = eventSequenceGenerators;

		generateMoreEnvelopes();
	}

	public Iterator<Envelope> iterator() {
		return this;
	}

	public boolean hasNext() {
		return (!envelopes.isEmpty());
	}

	public Envelope next() {
		Envelope envelope = envelopes.poll();

		if (envelopes.isEmpty()) {
			generateMoreEnvelopes();
		}

		return envelope;
	}

	public void remove() {
		// do nothing
	}

	private void generateMoreEnvelopes() {
		int randomIndex = random.nextInt(eventSequenceGenerators.size());
		EventSequenceGenerator generator = eventSequenceGenerators
				.get(randomIndex);

		List<Event> eventSequence = generator.generate();
		List<Envelope> envelopeSequence = eventsToEnvelopes(eventSequence);
		envelopes.addAll(envelopeSequence);
	}

	private List<Envelope> eventsToEnvelopes(List<Event> eventSequence) {
		List<Envelope> envelopes = new ArrayList<Envelope>(eventSequence.size());

		try {
			for (EventSequenceProcessor processor : outboundProcessors) {
				processor.restartEventSequence();
			}
		} catch (Exception e) {
			return envelopes;
		}

		for (Event event : eventSequence) {
			EventContext context = new EventContext(Directions.Out,
					new Envelope(), event);

			for (IEventProcessor processor : outboundProcessors) {
				try {
					processor.processEvent(context,
							DEFAULT_CONTINUATION_CALLBACK);
				} catch (Exception e) {
					logger.error("Unable to process event: " + event, e);
				}
			}

			Envelope envelope = context.getEnvelope();
			envelopes.add(envelope);
		}

		return envelopes;
	}
}
