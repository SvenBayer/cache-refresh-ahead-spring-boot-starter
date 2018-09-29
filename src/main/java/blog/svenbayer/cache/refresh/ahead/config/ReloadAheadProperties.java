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

package blog.svenbayer.cache.refresh.ahead.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * Properties for properties of reload ahead caches.
 *
 * @author Sven Bayer
 */
@ConfigurationProperties(prefix = ReloadAheadProperties.CACHE_RELOAD_AHEAD)
public class ReloadAheadProperties {

	static final String CACHE_RELOAD_AHEAD = "cache.reload.ahead";

	private Duration refreshAheadInterval;

	private Map<String, Duration> refreshAheadIntervalCaches;

	private Duration timeToLive;

	public Duration getRefreshAheadInterval() {
		return this.refreshAheadInterval;
	}

	public Map<String, Duration> getRefreshAheadIntervalCaches() {
		return this.refreshAheadIntervalCaches;
	}

	public Duration getTimeToLive() {
		return this.timeToLive;
	}

	public void setRefreshAheadInterval(Duration refreshAheadInterval) {
		this.refreshAheadInterval = refreshAheadInterval;
	}

	public void setRefreshAheadIntervalCaches(
			Map<String, Duration> refreshAheadIntervalCaches) {
		this.refreshAheadIntervalCaches = refreshAheadIntervalCaches;
	}

	public void setTimeToLive(Duration timeToLive) {
		this.timeToLive = timeToLive;
	}

}
