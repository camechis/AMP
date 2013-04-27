package amp.actors;

/**
 * Created with IntelliJ IDEA.
 * User: jruiz
 * Date: 4/26/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateActorRequest {

    private Actor _actor;


    public Actor getActor() { return _actor; }
    public void setActor(Actor value) { _actor = value; }


    public CreateActorRequest() {

    }

    public CreateActorRequest(Actor actor) {
        _actor = actor;
    }
}
