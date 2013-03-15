package amp.bus;


public interface IEnvelopeReceivedCallback {

    void handleReceive(IEnvelopeDispatcher envelope);
}
