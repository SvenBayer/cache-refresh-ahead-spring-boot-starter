package blog.svenbayer.cache.refresh.ahead.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.Cache;

public interface ReloadAheadValueUpdater {

    void writeValueToCache(Cache cache, ReloadAheadKey key, Object value);
}
