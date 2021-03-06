﻿using System;
using System.Net;
using amp.bus.security;
using Common.Logging;

namespace amp.utility.http
{
    class CertifacateWebRequestFactory : IWebRequestFactory
    {
        private readonly ILog _log;

        private readonly ICertificateProvider _certificateProvider;

        public CertifacateWebRequestFactory(ICertificateProvider certificateProvider)
        {
            _log = LogManager.GetLogger(this.GetType());
            _certificateProvider = certificateProvider;
        }

        public WebRequest CreateRequest(string url)
        {
            try
            {
                WebRequest request = WebRequest.Create(url);

                if (request is HttpWebRequest)
                {
                    ((HttpWebRequest)request).ClientCertificates.Add(_certificateProvider.GetCertificate());
                }
                else
                {
                    throw new ApplicationException(string.Format("WebRequest.Create() did not return an HttpWebRequest for the Url: {0}  Only HTTPS urls are supported by the CertificateWebRequestFactory.", url));
                }

                return request;
            }
            catch (Exception ex)
            {
                _log.Error("Failed to create WebRequest.", ex);
                throw;
            }

        }

        public void Dispose()
        {
            //nothing to do.
        }
    }
}