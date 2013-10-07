using System;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using amp.messaging;
using Common.Logging;

using cmf.bus;
using amp.bus.security;

namespace amp.eventing
{
    public class DigitalSignatureProcessor : IMessageProcessor
    {
        protected ICertificateProvider _certProvider;
        protected X509Certificate2 _cert;
        protected ILog _log;


        public DigitalSignatureProcessor(ICertificateProvider certProvider)
        {
            _certProvider = certProvider;

            try
            {
                _cert = certProvider.GetCertificate();
            }
            catch (Exception ex)
            {
                _log.Error("Failed to get a certificate from the provider", ex);
            }
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

        public void ProcessInbound(MessageContext context, Action continueProcessing)
        {
            bool signatureVerified = false;
            Envelope env = context.Envelope;

            // start by getting the purported sender's identity
            string sender = env.GetSenderIdentity();
            if (string.IsNullOrEmpty(sender))
            {
                _log.Error("An inbound event arrived with no sender identity");
            }
            else
            {
                // use the identity to lookup their certificate
                X509Certificate2 senderCert = _certProvider.GetCertificateFor(sender);
                if (null == senderCert)
                {
                    _log.Error("Sender " + sender + " does not have a public key certificate available");
                }
                else
                {
                    // get the digital signature from the headers
                    byte[] digitalSignature = env.GetDigitalSignature();
                    if (0 == digitalSignature.LongLength)
                    {
                        _log.Error("Sender " + sender + " did not digitally sign the event");
                    }
                    else
                    {
                        // verify that the payload hasn't been tampered with by using the 
                        // sender's public key and the digital signature on the envelope
                        RSACryptoServiceProvider rsaProvider = senderCert.PublicKey.Key as RSACryptoServiceProvider;
                        signatureVerified = rsaProvider.VerifyData(env.Payload, new SHA1CryptoServiceProvider(), digitalSignature);
                    }
                }
            }

            if (signatureVerified) { continueProcessing(); }
        }

        public void ProcessOutbound(MessageContext context, Action continueProcessing)
        {
            if (null == _cert) { return; }

            Envelope env = context.Envelope;
            env.SetSenderIdentity(_cert.Subject);

            try
            {
                RSACryptoServiceProvider rsaProvider = _cert.PrivateKey as RSACryptoServiceProvider;
                env.SetDigitalSignature(rsaProvider.SignData(env.Payload, new SHA1CryptoServiceProvider()));

                continueProcessing();
            }
            catch (Exception ex)
            {
                _log.Error("Failed to digitally sign the event", ex);
                throw;
            }
        }

        public void Dispose()
        {
            //Nothing to do.
        }
    }
}
