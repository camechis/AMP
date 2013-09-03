using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;

using log4net;

using amp.rabbit.topology;


namespace amp.topology.client
{
    /// <summary>
    /// Provides routing info from an HTTP-based endpoint
    /// </summary>
    /// <author>John Ruiz (Berico Technologies)</author>
    public class HttpRoutingInfoRetreiver : IRoutingInfoRetreiver
    {
        protected static readonly ILog Log = LogManager.GetLogger(typeof(HttpRoutingInfoRetreiver));
        private readonly IRoutingDeserializer _serializer;
        private readonly string _urlExpression;
        private readonly IWebRequestFactory _webRequestFactory;


        public HttpRoutingInfoRetreiver(IWebRequestFactory webRequestFactory, string urlExpression, IRoutingDeserializer serializer)
        {
            _webRequestFactory = webRequestFactory;
            _urlExpression = urlExpression;
            _serializer = serializer;
        }


        /// <summary>
        /// Retreive routing info for the supplied topic.
        /// </summary>
        /// <param name="topic">The topic for which routing is being retreived</param>
        /// <returns>RoutingInfo or null</returns>
        public RoutingInfo RetrieveRoutingInfo(string topic)
        {
            Log.DebugFormat("Getting routing info for topic {0}", topic);

            // the routing that we'll return
            RoutingInfo routing = null;

            // take the template expression provided by the user and create
            // the complete URL of the GTS
            string urlString = string.Format(_urlExpression, topic);

            // log the url for debugging purposes
            Log.DebugFormat("Calling GTS with url {0}", urlString);

            try
            {
                // use the web request factory to create a web request
                WebRequest request = _webRequestFactory.CreateRequest(urlString);
            
                // get some response to the request
                using (Stream responseStream = request.GetResponse().GetResponseStream())
                {
                    routing = _serializer.DeserializeRouting(responseStream);
                }
            }
            catch (Exception ex)
            {
                Log.Error("Failed to get RoutingInfo from the GTS.", ex);
            }

            return routing;
        }

        public void Dispose()
        {
            _webRequestFactory.Dispose();
            _serializer.Dispose();
        }
    }
}
