using System;
using System.Net;
using System.Windows.Forms;

using Spring.Context;
using Spring.Context.Support;

namespace amp.examples.gui.duplex
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            //Hack so that we don't have to validate the Anubis server certificate.
            ServicePointManager.ServerCertificateValidationCallback = delegate { return true; };

            IApplicationContext springContainer = ContextRegistry.GetContext();
            DuplexEventForm form = springContainer.GetObject(typeof(DuplexEventForm).FullName) as DuplexEventForm;

            Application.Run(form);
        }
    }
}
