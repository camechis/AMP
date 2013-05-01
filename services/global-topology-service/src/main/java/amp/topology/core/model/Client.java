package amp.topology.core.model;

import java.util.ArrayList;
import java.util.Collection;

public class Client extends TopologyModel {
	
	protected String principalName;
	protected ArrayList<String> authorities = new ArrayList<String>();
	
	public Client(){}
	
	public Client(
			String id, String description, String principalName, Collection<String> authorities) {
		
		super(id, description);
		
		this.principalName = principalName;
		setAuthorities(authorities);
	}
	
	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String name) {
		this.principalName = name;
	}

	public ArrayList<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<String> authorities) {
		if (authorities != null){
			this.authorities.addAll(authorities);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + ((principalName == null) ? 0 : principalName.hashCode());
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
		Client other = (Client) obj;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (principalName == null) {
			if (other.principalName != null)
				return false;
		} else if (!principalName.equals(other.principalName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Client [name=" + principalName + ", authorities=" + authorities
				+ ", id=" + id + ", description=" + description + "]";
	}
}
