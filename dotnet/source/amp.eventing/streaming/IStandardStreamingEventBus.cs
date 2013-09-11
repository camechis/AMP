using amp.messaging;
using cmf.bus;
using cmf.eventing.patterns.streaming;

namespace amp.eventing.streaming
{
    public interface IStandardStreamingEventBus : IStreamingEventBus , IMessageProcessor
    {
        IEnvelopeBus EnvelopeBus { get; }
       
    }
}
