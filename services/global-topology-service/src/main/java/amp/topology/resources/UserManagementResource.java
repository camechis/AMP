package amp.topology.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.util.JSONPObject;
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
	public Object getRoutingInfo(@Auth UserDetails client, @QueryParam("callback") String callback){
	
		logger.info("Who Am I? Request from {}.", client.getUsername());
        String formattedString = String.format("{ \"user\": \"%s\" }", client.getUsername());
        if(callback != null && callback.length()>0){
            return new JSONPObject(callback, formattedString);
        }
        return formattedString;

	}
	
}
