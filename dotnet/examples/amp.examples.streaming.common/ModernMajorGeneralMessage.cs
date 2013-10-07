namespace amp.examples.streaming.common
{
    public class ModernMajorGeneralMessage
    {
        public string Content { get; protected set; }

        public ModernMajorGeneralMessage(string content)
        {
            Content = content;
        }

    }
}
