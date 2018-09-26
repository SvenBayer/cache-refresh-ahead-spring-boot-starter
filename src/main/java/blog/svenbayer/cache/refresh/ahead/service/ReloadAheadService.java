package blog.svenbayer.cache.refresh.ahead.service;

public class ReloadAheadService {

    private ReloadAheadCacheRetriever cacheRetriever;
    private ReloadAheadKeyRetriever keyRetriever;
    private ReloadAheadValueReloader valueReloader;
    private ReloadAheadValueUpdater valueUpdater;

    public ReloadAheadService(ReloadAheadCacheRetriever cacheRetriever, ReloadAheadKeyRetriever keyRetriever, ReloadAheadValueReloader valueReloader, ReloadAheadValueUpdater valueUpdater) {
        this.cacheRetriever = cacheRetriever;
        this.keyRetriever = keyRetriever;
        this.valueReloader = valueReloader;
        this.valueUpdater = valueUpdater;
    }

    public void reloadAheadValuesOfCaches() {
        cacheRetriever.retrieveCaches()
                .forEach(cache -> keyRetriever.retrieveKeysForCache(cache)
                        .forEach(key -> {
                            Object value = valueReloader.reloadCacheForKey(key);
                            valueUpdater.writeValueToCache(cache, key, value);
                        }));
    }
}
