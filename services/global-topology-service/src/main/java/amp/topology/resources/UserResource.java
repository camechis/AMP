package amp.topology.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rabbitmq.mgmt.model.User;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.model.UserRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/clusters/{cluster}/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	UserRepository userRepository;
	
	public UserResource(UserRepository userRepository){
		
		this.userRepository = userRepository;
	}
	
	@GET
    @Timed
	public Collection<User> getUsers(@PathParam("cluster") String cluster) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all users on {}.", cluster);
		
		return userRepository.all(cluster);
	}
	
	@GET
	@Path("/{name}")
    @Timed
	public User getUser(@PathParam("cluster") String cluster, @PathParam("name") String username) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting all user {} on {}.", username, cluster);
		
		return userRepository.get(cluster, username);
	}
	
	@PUT
	@Path("/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response createUser(@PathParam("cluster") String cluster, User user) 
			throws ClusterDoesntExistException{
		
		logger.info("Creating user {} on cluster {}.", user.getName(), cluster);
		
		userRepository.create(cluster, user);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{name}")
	@Timed
	public Response deleteUser(@PathParam("cluster") String cluster, @PathParam("name") String username) 
			throws ClusterDoesntExistException{
		
		logger.info("Deleting user {} on cluster {}.", username, cluster);
		
		userRepository.delete(cluster, username);
		
		return Response.ok().build();
	}
	
}
