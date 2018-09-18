package blog.svenbayer.cache.refresh.ahead.cache2k;

import blog.svenbayer.cache.refresh.ahead.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.key.ReloadAheadKey;
import org.cache2k.Cache2kBuilder;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.cache2k.integration.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(SpringCache2kCacheManager.class)
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Component
public class Cache2kCacheReloader {

    private ReloadAheadProperties reloadAheadProperties;
    private ReloadAheadService reloadAheadService;

    @Autowired
    public Cache2kCacheReloader(ReloadAheadProperties reloadAheadProperties, ReloadAheadService reloadAheadService) {
        this.reloadAheadProperties = reloadAheadProperties;
        this.reloadAheadService = reloadAheadService;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager reloadAheadCache2kManager() {
        CacheLoader<ReloadAheadKey, Object> loader = new CacheLoader<ReloadAheadKey, Object>() {
            @Override
            public Object load(ReloadAheadKey o) {
                return reloadAheadService.reloadAheadMethod(o);
            }
        };
        SpringCache2kCacheManager cacheManager = new SpringCache2kCacheManager()
                .defaultSetup(builder -> Cache2kBuilder.of(ReloadAheadKey.class, Object.class)
                        .expireAfterWrite(reloadAheadProperties.getTimeToLive().getSeconds(), TimeUnit.SECONDS)
                        .resilienceDuration(reloadAheadProperties.getRefreshAheadInterval().getSeconds(), TimeUnit.SECONDS)
                        .refreshAhead(true)
                        .loader(loader));
        cacheManager.setAllowUnknownCache(true);
        return cacheManager;
    }
}
