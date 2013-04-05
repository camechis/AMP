package amp.examples.adaptor.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.security.core.userdetails.UserDetails;

import amp.examples.adaptor.integration.ResultsQueue;
import amp.examples.adaptor.views.ResultQueueView;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
public class ResultsResource {

	ResultsQueue results;
	
	public ResultsResource(ResultsQueue results){
		
		this.results = results;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
    @Timed
	public ResultQueueView getForm(@Auth UserDetails user){

		return new ResultQueueView(results, user);
	}
	
	@DELETE
	@Timed
	public Response clearQueue(){
		
		results.clear();
		
		return Response.ok().build();
	}
}
