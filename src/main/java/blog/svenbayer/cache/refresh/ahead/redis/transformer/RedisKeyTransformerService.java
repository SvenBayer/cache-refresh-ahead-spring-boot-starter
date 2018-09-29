/*
 * Copyright 2018-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package blog.svenbayer.cache.refresh.ahead.redis.transformer;

import blog.svenbayer.cache.refresh.ahead.exception.ReloadAheadException;
import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Base64;

/**
 * Transforms a key from Redis to a {@link ReloadAheadKey}.
 *
 * @author Sven Bayer
 */
public class RedisKeyTransformerService {

	public ReloadAheadKey transformReloadAheadKey(byte[] key, String cacheName) {
		byte[] prefixBytes = (cacheName + "::").getBytes();
		byte[] keyWithoutPrefix = Arrays.copyOfRange(key, prefixBytes.length, key.length);
		byte[] decodedKey = Base64.getDecoder().decode(keyWithoutPrefix);
		try (ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(decodedKey))) {
			return (ReloadAheadKey) ois.readObject();
		}
		catch (IOException | ClassNotFoundException ex) {
			throw new ReloadAheadException(
					"Could not transform Redis key of cache '" + cacheName + "'!", ex);
		}
	}

}
