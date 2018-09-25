package blog.svenbayer.cache.refresh.ahead.caffeine;

import blog.svenbayer.cache.refresh.ahead.caffeine.service.CaffeineCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineCacheUnwrapper;
import com.github.benmanes.caffeine.cache.CaffeineKeyRetriever;
import com.github.benmanes.caffeine.cache.CaffeineValueUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass({CaffeineCacheManager.class, Caffeine.class})
@ConditionalOnBean(CaffeineCacheManager.class)
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Configuration
public class CaffeineCacheRefreshAheadConfiguration {

    @Bean
    public CaffeineCacheRetriever caffeineCacheRetriever(CaffeineCacheManager caffeineCacheManager) {
        return new CaffeineCacheRetriever(caffeineCacheManager);
    }

    @Bean
    public CaffeineCacheUnwrapper caffeineCacheUnwrapper() {
        return new CaffeineCacheUnwrapper();
    }

    @Bean
    public CaffeineKeyRetriever caffeineKeyRetriever(CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
        return new CaffeineKeyRetriever(caffeineCacheUnwrapper);
    }

    @Bean
    public CaffeineValueUpdater caffeineValueUpdater(CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
        return new CaffeineValueUpdater(caffeineCacheUnwrapper);
    }

    @Bean
    public ReloadAheadService reloadAheadService(CaffeineCacheRetriever reloadAheadCacheRetriever, CaffeineKeyRetriever reloadAheadKeyRetriever, ReloadAheadValueReloader reloadAheadValueReloader, ReloadAheadValueUpdater reloadAheadValueUpdater) {
        return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever, reloadAheadValueReloader, reloadAheadValueUpdater);
    }
}