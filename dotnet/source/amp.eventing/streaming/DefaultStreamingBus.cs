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
        private IList<IEventProcessor> _inboundProcessors;
        private IList<IEventProcessor> _outboundProcessors;

        protected int _batchLimit = 10;

        public DefaultStreamingBus(IEnvelopeBus envelopeBus) : base(envelopeBus)
        {
            _inboundProcessors = new List<IEventProcessor>();
            _outboundProcessors = new List<IEventProcessor>();

            InitializeDefaults();
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus, 
                                    IList<IEventProcessor> inboundProcessors,
                                    IList<IEventProcessor> outboundProcessors) : base(envelopeBus)
        {
             _inboundProcessors = inboundProcessors;
            _outboundProcessors = outboundProcessors;
            
            InitializeDefaults();
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus, 
                                    IList<IEventProcessor> inboundProcessors,
                                    IList<IEventProcessor> outboundProcessors, 
                                    IEventStreamFactory eventStreamFactory) : base(envelopeBus)
        {
            _inboundProcessors = inboundProcessors;
            _outboundProcessors = outboundProcessors;
            SetBaseProcessingChains();
            _eventStreamFactory = eventStreamFactory;
            _eventStreams = new Dictionary<string, IEventStream>();
           
        }

        private void InitializeDefaults()
        {

            SetBaseProcessingChains();
            _eventStreamFactory = new DefaultEventStreamFactory();
            _eventStreamFactory.EventBus = this;
            _eventStreams = new Dictionary<string,IEventStream>();
        }

        private void SetBaseProcessingChains()
        {
            IDictionary<int, IEventProcessor> inChain = new Dictionary<int, IEventProcessor>();
            for (int i = 0; i < _inboundProcessors.Count; i++)
            {
                inChain.Add(i, _inboundProcessors[i]);
            }
            base.InboundChain = inChain;

            IDictionary<int, IEventProcessor> outChain = new Dictionary<int, IEventProcessor>();
            for (int i = 0; i < _outboundProcessors.Count; i++)
            {
                outChain.Add(i, _outboundProcessors[i]);
            }
            base.OutboundChain = outChain;
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

        public void SubscribeToReader<TEvent>(IStreamingReaderHandler<TEvent> handler) where TEvent : IStreamingEventItem<TEvent>
        {
            StreamingReaderRegistration<TEvent> registration = new StreamingReaderRegistration<TEvent>(handler, this.InterceptEvent);
            _envBus.Register(registration);
        }

        
        public IList<IEventProcessor> InboundProcessors
        {
            get
            {
                if (null == _inboundProcessors)
                {
                    IEnumerable<int> sortedKeys = this.OutboundChain.Keys.OrderBy(k => k);
                    foreach (int key in this.InboundChain.Keys)
                    {
                        _inboundProcessors.Add(InboundChain[key]);
                    }
                }
                return _inboundProcessors;
            }
        }

        public IList<IEventProcessor> OutboundProcessors
        {
            get
            {
                if (null == _outboundProcessors)
                {
                    IEnumerable<int> sortedKeys = this.OutboundChain.Keys.OrderBy(k => k);
                    foreach (int key in sortedKeys)
                    {
                        _outboundProcessors.Add(OutboundChain[key]);
                    }
                }
                return _outboundProcessors;
            }
        }

        public IEnvelopeBus EnvelopeBus
        {
            get
            {
                return _envBus;
            }
        }


        
    }
}