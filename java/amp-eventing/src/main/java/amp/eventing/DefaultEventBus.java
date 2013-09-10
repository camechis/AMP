package amp.eventing;

import java.util.List;

import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageException;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultEventBus implements IEventBus {

    protected final DefaultEventProducer _eventProducer;
    protected final DefaultEventConsumer _eventConsumer;


    public DefaultEventBus(IEnvelopeBus envelopeBus, 
    		List<IMessageProcessor> inboundProcessors,
    		List<IMessageProcessor> outboundProcessors)  {
        _eventProducer = new DefaultEventProducer(envelopeBus, inboundProcessors);
        _eventConsumer = new DefaultEventConsumer(envelopeBus, outboundProcessors);
    }


	@Override
	public void publish(Object event) throws MessageException {
		_eventProducer.publish(event);
	}


	@Override
	public <TEVENT> void subscribe(IEventHandler<TEVENT> handler) throws MessageException {
		_eventConsumer.subscribe(handler);
	}


	@Override
	public <TEVENT> void subscribe(IEventHandler<TEVENT> handler,
			IEventFilterPredicate predicate) throws Exception {
		_eventConsumer.subscribe(handler, predicate);
	}


	@Override
	public void dispose() {
		_eventProducer.dispose();
		_eventConsumer.dispose();
	}
}
