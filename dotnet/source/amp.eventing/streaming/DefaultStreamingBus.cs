using cmf.bus;
using cmf.eventing.patterns.streaming;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.eventing.streaming
{
    public class DefaultStreamingBus : DefaultEventBus, IStreamingEventBus
    {
        private IEventStreamFactory _eventStreamFactory;
        private Dictionary<string, IEventStream> _eventStreams;

        protected int _batchLimit = 10;

        public DefaultStreamingBus(IEnvelopeBus envelopeBus) : base(envelopeBus)
        {
            InitializeDefaults();
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                                    List<IEventProcessor> outboundProcessors) : 
            base(envelopeBus, inboundProcessors, outboundProcessors)
        { 
            InitializeDefaults();
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus, List<IEventProcessor> inboundProcessors,
                                    List<IEventProcessor> outboundProcessors, IEventStreamFactory eventStreamFactory) : 
            base(envelopeBus, inboundProcessors, outboundProcessors)
        {
            _eventStreamFactory = new DefaultEventStreamFactory();
            _eventStreamFactory.EventBus = this;
            _eventStreams = new Dictionary<string, IEventStream>();
        }

        private void InitializeDefaults()
        {
            _eventStreamFactory = new DefaultEventStreamFactory();
            _eventStreamFactory.EventBus = this;
            _eventStreams = new Dictionary<string,IEventStream>();
        }

        public IEventStream CreateStream(string topic)
        {
            if (false == _eventStreams.ContainsKey(topic))
            {
                _eventStreamFactory.Topic = topic;
                IEventStream eventStream = _eventStreamFactory.GenerateEventStream();
                _eventStreams.Add(topic, eventStream);
                return eventStream;
            }
            else
            {
                return _eventStreams[topic];
            }
        }

        public void removeStream(string topic) 
        {
            if (_eventStreams.ContainsKey(topic))
            {
                _eventStreams.Remove(topic);
            }
        }

        public void publishChunkedSequence<TEvent>(IEnumerable<object> dataSet,
                                            Func<object, TEvent> objectMapper)
        {
            _log.Debug("enter publish to chunked sequence");
            IEventStream eventStream = null;
            string topic = null;

            ValidateEventIterator(dataSet);
            bool doMap = IsValidMapper(objectMapper);

            try 
            {
                foreach (object eventItem in dataSet)
                {
                    object item = eventItem;
                    if (doMap)
                    { 
                        //This may look a little strange, but just reusing eventItem again for efficiency.
                        //The eventItem is now of type TEvent which was converted to conform to the sender's desired format
                        //before getting serialized
                        item = objectMapper(eventItem);
                    }

                    if (null == eventStream)
                    {
                        topic = item.GetType().ToString();
                        eventStream = new DefaultEventStream(this, topic); //Skipping use of the factory so that we ensure sequencing based event stream is used
                        eventStream.BatchLimit = _batchLimit;

                    }
                }
            }
        }
    }
}