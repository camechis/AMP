using cmf.bus;
using cmf.eventing;
using cmf.eventing.patterns.streaming;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.eventing.streaming
{
    public class DefaultStreamingBus : DefaultEventBus, IStandardStreamingEventBus
    {
        private IEventStreamFactory _eventStreamFactory;
        private Dictionary<string, IEventStream> _eventStreams;
        
        protected int _batchLimit = 10;

        public DefaultStreamingBus(IEnvelopeBus envelopeBus) : base(envelopeBus)
        {
            InboundChain = new Dictionary<int, IEventProcessor>();
            OutboundChain = new Dictionary<int, IEventProcessor>();

            InitializeDefaults();
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus, 
                                    IEventStreamFactory eventStreamFactory) : base(envelopeBus)
        {
            _eventStreamFactory = eventStreamFactory;
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

        public void RemoveStream(string topic) 
        {
            if (_eventStreams.ContainsKey(topic))
            {
                _eventStreams.Remove(topic);
            }
        }

        public void PublishChunkedSequence<TEvent>(IEnumerable<object> dataSet,
                                            Func<object, TEvent> objectMapper)
        {
            _log.Debug("enter publish to chunked sequence");
            IEventStream eventStream = null;
            string topic = null;

            ValidateEventEnumerator(dataSet);
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
                        _eventStreams.Add(topic, eventStream);
                    }

                    eventStream.Publish(item);
                }
            }
            finally
            {
                if (null != eventStream)
                {
                    eventStream.Dispose();
                    RemoveStream(topic);
                }
            }
            _log.Debug("leave publish to chunked sequence");
        }

        public int BatchLimit 
        { 
            set 
            {
                if (value <= 0)
                {
                    _log.Warn("Message batch limit cannot be less than or equal to zero, using 10 as the default limit.");
                    _batchLimit = 10;
                }
                else
                {
                    _batchLimit = value;
                }
            } 
        }

        private void ValidateEventEnumerator(IEnumerable<object> eventEnumerator)
        {
            if (null == eventEnumerator)
            {
                throw new ArgumentException("Cannot publish from a null event iterator.");
            }
        }

        private bool IsValidMapper<TEvent>(Func<object, TEvent> objectMapper)
        {
            bool doMap = true;
            if (null == objectMapper) 
            {
                _log.Warn("No object mapper supplied. Will not perform transformational mapping of elements in chunked sequence");
                doMap = false;
            }
            return doMap;
        }

        public void SubscribeToCollection<TEvent>(IStreamingCollectionHandler<TEvent> handler)
        {
            StreamingCollectionRegistration<TEvent> registration = new StreamingCollectionRegistration<TEvent>(handler, this.InterceptEvent);
            _envBus.Register(registration);
        }

        public void SubscribeToReader<TEvent>(IStreamingReaderHandler<TEvent> handler)
        {
            StreamingReaderRegistration<TEvent> registration = new StreamingReaderRegistration<TEvent>(handler, this.InterceptEvent);
            _envBus.Register(registration);
        }

        
        public IList<IEventProcessor> InboundProcessors
        {
            get
            {
                return new List<IEventProcessor>(InboundChain.Sort());
            }
        }

        public IList<IEventProcessor> OutboundProcessors
        {
            get
            {
                return new List<IEventProcessor>(OutboundChain.Sort());
            }
        }

        public IEnvelopeBus EnvelopeBus
        {
            get
            {
                return _envBus;
            }
        }

        public override object InterceptEvent(IEventHandler handler, Envelope env)
        {
            _log.Debug("Enter InterceptEvent");

            var context = new EventContext(EventContext.Directions.In, env);

            this.ProcessEvent(context, this.InboundChain.Sort(), () =>
            {
                _log.Info("Completed inbound processing - invoking IEventHandler");

            });

            _log.Debug("Leave InterceptEvent");
            return context.Event;
        }
        
    }
}