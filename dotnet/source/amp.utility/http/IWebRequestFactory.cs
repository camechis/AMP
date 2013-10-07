using System;
using System.Net;

namespace amp.utility.http
{
    /// <summary>
    /// Defines the interface for a factory that knows how to build a
    /// WebRequest.  This indirection allows clients to configure the
    /// WebRequest for their needs. 
    /// </summary>
    public interface IWebRequestFactory : IDisposable
    {
        WebRequest CreateRequest(string url);
    }
}
