package amp.actors;

import cmf.eventing.patterns.rpc.IRpcEventBus;
import com.google.common.base.Strings;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: jruiz
 * Date: 4/26/13
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActorManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(ActorManagementService.class);

    private IRpcEventBus _eventBus;
    private long _responseTimeout;


    public ActorManagementService(IRpcEventBus eventBus, long responseTimeout) {
        _eventBus = eventBus;
    }


    public void createActor(Actor actor) throws IllegalArgumentException, ActorException {

        if (null == actor) { throw new IllegalArgumentException("Actor cannot be null"); }
        LOG.debug("Enter createActor(): {}", actor.toString());

        try {

            CreateActorResponse response = _eventBus.getResponseTo(
                    new CreateActorRequest(actor),
                    Duration.millis(_responseTimeout),
                    CreateActorResponse.class
            );

            if (null == response) {

                throw new ActorException(String.format(
                    "The server did not respond within {} seconds; check to see if the actor was in fact created.",
                    Duration.millis(_responseTimeout).getStandardSeconds())
                );
            }
        }
        catch (Exception ex) {
            String message = "Exception while getting a response to a CreateActorRequest.";
            LOG.error(message, ex);
            throw new ActorException(message, ex);
        }
    }

    public void assignAttribute(Actor actor, String attributeName, String attributeValue)
            throws IllegalArgumentException, ActorException {

        if (null == actor) { throw new IllegalArgumentException("Actor cannot be null"); }
        if (Strings.isNullOrEmpty(attributeName)) { throw new IllegalArgumentException("attributeName cannot be null or empty"); }
        if (Strings.isNullOrEmpty(attributeValue)) { throw new IllegalArgumentException("attributeValue cannot be null or empty"); }
        LOG.debug("Enter assignAttribute(): actor={}, attribute={}, value={}", Arrays.asList(actor.toString(), attributeName, attributeValue));

        try {

            AssignAttributeResponse response = _eventBus.getResponseTo(
                    new AssignAttributeRequest(actor, attributeName, attributeValue),
                    Duration.millis(_responseTimeout),
                    AssignAttributeResponse.class
            );

            if (null == response) {

                throw new ActorException(String.format(
                        "The server did not respond within {} seconds; check to see if the attribute was assigned.",
                        Duration.millis(_responseTimeout).getStandardSeconds())
                );
            }
        }
        catch (Exception ex) {
            String message = "Exception while assigning an attribute to an actor.";
            LOG.error(message, ex);
            throw new ActorException(message, ex);
        }
    }

    public void removeAttribute(Actor actor, String attributeName) throws IllegalArgumentException, ActorException {

        if (null == actor) { throw new IllegalArgumentException("Actor cannot be null"); }
        if (Strings.isNullOrEmpty(attributeName)) { throw new IllegalArgumentException("attributeName cannot be null or empty"); }
        LOG.debug("Enter removeAttribute(): actor={}, attribute={}", actor.toString(), attributeName);

        try {

            RemoveAttributeResponse response = _eventBus.getResponseTo(
                    new RemoveAttributeRequest(actor, attributeName),
                    Duration.millis(_responseTimeout),
                    RemoveAttributeResponse.class
            );

            if (null == response) {

                throw new ActorException(String.format(
                        "The server did not respond within {} seconds; check to see if the attribute was removed.",
                        Duration.millis(_responseTimeout).getStandardSeconds())
                );
            }
        }
        catch (Exception ex) {
            String message = "Exception while removing an attribute from an actor.";
            LOG.error(message, ex);
            throw new ActorException(message, ex);
        }
    }
}
