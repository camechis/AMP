using System.Linq;
using System.Net;
using System.Security;

namespace amp.utility.http
{
    public class BasicAuthWebRequestFactory : IWebRequestFactory
    {
        private readonly string _username;
        private readonly SecureString _password;


        public BasicAuthWebRequestFactory(string username, string password)
        {
            _username = username;

            // the following keeps the password encrypted in memory
            _password = new SecureString();
            password.ToCharArray().ToList().ForEach(_password.AppendChar);
            
            _password.MakeReadOnly();
        }


        public WebRequest CreateRequest(string url)
        {
            WebRequest request = WebRequest.Create(url);
            request.Credentials = new NetworkCredential(_username, _password);

            return request;
        }

        public void Dispose()
        {
            _password.Dispose();
        }
    }
}
