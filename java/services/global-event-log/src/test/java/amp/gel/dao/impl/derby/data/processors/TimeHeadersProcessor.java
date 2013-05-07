package amp.gel.dao.impl.derby.data.processors;

import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import amp.eventing.EnvelopeHelper;
import amp.eventing.EventContext;
import amp.eventing.EventContext.Directions;
import amp.eventing.IContinuationCallback;

public class TimeHeadersProcessor implements EventSequenceProcessor {

	private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat
			.date();

	private DateTime startDate;

	private DateTime stopDate = new DateTime();

	public void setStartDate(String startDate) {
		this.startDate = DATE_FORMATTER.parseDateTime(startDate);
		this.currentTime = this.startDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = DATE_FORMATTER.parseDateTime(stopDate);
	}

	private DateTime currentTime;

	private Random random = new Random();

	public void processEvent(EventContext eventContext,
			IContinuationCallback continuationCallback) throws Exception {
		if (Directions.Out == eventContext.getDirection()) {
			EnvelopeHelper env = new EnvelopeHelper(eventContext.getEnvelope());

			DateTime creationTime = getCreationTime();
			env.setCreationTime(creationTime);

			DateTime receiptTime = getReceiptTime(creationTime);
			env.setReceiptTime(receiptTime);

			advanceTimeBetweenEvents();
		}
	}

	public void restartEventSequence() throws Exception {
		advanceTimeBetweenSequencesOfEvents();

		if (stopDate.isBefore(currentTime)) {
			throw new Exception("Time to stop generating event sequences!");
		}
	}

	private DateTime getReceiptTime(DateTime creationTime) {
		DateTime receiptTime = creationTime
				.plusMillis(randomTimeBetweenCreationTimeAndReceiptTime());
		return receiptTime;
	}

	private DateTime getCreationTime() {
		DateTime creationTime = new DateTime(currentTime.getMillis());
		return creationTime;
	}

	private void advanceTimeBetweenEvents() {
		currentTime = currentTime.plusMillis(randomTimeBetweenEvents());
	}

	private void advanceTimeBetweenSequencesOfEvents() {
		currentTime = currentTime
				.plusMillis(randomTimeBetweenSequencesOfEvents());
	}

	private int randomTimeBetweenEvents() {
		int randomNumberOfMinutes = random.nextInt(60) + 1;
		int millis = randomNumberOfMinutes * 60 * 1000;
		return millis;
	}

	private int randomTimeBetweenSequencesOfEvents() {
		int randomNumberOfHours = random.nextInt(24) + 1;
		int millis = randomNumberOfHours * 60 * 60 * 1000;
		return millis;
	}

	private int randomTimeBetweenCreationTimeAndReceiptTime() {
		int randomNumberOfSeconds = random.nextInt(60) + 1;
		int millis = randomNumberOfSeconds * 1000;
		return millis;
	}
}
