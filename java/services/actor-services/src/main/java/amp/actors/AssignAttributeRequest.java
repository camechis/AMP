package amp.actors;

/**
 * Created with IntelliJ IDEA.
 * User: jruiz
 * Date: 4/26/13
 * Time: 11:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssignAttributeRequest {

    private Actor _actor;
    private String _attributeName;
    private String _attributeValue;


    public Actor getActor() { return _actor; }
    public void setActor(Actor value) { _actor = value; }

    public String getAttributeName() { return _attributeName; }
    public void setAttributeName(String value) { _attributeName = value; }

    public String getAttributeValue() { return _attributeValue; }
    public void setAttributeValue(String value) { _attributeValue = value; }


    public AssignAttributeRequest() {}

    public AssignAttributeRequest(Actor actor, String attributeName, String attributeValue) {
        _actor = actor;
        _attributeName = attributeName;
        _attributeValue = attributeValue;
    }
}
