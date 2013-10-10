package amp.rabbit.transport;

import amp.rabbit.connection.IRabbitConnectionFactory;
import amp.rabbit.topology.Exchange;
import amp.rabbit.topology.ITopologyService;
import amp.rabbit.topology.ProducingRoute;
import amp.rabbit.topology.RouteInfo;
import amp.rabbit.topology.RoutingInfo;
import cmf.bus.Envelope;
import cmf.bus.IEnvelopeSender;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 */
public class RabbitEnvelopeSender implements IEnvelopeSender {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitEnvelopeSender.class);

    private ITopologyService _topologyService;
    private IRabbitConnectionFactory _channelFactory;


    public RabbitEnvelopeSender(ITopologyService topologyService, IRabbitConnectionFactory channelFactory) {

        _topologyService = topologyService;
        _channelFactory = channelFactory;
    }



    @Override
    public void send(Envelope envelope) throws Exception {

        LOG.debug("Enter Send");

        // first, get the topology based on the headers
        RoutingInfo routing = _topologyService.getRoutingInfo(envelope.getHeaders());

        // next, pull out all the producer exchanges
        List<ProducingRoute> proutes = routing.getProducingRoutes();


        // for each exchange, send the envelope
        for (ProducingRoute proute : proutes) {
            LOG.info("Sending to exchange: " + proute.getExchange().toString());

            Channel channel = null;

            try {
                channel = _channelFactory.getConnectionFor(proute).createChannel();
//TODO:JM      - Old Version               channel = _channelFactory.getChannelFor(ex);
//TODO:JM      + Justin Refactor Version    channel = _channelFactory.getChannelFor(proute);
//TODO:JM      + MostRecent/Ken Version     channel = _channelFactory.getConnectionFor(ex).createChannel();

                AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().build();

                Map<String, Object> headers = new HashMap<String, Object>();

                for (Map.Entry<String, String> entry : envelope.getHeaders().entrySet()) {

                    headers.put(entry.getKey(), entry.getValue());
                }

                props.setHeaders(headers);

                Exchange ex = proute.getExchange();
                Collection<String> keys = proute.getRoutingKeys();
                

                channel.exchangeDeclare(
                        ex.getName(), ex.getExchangeType(), ex.isDurable(),
                        ex.isAutoDelete(), ex.getArguments());

                for (String key: keys) {
                	channel.basicPublish(ex.getName(), key, props, envelope.getPayload());
                }



            } catch (Exception e) {
                LOG.error("Failed to send an envelope", e);
                throw e;
            }
        }

        LOG.debug("Leave Send");
    }
}
