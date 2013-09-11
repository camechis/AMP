package amp.anubis.services;

import amp.anubis.core.AnubisException;
import amp.anubis.core.AttributedNamedToken;
import amp.anubis.core.IRabbitAccessManager;
import amp.anubis.core.ITokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenRabbitAccessManager implements IRabbitAccessManager {

    protected static final Logger LOG = LoggerFactory.getLogger(TokenRabbitAccessManager.class);

    protected ITokenManager _tokenManager;


    public TokenRabbitAccessManager(ITokenManager tokenManager) {

        if (null == tokenManager)
            throw new IllegalArgumentException("tokenManager cannot be null.");

        _tokenManager = tokenManager;
    }


    @Override
    public AccessLevel authorizeUser(AttributedNamedToken token) {

        AccessLevel accessLevel = AccessLevel.NONE;

        try {
            boolean verified = _tokenManager.verifyToken(token.getNamedToken());

            if (verified) {
                if (token.hasAtLeastOneAttribute("Domain Admins", "RabbitMQ Admins")) {
                    accessLevel = AccessLevel.ADMIN;
                }
                else if (token.hasAttribute("RabbitMQ Managers")) {
                    accessLevel = AccessLevel.MANAGER;
                }
                else {
                    accessLevel = AccessLevel.USER;
                }
            }
        }
        catch (NullPointerException npe) {
            // do nothing: we want AccessLevel.NONE to be returned in this case
        }
        catch (AnubisException ane) {
            // just log the exception: we want AccessLevel.NONE to be returned in this case
            LOG.warn("Exception while attempting to verify a token.  It's possible that a user is being denied " +
                "access to a resource to which they should have access.", ane);
        }

        return accessLevel;
    }

    @Override
    public AccessLevel authorizeVHost(UserDetails userDetails, String vhost) {
        return AccessLevel.USER;
    }

    @Override
    public AccessLevel authorizeResource(UserDetails userDetails, String vhost, String resource, String name, String permission) {
        return AccessLevel.USER;
    }
}
