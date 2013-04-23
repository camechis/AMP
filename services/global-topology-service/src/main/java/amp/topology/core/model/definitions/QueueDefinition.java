package amp.topology.core.model.definitions;

import java.util.HashMap;

public class QueueDefinition extends BaseAmqpModelDefinition {

	protected String isExclusiveExpression;
	
	public QueueDefinition(String id, String description,
			String nameExpression, String isAutoDeleteExpression,
			String isDurableExpression, String shouldDeclareExpression,
			String virtualHostExpression,
			String isExclusiveExpression,
			HashMap<String, String> argumentExpressions) {
		
		super(id, description, nameExpression, isAutoDeleteExpression,
				isDurableExpression, shouldDeclareExpression,
				virtualHostExpression, argumentExpressions);
		
		this.isExclusiveExpression = isExclusiveExpression;
	}
	
	public String getIsExclusiveExpression() {
		return isExclusiveExpression;
	}

	@Override
	public String toString() {
		return "QueueDefinition [isExclusiveExpression="
				+ isExclusiveExpression + ", nameExpression=" + nameExpression
				+ ", isAutoDeleteExpression=" + isAutoDeleteExpression
				+ ", isDurableExpression=" + isDurableExpression
				+ ", shouldDeclareExpression=" + shouldDeclareExpression
				+ ", virtualHostExpression=" + virtualHostExpression
				+ ", argumentExpressions=" + argumentExpressions + ", id=" + id
				+ ", description=" + description + "]";
	}
}
