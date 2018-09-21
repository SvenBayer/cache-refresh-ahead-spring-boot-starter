package com.github.benmanes.caffeine.cache;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Stream;

@Service
public class CaffeineKeyRetriever implements ReloadAheadKeyRetriever {

    @Override
    public Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache) {
        Object nativeCacheO = cache.getNativeCache();
        if (! (nativeCacheO instanceof BoundedLocalCache.BoundedLocalManualCache)) {
            throw new IllegalStateException("Cache is not an instance of BoundedLocalManualCache of Caffeine!");
        }
        BoundedLocalCache.BoundedLocalManualCache<ReloadAheadKey, Object> nativeCache = (BoundedLocalCache.BoundedLocalManualCache) nativeCacheO;
        BoundedLocalCache<ReloadAheadKey, Object> boundedLocalCache = nativeCache.cache();
        Set<ReloadAheadKey> keySet = boundedLocalCache.keySet();
        return keySet.stream();
    }
}
