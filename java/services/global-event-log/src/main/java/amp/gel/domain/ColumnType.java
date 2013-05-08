package amp.gel.domain;

public enum ColumnType {
	STRING, LONG, DOUBLE, DATE_YEAR("yyyy"), DATE_YEAR_MONTH("yyyy-MM"), DATE_YEAR_MONTH_DAY(
			"yyyy-MM-dd"), DATE_YEAR_MONTH_DAY_HOUR("YYYY-MM-dd'T'hh"), DATE_YEAR_MONTH_DAY_HOUR_MINUTE(
			"YYYY-MM-dd'T'hh:mm"), DATE_YEAR_MONTH_DAY_HOUR_SECOND(
			"YYYY-MM-dd'T'hh:mm:ss"), DATE_YEAR_MONTH_DAY_HOUR_SECOND_MILLISECOND(
			"YYYY-MM-dd'T'hh:mm:ss.SSS"), ;

	private String format = "";

	private ColumnType() {
	}

	private ColumnType(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
}
