using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Text;

using Common.Logging;
using cmf.bus;

namespace amp.commanding
{
    public class OutboundHeadersProcessor : ICommandProcessor
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(OutboundHeadersProcessor));


        public void ProcessCommand(CommandContext context, Action continueProcessing)
        {
            if (CommandContext.Directions.Out == context.Direction)
            {
                Envelope env = context.Envelope;

                Guid messageId = env.GetMessageId();
                messageId = Guid.Equals(Guid.Empty, messageId) ? Guid.NewGuid() : messageId;
                env.SetMessageId(messageId.ToString());

                Guid correlationId = env.GetCorrelationId();

                string messageType = env.GetMessageType();
                messageType = string.IsNullOrEmpty(messageType) ? this.GetMessageType(context.Command) : messageType;
                env.SetMessageType(messageType);

                string messageTopic = env.GetMessageTopic();
                messageTopic = string.IsNullOrEmpty(messageTopic) ? this.GetMessageTopic(context.Command) : messageTopic;
                if (Guid.Empty != correlationId)
                {
                    messageTopic = messageTopic + "#" + correlationId.ToString();
                }
                env.SetMessageTopic(messageTopic);

                string senderIdentity = env.GetSenderIdentity();
                senderIdentity = string.IsNullOrEmpty(senderIdentity) ? UserPrincipal.Current.DistinguishedName.Replace(",", ", ") : senderIdentity;
                senderIdentity = string.IsNullOrEmpty(senderIdentity) ? UserPrincipal.Current.Name : senderIdentity;
                env.SetSenderIdentity(senderIdentity);
            }

            // either way, continue processing
            continueProcessing();
        }

        public void Dispose()
        {
        }

        public string GetMessageTopic(object command)
        {
            string topic = command.GetType().FullName;

            try
            {
                object[] attributes = command.GetType().GetCustomAttributes(typeof (CommandAttribute), true);
                CommandAttribute attr = attributes.OfType<CommandAttribute>().FirstOrDefault();
                if ((null != attr) &&
                    (false == string.IsNullOrEmpty(attr.Topic)))
                {
                    Log.Debug("CommandAttribute: topic=" + attr.Topic);
                    topic = attr.Topic;
                }
            }
            catch (Exception ex)
            {
                Log.Error("An exception occurred while checking a command for overriden topic.", ex);
            }

            return topic;
        }

        public string GetMessageType(object command)
        {
            string type = command.GetType().FullName;

            try
            {
                object[] attributes = command.GetType().GetCustomAttributes(typeof(CommandAttribute), true);
                CommandAttribute attr = attributes.OfType<CommandAttribute>().FirstOrDefault();
                if ((null != attr) && (false == string.IsNullOrEmpty(attr.Type)))
                {
                    Log.Debug("CommandAttribute: type=" + attr.Type);
                    type = attr.Type;
                }
            }
            catch (Exception ex)
            {
                Log.Error("An exception occurred while checking a command for overriden type.", ex);
            }

            return type;
        }
    }
}
