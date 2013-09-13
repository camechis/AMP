namespace amp.tests.integration
{
    internal class Config
    {
        internal class Bus
        {
            public const string All = "assembly://amp.tests.integration/amp.tests.integration.Config/AllBussesConfig.xml";
        }

        internal class Authorization
        {
            public const string Basic = "assembly://amp.tests.integration/amp.tests.integration.Config.Authorization/BasicAuthRabbitConfig.xml";
            public const string AnubisBasic = "assembly://amp.tests.integration/amp.tests.integration.Config.Authorization/AnubisAuthRabbitConfig.xml";
        }

        internal class Topology
        {
            public const string Simple = "assembly://amp.tests.integration/amp.tests.integration.Config.Topology/SimpleTopologyConfig.xml";
        }
    }
}
