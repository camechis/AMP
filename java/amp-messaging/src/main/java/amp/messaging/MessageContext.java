package amp.messaging;

import cmf.bus.Envelope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class MessageContext {

    public enum Directions { In, Out }


    private Map<String, Object> context;
    private Object message;
    private Directions direction;
    private Envelope envelope;


    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }

    public Object getMessage() { return message; }
    public void setMessage(Object message) { this.message = message; }

    public Directions getDirection() { return direction; }
    public void setDirection(Directions direction) { this.direction = direction; }

    public Envelope getEnvelope() { return this.envelope; }
    public void setEnvelope(Envelope value) { this.envelope = value; }


    public MessageContext(Directions direction) {

        this.direction = direction;
        this.context = new HashMap<String, Object>();
    }

    public MessageContext(Directions direction, Envelope envelope, Object message) {

        this.direction = direction;
        this.message = message;
        this.envelope = envelope;
        this.context = new HashMap<String, Object>();
    }

    public MessageContext(Directions direction, Envelope envelope) {

        this.direction = direction;
        this.envelope = envelope;
        this.context = new HashMap<String, Object>();
    }
}
