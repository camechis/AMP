package amp.esp.service.resources;

import amp.esp.EventStreamProcessor;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.yammer.metrics.annotation.Timed;

@Path("/monitors")
@Produces(MediaType.APPLICATION_JSON)
public class ESPResource {

    EventStreamProcessor engine = null;

	public EventStreamProcessor getEngine() {
        return engine;
    }

    public void setEngine(EventStreamProcessor engine) {
        this.engine = engine;
    }

    @GET
	@Path("/list")
    @Timed
	public String listMonitors(){

		return String.format("Engine: " + engine);
	}

    @GET
    public String showDate(){

        return String.format("it is now " + new Date());
    }
}