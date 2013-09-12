using amp.commanding;
using amp.messaging;
using Common.Logging;

namespace amp.rabbit.transport
{
    public class CommandableCache : SimpleRoutingInfoCache
    {
        private static readonly ILog Log = LogManager.GetLogger(typeof(CommandableCache));

        private readonly ICommandReceiver _commandReceiver;
        
        public CommandableCache(ICommandReceiver commandReceiver, int cacheExpiryInSeconds)
            : base(cacheExpiryInSeconds)
        {
            _commandReceiver = commandReceiver;

            try {
                // subscribe for the command to burst the routing cache.  Pass a cache
                // reference into the cache buster that handles incoming BurstRoutingCache commands
                _commandReceiver.ReceiveCommand(new RoutingCacheBuster(_routingInfoCache, _cacheLock, _cacheExpiryInSeconds));
            }
            catch (MessageException cex) {
                Log.Warn("Failed to subscribe for Routing Cache Bust commands - the cache cannot be remotely commanded.", cex);
            }
        }


        public override void Dispose()
        {
            try { _commandReceiver.Dispose(); }
            catch { }
        }
    }
}
