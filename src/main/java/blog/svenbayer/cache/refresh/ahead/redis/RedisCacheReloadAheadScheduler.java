package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Component
public class RedisCacheReloadAheadScheduler {

    private RedisCacheReloader redisCacheReloader;
    private ReloadAheadProperties reloadAheadProperties;
    private CacheManager redisCacheManager;

    @Autowired
    public RedisCacheReloadAheadScheduler(RedisCacheReloader redisCacheReloader, ReloadAheadProperties reloadAheadProperties, CacheManager redisCacheManager) {
        this.redisCacheReloader = redisCacheReloader;
        this.reloadAheadProperties = reloadAheadProperties;
        this.redisCacheManager = redisCacheManager;
    }

    @EventListener
    public void refreshCaches(ContextRefreshedEvent event) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::reloadAheadValuesForKeys,0L, reloadAheadProperties.getRefreshAheadInterval().getSeconds(), TimeUnit.SECONDS);
    }

    private void reloadAheadValuesForKeys() {
        redisCacheManager.getCacheNames().stream()
                .map(cacheName -> redisCacheManager.getCache(cacheName))
                .filter(Objects::nonNull)
                .forEach(redisCacheReloader::reloadCache);
    }
}
