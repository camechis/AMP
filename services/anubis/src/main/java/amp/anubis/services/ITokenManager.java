package amp.anubis.services;

import amp.anubis.core.NamedToken;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Defines the interface for a component that manages authentication tokens.
 */
public interface ITokenManager {

    public NamedToken generateToken(UserDetails requestor) throws IllegalArgumentException;

    public boolean verifyToken(NamedToken token) throws IllegalArgumentException;
}
