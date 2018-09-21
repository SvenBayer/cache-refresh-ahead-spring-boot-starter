package blog.svenbayer.cache.refresh.ahead;

import blog.svenbayer.cache.refresh.ahead.caffeine.CaffeineCacheRefreshAheadConfiguration;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.redis.RedisCacheRefreshAheadConfiguration;
import blog.svenbayer.cache.refresh.ahead.service.*;
import blog.svenbayer.cache.refresh.ahead.task.ReloadAheadCacheRefreshAheadScheduler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;

@Conditional(value = EnableCachingCondition.class)
@Import({ CaffeineCacheRefreshAheadConfiguration.class, RedisCacheRefreshAheadConfiguration.class })
@EnableConfigurationProperties(ReloadAheadProperties.class)
public class CacheRefreshAheadConfiguration {

    @Bean
    public ReloadAheadKeyGenerator reloadAheadKeyGenerator() {
        return new ReloadAheadKeyGenerator();
    }

    @Bean
    public ReloadAheadService reloadAheadService(ReloadAheadCacheRetriever reloadAheadCacheRetriever, ReloadAheadKeyRetriever reloadAheadKeyRetriever, ReloadAheadValueReloader reloadAheadValueReloader) {
        return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever, reloadAheadValueReloader);
    }

    @Bean
    public ReloadAheadValueReloader reloadAheadService(BeanFactory beanFactory) {
        return new ReloadAheadValueReloader(beanFactory);
    }

    @Bean
    public ReloadAheadCacheRefreshAheadScheduler reloadAheadCacheRefreshAheadScheduler(ReloadAheadProperties reloadAheadProperties, ReloadAheadService reloadAheadService) {
        return new ReloadAheadCacheRefreshAheadScheduler(reloadAheadProperties, reloadAheadService);
    }
}
