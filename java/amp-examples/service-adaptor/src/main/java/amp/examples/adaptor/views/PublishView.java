package amp.examples.adaptor.views;

import org.springframework.security.core.userdetails.UserDetails;

import com.yammer.dropwizard.views.View;

public class PublishView extends View {

	UserDetails userDetails;
	
	public PublishView(UserDetails userDetails) {
		
		super("publish.ftl");
		
		this.userDetails = userDetails;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
}