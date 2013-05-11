package amp.bus.rabbit;

import amp.rabbit.topology.RoutingInfo;

/**
 * Created with IntelliJ IDEA.
 * User: John
 * Date: 5/11/13
 */
public interface IRoutingInfoCache {

    RoutingInfo getIfPresent(String topic);

    void put(String topic, RoutingInfo routingInfo);
}
