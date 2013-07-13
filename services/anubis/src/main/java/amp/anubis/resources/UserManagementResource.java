package amp.anubis.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

@Path("/manage/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserManagementResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserManagementResource.class);


    @GET
    @Path("/who-am-i")
    @Timed
    public String getRequestorIdentity(@Auth UserDetails requestor) {

        LOG.debug("Enter getRequestorIdentity, returning {}", requestor.getUsername());
        return requestor.getUsername();
    }
}
