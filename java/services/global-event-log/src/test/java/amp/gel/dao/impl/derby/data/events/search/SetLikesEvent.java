package amp.gel.dao.impl.derby.data.events.search;

import java.io.Serializable;

import amp.gel.dao.impl.derby.data.events.core.ClickstreamEvent;

/**
 * Instance of this event may be published in order to specify a set of
 * documents which the current user as elected to like or dislike. The
 * likes/dislikes service will handle such events and appropriately update the
 * likes and dislikes values for the documents in the likes/dislikes store.
 * 
 * The user who is doing the liking/disliking must be specified via the
 * inherited {@link ClickstreamEvent#setUser(orion.events.core.User)} method.
 * 
 * SetLikesEvent is a subclass of {@link ClickstreamEvent} that specifies
 * {@link Action#UPDATE} as its action.
 */
public class SetLikesEvent extends ClickstreamEvent implements Serializable {
	private static final long serialVersionUID = -4220260818541490194L;

	/**
	 * Creates a new instance of the SetLikesEvent event.
	 */
	public SetLikesEvent() {
		super(Action.UPDATE);
	}
}