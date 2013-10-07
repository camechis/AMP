namespace amp.eventing.streaming
{
    public class EndOfStream
    {
        public string StreamType { get; set; }
        public string SequenceId { get; set; }

        public EndOfStream()
        {
            StreamType = null;
            SequenceId = null;
        }

        public EndOfStream(string streamType, string sequenceId)
        {
            StreamType = streamType;
            SequenceId = sequenceId;
        }
    }
}
