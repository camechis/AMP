package amp.actors;

/**
 * Created with IntelliJ IDEA.
 * User: jruiz
 * Date: 4/26/13
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveAttributeRequest {

    private Actor _actor;
    private String _attributeName;


    public Actor getActor() { return _actor; }
    public void setActor(Actor value) { _actor = value; }

    public String getAttributeName() { return _attributeName; }
    public void setAttributeName(String value) { _attributeName = value; }


    public RemoveAttributeRequest() {}

    public RemoveAttributeRequest(Actor actor, String attributeName) {
        _actor = actor;
        _attributeName = attributeName;
    }
}
