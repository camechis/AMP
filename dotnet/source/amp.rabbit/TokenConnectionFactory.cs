using System;
using System.IO;
using System.Net;
using amp.rabbit.topology;
using amp.utility.http;
using amp.utility.serialization;
using RabbitMQ.Client;

namespace amp.rabbit
{
    public class TokenConnectionFactory : BaseConnectionFactory
    {
        private readonly string _anubisUrl;
        private readonly IWebRequestFactory _webRequestFactory;
        private readonly IDeserializer<NamedToken> _serializer;
        private readonly BaseConnectionFactory _secureConnectionFactory;

        public TokenConnectionFactory(
            string anubisUrl,
            IWebRequestFactory webRequestFactory,
            IDeserializer<NamedToken> serializer)
            : this(anubisUrl, webRequestFactory, serializer, null)
        {
        }

        public TokenConnectionFactory(
            string anubisUrl,
            IWebRequestFactory webRequestFactory,
            IDeserializer<NamedToken> serializer,
            BaseConnectionFactory secureConnectionFactory)
        {
            _anubisUrl = anubisUrl;
            _webRequestFactory = webRequestFactory;
            _serializer = serializer;
            _secureConnectionFactory = secureConnectionFactory;
        }

        public override void ConfigureConnectionFactory(ConnectionFactory factory, Exchange exchange)
        {
            NamedToken token = GetNamedToken();

            if (_secureConnectionFactory != null)
            {
                _secureConnectionFactory.ConfigureConnectionFactory(factory, exchange);
            }
            
            factory.UserName = token.Identity;
            factory.Password = token.Token;
        }

        private NamedToken GetNamedToken()
        {
            try
            {
                // use the web request factory to create a web request
                WebRequest request = _webRequestFactory.CreateRequest(_anubisUrl);

                // get some response to the request
                using (Stream responseStream = request.GetResponse().GetResponseStream())
                {
                    return _serializer.Deserialize(responseStream);
                }
            }
            catch (Exception ex)
            {
                _log.Error(string.Format("Failed to get token from {0}.", _anubisUrl), ex);
                // ReSharper disable once PossibleIntendedRethrow
                throw ex;
            }
        }

        public class NamedToken
        {
            public NamedToken(String identity, String token)
            {
                Identity = identity;
                Token = token;
            }

            public string Identity { get; set; }
            public string Token { get; set; }
        }
    }
}