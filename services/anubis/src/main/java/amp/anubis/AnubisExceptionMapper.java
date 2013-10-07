package amp.anubis;


import amp.anubis.core.AnubisException;import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;import java.lang.Override;

public class AnubisExceptionMapper implements ExceptionMapper<AnubisException> {

    @Override
    public Response toResponse(AnubisException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
