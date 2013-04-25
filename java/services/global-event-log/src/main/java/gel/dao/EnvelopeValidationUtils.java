package gel.dao;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class EnvelopeValidationUtils {

	public static boolean areHeadersValid(Map<String, String> headers) {
		return headers != null && !headers.isEmpty()
				&& EnvelopeValidationUtils.atLeastOneValidHeaderValue(headers.values());
	}

	public static boolean atLeastOneValidHeaderValue(Collection<String> values) {
		for (String value : values) {
			if (StringUtils.isNotBlank(value)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isValidTimeMillis(String receiptTime) {
		return StringUtils.isNotBlank(receiptTime)
				&& StringUtils.isNumeric(receiptTime);
	}

}
