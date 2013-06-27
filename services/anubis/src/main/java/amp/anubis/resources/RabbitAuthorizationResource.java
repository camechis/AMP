package amp.anubis.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Path("/rabbitmq")
@Produces(MediaType.APPLICATION_JSON)
public class RabbitAuthorizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitAuthorizationResource.class);

    private UserDetailsService _userDetailsService;
    private IRabbitAccessManager _accessManager;


    public RabbitAuthorizationResource(UserDetailsService userDetailsService, IRabbitAccessManager accessManager) {
        _userDetailsService = userDetailsService;
        _accessManager = accessManager;
    }


    @GET
    @Path("/user")
    @Timed
    public String authorizeUser(
            @QueryParam("username") String username,
            @QueryParam("password") String password) {

        LOG.info("Received request to authorize user {}.", username);

        return _accessManager.authorizeUser(username, password);
    }

    @GET
    @Path("/vhost")
    @Timed
    public String authorizeVHost(
            @QueryParam("username") String username,
            @QueryParam("vhost") String vhost) {

        LOG.info("Received request to authorize {} to virtual host '{}'.", username, vhost);

        String response = "deny";

        try {

            UserDetails userDetails = _userDetailsService.loadUserByUsername(username);
            response = _accessManager.authorizeVHost(userDetails, vhost);
        }
        catch (UsernameNotFoundException unfe) {
            LOG.debug("user {} not found.", username);
        }

        return response;
    }

    @GET
    @Path("/resource")
    @Timed
    public String authorizeResourcePath(
            @QueryParam("username") String username,
            @QueryParam("vhost") String vhost,
            @QueryParam("resource") String resource,
            @QueryParam("name") String name,
            @QueryParam("permission") String permission) {

        LOG.info("Received request to authorize {} to {} a(n) {} named {} on virtual host {}.",
                username, permission, resource, name, vhost);

        String response = "deny";

        try {

            UserDetails userDetails = _userDetailsService.loadUserByUsername(username);
            response = _accessManager.authorizeResource(userDetails, vhost, resource, name, permission);
        }
        catch (UsernameNotFoundException unfe) {
            LOG.debug("user {} not found.", username);
        }

        return response;
    }
}
