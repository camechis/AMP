using System;
using System.Security.Cryptography.X509Certificates;
using Common.Logging;

namespace amp.bus.security
{
    /// <summary>
    /// Provides client certificates loaded from .pfx or similar files.
    /// </summary>
    public class FileBasedCertProvider : ICertificateProvider
    {
        private readonly ILog _log;
        private readonly X509Certificate2 _certificate;
       
        public FileBasedCertProvider(string clientCertFilename, string clientCertPassword)
        {
            _log = LogManager.GetLogger(this.GetType());
            _certificate = LoadCertificate(clientCertFilename, clientCertPassword);
        }


        private X509Certificate2 LoadCertificate(string clientCertFilename, string clientCertPassword)
        {
            _log.Debug("Enter LoadCertificate()");

            try
            {
                X509Certificate2 cert = new X509Certificate2(clientCertFilename, clientCertPassword);
                _log.Debug("Leave LoadCertificate()");
                return cert;
            }
            catch (Exception ex)
            {
                _log.Error("Failed to open certificate file.", ex);
                throw;
            }
        }

        public X509Certificate2 GetCertificate()
        {
            return _certificate;
        }

        public virtual X509Certificate2 GetCertificateFor(string distinguishedName)
        {
            throw new NotImplementedException();
        }
    }
}
