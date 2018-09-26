package blog.svenbayer.cache.refresh.ahead;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.redis.RedisCacheRefreshAheadConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Conditional(value = EnableCachingCondition.class)
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Configuration
public class CacheRefreshAheadConfiguration {

    @ConditionalOnClass(name = { "org.springframework.data.redis.connection.RedisConnectionFactory", "org.springframework.data.redis.cache.RedisCacheManager" })
    @ConditionalOnBean(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
    @Bean
    public RedisCacheRefreshAheadConfiguration redisCacheRefreshAheadConfiguration() {
        return new RedisCacheRefreshAheadConfiguration();
    }
}
