using cmf.eventing.patterns.streaming;

namespace amp.eventing.streaming
{
    public interface IEventStreamFactory
    {
        IStandardStreamingEventBus EventBus { set; }
        
        string Topic { set; }

        IEventStream GenerateEventStream();
    }
}
