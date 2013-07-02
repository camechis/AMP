using cmf.bus;
using cmf.eventing;
using cmf.eventing.patterns.streaming;
using Common.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SEC = amp.eventing.streaming.StreamingEnvelopeConstants;

namespace amp.eventing.streaming
{
    public class StreamingReaderRegistration<TEvent> : IRegistration, IObservable<TEvent> where TEvent : IStreamingEventItem<TEvent> 
    {
        protected ILog _log;
        protected IStreamingReaderHandler<TEvent> _eventHandler;
        protected Func<IEventHandler, Envelope, object> _processorCallback;
        protected IDictionary<string, string> _registrationInfo;
        private IList<IObserver<TEvent>> _observers;

        public StreamingReaderRegistration(IStreamingReaderHandler<TEvent> eventHandler, Func<IEventHandler, Envelope, object> processorCallback)
        {
            _observers = new List<IObserver<TEvent>>();
            
            _eventHandler = eventHandler;
            _processorCallback = processorCallback;

            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.Add(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.GetType().AssemblyQualifiedName);
        }

        public Predicate<Envelope> Filter
        {
            get { throw new NotImplementedException(); }
        }

        public object Handle(Envelope env)
        {
            TEvent evt = (TEvent)_processorCallback(_eventHandler, env);
            object result = null;

            if (null != evt)
            {
                try
                {
                    bool isLast = bool.Parse(env.Headers[SEC.IS_LAST]);

                    IStreamingEventItem<TEvent> eventItem = new StreamingEventItem<TEvent>(evt, env.Headers);
                   
                    foreach (IObserver<IStreamingEventItem<TEvent>> o in _observers)
                    {
                        o.OnNext(eventItem);    
                    }
                    
                    if (isLast)
                    {
                        foreach (IObserver<IStreamingEventItem<TEvent>> o in _observers)
                        {
                            o.OnCompleted();
                        }
                        _eventHandler.Dispose();
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

        public IDisposable Subscribe(IObserver<TEvent> observer)
        {
            if (false == _observers.Contains(observer))
            {
                _observers.Add(observer);
            }
            return new StreamingReaderUnsubscriber<TEvent>(_observers, observer);
        }

        private class StreamingReaderUnsubscriber<TEvent> : IDisposable where TEvent : IStreamingEventItem<TEvent>
        { 
            /// <summary>
            /// A reference to a list of observers being maintained by implementation of IObservable.
            /// </summary>
            private IList<IObserver<TEvent>> _observers;
            private IObserver<TEvent> _observer;

            public StreamingReaderUnsubscriber(IList<IObserver<TEvent>> observers,
                IObserver<TEvent> observer)
            {
                _observers = observers;
                _observer = observer;
            }

            public void Dispose()
            {
                if (_observer != null && _observers.Contains(_observer))
                {
                    _observers.Remove(_observer);
                }
            }
        
        }
    }
}
