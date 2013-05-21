package amp.rabbit.topology;


import java.util.ArrayList;


public class RoutingInfo {

    protected Iterable<RouteInfo> routes;

    public RoutingInfo() {
        this.routes = new ArrayList<RouteInfo>();
    }

    public RoutingInfo(Iterable<RouteInfo> routes) {
        this.routes = routes;
    }

    public Iterable<RouteInfo> getRoutes() {
        return routes;
    }

    public void setRoutes(Iterable<RouteInfo> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ \"routes\" : [");
        for (RouteInfo route : this.routes) {
            sb.append(route.toString());
        }
        sb.append("]}");

        return sb.toString();
    }
}
