package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.redis.config.RedisCacheConfig;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@ConditionalOnBean(RedisConnectionFactory.class)
@EnableConfigurationProperties({CacheProperties.class, RedisProperties.class})
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
    public RedisKeyTransformerService redisKeyTransformerService() {
        return new RedisKeyTransformerService();
    }
}
