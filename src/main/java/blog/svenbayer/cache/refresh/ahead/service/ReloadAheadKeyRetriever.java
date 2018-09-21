package blog.svenbayer.cache.refresh.ahead.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.Cache;

import java.util.stream.Stream;

public interface ReloadAheadKeyRetriever {

    Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache);
}
