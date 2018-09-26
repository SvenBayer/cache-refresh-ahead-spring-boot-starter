package com.github.benmanes.caffeine.cache;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import org.springframework.cache.Cache;

import java.util.Set;
import java.util.stream.Stream;

public class CaffeineKeyRetriever implements ReloadAheadKeyRetriever {

    private CaffeineCacheUnwrapper cacheUnwrapper;

    public CaffeineKeyRetriever(CaffeineCacheUnwrapper cacheUnwrapper) {
        this.cacheUnwrapper = cacheUnwrapper;
    }

    @Override
    public Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache) {
        BoundedLocalCache<ReloadAheadKey, Object> boundedLocalCache = cacheUnwrapper.unwrapToUnderlyingCache(cache);
        Set<ReloadAheadKey> keySet = boundedLocalCache.keySet();
        return keySet.stream();
    }
}
