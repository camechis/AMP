package amp.gel.domain;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private List<ColumnHeader> columnHeaders = new ArrayList<ColumnHeader>();

	private List<Row> rows = new ArrayList<Row>();

	public Table() {
		super();
	}

	public List<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}

	public void setColumnHeaders(List<ColumnHeader> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public void addRow(Row row) {
		this.rows.add(row);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("Table [\n\t columnHeaders=" + columnHeaders);
		string.append(", \n\t rows=");

		for (Row row : rows) {
			string.append("\n\t\t " + row);
		}

		string.append("]");

		return string.toString();
	}
}
