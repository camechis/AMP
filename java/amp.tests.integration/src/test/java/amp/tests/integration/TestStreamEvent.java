package amp.tests.integration;

public class TestStreamEvent extends TestEvent {
    public int Sequence;

    public TestStreamEvent(int sequence){
        Sequence = sequence;
    }

    @Override
    public String toString() {
        return String.format("Stream Event %d-%s", Sequence, Id.toString());
    }
}