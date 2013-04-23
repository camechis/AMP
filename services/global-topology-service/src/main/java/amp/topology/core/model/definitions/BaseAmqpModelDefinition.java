package amp.topology.core.model.definitions;

import java.util.HashMap;

import amp.topology.core.model.TopologyModel;

public class BaseAmqpModelDefinition extends TopologyModel implements TopologyDefinition {

	protected String nameExpression;
    protected String isAutoDeleteExpression;
    protected String isDurableExpression;
    protected String shouldDeclareExpression;
    protected String virtualHostExpression;
	protected HashMap<String, String> argumentExpressions = new HashMap<String, String>();
	
	public BaseAmqpModelDefinition(){}
	
	public BaseAmqpModelDefinition(String id, String description,
			String nameExpression, String isAutoDeleteExpression,
			String isDurableExpression, String shouldDeclareExpression,
			String virtualHostExpression,
			HashMap<String, String> argumentExpressions) {
		
		super(id, description);
		
		this.nameExpression = nameExpression;
		this.isAutoDeleteExpression = isAutoDeleteExpression;
		this.isDurableExpression = isDurableExpression;
		this.shouldDeclareExpression = shouldDeclareExpression;
		this.virtualHostExpression = virtualHostExpression;
		this.argumentExpressions = argumentExpressions;
	}

	public String getNameExpression() {
		return nameExpression;
	}
	
	public String getIsAutoDeleteExpression() {
		return isAutoDeleteExpression;
	}
	
	public String getIsDurableExpression() {
		return isDurableExpression;
	}
	
	public String getShouldDeclareExpression() {
		return shouldDeclareExpression;
	}
	
	public String getVirtualHostExpression() {
		return virtualHostExpression;
	}
	
	public HashMap<String, String> getArgumentExpressions() {
		return argumentExpressions;
	}
}
