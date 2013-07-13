package amp.topology.resources;


import amp.rabbit.topology.RouteInfo;
import amp.topology.core.RouteCreator;
import amp.topology.core.TopologyRequestContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/fallbackRouting")
@Produces(MediaType.APPLICATION_JSON)
public class FallbackRoutingResource {

    /*
    For some reason rmq doesn't urlencode arguments when sending them over the wire using the
    management API. Therefore we need to intercept the deserialization and urlencode some properties so they don't
    make rmq explode...
    */
    static class URLEncodingDeserializer extends JsonDeserializer<String>{
        @Override
        public String deserialize(JsonParser jsonParser,
          DeserializationContext deserializationContext) throws IOException {
            return URLEncoder.encode(jsonParser.getValueAsString(), StandardCharsets.UTF_8.toString());
        }
    }
    /*
    We want to reuse the exchange model from AMP but it is not annotated to be used with jackson. This is a simple
    wrapper to enhance the exchange model with stuff we need to deserialize the json correctly
     */
    @XmlRootElement
    static class Exchange extends amp.rabbit.topology.Exchange{
        @JsonProperty("name")
        private String name;

        @JsonProperty("vHost")
        private String vHost;

        //i think this is the only field w/ weird characters... but if necessary i can urlencode the rest of them...
        @JsonDeserialize(using=URLEncodingDeserializer.class)
        private String queueName;

        public Exchange(){
            super(null,null,"/",0,null,null,null,false,false,null);
        }
        public Exchange(String name, String hostName, String vHost, int port, String routingKey, String queueName, String exchangeType, boolean isDurable, boolean autoDelete, Map arguments){
            super(name, hostName, vHost, port, routingKey, queueName, exchangeType, isDurable, autoDelete, arguments);
        }
    }
    private RouteCreator routeCreator;
    private static final Logger logger = LoggerFactory.getLogger(FallbackRoutingResource.class);
    public static final String HEADER_PREFERRED_QUEUENAME = "amp.topology.request.prefs.queue.name";
    public static final String HEADER_PREFERRED_QUEUE_PREFIX = "amp.topology.request.prefs.queue.prefix";

    public FallbackRoutingResource(RouteCreator routeCreator) {
        this.routeCreator = routeCreator;
    }

    /*
    a simple passthrough service to call the GTS routecreator for fallback route creation
     */
    @GET
    @Timed
    @Path("/routeCreator")
    public Object routeCreator(@Auth UserDetails userDetails, @QueryParam(value="callback") String callback, @QueryParam(value="data") String exchangeIn) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> routingHints = new HashMap<String, String>();
        try{
            Exchange exchange = mapper.readValue(exchangeIn, Exchange.class);

            String client = userDetails.getUsername();
            String topic = exchange.getQueueName();
            ArrayList<RouteInfo> routeInfos = new ArrayList<RouteInfo>();

            routeInfos.add(new RouteInfo(exchange, exchange));

            TopologyRequestContext context =
                new TopologyRequestContext(
                    client, topic,
                    routingHints.get(HEADER_PREFERRED_QUEUE_PREFIX),
                    routingHints.get(HEADER_PREFERRED_QUEUENAME));
            routeCreator.create(routeInfos, context);

            if(callback != null && callback.length()>0){
                return new JSONPObject(callback, Response.ok().build());
            }
            return Response.ok().build();

        }catch(Exception e){
            e.printStackTrace();
        }
        return Response.serverError().build();
    }
}
