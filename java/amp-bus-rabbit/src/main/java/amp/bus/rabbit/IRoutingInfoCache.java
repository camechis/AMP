package amp.bus.rabbit;

import amp.rabbit.topology.RoutingInfo;
import cmf.bus.IDisposable;

/**
 * Created with IntelliJ IDEA.
 * User: John
 * Date: 5/11/13
 */
public interface IRoutingInfoCache extends IDisposable {

    RoutingInfo getIfPresent(String topic);

    void put(String topic, RoutingInfo routingInfo);
}
