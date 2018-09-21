package blog.svenbayer.cache.refresh.ahead.service;

import org.springframework.stereotype.Service;

@Service
public class ReloadAheadService {

    private ReloadAheadCacheRetriever reloadAheadCacheRetriever;
    private ReloadAheadKeyRetriever reloadAheadKeyRetriever;
    private ReloadAheadValueReloader reloadAheadValueReloader;

    public ReloadAheadService(ReloadAheadCacheRetriever reloadAheadCacheRetriever, ReloadAheadKeyRetriever reloadAheadKeyRetriever, ReloadAheadValueReloader reloadAheadValueReloader) {
        this.reloadAheadCacheRetriever = reloadAheadCacheRetriever;
        this.reloadAheadKeyRetriever = reloadAheadKeyRetriever;
        this.reloadAheadValueReloader = reloadAheadValueReloader;
    }

    public void reloadAheadValuesOfCaches() {
        reloadAheadCacheRetriever.retrieveCaches()
                .forEach(cache -> reloadAheadKeyRetriever.retrieveKeysForCache(cache)
                        .forEach(key -> {
                            Object value = reloadAheadValueReloader.reloadCacheForKey(key);
                            cache.put(key, value);
                        }));
    }
}
