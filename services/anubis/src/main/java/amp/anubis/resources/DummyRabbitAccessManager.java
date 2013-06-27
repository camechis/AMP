package amp.anubis.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import java.util.Collection;

public class DummyRabbitAccessManager implements IRabbitAccessManager {

    private static final Logger LOG = LoggerFactory.getLogger(DummyRabbitAccessManager.class);

    private AuthenticationProvider _authProvider;


    public DummyRabbitAccessManager(AuthenticationProvider authProvider) {

        _authProvider = authProvider;
    }


    public String authorizeUser(String username, String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication result = _authProvider.authenticate(token);

        this.logUser(username, result.getAuthorities());
        return result.isAuthenticated() ? "allow administrator management" : "deny";
    }

    @Override
    public String authorizeVHost(UserDetails userDetails, String vhost) {

        this.logUser(userDetails);
        return "allow";
    }

    @Override
    public String authorizeResource(UserDetails userDetails, String vhost, String resource, String name, String permission) {

        this.logUser(userDetails);
        return "allow";
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
