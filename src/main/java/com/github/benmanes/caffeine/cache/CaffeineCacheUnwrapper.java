package com.github.benmanes.caffeine.cache;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class CaffeineCacheUnwrapper {

    public BoundedLocalCache<ReloadAheadKey, Object> unwrapToUnderlyingCache(Cache cache) {
        Object nativeCacheO = cache.getNativeCache();
        if (! (nativeCacheO instanceof BoundedLocalCache.BoundedLocalManualCache)) {
            throw new IllegalStateException("Cache is not an instance of BoundedLocalManualCache of Caffeine!");
        }
        BoundedLocalCache.BoundedLocalManualCache<ReloadAheadKey, Object> nativeCache = (BoundedLocalCache.BoundedLocalManualCache) nativeCacheO;
        return nativeCache.cache();
    }
}
