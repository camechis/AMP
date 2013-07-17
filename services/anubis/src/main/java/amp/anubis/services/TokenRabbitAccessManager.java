package amp.anubis.services;

import amp.anubis.core.AttributedNamedToken;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenRabbitAccessManager implements IRabbitAccessManager {

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
                if (token.hasAtLeastOneAttribute("ROLE_DOMAIN ADMINS", "ROLE_RABBITMQ ADMINS")) {
                    accessLevel = AccessLevel.ADMIN;
                }
                else if (token.hasAttribute("ROLE_RABBITMQ_MANAGERS")) {
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
