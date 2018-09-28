package blog.svenbayer.cache.refresh.ahead;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyGenerator;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.task.ReloadAheadCacheRefreshAheadScheduler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ReloadAheadService.class)
@Conditional(value = EnableCachingCondition.class)
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Configuration
public class CacheRefreshAheadConfiguration {

    @Bean
    public ReloadAheadKeyGenerator reloadAheadKeyGenerator() {
        return new ReloadAheadKeyGenerator();
    }

    @Bean
    public ReloadAheadCacheRefreshAheadScheduler reloadAheadCacheRefreshAheadScheduler(ReloadAheadProperties reloadAheadProperties, ReloadAheadService reloadAheadService) {
        return new ReloadAheadCacheRefreshAheadScheduler(reloadAheadProperties, reloadAheadService);
    }
}
