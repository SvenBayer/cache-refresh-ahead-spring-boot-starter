package blog.svenbayer.cache.refresh.ahead.redis.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class RedisKeyRetriever implements ReloadAheadKeyRetriever {

    private RedisConnectionFactory redisConnectionFactory;
    private RedisKeyTransformerService redisKeyTransformerService;

    public RedisKeyRetriever(RedisConnectionFactory redisConnectionFactory, RedisKeyTransformerService redisKeyTransformerService) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.redisKeyTransformerService = redisKeyTransformerService;
    }

    @Override
    public Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache) {
        String cacheName = cache.getName();
        Cursor<byte[]> scan = scan(cacheName);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(scan, Spliterator.ORDERED), false)
                .map(key -> redisKeyTransformerService.transformReloadAheadKey(key, cacheName));
    }

    private Cursor<byte[]> scan(String cacheName) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        return connection.keyCommands().scan(ScanOptions.scanOptions().match(cacheName + "*").build());
    }
}