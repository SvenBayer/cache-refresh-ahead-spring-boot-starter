package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.redis.service.RedisCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisValueUpdater;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import blog.svenbayer.cache.refresh.ahead.service.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@AutoConfigureAfter
@Import(ReloadAheadValueReloader.class)
@ConditionalOnClass(name = { "org.springframework.data.redis.connection.RedisConnectionFactory", "org.springframework.data.redis.cache.RedisCacheManager" })
@Configuration
public class RedisCacheRefreshAheadConfiguration {

    @Bean
    public ReloadAheadValueReloader reloadAheadService(BeanFactory beanFactory) {
        return new ReloadAheadValueReloader(beanFactory);
    }

    @Bean
    public ReloadAheadCacheRetriever redisCacheRetriever(RedisCacheManager redisCacheManager) {
        return new RedisCacheRetriever(redisCacheManager);
    }

    @Bean
    public ReloadAheadKeyRetriever redisKeyRetriever(RedisConnectionFactory redisConnectionFactory, RedisKeyTransformerService redisKeyTransformerService) {
        return new RedisKeyRetriever(redisConnectionFactory, redisKeyTransformerService);
    }

    @Bean
    public ReloadAheadValueUpdater redisValueUpdater() {
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
}
