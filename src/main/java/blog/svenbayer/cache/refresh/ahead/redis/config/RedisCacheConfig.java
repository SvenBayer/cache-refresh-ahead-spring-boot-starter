package blog.svenbayer.cache.refresh.ahead.redis.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

public class RedisCacheConfig extends CachingConfigurerSupport {

    private CacheProperties cacheProperties;
    private RedisProperties redisProperties;

    public RedisCacheConfig(CacheProperties cacheProperties, RedisProperties redisProperties) {
        this.cacheProperties = cacheProperties;
        this.redisProperties = redisProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .disableCachingNullValues();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(redisProperties.getHost());
        redisConf.setPort(redisProperties.getPort());
        redisConf.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisCacheManager reloadAheadRedisCacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }
}