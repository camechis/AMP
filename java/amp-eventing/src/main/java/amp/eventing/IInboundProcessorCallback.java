package amp.eventing;


import cmf.bus.Envelope;


public interface IInboundProcessorCallback {
	Object ProcessInbound(Envelope envelope) throws Exception;
}
