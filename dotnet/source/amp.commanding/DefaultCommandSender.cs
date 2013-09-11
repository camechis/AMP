using System.Collections.Generic;
using amp.messaging;
using cmf.bus;

namespace amp.commanding
{
    public class DefaultCommandSender : MessageSender, ICommandSender
    {
        public DefaultCommandSender(IEnvelopeSender envelopeSender) 
            : base(envelopeSender)
        {
        }

        public DefaultCommandSender(IEnvelopeSender envelopeSender, List<IMessageProcessor> processingChain) 
            : base(envelopeSender, processingChain)
        {
        }
    }
}
