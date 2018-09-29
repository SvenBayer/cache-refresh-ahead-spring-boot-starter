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

package blog.svenbayer.cache.refresh.ahead.service;

import org.springframework.cache.Cache;

import java.util.stream.Stream;

/**
 * Retrieves the keys of a cache and updates the values of a cache.
 *
 * @author Sven Bayer
 */
public class ReloadAheadService {

	private ReloadAheadCacheRetriever cacheRetriever;

	private ReloadAheadKeyRetriever keyRetriever;

	private ReloadAheadValueReloader valueReloader;

	private ReloadAheadValueUpdater valueUpdater;

	public ReloadAheadService(ReloadAheadCacheRetriever cacheRetriever,
			ReloadAheadKeyRetriever keyRetriever, ReloadAheadValueReloader valueReloader,
			ReloadAheadValueUpdater valueUpdater) {
		this.cacheRetriever = cacheRetriever;
		this.keyRetriever = keyRetriever;
		this.valueReloader = valueReloader;
		this.valueUpdater = valueUpdater;
	}

	public void reloadAheadValuesOfCache(String cacheName) {
		Stream<Cache> cacheStream = this.cacheRetriever.retrieveCaches()
				.filter(cache -> cache.getName().equals(cacheName));
		reloadAheadCachValuesForStream(cacheStream);
	}

	public void reloadAheadValuesOfCaches() {
		Stream<Cache> cacheStream = this.cacheRetriever.retrieveCaches();
		reloadAheadCachValuesForStream(cacheStream);

	}

	private void reloadAheadCachValuesForStream(Stream<Cache> cacheStream) {
		cacheStream.forEach(
				cache -> this.keyRetriever.retrieveKeysForCache(cache).forEach(key -> {
					Object value = this.valueReloader.reloadCacheForKey(key);
					this.valueUpdater.writeValueToCache(cache, key, value);
				}));
	}

}
