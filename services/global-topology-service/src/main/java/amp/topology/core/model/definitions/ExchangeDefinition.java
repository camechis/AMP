package amp.topology.core.model.definitions;

import java.util.HashMap;

public class ExchangeDefinition extends BaseAmqpModelDefinition {

	protected String exchangeTypeExpression;
	
	public ExchangeDefinition(){ super(); }
	
	public ExchangeDefinition(
			String id, String description,
			String nameExpression, 
			String exchangeTypeExpression,
			String virtualHostExpression,
			String isAutoDeleteExpression,
			String isDurableExpression, 
			String shouldDeclareExpression,
			HashMap<String, String> argumentExpressions) {
		
		super(id, description, nameExpression, isAutoDeleteExpression,
				isDurableExpression, shouldDeclareExpression, virtualHostExpression,
				argumentExpressions);
		
		this.exchangeTypeExpression = exchangeTypeExpression;
	}

	public String getExchangeTypeExpression() {
		return exchangeTypeExpression;
	}
	
}
