package amp.gel.dao.impl.accumulo;

import java.io.UnsupportedEncodingException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class MutationsGeneratorUtils {

	/**
	 * Datetime format that can be lexicographically ordered. For example,
	 * "2013-04-23T18:50:36.138Z"
	 */
	private static final DateTimeFormatter TIMESTAMP_FORMATTER = ISODateTimeFormat
			.dateTime().withZoneUTC();

	public static String longToFormattedTimestamp(long timeMillis) {
		return new DateTime(timeMillis).toString(TIMESTAMP_FORMATTER);
	}

	public static long formattedTimestampToLong(String formattedTimestamp) {
		return TIMESTAMP_FORMATTER.parseDateTime(formattedTimestamp)
				.getMillis();
	}

	public static byte[] toBytes(String value) {
		try {
			return value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			// do nothing since we know that UTF-8 is a supported
			// encoding
		}

		return null;
	}

	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// do nothing since we know that UTF-8 is a supported
			// encoding
		}

		return null;
	}
}