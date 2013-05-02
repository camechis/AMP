package amp.topology.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import amp.topology.core.model.Client;

public class Utils {

	public static Map<String, String> createRoutingContext(MultivaluedMap<String, String> formParams){
	
		HashMap<String, String> routingContext = new HashMap<String, String>();
		
		for(Entry<String, List<String>> entry : formParams.entrySet()){
			
			routingContext.put(entry.getKey(), entry.getValue().get(0));
		}
		
		return routingContext;
	}
	
	public static Client convertUserDetails(UserDetails user){
		
		Client client = new Client();
		
		client.setId(user.getUsername());
		
		client.setDescription(user.toString());
		
		client.setPrincipalName(user.getUsername());
		
		client.setAuthorities(convertAuthorities(user.getAuthorities()));
		
		return client;
	}
	
	public static List<String> convertAuthorities(
			Collection<? extends GrantedAuthority> authorities){
		
		ArrayList<String> strAuthorities = new ArrayList<String>();
		
		for (GrantedAuthority authority : authorities){
			
			strAuthorities.add(authority.getAuthority());
		}
		
		return strAuthorities;
	}
	
}
