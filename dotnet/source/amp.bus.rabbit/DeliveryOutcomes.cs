using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace amp.bus.rabbit
{
    public enum DeliveryOutcomes
    {
        Null,
        Acknowledge,
        Reject,
        Exception
    }
}
