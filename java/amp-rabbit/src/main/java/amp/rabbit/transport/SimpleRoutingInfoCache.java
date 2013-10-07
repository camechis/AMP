package amp.rabbit.transport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import amp.rabbit.topology.RoutingInfo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class SimpleRoutingInfoCache  implements IRoutingInfoCache {

	protected volatile Cache<String, RoutingInfo> routingInfoCache;
	protected Lock cacheLock;

	public SimpleRoutingInfoCache(long cacheExpiryInSeconds) {
		super();
        this.routingInfoCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(cacheExpiryInSeconds, TimeUnit.SECONDS)
                .build();

        this.cacheLock = new ReentrantLock();
	}

	@Override
	public RoutingInfo getIfPresent(String topic) {
	
	    // I don't think we should lock this call, otherwise we'll lock everytime we try to read the cache, which
	    // is going to kill performance.  Instead, I've made the cache volatile so that - at the very least -
	    // this read will see the latest value.
	    return routingInfoCache.getIfPresent(topic);
	}

	@Override
	public void put(String topic, RoutingInfo routingInfo) {
	
	    this.cacheLock.lock();
	
	    try {
	        this.routingInfoCache.put(topic, routingInfo);
	    }
	    finally {
	        this.cacheLock.unlock();
	    }
	}

	@Override
	public void dispose() {
	}
}