package amp.esp.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yammer.metrics.annotation.Timed;

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
public class DemoESPResource {

    @GET
    @Timed
	public String doDemo(){

		return String.format("Demo method called");
	}
}