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

package com.github.benmanes.caffeine.cache;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import org.springframework.cache.Cache;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Retrieves the key of a Caffeine cache.
 *
 * @author Sven Bayer
 */
public class CaffeineKeyRetriever implements ReloadAheadKeyRetriever {

	private CaffeineCacheUnwrapper cacheUnwrapper;

	public CaffeineKeyRetriever(CaffeineCacheUnwrapper cacheUnwrapper) {
		this.cacheUnwrapper = cacheUnwrapper;
	}

	@Override
	public Stream<ReloadAheadKey> retrieveKeysForCache(Cache cache) {
		BoundedLocalCache<ReloadAheadKey, Object> boundedLocalCache = this.cacheUnwrapper
				.unwrapToUnderlyingCache(cache);
		Set<ReloadAheadKey> keySet = boundedLocalCache.keySet();
		return keySet.stream();
	}

}
