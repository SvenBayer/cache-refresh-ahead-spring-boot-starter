package blog.svenbayer.cache.refresh.ahead.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = ReloadAheadProperties.CACHE_RELOAD_AHEAD)
public class ReloadAheadProperties {

    static final String CACHE_RELOAD_AHEAD = "cache.reload.ahead";

    private Duration refreshAheadInterval;
    private Duration timeToLive;

    public Duration getRefreshAheadInterval() {
        return refreshAheadInterval;
    }

    public void setRefreshAheadInterval(Duration refreshAheadInterval) {
        this.refreshAheadInterval = refreshAheadInterval;
    }

    public Duration getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }
}