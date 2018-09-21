package blog.svenbayer.cache.refresh.ahead.redis.service;

import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class RedisCacheRetriever implements ReloadAheadCacheRetriever {

    private RedisCacheManager redisCacheManager;

    @Autowired
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
