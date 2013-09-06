using System;
using System.Collections.Generic;
using cmf.bus;
using Common.Logging;

namespace amp.messaging
{
    public class TypedMessageHandler<TMessage> : IMessageHandler
    {
        private static readonly ILog Log = LogManager.GetLogger("TypedMessageHandler");

        private Action<TMessage, IDictionary<string, string>> _handler;
        private Func<Envelope, Exception, Object> _faildHandler;


        public string Topic
        {
            get { return typeof(TMessage).FullName; }
        }

        protected Action<TMessage, IDictionary<string, string>> Handler
        {
            get { return _handler; }
            set { _handler = value; }
        }


        public TypedMessageHandler(Action<TMessage, IDictionary<string, string>> handler)
            : this(handler, (env, ex) => null)
        {
        }

        public TypedMessageHandler(Action<TMessage, IDictionary<string, string>> handler,
            Func<Envelope, Exception, Object> faildHandler)
        {
            _handler = handler;
            _faildHandler = faildHandler;
        }

        protected TypedMessageHandler()
        {
            
        }


        public object Handle(object message, IDictionary<string, string> headers)
        {
            if (null == _handler) { throw new ArgumentNullException("Typed Message Handler was asked to handle a message, but was given no handler."); }

            try
            {
                _handler((TMessage)message, headers);
            }
            catch (Exception ex)
            {
                Log.Warn("Caught an exception in a message handler", ex);
            }

            return null;
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            return _faildHandler(env, ex);
        }
    }
}
