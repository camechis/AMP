package amp.anubis.resources;

import org.springframework.security.core.userdetails.UserDetails;

public interface IRabbitAccessManager {

    public String authorizeUser(String userDetails, String password);

    public String authorizeVHost(UserDetails userDetails, String vhost);

    public String authorizeResource(UserDetails userDetails, String vhost, String resource, String name, String permission);
}
