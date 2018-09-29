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

package blog.svenbayer.cache.refresh.ahead;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.exception.ReloadAheadException;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyGenerator;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.task.AllCachesRefreshAheadScheduler;
import blog.svenbayer.cache.refresh.ahead.task.ReloadAheadCacheRefreshAheadScheduler;
import blog.svenbayer.cache.refresh.ahead.task.SelectiveCacheRefreshAheadScheduler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration for reloading caches.
 *
 * @author Sven Bayer
 */
@Import(ReloadAheadService.class)
@Conditional(EnableCachingCondition.class)
@EnableConfigurationProperties(ReloadAheadProperties.class)
@Configuration
public class CacheRefreshAheadConfiguration {

	@Bean
	public ReloadAheadKeyGenerator reloadAheadKeyGenerator() {
		return new ReloadAheadKeyGenerator();
	}

	@Bean
	public ReloadAheadCacheRefreshAheadScheduler reloadAheadCacheRefreshAheadScheduler(
			ReloadAheadProperties reloadAheadProperties,
			ReloadAheadService reloadAheadService) {
		if (reloadAheadProperties.getRefreshAheadInterval() != null) {
			return new AllCachesRefreshAheadScheduler(reloadAheadProperties,
					reloadAheadService);
		}
		else if (reloadAheadProperties.getRefreshAheadIntervalCaches() != null) {
			return new SelectiveCacheRefreshAheadScheduler(reloadAheadProperties,
					reloadAheadService);
		}
		throw new ReloadAheadException(
				"Please, specify the reload aheah interval in seconds as one of the following:\n"
						+ "cache.reload.ahead.refresh-ahead-interval-caches=5s\n"
						+ "cache.reload.ahead.refresh-ahead-interval-caches.longrun=5s");
	}

}
