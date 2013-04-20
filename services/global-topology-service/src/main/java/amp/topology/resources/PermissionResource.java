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

import rabbitmq.mgmt.model.Permission;
import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.core.repo.UserRepository;

import com.yammer.metrics.annotation.Timed;

@Path("/clusters/{cluster}/permissions")
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	UserRepository userRepository;
	
	public PermissionResource(UserRepository userRepository){
		
		this.userRepository = userRepository;
	}
	
	@GET
	@Path("/user/{name}")
    @Timed
	public Collection<Permission> getPermissions(@PathParam("cluster") String cluster, @PathParam("name") String username) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting permissions for user {} on {}.", username, cluster);
		
		return userRepository.getPermissions(cluster, username);
	}
	
	
	@DELETE
	@Path("/vhost/{vhost}/user/{name}")
    @Timed
	public Response removePermission(
			@PathParam("cluster") String cluster, 
			@PathParam("vhost") String vhost, 
			@PathParam("name") String username) 
			throws ClusterDoesntExistException{
		
		logger.info("Removing permissions for user {} on {} at {}.", 
			new Object[]{ username, cluster, vhost });
		
		userRepository.removePermission(cluster, vhost, username);
		
		return Response.ok().build();
	}
	
	@PUT
	@Path("/vhost/{vhost}/user/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Timed
	public Response setPermission(
			@PathParam("cluster") String cluster, 
			@PathParam("vhost") String vhost, 
			@PathParam("name") String username,
			Permission permission) 
			throws ClusterDoesntExistException{
		
		logger.info("Setting permissions for user {} on cluster {} at {}.", 
				new Object[]{ username, cluster, vhost });
		
		userRepository.setPermission(cluster, permission);
		
		return Response.ok().build();
	}
	
}
