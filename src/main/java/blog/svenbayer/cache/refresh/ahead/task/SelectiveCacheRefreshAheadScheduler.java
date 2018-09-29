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

package blog.svenbayer.cache.refresh.ahead.task;

import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Updates only the caches defined.
 * cache.reload.ahead.refresh-ahead-interval-caches.longrun=5s
 *
 * @author Sven Bayer
 */
public class SelectiveCacheRefreshAheadScheduler
		implements ReloadAheadCacheRefreshAheadScheduler {

	private ReloadAheadProperties reloadAheadProperties;

	private ReloadAheadService reloadAheadService;

	public SelectiveCacheRefreshAheadScheduler(
			ReloadAheadProperties reloadAheadProperties,
			ReloadAheadService reloadAheadService) {
		this.reloadAheadProperties = reloadAheadProperties;
		this.reloadAheadService = reloadAheadService;
	}

	@EventListener
	@Override
	public void refreshCaches(ContextRefreshedEvent event) {
		ScheduledExecutorService executorService = Executors
				.newSingleThreadScheduledExecutor();
		Map<String, Duration> intervalCaches = this.reloadAheadProperties
				.getRefreshAheadIntervalCaches();
		intervalCaches.forEach((key, value) -> executorService.scheduleAtFixedRate(
				() -> this.reloadAheadService.reloadAheadValuesOfCache(key), 0L,
				value.getSeconds(), TimeUnit.SECONDS));
	}

}
