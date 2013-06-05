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

        protected Action<TCommand, IDictionary<string, string>> _handler;


        public Type CommandType
        {
            get { return typeof(TCommand); }
        }


        public TypedCommandHandler(Action<TCommand, IDictionary<string, string>> handler)
        {
            _handler = handler;
        }


        public object Handle(object command, IDictionary<string, string> headers)
        {
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
