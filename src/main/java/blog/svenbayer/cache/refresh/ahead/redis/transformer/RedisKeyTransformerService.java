package blog.svenbayer.cache.refresh.ahead.redis.transformer;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.key.ReloadAheadKey;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Base64;

@Conditional(value = EnableCachingCondition.class)
@ConditionalOnClass(value = { RedisConnectionFactory.class, RedisCacheManager.class })
@Service
public class RedisKeyTransformerService {

    public ReloadAheadKey transformReloadAheadKey(byte[] key, String cacheName) {
        byte[] prefixBytes = (cacheName + "::").getBytes();
        byte[] keyWithoutPrefix = Arrays.copyOfRange(key, prefixBytes.length, key.length);
        byte[] decodedKey = Base64.getDecoder().decode(keyWithoutPrefix);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decodedKey))) {
            return (ReloadAheadKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
