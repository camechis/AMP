using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

namespace amp.commanding
{
    public class TypedCommandHandler<TCommand> : ICommandHandler
    {
        private static readonly ILog Log = LogManager.GetLogger("TypedCommandHandler");

        private Action<TCommand, IDictionary<string, string>> _handler;


        public Type CommandType
        {
            get { return typeof(TCommand); }
        }

        protected Action<TCommand, IDictionary<string, string>> Handler
        {
            get { return _handler; }
            set { _handler = value; }
        }


        public TypedCommandHandler(Action<TCommand, IDictionary<string, string>> handler)
        {
            _handler = handler;
        }

        protected TypedCommandHandler()
        {
            
        }


        public object Handle(object command, IDictionary<string, string> headers)
        {
            if (null == _handler) { throw new ArgumentNullException("Typed Command Handler was asked to handle a command, but was given no handler."); }

            try
            {
                _handler((TCommand)command, headers);
            }
            catch (Exception ex)
            {
                Log.Warn("Caught an exception in a command handler", ex);
            }

            return null;
        }
    }
}
