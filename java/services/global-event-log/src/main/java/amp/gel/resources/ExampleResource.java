package amp.gel.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.security.core.userdetails.UserDetails;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

@Path("/example")
@Produces(MediaType.APPLICATION_JSON)
public class ExampleResource {

	
	@GET
	@Path("/{name}")
    @Timed
	public String sayHello(@PathParam("name") String name){

		return String.format("Hello %s", name);
	}
	
	@GET
	@Timed
	public String sayHelloAuthenticated(@Auth UserDetails user){

		return String.format("Hello %s", user.getUsername());
	}
}