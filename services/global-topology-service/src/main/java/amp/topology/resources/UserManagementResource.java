package amp.topology.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserManagementResource {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementResource.class);
	
	@GET
	@Timed
	@Path("/who-am-i")
	public String getRoutingInfo(@Auth UserDetails client){
	
		logger.info("Who Am I? Request from {}.", client.getUsername());
		
		// Sorry, I can't justify doing something more complex!
		return String.format("{ \"user\": \"%s\" }", client.getUsername());
	}
	
}
