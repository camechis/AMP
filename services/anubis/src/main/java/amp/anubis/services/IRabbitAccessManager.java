package amp.anubis.services;

import amp.anubis.core.AttributedNamedToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface IRabbitAccessManager {

    public enum AccessLevel { NONE, ADMIN, MANAGER, USER };


    public AccessLevel authorizeUser(AttributedNamedToken token);

    public AccessLevel authorizeVHost(UserDetails userDetails, String vhost);

    public AccessLevel authorizeResource(UserDetails userDetails, String vhost, String resource, String name, String permission);
}
