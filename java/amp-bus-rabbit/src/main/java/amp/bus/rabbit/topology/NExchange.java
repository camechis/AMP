package amp.bus.rabbit.topology;

import java.util.Map;

public class NExchange {

	protected String exchangeName;
    protected String exchangeType = "direct";
    protected boolean isAutoDelete = false;
    protected boolean isDurable = false;
    protected String virtualHost = "/";
	
	// Almost never used.
    @SuppressWarnings("rawtypes")
	protected Map arguments;
    
    public NExchange(){}
    
    public NExchange(String exchangeName, String exchangeType,
			boolean isAutoDelete, boolean isDurable, String virtualHost,
			@SuppressWarnings("rawtypes") Map arguments) {
		
		this.exchangeName = exchangeName;
		this.exchangeType = exchangeType;
		this.isAutoDelete = isAutoDelete;
		this.isDurable = isDurable;
		this.virtualHost = virtualHost;
		this.arguments = arguments;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
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

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	@SuppressWarnings("rawtypes")
	public Map getArguments() {
		return arguments;
	}
	
	@SuppressWarnings("rawtypes") 
	public void setArguments(Map arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		return "NExchange [exchangeName=" + exchangeName + ", exchangeType="
				+ exchangeType + ", isAutoDelete=" + isAutoDelete
				+ ", isDurable=" + isDurable + ", virtualHost=" + virtualHost
				+ ", arguments=" + arguments + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((exchangeName == null) ? 0 : exchangeName.hashCode());
		result = prime * result
				+ ((exchangeType == null) ? 0 : exchangeType.hashCode());
		result = prime * result + (isAutoDelete ? 1231 : 1237);
		result = prime * result + (isDurable ? 1231 : 1237);
		result = prime * result
				+ ((virtualHost == null) ? 0 : virtualHost.hashCode());
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
		NExchange other = (NExchange) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (exchangeName == null) {
			if (other.exchangeName != null)
				return false;
		} else if (!exchangeName.equals(other.exchangeName))
			return false;
		if (exchangeType == null) {
			if (other.exchangeType != null)
				return false;
		} else if (!exchangeType.equals(other.exchangeType))
			return false;
		if (isAutoDelete != other.isAutoDelete)
			return false;
		if (isDurable != other.isDurable)
			return false;
		if (virtualHost == null) {
			if (other.virtualHost != null)
				return false;
		} else if (!virtualHost.equals(other.virtualHost))
			return false;
		return true;
	}
	
    public static ExchangeBuilder build(){
    	
    		return new ExchangeBuilder();
    }
	
    public static class ExchangeBuilder {
    	
    		NExchange exchange = new NExchange();
    	
    		public ExchangeBuilder name(String exchangeName){
    			
    			this.exchange.setExchangeName(exchangeName);
    			
    			return this;
    		}
    		
		public ExchangeBuilder type(String exchangeType){
		    			
    			this.exchange.setExchangeType(exchangeType);
    			
    			return this;
    		}
		
		public ExchangeBuilder isDurable(boolean isDurable){
			
			this.exchange.setDurable(isDurable);
			
			return this;
		}
		
		public ExchangeBuilder isAutoDelete(boolean isAutoDelete){
			
			this.exchange.setAutoDelete(isAutoDelete);
			
			return this;
		}
		
		public ExchangeBuilder vhost(String vhost){
			
			this.exchange.setVirtualHost(vhost);
			
			return this;
		}
		
		@SuppressWarnings("rawtypes")
		public ExchangeBuilder arguments(Map arguments){
			
			this.exchange.setArguments(arguments);
			
			return this;
		}
    		
		public NExchange done(){
			
			return this.exchange;
		}
    }
}