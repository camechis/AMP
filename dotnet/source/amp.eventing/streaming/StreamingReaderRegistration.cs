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
    public class StreamingReaderRegistration<TEvent> : IRegistration, IObservable<IStreamingEventItem<TEvent>> 
    {
        protected ILog _log;
        protected IStreamingReaderHandler<TEvent> _eventHandler;
        protected Func<IEventHandler, Envelope, object> _processorCallback;
        protected IDictionary<string, string> _registrationInfo;
        private IList<IObserver<IStreamingEventItem<TEvent>>> _observers;

        public StreamingReaderRegistration(IStreamingReaderHandler<TEvent> eventHandler, Func<IEventHandler, Envelope, object> processorCallback)
        {
            //IStreamingReaderHandler implements IObserver<IStreamingEventItem<TEvent>>
            _observers = new List<IObserver<IStreamingEventItem<TEvent>>>();
            _observers.Add(eventHandler);

            _eventHandler = eventHandler;
            _processorCallback = processorCallback;

            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.Add(EnvelopeHeaderConstants.MESSAGE_TOPIC, typeof(TEvent).FullName);
            _log = LogManager.GetLogger(typeof(StreamingReaderRegistration<TEvent>).Name);
        }

        public Predicate<Envelope> Filter
        {
            get { return null; }
        }

        private static readonly object padLock = new object();

        public object Handle(Envelope env)
        {
            lock (padLock)
            {
                _log.Debug("enter Handle for StreamingReaderRegistration");
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
                _log.Debug("leaving Handle for StreamingReaderRegistration");
                return result;
            }
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

        public IDisposable Subscribe(IObserver<IStreamingEventItem<TEvent>> observer)
        {
            if (false == _observers.Contains(observer))
            {
                _observers.Add(observer);
            }
            return new StreamingReaderUnsubscriber<TEvent>(_observers, observer);
        }

        private class StreamingReaderUnsubscriber<TEvent>: IDisposable 
        { 
            /// <summary>
            /// A reference to a list of observers being maintained by implementation of IObservable.
            /// </summary>
            private IList<IObserver<IStreamingEventItem<TEvent>>> _observers;
            private IObserver<IStreamingEventItem<TEvent>> _observer;

            public StreamingReaderUnsubscriber(IList<IObserver<IStreamingEventItem<TEvent>>> observers,
                IObserver<IStreamingEventItem<TEvent>> observer)
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
