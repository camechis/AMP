using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;
using cmf.eventing.patterns.streaming;
using Common.Logging;

using SEC = amp.eventing.streaming.StreamingEnvelopeConstants;

namespace amp.eventing.streaming
{
    public class StreamingCollectionRegistration<TEvent> : IRegistration 
    {
        protected IStreamingCollectionHandler<TEvent> _eventHandler;
        protected Predicate<Envelope> _filterPredicate;
        protected Func<Envelope, TEvent> _processInbound;
        protected IDictionary<string, string> _registrationInfo;
        protected ILog _log;
        protected IDictionary<string, IList<IStreamingEventItem<TEvent>>> _collectedEvents;

        public StreamingCollectionRegistration(IStreamingCollectionHandler<TEvent> handler, Func<Envelope, TEvent> processorCallback) 
        {
            _eventHandler = handler;
            _processInbound = processorCallback;
            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.SetMessageTopic(handler.Topic);

            _log = LogManager.GetLogger(this.GetType());
            _collectedEvents = new Dictionary<string, IList<IStreamingEventItem<TEvent>>>();
        }

        public Predicate<Envelope> Filter
        {
            get { return _filterPredicate; }
        }

        public object Handle(Envelope env)
        {
            TEvent streamEvent = _processInbound(env);
            object result = null;

            if (null != streamEvent)
            {
                try
                {
                    string sequenceId = env.Headers[SEC.SequenceId];
                    bool isLast = bool.Parse(env.Headers[SEC.IsLast]);

                    if (!_collectedEvents.ContainsKey(sequenceId))
                    { 
                        _collectedEvents.Add(sequenceId, new List<IStreamingEventItem<TEvent>>());
                    }
                    

                    IStreamingEventItem<TEvent> eventItem = new StreamingEventItem<TEvent>(streamEvent, env.Headers);
                    _collectedEvents[sequenceId].Add(eventItem);
                    IStreamingProgressUpdater notifier = _eventHandler.Progress;
                    if (null != notifier)
                    {
                        notifier.UpdateProgress(sequenceId, 
                            _collectedEvents[sequenceId].Count);
                    }

                    if (isLast)
                    { 
                        result = _eventHandler.HandleCollection(
                            new List<IStreamingEventItem<TEvent>>(_collectedEvents[sequenceId]), 
                            env.Headers);
                    }

                }
                catch (Exception ex)
                { 
                    result = HandleFailed(env, ex);    
                }
            }
            
            return result;
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            try
            {
                return _eventHandler.HandleFailed(env, ex);
            }
            catch (Exception failedToFail)
            {
                throw failedToFail;
            }
        }

        public IDictionary<string, string> Info
        {
            get { return _registrationInfo; }
        }
    }
}
