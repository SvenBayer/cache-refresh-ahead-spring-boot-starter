package blog.svenbayer.cache.refresh.ahead.redis.service;

import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Objects;
import java.util.stream.Stream;

public class RedisCacheRetriever implements ReloadAheadCacheRetriever {

    private RedisCacheManager redisCacheManager;

    public RedisCacheRetriever(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public Stream<Cache> retrieveCaches() {
        return redisCacheManager.getCacheNames().stream()
                .map(cacheName -> redisCacheManager.getCache(cacheName))
                .filter(Objects::nonNull);
    }
}
