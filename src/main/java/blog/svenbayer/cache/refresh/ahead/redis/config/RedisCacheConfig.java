package blog.svenbayer.cache.refresh.ahead.redis.config;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class, RedisProperties.class})
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    private CacheProperties cacheProperties;
    private RedisProperties redisProperties;

    @Autowired
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
    public CacheManager reloadAheadRedisCacheManager(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
    }
}