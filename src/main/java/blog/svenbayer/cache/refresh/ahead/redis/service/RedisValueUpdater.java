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
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

/**
 * Updates values of a Redis cache.
 *
 * @author Sven Bayer
 */
public class RedisValueUpdater implements ReloadAheadValueUpdater {

	@Override
	public void writeValueToCache(Cache cache, ReloadAheadKey key, Object value) {
		TransactionAwareCacheDecorator nativeCache = (TransactionAwareCacheDecorator) cache
				.getNativeCache();
		nativeCache.getTargetCache().put(key, value);
	}

}
