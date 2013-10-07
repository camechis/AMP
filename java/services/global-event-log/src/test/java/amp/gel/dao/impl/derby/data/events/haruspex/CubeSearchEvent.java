package amp.gel.dao.impl.derby.data.events.haruspex;

/**
 * A {@link CubeRequestEvent} to search for cube data.
 */
public class CubeSearchEvent extends CubeRequestEvent {

	private static final long serialVersionUID = 1640352676875285328L;

	/** The name of the cube being requested. */
	private String cubeName;

	/**
	 * Get the name of the cube.
	 * 
	 * @return the cube's name. It may be {@code null}.
	 */
	public String getCubeName() {
		return cubeName;
	}

	/**
	 * Set the cube's name.
	 * 
	 * @param cubeName
	 *            the cube's name. This is <strong>not</strong> validated.
	 */
	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}
}
