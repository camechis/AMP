using System.Collections.Generic;
using amp.messaging;
using cmf.bus;
using cmf.eventing;

namespace amp.eventing
{
    public class DefaultEventProducer : MessageSender, IEventProducer
    {
        public DefaultEventProducer(IEnvelopeSender envelopeSender) 
            : base(envelopeSender)
        {
        }

        public DefaultEventProducer(IEnvelopeSender envelopeSender, List<IMessageProcessor> processingChain) 
            : base(envelopeSender, processingChain)
        {
        }

        public void Publish(object ev)
        {
            this.Send(ev);
        }

		public void Publish(object ev, IDictionary<string, string> headers)
		{
			this.Send(ev, headers);
		}
    }
}
