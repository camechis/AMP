package amp.bus.rabbit.topology;

import java.util.Map;

/**
 * Represents an AMQP Exchange and the necessary information to construct
 * the particular exchange if it does not exist.
 * 
 * The easiest way to construct an Exchange is to use the builder:
 * 
 * Exchange.builder()
 *   .name("test-exchange")
 *   .type("topic")
 *   .vhost("test")
 *   .isDurable(true)
 *   .isAutoDelete(false)
 *   .build()
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class Exchange extends AmqpModelBase {
	
    protected String exchangeType = "direct";
    protected String virtualHost = "/";
    
    public Exchange(){}
    
    public Exchange(String exchangeName, String exchangeType,
			boolean isAutoDelete, boolean isDurable, boolean shouldDeclare,
			String virtualHost, Map<String, Object> arguments) {
		
    		super(exchangeName, isAutoDelete, isDurable, shouldDeclare, arguments);
    		
		this.exchangeType = exchangeType;
		this.virtualHost = virtualHost;
	}
    

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}
	
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((exchangeType == null) ? 0 : exchangeType.hashCode());
		result = prime * result
				+ ((virtualHost == null) ? 0 : virtualHost.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exchange other = (Exchange) obj;
		if (exchangeType == null) {
			if (other.exchangeType != null)
				return false;
		} else if (!exchangeType.equals(other.exchangeType))
			return false;
		if (virtualHost == null) {
			if (other.virtualHost != null)
				return false;
		} else if (!virtualHost.equals(other.virtualHost))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Exchange [exchangeType=" + exchangeType + ", virtualHost="
				+ virtualHost + ", name=" + name + ", isAutoDelete="
				+ isAutoDelete + ", isDurable=" + isDurable
				+ ", shouldDeclare=" + shouldDeclare + ", arguments="
				+ arguments + "]";
	}

	public static ExchangeBuilder builder(){
    	
    		return new ExchangeBuilder();
    }
	
    public static class ExchangeBuilder {
    	
    		Exchange exchange = new Exchange();
    	
    		public ExchangeBuilder name(String exchangeName){
    			
    			this.exchange.setName(exchangeName);
    			
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
		
		public ExchangeBuilder declare(boolean trueIfShouldDeclare){
			
			this.exchange.setShouldDeclare(trueIfShouldDeclare);
			
			return this;
		}
		
		public ExchangeBuilder vhost(String vhost){
			
			this.exchange.setVirtualHost(vhost);
			
			return this;
		}
		
		public ExchangeBuilder arguments(Map<String, Object> arguments){
			
			if (arguments != null){
			
				this.exchange.setArguments(arguments);
			}
			return this;
		}
    		
		public Exchange build(){
			
			return this.exchange;
		}
    }
}