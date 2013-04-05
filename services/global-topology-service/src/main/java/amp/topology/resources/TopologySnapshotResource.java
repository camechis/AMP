package amp.topology.resources;

import java.io.IOException;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.annotation.Timed;

import amp.topology.core.repo.snapshot.TopologySnapshotUtility;

@Path("/snapshot")
@Produces(MediaType.APPLICATION_JSON)
public class TopologySnapshotResource {

	private static final Logger logger = LoggerFactory.getLogger(TopologySnapshotResource.class);
	
	TopologySnapshotUtility snapshotUtility;
	
	public TopologySnapshotResource(TopologySnapshotUtility snapshotUtility){
		
		this.snapshotUtility = snapshotUtility;
	}
	
	@GET
	@Path("/format")
	@Timed
	public String getSerializationFormat(){
		
		logger.info("Getting snapshot format.");
		
		return String.format("{ \"format\": \"%s\" }", this.snapshotUtility.getSnapshotFileFormat());
	}
	
	@GET
	@Path("/files")
	@Timed
	public Collection<String> getSnapshots(){
		
		logger.info("Getting snapshots");
		
		return this.snapshotUtility.getSnapshotFiles();
	}
	
	@GET
	@Timed
	@Path("/file/{snapshot}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSnapshot(@PathParam("snapshot") String snapshotName) throws IOException{
		
		logger.info("Getting snapshot with name: {}", snapshotName);
		
		return this.snapshotUtility.getSnapshotFileContents(snapshotName);
	}
	
	@POST
	@Timed
	@Path("/import/{snapshot}")
	public Response importSnapshot(@PathParam("snapshot") String snapshotName) throws IOException{
		
		logger.info("Importing snapshot: {}", snapshotName);
		
		this.snapshotUtility.importFileIntoRepository(snapshotName);
		
		return Response.ok().build();
	}
	
	@POST
	@Timed
	@Path("/import-current")
	public Response importSnapshot() throws IOException{
		
		logger.info("Importing current snapshot.");
		
		this.snapshotUtility.importFileIntoRepository();
		
		return Response.ok().build();
	}
	
	@POST
	@Timed
	@Path("/import")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response importSnapshotFromString(String snapshot) throws IOException{
		
		logger.info("Importing snapshot from form.");
		
		this.snapshotUtility.importSerializedSnapshotIntoRepository(snapshot);
		
		return Response.ok().build();
	}
	
	@POST
	@Timed
	@Path("/merge/{snapshot}")
	public Response mergeSnapshot(@PathParam("snapshot") String snapshotName) throws IOException{
		
		logger.info("Merging snapshot: {}", snapshotName);
		
		this.snapshotUtility.mergeFileIntoRepository(snapshotName);
		
		return Response.ok().build();
	}
	
	@POST
	@Timed
	@Path("/merge-current")
	public Response mergeSnapshot() throws IOException{
		
		logger.info("Merging current snapshot.");
		
		this.snapshotUtility.mergeFileIntoRepository();
		
		return Response.ok().build();
	}
	
	@POST
	@Timed
	@Path("/merge")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response mergeSnapshotFromString(String snapshot) throws IOException{
		
		logger.info("Merging snapshot from form.");
		
		this.snapshotUtility.mergeSerializedSnapshotIntoRepository(snapshot);
		
		return Response.ok().build();
	}
	
	
	@POST
	@Timed
	@Path("/export")
	public Response exportSnapshot() throws IOException{
		
		logger.info("Exporting snapshot");
		
		this.snapshotUtility.exportSnapshotToFile();
		
		return Response.ok().build();
	}
}
