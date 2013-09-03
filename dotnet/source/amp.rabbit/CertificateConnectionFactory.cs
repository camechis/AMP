using System;
using System.Security.Cryptography.X509Certificates;
using RabbitMQ.Client;

using amp.rabbit.topology;
using amp.bus.security;

namespace amp.rabbit
{
    public class CertificateConnectionFactory : BaseConnectionFactory
    {
        protected ICertificateProvider _certProvider;
    
        public CertificateConnectionFactory(ICertificateProvider certificateProvider)
        {
            _certProvider = certificateProvider;
        }

        protected override IConnection CreateConnection(Exchange ex)
        {
            _log.Debug("Enter CreateConnection");

            // try to get a certificate
            X509Certificate2 cert = _certProvider.GetCertificate();
            if (null != cert)
            {
                _log.Info("A certificate was located with subject: " + cert.Subject);
            }
            else
            {
                throw new RabbitException("Cannot connect to an exchange because no certificate was found");
            }

            IConnection conn = null;

            // we use the rabbit connection factory, just like normal
            ConnectionFactory cf = new ConnectionFactory();

            // set the hostname and the port
            cf.HostName = ex.HostName;
            cf.VirtualHost = ex.VirtualHost;
            cf.Port = ex.Port;

            // now, let's set the connection factory's ssl-specific settings
            // NOTE: it's absolutely required that what you set as Ssl.ServerName be
            //       what's on your rabbitmq server's certificate (its CN - common name)
            cf.AuthMechanisms = new AuthMechanismFactory[] { new ExternalMechanismFactory() };
            cf.Ssl.Certs = new X509CertificateCollection(new X509Certificate[] { cert });
            cf.Ssl.ServerName = ex.HostName;
            cf.Ssl.Enabled = true;

            // we either now create an SSL connection or a default "guest/guest" connection
            try
            {
                conn = cf.CreateConnection();
            }
            catch (Exception e) 
            {
                _log.Error("Unable to establish connection with RabbitMQ.", e);
                throw e;
            }

            _log.Debug("Leave CreateConnection");
            return conn;
        }
    }
}
