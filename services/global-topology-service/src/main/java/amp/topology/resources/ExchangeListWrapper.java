package amp.topology.resources;

import java.util.List;

import amp.topology.core.ExtendedExchange;


public class ExchangeListWrapper {

	private List<ExtendedExchange> exchange;
	
	public ExchangeListWrapper(List<ExtendedExchange> exchange) {
		this.exchange = exchange;
	}

	public List<ExtendedExchange> getExchange() {
		return exchange;
	}

	public void setExchange(List<ExtendedExchange> exchange) {
		this.exchange = exchange;
	}

}
