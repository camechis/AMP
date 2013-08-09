package amp.rabbit;


import com.rabbitmq.client.Connection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import amp.anubis.core.NamedToken;
import amp.rabbit.topology.Exchange;
import amp.utility.http.HttpClientProvider;
import amp.utility.serialization.ISerializer;


public class TokenChannelFactory extends BaseChannelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(TokenChannelFactory.class);

    private HttpClientProvider _httpClientFactory;
    private String _anubisUri;
    private ISerializer _serializer;
    private SSLChannelFactory _sslChannelFactory;


    public TokenChannelFactory(
            HttpClientProvider httpClientFactory,
            String anubisUri,
            ISerializer serializer,
            SSLChannelFactory sslChannelFactory) {
        _httpClientFactory = httpClientFactory;
        _anubisUri = anubisUri;
        _serializer = serializer;
        _sslChannelFactory = sslChannelFactory;
    }


    @Override
    public Connection getConnection(Exchange exchange) throws Exception {

        HttpClient client = _httpClientFactory.getClient();
        HttpGet getMethod = new HttpGet(_anubisUri);

        try {

            NamedToken token = this.getNamedToken();

            // set the username and password from the token
            _sslChannelFactory.setUsername(token.getIdentity());
            _sslChannelFactory.setPassword(token.getToken());

            return _sslChannelFactory.getConnection(exchange);
        }
        catch(Exception ex) {

        }

        return null;
    }

    public NamedToken getNamedToken() throws Exception {

        // create the client and the GET method
        HttpClient client = _httpClientFactory.getClient();
        HttpGet getMethod = new HttpGet(_anubisUri);

        // get a response by executing the GET method
        HttpResponse response = client.execute(getMethod);

        // extract the string content from the response
        String content = EntityUtils.toString(response.getEntity());
        LOG.debug("Received the following content from Anubis: {}", content);

        // deserialize the named token and return it
        return _serializer.stringDeserialize(content, NamedToken.class);
    }


    @Override
    public void dispose() {
    }
}
