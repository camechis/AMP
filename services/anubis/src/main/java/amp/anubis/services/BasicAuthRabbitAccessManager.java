package amp.anubis.services;

import amp.anubis.core.AttributedNamedToken;
import amp.anubis.core.IRabbitAccessManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class BasicAuthRabbitAccessManager implements IRabbitAccessManager {

    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthRabbitAccessManager.class);

    private AuthenticationProvider _authProvider;


    public BasicAuthRabbitAccessManager(AuthenticationProvider authProvider) {

        _authProvider = authProvider;
    }


    public AccessLevel authorizeUser(AttributedNamedToken authzToken) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authzToken.getIdentity(),
                authzToken.getToken());

        Authentication result = _authProvider.authenticate(token);

        this.logUser(authzToken.getIdentity(), result.getAuthorities());
        return result.isAuthenticated() ? AccessLevel.ADMIN : AccessLevel.NONE;
    }

    public AccessLevel authorizeVHost(UserDetails userDetails, String vhost) {

        this.logUser(userDetails);
        return AccessLevel.USER;
    }

    public AccessLevel authorizeResource(UserDetails userDetails, String vhost, String resource, String name, String permission) {

        this.logUser(userDetails);
        return AccessLevel.USER;
    }


    private void logUser(UserDetails userDetails) {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("User %s has the following authorities:", userDetails.getUsername()));

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            builder.append(String.format(" %s", authority.getAuthority()));
        }

        LOG.debug(builder.toString());
    }

    private void logUser(String username, Collection<? extends GrantedAuthority> authorities) {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("User %s has the following authorities:", username));

        for (GrantedAuthority authority : authorities) {
            builder.append(String.format(" %s", authority.getAuthority()));
        }

        LOG.debug(builder.toString());
    }
}
