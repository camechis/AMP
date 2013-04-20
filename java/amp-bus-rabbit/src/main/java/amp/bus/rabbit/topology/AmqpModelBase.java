package amp.bus.rabbit.topology;

import java.util.Map;

/**
 * Queues and Exchanges share a number of properties, which
 * are modeled here for the purposes of cleaning up the Queue and Exchange
 * classes.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public abstract class AmqpModelBase {

	protected String name;
    protected boolean isAutoDelete = false;
    protected boolean isDurable = false;
    protected boolean shouldDeclare = true;
	protected Map<String, Object> arguments;
	
	public AmqpModelBase(){}
	
	public AmqpModelBase(
			String name, boolean isAutoDelete, boolean isDurable, 
			boolean shouldDeclare, Map<String, Object> arguments) {
		
		this.name = name;
		this.isAutoDelete = isAutoDelete;
		this.isDurable = isDurable;
		this.shouldDeclare = shouldDeclare;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAutoDelete() {
		return isAutoDelete;
	}

	public void setAutoDelete(boolean isAutoDelete) {
		this.isAutoDelete = isAutoDelete;
	}

	public boolean isDurable() {
		return isDurable;
	}

	public void setDurable(boolean isDurable) {
		this.isDurable = isDurable;
	}
	
	public boolean shouldDeclare() {
		return shouldDeclare;
	}

	public void setShouldDeclare(boolean shouldDeclare) {
		this.shouldDeclare = shouldDeclare;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public void setArguments(Map<String, Object> arguments) {
		this.arguments = arguments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + (isAutoDelete ? 1231 : 1237);
		result = prime * result + (isDurable ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (shouldDeclare ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmqpModelBase other = (AmqpModelBase) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (isAutoDelete != other.isAutoDelete)
			return false;
		if (isDurable != other.isDurable)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (shouldDeclare != other.shouldDeclare)
			return false;
		return true;
	}

}
