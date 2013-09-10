package amp.messaging;


import java.util.Map;

import cmf.bus.Envelope;


/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public interface IMessageHandler<TMESSAGE> {

	/**
	 * Returns the type of the message which the handler is intended to receive.  
	 * Must be a non-abstract,  non-generic type.  Only messages of the exact type
	 * will be received. I.e. messages that are sub-types of the returned type 
 	 * will not be received.
	 * 
	 * @return The Class of the message to be handled.
	 */
    Class<TMESSAGE> getMessageType();

    /**
     * The method that processes the received message.
     * 
     * @param message The message to be processed.
     * @param headers The {@link cmf.bus.Envelope} headers of the envelope in which
     * the message arrived. 
     * 
     * @return An object indicating the outcome of handling the message.
     */
    Object handle(TMESSAGE message, Map<String, String> headers);

    /**
     * This method will be invoked when an exception occurs attempting to handle an 
     * envelope that meets the registration and filter criteria.
     * 
     * @param env The envelope in which the message was received.
     * @param ex The exception that occurred.
     * 
     * @return An object indicating the outcome of handling the envelope.
     */
    Object handleFailed(Envelope envelope, Exception e);
}
