using System;
using System.Reflection;
using System.Text;
using cmf.bus;
using Common.Logging;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace amp.messaging
{
    public class JsonSerializationProcessor : IMessageProcessor
    {
        protected ILog _log;
        protected JsonSerializerSettings _settings;

        /// <summary>
        /// We need to resolve properties to lowercase in order to be interoperable
        /// with java and other json serializers.
        /// </summary>
        protected class LowercaseContractResolver : DefaultContractResolver
        {
            protected override string ResolvePropertyName(string propertyName)
            {
                string firstChar = propertyName.Substring(0, 1).ToLower();
                string camelName = string.Format("{0}{1}", firstChar, propertyName.Substring(1));

                return camelName;
            }
        }

        public JsonSerializationProcessor()
        {
            _log = LogManager.GetLogger(this.GetType());

            _settings = new JsonSerializerSettings();
            _settings.ContractResolver = new LowercaseContractResolver();
            _settings.TypeNameHandling = TypeNameHandling.Objects;
        }


        public void ProcessMessage(MessageContext context, Action continueProcessing)
        {
            if (MessageContext.Directions.In == context.Direction)
            {
                this.ProcessInbound(context, continueProcessing);
            }
            if (MessageContext.Directions.Out == context.Direction)
            {
                this.ProcessOutbound(context, continueProcessing);
            }
        }

        public virtual void ProcessInbound(MessageContext context, Action continueProcessing)
        {
            bool success = false;
            Envelope env = context.Envelope;

            try
            {
                // get the type from the headers on the envelope
                string eventType = env.GetMessageType();

                // if no type, try the topic
                if (string.IsNullOrEmpty(eventType))
                {
                    eventType = env.GetMessageTopic();
                }

                // and make sure we get something
                if (string.IsNullOrEmpty(eventType))
                {
                    throw new Exception("Cannot deserialize an envelope that does not specify the event's topic");
                }


                // start with a null Type
                Type type = null;

                // go through each assembly loaded into the app domain
                StringBuilder assembliesLoaded = new StringBuilder();
                foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
                {
                    assembliesLoaded.AppendLine("Loaded Assembly Found: " + assembly.FullName);

                    // and see if it can get us our Type
                    type = assembly.GetType(eventType);
                    if (null != type)
                    {
                        _log.Debug("Found type " + type + " in assembly " + assembly.FullName);
                        break;
                    }
                }
                _log.Debug("Currently Loaded Assemblies:" + "\r\n" + assembliesLoaded.ToString());

                if (null != type) // if we did get a Type, we can deserialize the event
                {

                    string jsonString = new UTF8Encoding().GetString(env.Payload); //Encoding.UTF8.GetString(env.Payload);
                    _log.Debug("Will attempt to deserialize: " + jsonString);
                    context.Message = JsonConvert.DeserializeObject(jsonString, type, _settings);

                    success = true;
                }
                else // otherwise, throw an exception
                {
                    throw new Exception("Cannot deserialize an event of topic '" + eventType + "' because no type definition could be found for it");
                }
            }
            catch (Exception ex)
            {
                _log.Error("Failed to deserialize an event", ex);
            }

            if (success) { continueProcessing(); }
        }

        public virtual void ProcessOutbound(MessageContext context, Action continueProcessing)
        {
            object ev = context.Message;

            try
            {
                // first, serialize the event (make it pretty!)
                string json = JsonConvert.SerializeObject(ev, Formatting.Indented, _settings);

                _log.Debug("Serialized event: " + json);

                // next, convert the string into bytes using UTF-8
                context.Envelope.Payload = new UTF8Encoding().GetBytes(json);
            }
            catch (Exception ex)
            {
                _log.Error("Failed to serialize an event", ex);
                throw;
            }

            continueProcessing();
        }

        public void Dispose()
        {
            //nothing to do.
        }
    }
}
