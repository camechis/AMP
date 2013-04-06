package amp.bus.rabbit.topology;


public class RouteInfo {

    protected Exchange consumerExchange;
    protected Exchange producerExchange;


    public RouteInfo() {

    }

    public RouteInfo(Exchange producerExchange, Exchange consumerExchange) {
        this.producerExchange = producerExchange;
        this.consumerExchange = consumerExchange;
    }


    public Exchange getConsumerExchange() {
        return consumerExchange;
    }

    public Exchange getProducerExchange() {
        return producerExchange;
    }

    public void setConsumerExchange(Exchange exchange) {
        consumerExchange = exchange;
    }

    public void setProducerExchange(Exchange exchange) {
        producerExchange = exchange;
    }
}
