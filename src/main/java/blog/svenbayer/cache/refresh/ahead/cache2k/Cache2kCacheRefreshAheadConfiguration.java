package blog.svenbayer.cache.refresh.ahead.cache2k;

import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import org.cache2k.Cache2kBuilder;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.cache2k.integration.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@ConditionalOnClass(SpringCache2kCacheManager.class)
//@EnableConfigurationProperties(ReloadAheadProperties.class)
//@Component
public class Cache2kCacheRefreshAheadConfiguration {

    private ReloadAheadProperties reloadAheadProperties;
    private ReloadAheadValueReloader reloadAheadValueReloader;

    //@Autowired
    public Cache2kCacheRefreshAheadConfiguration(ReloadAheadProperties reloadAheadProperties, ReloadAheadValueReloader reloadAheadValueReloader) {
        this.reloadAheadProperties = reloadAheadProperties;
        this.reloadAheadValueReloader = reloadAheadValueReloader;
    }

    //@Bean
    //@ConditionalOnMissingBean
    public CacheManager reloadAheadCache2kManager() {
        CacheLoader<ReloadAheadKey, Object> loader = new CacheLoader<ReloadAheadKey, Object>() {
            @Override
            public Object load(ReloadAheadKey o) {
                return reloadAheadValueReloader.reloadCacheForKey(o);
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
