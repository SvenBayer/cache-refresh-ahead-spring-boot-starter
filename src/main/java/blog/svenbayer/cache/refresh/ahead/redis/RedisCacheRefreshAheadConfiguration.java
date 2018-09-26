package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.redis.config.RedisCacheConfig;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisValueUpdater;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import blog.svenbayer.cache.refresh.ahead.task.ReloadAheadCacheRefreshAheadScheduler;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@EnableConfigurationProperties({CacheProperties.class, RedisProperties.class, ReloadAheadProperties.class})
@Configuration
public class RedisCacheRefreshAheadConfiguration {

    @Bean
    public RedisCacheConfig redisCacheConfig(CacheProperties cacheProperties, RedisProperties redisProperties) {
        return new RedisCacheConfig(cacheProperties, redisProperties);
    }

    @Bean
    public RedisCacheRetriever redisCacheRetriever(RedisCacheManager redisCacheManager) {
        return new RedisCacheRetriever(redisCacheManager);
    }

    @Bean
    public RedisKeyRetriever redisKeyRetriever(RedisConnectionFactory redisConnectionFactory, RedisKeyTransformerService redisKeyTransformerService) {
        return new RedisKeyRetriever(redisConnectionFactory, redisKeyTransformerService);
    }

    @Bean
    public RedisValueUpdater redisValueUpdater() {
        return new RedisValueUpdater();
    }

    @Bean
    public RedisKeyTransformerService redisKeyTransformerService() {
        return new RedisKeyTransformerService();
    }

    @Bean
    public ReloadAheadService reloadAheadService(RedisCacheRetriever reloadAheadCacheRetriever, RedisKeyRetriever reloadAheadKeyRetriever, ReloadAheadValueReloader reloadAheadValueReloader, RedisValueUpdater reloadAheadValueUpdater) {
        return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever, reloadAheadValueReloader, reloadAheadValueUpdater);
    }

    @Bean
    public ReloadAheadCacheRefreshAheadScheduler reloadAheadCacheRefreshAheadScheduler(ReloadAheadProperties reloadAheadProperties, ReloadAheadService reloadAheadService) {
        return new ReloadAheadCacheRefreshAheadScheduler(reloadAheadProperties, reloadAheadService);
    }
}
