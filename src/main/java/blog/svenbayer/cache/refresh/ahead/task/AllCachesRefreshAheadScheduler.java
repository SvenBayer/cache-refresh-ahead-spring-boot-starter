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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduler to update all caches. cache.reload.ahead.refresh-ahead-interval=5s
 *
 * @author Sven Bayer
 */
public class AllCachesRefreshAheadScheduler
		implements ReloadAheadCacheRefreshAheadScheduler {

	private ReloadAheadProperties reloadAheadProperties;

	private ReloadAheadService reloadAheadService;

	public AllCachesRefreshAheadScheduler(ReloadAheadProperties reloadAheadProperties,
			ReloadAheadService reloadAheadService) {
		this.reloadAheadProperties = reloadAheadProperties;
		this.reloadAheadService = reloadAheadService;
	}

	@EventListener
	@Override
	public void refreshCaches(ContextRefreshedEvent event) {
		ScheduledExecutorService executorService = Executors
				.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(
				this.reloadAheadService::reloadAheadValuesOfCaches, 0L,
				this.reloadAheadProperties.getRefreshAheadInterval().getSeconds(),
				TimeUnit.SECONDS);
	}

}
