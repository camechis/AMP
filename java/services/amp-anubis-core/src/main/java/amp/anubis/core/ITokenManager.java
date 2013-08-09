package amp.anubis.core;

import amp.anubis.core.NamedToken;
import org.springframework.security.core.userdetails.UserDetails;import java.lang.IllegalArgumentException;


/**
 * Defines the interface for a component that manages authentication tokens.
 */
public interface ITokenManager {

    public NamedToken generateToken(UserDetails requestor) throws IllegalArgumentException;

    public boolean verifyToken(NamedToken token) throws IllegalArgumentException;
}
