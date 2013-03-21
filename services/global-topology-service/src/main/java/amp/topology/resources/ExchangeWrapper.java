package amp.topology.resources;

import amp.topology.core.ExtendedExchange;

public class ExchangeWrapper {

	private ExtendedExchange exchange;
	
	public ExchangeWrapper(ExtendedExchange exchange) {
		this.exchange = exchange;
	}

	public ExtendedExchange getExchange() {
		return exchange;
	}

	public void setExchange(ExtendedExchange exchange) {
		this.exchange = exchange;
	}

}
