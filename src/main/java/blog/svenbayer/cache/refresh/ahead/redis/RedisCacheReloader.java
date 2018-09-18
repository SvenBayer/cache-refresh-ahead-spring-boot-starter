package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.key.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@Configuration
public class RedisCacheReloader {

    private RedisKeyScanner redisKeyScanner;
    private RedisKeyTransformerService redisKeyTransformerService;
    private ReloadAheadService reloadAheadService;

    @Autowired
    public RedisCacheReloader(RedisKeyScanner redisKeyScanner, RedisKeyTransformerService redisKeyTransformerService, ReloadAheadService reloadAheadService) {
        this.redisKeyScanner = redisKeyScanner;
        this.redisKeyTransformerService = redisKeyTransformerService;
        this.reloadAheadService = reloadAheadService;
    }

    public void reloadCache(Cache cache) {
        String cacheName = cache.getName();
        Cursor<byte[]> scan = redisKeyScanner.scan(cacheName);
        scan.forEachRemaining(key -> {
            ReloadAheadKey convertedKey = redisKeyTransformerService.transformReloadAheadKey(key, cacheName);
            Object updatedValue = reloadAheadService.reloadAheadMethod(convertedKey);
            cache.put(convertedKey, updatedValue);
        });
    }
}
