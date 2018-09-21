package blog.svenbayer.cache.refresh.ahead.service;

import org.springframework.cache.Cache;

import java.util.stream.Stream;

public interface ReloadAheadCacheRetriever {

    Stream<Cache> retrieveCaches();
}
