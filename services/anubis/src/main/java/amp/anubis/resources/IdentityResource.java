package amp.anubis.resources;

import amp.anubis.core.AnubisException;
import amp.anubis.core.NamedToken;
import amp.anubis.services.ITokenManager;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * This POJO is exposed as a REST-ful
 */
@Path("/identity")
@Produces(MediaType.APPLICATION_JSON)
public class IdentityResource {

    private static final Logger Log = LoggerFactory.getLogger(IdentityResource.class);

    private ITokenManager _tokenManager;


    public IdentityResource(ITokenManager tokenManager) {

        if (null == tokenManager)
            throw new IllegalArgumentException("tokenManager cannot be null.");


        _tokenManager = tokenManager;
    }


    @GET
    @Path("/authenticate")
    @Timed
    public Object getNamedToken(
            @Auth(required = true) UserDetails requestor,
            @QueryParam("callback") String callback)
            throws AnubisException {

        Log.debug("Received a request for a NamedToken from {}", requestor.getUsername());

        // use the token manager to get a token for the requestor
        NamedToken token = _tokenManager.generateToken(requestor);

        // something is wrong if the token manager gives us null
        if (null == token)
            throw new AnubisException("The Token Manager generated a null token.");

        // build the return object based on whether a callback was passed
        return this.buildReturnObject(token, callback);
    }


    public Object buildReturnObject(Object unwrappedObject, String maybeCallback) {

        if ( (null != maybeCallback) && (maybeCallback.length() > 0) ) {
            Log.debug("Returning JSONPObject instead of naked object.");
            return new JSONPObject(maybeCallback, unwrappedObject);
        }
        else {
            return unwrappedObject;
        }
    }
}
