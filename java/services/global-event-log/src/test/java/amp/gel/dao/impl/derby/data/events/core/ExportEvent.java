package amp.gel.dao.impl.derby.data.events.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A generic clickstream event to capture the export of information. Can be for
 * emailing a link, actually downloading a document, and other exporty things.
 */
public class ExportEvent extends ClickstreamEvent {

	private static final long serialVersionUID = -4241312159038855860L;

	/*
	 * ID, whatever an ID means, for the thing that was exported. Initially some
	 * kind of document id, but could be a chart, etc
	 */
	private String exportedId;

	/*
	 * Description of what was exported.
	 */
	private String description;

	/**
	 * Creates a new instance of the ExportEvent type.
	 */
	public ExportEvent() {
		super(Action.EXPORT);
	}

	/**
	 * Produces a meaningful rendering of the event including the exportId and
	 * description.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.MULTI_LINE_STYLE);
		builder.appendSuper(super.toString());
		builder.append("exportedId", exportedId);
		builder.append("description", description);
		return builder.toString();
	}

	/**
	 * Returns the ID of the item exported, whatever ID means in the context of
	 * what was exported. E.g. the document Id of the document exported.
	 * 
	 * @return The ID of the exported resource.
	 */
	public String getExportedId() {
		return exportedId;
	}

	/**
	 * Specifies an ID that adauately identifies the item exported.
	 * 
	 * @param exportedId
	 *            The value to set the exportId to.
	 */
	public void setExportedId(String exportedId) {
		this.exportedId = exportedId;
	}

	/**
	 * Returns a textual description of whatever was exported.
	 * 
	 * @return The description of what was exported.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Specifies a textual description for whatever was exported.
	 * 
	 * @param description
	 *            The value to set the description to.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
