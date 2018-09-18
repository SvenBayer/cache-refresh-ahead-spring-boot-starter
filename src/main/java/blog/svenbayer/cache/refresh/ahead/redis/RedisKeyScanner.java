package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@Component
public class RedisKeyScanner {

    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisKeyScanner(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public Cursor<byte[]> scan(String cacheName) {
        RedisConnection connection = redisConnectionFactory.getConnection();
        return connection.keyCommands().scan(ScanOptions.scanOptions().match(cacheName + "*").build());
    }
}
