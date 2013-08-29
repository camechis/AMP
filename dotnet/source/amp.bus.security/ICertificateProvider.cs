using System.Security.Cryptography.X509Certificates;

namespace amp.bus.security
{
    public interface ICertificateProvider
    {
        X509Certificate2 GetCertificate();

        X509Certificate2 GetCertificateFor(string distinguishedName);
    }
}