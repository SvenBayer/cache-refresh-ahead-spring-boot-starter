package blog.svenbayer.cache.refresh.ahead.caffeine;

import blog.svenbayer.cache.refresh.ahead.caffeine.service.CaffeineCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.service.*;
import com.github.benmanes.caffeine.cache.CaffeineCacheUnwrapper;
import com.github.benmanes.caffeine.cache.CaffeineKeyRetriever;
import com.github.benmanes.caffeine.cache.CaffeineValueUpdater;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ReloadAheadValueReloader.class)
@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(name = {"org.springframework.cache.caffeine.CaffeineCacheManager", "com.github.benmanes.caffeine.cache.Caffeine"})
@Configuration
public class CaffeineCacheRefreshAheadConfiguration {

    @Bean
    public ReloadAheadValueReloader reloadAheadService(BeanFactory beanFactory) {
        return new ReloadAheadValueReloader(beanFactory);
    }

    @Bean
    public ReloadAheadCacheRetriever caffeineCacheRetriever(CaffeineCacheManager caffeineCacheManager) {
        return new CaffeineCacheRetriever(caffeineCacheManager);
    }

    @Bean
    public CaffeineCacheUnwrapper caffeineCacheUnwrapper() {
        return new CaffeineCacheUnwrapper();
    }

    @Bean
    public ReloadAheadKeyRetriever caffeineKeyRetriever(CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
        return new CaffeineKeyRetriever(caffeineCacheUnwrapper);
    }

    @Bean
    public CaffeineValueUpdater caffeineValueUpdater(CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
        return new CaffeineValueUpdater(caffeineCacheUnwrapper);
    }

    @Bean
    public ReloadAheadService reloadAheadService(ReloadAheadCacheRetriever reloadAheadCacheRetriever, ReloadAheadKeyRetriever reloadAheadKeyRetriever, ReloadAheadValueReloader reloadAheadValueReloader, ReloadAheadValueUpdater reloadAheadValueUpdater) {
        return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever, reloadAheadValueReloader, reloadAheadValueUpdater);
    }
}