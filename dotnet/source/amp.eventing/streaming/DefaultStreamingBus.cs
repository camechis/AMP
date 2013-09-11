using amp.messaging;
using cmf.bus;
using cmf.eventing.patterns.streaming;
using Common.Logging;
using System;
using System.Collections.Generic;

namespace amp.eventing.streaming
{
    public class DefaultStreamingBus : DefaultEventBus, IStandardStreamingEventBus
    {
        protected ILog Log;
        
        private readonly IEnvelopeBus _envelopeBus;
        private readonly IEventStreamFactory _eventStreamFactory;
        private readonly Dictionary<string, IEventStream> _eventStreams;
        
        protected int _batchLimit = 10;

        public DefaultStreamingBus(IEnvelopeBus envelopeBus
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
            : this (envelopeBus,  new DefaultEventStreamFactory(), inboundChain, outboundChain)
        {
            _eventStreamFactory.EventBus = this;
        }

        public DefaultStreamingBus(IEnvelopeBus envelopeBus
            , IEventStreamFactory eventStreamFactory
            , List<IMessageProcessor> inboundChain
            , List<IMessageProcessor> outboundChain)
            : base(envelopeBus, inboundChain, outboundChain)
        {
            Log = LogManager.GetLogger(this.GetType());

            _envelopeBus = envelopeBus;
            
            _eventStreamFactory = eventStreamFactory;
            _eventStreams = new Dictionary<string, IEventStream>();
           
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

        public void PublishChunkedSequence<TEvent>(ICollection<TEvent> dataSet)
        {
            Log.Debug("enter publish to chunked sequence");
            IEventStream eventStream = null;
            string topic = null;

            ValidateEventCollection(dataSet);
            
            try
            {
                foreach (TEvent eventItem in dataSet)
                {
                    if (null == eventStream)
                    {
                        topic = eventItem.GetType().ToString();
                        eventStream = new DefaultEventStream(this, topic); //Skipping use of the factory so that we ensure sequencing based event stream is used
                        eventStream.BatchLimit = _batchLimit;
                        //Notify the receiver what the size of the collection will be
                        Publish(new CollectionSizeNotifier(dataSet.Count, topic, eventStream.SequenceId));
                        _eventStreams.Add(topic, eventStream);
                    }

                    eventStream.Publish(eventItem);
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
            Log.Debug("leave publish to chunked sequence");
        }

        private void ValidateEventCollection<TEvent>(ICollection<TEvent> eventEnumerator)
        {
            if (null == eventEnumerator)
            {
                throw new ArgumentException("Cannot publish from a null event iterator.");
            }
        }

        public void SubscribeToCollection<TEvent>(IStreamingCollectionHandler<TEvent> handler)
        {
            Log.Debug("enter SubscribeToCollection");
            StreamingCollectionRegistration<TEvent> registration = new StreamingCollectionRegistration<TEvent>(handler, this.ProcessInboundCallback);
            _envelopeBus.Register(registration);
            Log.Debug("leave SubscribetoCollection");
        }

        public void SubscribeToReader<TEvent>(IStreamingReaderHandler<TEvent> handler)
        {
            Log.Debug("enter SubscribeToReader");
            StreamingReaderRegistration<TEvent> registration = new StreamingReaderRegistration<TEvent>(handler, this.ProcessInboundCallback);
            _envelopeBus.Register(registration);
            Log.Debug("leave SubscribeToReader");
        }

        public IEnvelopeBus EnvelopeBus
        {
            get
            {
                return _envelopeBus;
            }
        }

        public object ProcessInboundCallback(Envelope env)
        {
            Log.Debug("Enter InterceptEvent");

            var context = new MessageContext(MessageContext.Directions.In, env);

            this.ProcessMessage(context, () =>
            {
                Log.Info("Completed inbound processing - invoking IEventHandler");

            });

            Log.Debug("Leave InterceptEvent");
            return context.Message;
        }

        public void ProcessMessage(MessageContext context, Action continueProcessing)
        {
            _eventProducer.ProcessMessage(context, continueProcessing);
        }
    }
}