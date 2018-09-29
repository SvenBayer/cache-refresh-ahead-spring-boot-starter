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

package blog.svenbayer.cache.refresh.ahead.caffeine.service;

import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Retrieves caches from Caffeine.
 *
 * @author Sven Bayer
 */
public class CaffeineCacheRetriever implements ReloadAheadCacheRetriever {

	private CaffeineCacheManager caffeineCacheManager;

	public CaffeineCacheRetriever(CaffeineCacheManager caffeineCacheManager) {
		this.caffeineCacheManager = caffeineCacheManager;
	}

	@Override
	public Stream<Cache> retrieveCaches() {
		return this.caffeineCacheManager.getCacheNames().stream()
				.map(cacheName -> this.caffeineCacheManager.getCache(cacheName))
				.filter(Objects::nonNull);
	}

}
