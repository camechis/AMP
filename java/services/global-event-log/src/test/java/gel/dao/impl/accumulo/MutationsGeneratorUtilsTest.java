package gel.dao.impl.accumulo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MutationsGeneratorUtilsTest {

	private static final long TIME_MILLIS_1 = 1366381160190L;

	private static final String FORMATTED_TIMESTAMP_1 = "2013-04-19T14:19:20.190Z";

	private static final long TIME_MILLIS_2 = 1366381160219L;

	private static final String FORMATTED_TIMESTAMP_2 = "2013-04-19T14:19:20.219Z";

	@Test
	public void testLongToFormattedTimestamp() {
		String formattedTimestamp = MutationsGeneratorUtils
				.longToFormattedTimestamp(TIME_MILLIS_1);
		assertEquals(FORMATTED_TIMESTAMP_1, formattedTimestamp);
	}

	@Test
	public void testFormattedTimestampToLong() {
		long timeMillis = MutationsGeneratorUtils
				.formattedTimestampToLong(FORMATTED_TIMESTAMP_1);
		assertEquals(TIME_MILLIS_1, timeMillis);
	}

	/**
	 * Test that the formatted timestamps are lexicographical ordered.
	 */
	@Test
	public void testLexicographicalOrderOfFormattedTimestamps() {
		String formattedTimestamp1 = MutationsGeneratorUtils
				.longToFormattedTimestamp(TIME_MILLIS_1);
		String formattedTimestamp2 = MutationsGeneratorUtils
				.longToFormattedTimestamp(TIME_MILLIS_2);

		assertEquals(
				-1,
				Long.valueOf(TIME_MILLIS_1).compareTo(
						Long.valueOf(TIME_MILLIS_2)));
		assertEquals(-1, formattedTimestamp1.compareTo(formattedTimestamp2));

		assertEquals(FORMATTED_TIMESTAMP_2, formattedTimestamp2);
	}
}