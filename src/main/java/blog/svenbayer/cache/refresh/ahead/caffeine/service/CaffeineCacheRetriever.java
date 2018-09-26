package blog.svenbayer.cache.refresh.ahead.caffeine.service;

import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Objects;
import java.util.stream.Stream;

public class CaffeineCacheRetriever implements ReloadAheadCacheRetriever {

    private CaffeineCacheManager caffeineCacheManager;

    public CaffeineCacheRetriever(CaffeineCacheManager caffeineCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
    }

    @Override
    public Stream<Cache> retrieveCaches() {
        return caffeineCacheManager.getCacheNames().stream()
                .map(cacheName -> caffeineCacheManager.getCache(cacheName))
                .filter(Objects::nonNull);
    }
}
