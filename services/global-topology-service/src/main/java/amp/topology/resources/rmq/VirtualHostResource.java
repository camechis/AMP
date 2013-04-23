package amp.topology.resources.rmq;

import java.util.Collection;

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

import com.yammer.metrics.annotation.Timed;

import rabbitmq.mgmt.model.Permission;
import rabbitmq.mgmt.model.Status;
import rabbitmq.mgmt.model.VirtualHost;

import amp.topology.core.model.ClusterDoesntExistException;
import amp.topology.rmq.repo.VirtualHostRepository;

@Path("/rmq/clusters/{cluster}/vhosts")
@Produces(MediaType.APPLICATION_JSON)
public class VirtualHostResource {

	private static final Logger logger = LoggerFactory.getLogger(VirtualHostResource.class);
	
	VirtualHostRepository virtualHostRepository;
	
	public VirtualHostResource(VirtualHostRepository virtualHostRepository){
		
		this.virtualHostRepository = virtualHostRepository;
	}
	
	@GET
	@Timed
	public Collection<VirtualHost> getVirtualHosts(@PathParam("cluster") String cluster) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting virtual hosts for cluster {}.", cluster);
		
		return this.virtualHostRepository.getVirtualHosts(cluster);
	}
	
	
	@GET
	@Path("/{vhost}/permissions")
	@Timed
	public Collection<Permission> getPermissions(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting permissions for virtual host {} on cluster {}.", vhost, cluster);
		
		return this.virtualHostRepository.getPermissions(cluster, vhost);
	}
	
	@PUT
	@Path("/{vhost}/permissions")
	@Timed
	public Response setPermission(
			@PathParam("cluster") String cluster, Permission permission) 
			throws ClusterDoesntExistException{
		
		logger.info("Setting permissions for virtual host {} on cluster {}.", permission.getVhost(), cluster);
		
		this.virtualHostRepository.setPermission(cluster, permission);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{vhost}/permissions/{user}")
	@Timed
	public Response removePermission(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost, @PathParam("user") String user) 
			throws ClusterDoesntExistException{
		
		logger.info("Removing permissions for user {} on virtual host {} on cluster {}.", 
				new Object[]{ user, vhost, cluster });
		
		this.virtualHostRepository.removePermission(cluster, vhost, user);
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/{vhost}/status")
	@Timed
	public Status getStatus(
			@PathParam("cluster") String cluster, @PathParam("vhost") String vhost) 
			throws ClusterDoesntExistException{
		
		logger.info("Getting status for virtual host {} on cluster {}.", vhost, cluster);
		
		return this.virtualHostRepository.getStatus(cluster, vhost);
	}
}