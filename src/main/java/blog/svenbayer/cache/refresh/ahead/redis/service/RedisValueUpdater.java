package blog.svenbayer.cache.refresh.ahead.redis.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import org.springframework.cache.Cache;

public class RedisValueUpdater implements ReloadAheadValueUpdater {

    @Override
    public void writeValueToCache(Cache cache, ReloadAheadKey key, Object value) {
        Object nativeCache = cache.getNativeCache();
    }
}
