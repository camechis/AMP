package amp.commanding;

import cmf.bus.Envelope;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IEnvelopeInterceptor {

    void interceptEnvelope(Envelope envelope);
}
