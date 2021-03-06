﻿using System;
using System.DirectoryServices.AccountManagement;
using System.Linq;

using Common.Logging;

using cmf.bus;

namespace amp.messaging
{
    public class OutboundHeadersProcessor : IMessageProcessor
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(OutboundHeadersProcessor));

        protected readonly string _alternateIdentity;

        public OutboundHeadersProcessor()
        {

        }

        /// <summary>
        /// Sets an alternate user Identity to user if not found in the envelope when ProcessEvent executes.
        /// </summary>
        /// <param name="alternateSenderIdentity"></param>
        public OutboundHeadersProcessor(string alternateSenderIdentity)
        {
            _alternateIdentity = alternateSenderIdentity;
        }

        public void ProcessMessage(MessageContext context, Action continueProcessing)
        {
            if (MessageContext.Directions.Out == context.Direction)
            {
                Envelope env = context.Envelope;

                Guid messageId = env.GetMessageId();
                messageId = Guid.Equals(Guid.Empty, messageId) ? Guid.NewGuid() : messageId;
                env.SetMessageId(messageId.ToString());

                Guid correlationId = env.GetCorrelationId();

                string messageType = env.GetMessageType();
                messageType = string.IsNullOrEmpty(messageType) ? this.GetMessageType(context.Message) : messageType;
                env.SetMessageType(messageType);

                string messageTopic = env.GetMessageTopic();
                messageTopic = string.IsNullOrEmpty(messageTopic) ? this.GetMessageTopic(context.Message) : messageTopic;
                if (Guid.Empty != correlationId)
                {
                    messageTopic = messageTopic + "#" + correlationId.ToString();
                }
                env.SetMessageTopic(messageTopic);

                DateTime creation = env.GetCreationTime();
                creation = (DateTime.MinValue == creation) ? DateTime.UtcNow : creation;
                env.SetCreationTime(creation);

                string senderIdentity = env.GetSenderIdentity();
                if (string.IsNullOrEmpty(senderIdentity))
                {
                    if (!string.IsNullOrEmpty(_alternateIdentity))
                    {
                        senderIdentity = _alternateIdentity;
                    }
                    else
                    {
                        //This line will raise an exception if there is no active directory server available
                        try
                        {
                            senderIdentity = UserPrincipal.Current.DistinguishedName.Replace(",", ", ");
                        }
                        catch
                        {
                            senderIdentity = UserPrincipal.Current.Name;
                        }
                    }
                    env.SetSenderIdentity(senderIdentity);
                }
            }

            // either way, continue processing
            continueProcessing();
        }

        public void Dispose()
        {
        }

        public string GetMessageTopic(object message)
        {
            string topic = message.GetType().FullName;

            try
            {
                object[] attributes = message.GetType().GetCustomAttributes(typeof(MessageAttribute), true);
                MessageAttribute attr = attributes.OfType<MessageAttribute>().FirstOrDefault();
                if ((null != attr) &&
                    (false == string.IsNullOrEmpty(attr.Topic)))
                {
                    Log.Debug("MessageAttribute: topic=" + attr.Topic);
                    topic = attr.Topic;
                }
            }
            catch (Exception ex)
            {
                Log.Error("An exception occurred while checking a message for overriden topic.", ex);
            }

            return topic;
        }

        public string GetMessageType(object message)
        {
            string type = message.GetType().FullName;

            try
            {
                object[] attributes = message.GetType().GetCustomAttributes(typeof(MessageAttribute), true);
                MessageAttribute attr = attributes.OfType<MessageAttribute>().FirstOrDefault();
                if ((null != attr) && (false == string.IsNullOrEmpty(attr.Type)))
                {
                    Log.Debug("MessageAttribute: type=" + attr.Type);
                    type = attr.Type;
                }
            }
            catch (Exception ex)
            {
                Log.Error("An exception occurred while checking a message for overriden type.", ex);
            }

            return type;
        }
    }
}
