package amp.anubis.core;

/**
 * Immutable Value Object which associates an identity to a token.
 */
public class NamedToken {

    private String _identity;
    private String _token;


    /**
     * Gets the identity attached to the token.
     * @return
     */
    public String getIdentity() { return _identity; }

    /**
     * Gets the authentication token
     * @return
     */
    public String getToken() { return _token; }


    /**
     * Constructs a NamedToken using the given identity and token.
     * @param identity
     * @param token
     */
    public NamedToken(String identity, String token) {

        _identity = identity;
        _token = token;
    }
}
