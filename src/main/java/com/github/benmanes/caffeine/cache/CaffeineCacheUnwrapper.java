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

import blog.svenbayer.cache.refresh.ahead.exception.ReloadAheadException;
import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.Cache;

/**
 * Unwraps a Caffeine cache so it can be updated without changing the original creation
 * time.
 *
 * @author Sven Bayer
 */
public class CaffeineCacheUnwrapper {

	BoundedLocalCache<ReloadAheadKey, Object> unwrapToUnderlyingCache(Cache cache) {
		Object nativeCacheO = cache.getNativeCache();
		if (!(nativeCacheO instanceof BoundedLocalCache.BoundedLocalManualCache)) {
			throw new ReloadAheadException(
					"Cache is not an instance of BoundedLocalManualCache of Caffeine!");
		}
		BoundedLocalCache.BoundedLocalManualCache<ReloadAheadKey, Object> nativeCache = (BoundedLocalCache.BoundedLocalManualCache) nativeCacheO;
		return nativeCache.cache();
	}

}
