using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.rabbit
{
    public enum DeliveryOutcomes
    {
        Null,
        Acknowledge,
        Reject,
        Exception
    }
}
