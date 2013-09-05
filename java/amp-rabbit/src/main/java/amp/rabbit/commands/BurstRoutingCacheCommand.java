package amp.rabbit.commands;


import java.util.Map;

import amp.rabbit.topology.RoutingInfo;


/**
 * Hello world!
 *
 */
public class BurstRoutingCacheCommand
{
    private Map<String, RoutingInfo> _routingInfo;


    public Map<String, RoutingInfo> getNewRoutingInfo() { return _routingInfo; }
    public void setNewRoutingInfo(Map<String, RoutingInfo> value) { _routingInfo = value; }


    public BurstRoutingCacheCommand() {

    }

    public BurstRoutingCacheCommand(Map<String, RoutingInfo> routingInfo) {
        _routingInfo = routingInfo;
    }
}
