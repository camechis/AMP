package amp.gel.domain;

public class ColumnHeader {

	public static final ColumnHeader MIN = new ColumnHeader("Minimum",
			ColumnType.LONG);

	public static final ColumnHeader AVG = new ColumnHeader("Average",
			ColumnType.DOUBLE);

	public static final ColumnHeader MAX = new ColumnHeader("Maximum",
			ColumnType.LONG);

	public static final ColumnHeader SUM = new ColumnHeader("Sum",
			ColumnType.LONG);

	public static final ColumnHeader COUNT = new ColumnHeader("Count",
			ColumnType.LONG);

	public static final ColumnHeader TOPIC = new ColumnHeader("Topic",
			ColumnType.STRING);

	public static final ColumnHeader USER = new ColumnHeader("User",
			ColumnType.STRING);

	private String name;

	private ColumnType type;

	public ColumnHeader(String name, ColumnType type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ColumnType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ColumnHeader [name=" + name + ", type=" + type + "]";
	}
}
