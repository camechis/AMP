package amp.gel.dao.impl.derby.data.events.haruspex;

import java.io.Serializable;

/**
 * A {@link CubeRequestEvent} that adds no useful information of its own, but
 * that's enough for the listener to know what to do with it.
 */
public class CubeConfigRequestEvent extends CubeRequestEvent implements
		Serializable {

	private static final long serialVersionUID = -8474873382778303185L;

}
