package blog.svenbayer.cache.refresh.ahead.caffeine;

import blog.svenbayer.cache.refresh.ahead.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass({CaffeineCacheManager.class, Caffeine.class})
@Component
public class CaffeineCacheReloader {

    @Autowired
    public CaffeineCacheReloader(CaffeineCacheManager caffeineCacheManager, ReloadAheadService reloadAheadService) {
        caffeineCacheManager.setCacheLoader(reloadAheadService::reloadAheadMethod);
    }
}
