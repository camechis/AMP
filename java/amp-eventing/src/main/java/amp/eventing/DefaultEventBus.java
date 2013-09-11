package amp.eventing;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;
import amp.messaging.IContinuationCallback;
import amp.messaging.IMessageProcessor;
import amp.messaging.MessageContext;
import amp.messaging.MessageException;
import amp.messaging.MessageContext.Directions;

/**
 * Created with IntelliJ IDEA.
 * User: jar349
 * Date: 5/1/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultEventBus implements IEventBus {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultEventBus.class);

    protected final DefaultEventConsumer _eventConsumer;
    protected final DefaultEventProducer _eventProducer;

    public DefaultEventBus(IEnvelopeBus envelopeBus, 
    		List<IMessageProcessor> inboundProcessors,
    		List<IMessageProcessor> outboundProcessors)  {
        _eventConsumer = new DefaultEventConsumer(envelopeBus, inboundProcessors);
        _eventProducer = new DefaultEventProducer(envelopeBus, outboundProcessors);
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

    public Object ProcessInbound(Envelope envelope) throws Exception {
        final MessageContext context = new MessageContext(Directions.In, envelope);

        this._eventConsumer.getMessageProcessor().processMessage(
                context,
                new IContinuationCallback() {

                    @Override
                    public void continueProcessing() {
                        LOG.info("Completed inbound processing - returning event");
                    }
                });

        return context.getMessage();
    }

	@Override
	public void dispose() {
		_eventProducer.dispose();
		_eventConsumer.dispose();
	}
}
