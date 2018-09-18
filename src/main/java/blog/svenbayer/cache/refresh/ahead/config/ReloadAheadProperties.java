package blog.svenbayer.cache.refresh.ahead.config;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;

import java.time.Duration;

@Conditional(value = EnableCachingCondition.class)
@ConfigurationProperties(prefix = ReloadAheadProperties.CACHE_RELOAD_AHEAD)
public class ReloadAheadProperties {

    static final String CACHE_RELOAD_AHEAD = "cache.reload.ahead";

    private Duration refreshAheadInterval;

    public Duration getRefreshAheadInterval() {
        return refreshAheadInterval;
    }

    public void setRefreshAheadInterval(Duration refreshAheadInterval) {
        this.refreshAheadInterval = refreshAheadInterval;
    }
}