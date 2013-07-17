package amp.anubis.core;

public class AnubisException extends Throwable {

    public AnubisException() {
        super();
    }

    public AnubisException(String message) {
        super(message);
    }

    public AnubisException(String message, Throwable cause) {
        super(message, cause);
    }
}
