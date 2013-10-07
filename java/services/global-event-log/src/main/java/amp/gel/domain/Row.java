package amp.gel.domain;

import java.util.Arrays;

public class Row {
	private String[] values;

	public Row(String[] values) {
		super();
		this.values = values;
	}

	public Row(Object[] objects) {
		super();
		this.values = convertObjectsToStrings(objects);
	}

	public String[] getValues() {
		return values;
	}

	static private String[] convertObjectsToStrings(Object[] objects) {
		String[] values = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			values[i] = (objects[i] != null) ? objects[i].toString() : "0";
		}
		return values;
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}
}
