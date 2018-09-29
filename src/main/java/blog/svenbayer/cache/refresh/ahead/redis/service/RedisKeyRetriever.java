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

package blog.svenbayer.cache.refresh.ahead.redis.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Retrieves keys from a Redis cache.
 *
 * @author Sven Bayer
 */
public class RedisKeyRetriever implements ReloadAheadKeyRetriever {

	private RedisConnectionFactory redisConnectionFactory;

	private RedisKeyTransformerService redisKeyTransformerService;

	public RedisKeyRetriever(RedisConnectionFactory redisConnectionFactory,
			RedisKeyTransformerService redisKeyTransformerService) {
		this.redisConnectionFactory = redisConnectionFactory;
		this.redisKeyTransformerService = redisKeyTransformerService;
	}

	@Override
	public Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache) {
		String cacheName = cache.getName();
		Cursor<byte[]> scan = scan(cacheName);
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(scan, Spliterator.ORDERED),
						false)
				.map(key -> this.redisKeyTransformerService.transformReloadAheadKey(key,
						cacheName));
	}

	private Cursor<byte[]> scan(String cacheName) {
		RedisConnection connection = this.redisConnectionFactory.getConnection();
		return connection.keyCommands()
				.scan(ScanOptions.scanOptions().match(cacheName + "*").build());
	}

}
