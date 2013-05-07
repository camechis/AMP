package amp.gel.dao.impl.derby.data.events.haruspex;

import java.io.Serializable;

import amp.gel.dao.impl.derby.data.events.search.SearchEvent;

/**
 *  Base class for cube request events. Perhaps it should be marked {@code
 *  abstract}. Right now, it doesn't add anything to the parent {@link
 *  SearchEvent}.
 */
public class CubeRequestEvent extends SearchEvent implements Serializable {

    private static final long serialVersionUID = -8474873382778303175L;
}