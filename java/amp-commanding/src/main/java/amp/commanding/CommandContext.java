package amp.commanding;

import cmf.bus.Envelope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class CommandContext {

    public enum Directions { In, Out }


    private Map<String, Object> context;
    private Object command;
    private Directions direction;
    private Envelope envelope;


    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }

    public Object getCommand() { return command; }
    public void setCommand(Object command) { this.command = command; }

    public Directions getDirection() { return direction; }
    public void setDirection(Directions direction) { this.direction = direction; }

    public Envelope getEnvelope() { return this.envelope; }
    public void setEnvelope(Envelope value) { this.envelope = value; }


    public CommandContext(Directions direction) {

        this.direction = direction;
        this.context = new HashMap<String, Object>();
    }

    public CommandContext(Directions direction, Object command, Envelope envelope) {

        this.direction = direction;
        this.command = command;
        this.envelope = envelope;
        this.context = new HashMap<String, Object>();
    }

    public CommandContext(Directions direction, Envelope envelope) {

        this.direction = direction;
        this.envelope = envelope;
        this.context = new HashMap<String, Object>();
    }
}
