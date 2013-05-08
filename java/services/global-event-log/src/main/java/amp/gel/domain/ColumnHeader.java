package amp.gel.domain;

public class ColumnHeader {

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
