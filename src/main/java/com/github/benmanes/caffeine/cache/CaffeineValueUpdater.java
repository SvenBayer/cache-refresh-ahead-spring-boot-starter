package com.github.benmanes.caffeine.cache;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class CaffeineValueUpdater implements ReloadAheadValueUpdater {

    private CaffeineCacheUnwrapper cacheUnwrapper;

    @Autowired
    public CaffeineValueUpdater(CaffeineCacheUnwrapper cacheUnwrapper) {
        this.cacheUnwrapper = cacheUnwrapper;
    }

    @Override
    public void writeValueToCache(Cache cache, ReloadAheadKey key, Object value) {
        BoundedLocalCache<ReloadAheadKey, Object> localCache = cacheUnwrapper.unwrapToUnderlyingCache(cache);
        Node<ReloadAheadKey, Object> node = localCache.data.get(key);
        node.setValue(value, null);
    }
}
