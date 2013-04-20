package amp.bus.rabbit;


import cmf.bus.IDisposable;
import com.rabbitmq.client.Channel;

import amp.bus.rabbit.topology.ConsumingRoute;
import amp.bus.rabbit.topology.ProducingRoute;


public interface IRabbitChannelFactory extends IDisposable {

	Channel getChannelFor(ProducingRoute route) throws Exception;
	
	Channel getChannelFor(ConsumingRoute route) throws Exception;
}
