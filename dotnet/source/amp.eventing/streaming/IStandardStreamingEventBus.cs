using cmf.bus;
using cmf.eventing.patterns.streaming;
using System;
using System.Collections.Generic;

namespace amp.eventing.streaming
{
    public interface IStandardStreamingEventBus : IStreamingEventBus 
    {
        IList<IEventProcessor> InboundProcessors { get; }
        IList<IEventProcessor> OutboundProcessors { get; }
        IEnvelopeBus EnvelopeBus { get; }
        void ProcessEvent(EventContext context, IEnumerable<IEventProcessor> processorChain, Action processingComplete);
    }
}
