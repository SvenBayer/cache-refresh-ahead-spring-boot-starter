package blog.svenbayer.cache.refresh.ahead.caffeine.service;

import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CaffeineCacheRetriever implements ReloadAheadCacheRetriever {

    private CaffeineCacheManager caffeineCacheManager;

    @Autowired
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
