using System;
using cmf.bus;


namespace amp.bus
{
    public interface IEnvelopeDispatcher
    {
        Envelope Envelope { get; }


        void Dispatch();

        void Dispatch(Envelope env);

        void Fail(Exception ex);
    }
}
