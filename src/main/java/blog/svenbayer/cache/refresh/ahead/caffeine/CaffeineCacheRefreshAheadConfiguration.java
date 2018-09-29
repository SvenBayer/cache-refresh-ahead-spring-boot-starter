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

package blog.svenbayer.cache.refresh.ahead.caffeine;

import blog.svenbayer.cache.refresh.ahead.caffeine.service.CaffeineCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import com.github.benmanes.caffeine.cache.CaffeineCacheUnwrapper;
import com.github.benmanes.caffeine.cache.CaffeineKeyRetriever;
import com.github.benmanes.caffeine.cache.CaffeineValueUpdater;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration for Caffeine.
 *
 * @author Sven Bayer
 */
@Import(ReloadAheadValueReloader.class)
@Conditional(EnableCachingCondition.class)
@ConditionalOnClass(name = { "org.springframework.cache.caffeine.CaffeineCacheManager",
		"com.github.benmanes.caffeine.cache.Caffeine" })
@Configuration
public class CaffeineCacheRefreshAheadConfiguration {

	@Bean
	public ReloadAheadValueReloader reloadAheadService(BeanFactory beanFactory) {
		return new ReloadAheadValueReloader(beanFactory);
	}

	@Bean
	public ReloadAheadCacheRetriever caffeineCacheRetriever(
			CaffeineCacheManager caffeineCacheManager) {
		return new CaffeineCacheRetriever(caffeineCacheManager);
	}

	@Bean
	public CaffeineCacheUnwrapper caffeineCacheUnwrapper() {
		return new CaffeineCacheUnwrapper();
	}

	@Bean
	public ReloadAheadKeyRetriever caffeineKeyRetriever(
			CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
		return new CaffeineKeyRetriever(caffeineCacheUnwrapper);
	}

	@Bean
	public CaffeineValueUpdater caffeineValueUpdater(
			CaffeineCacheUnwrapper caffeineCacheUnwrapper) {
		return new CaffeineValueUpdater(caffeineCacheUnwrapper);
	}

	@Bean
	public ReloadAheadService reloadAheadService(
			ReloadAheadCacheRetriever reloadAheadCacheRetriever,
			ReloadAheadKeyRetriever reloadAheadKeyRetriever,
			ReloadAheadValueReloader reloadAheadValueReloader,
			ReloadAheadValueUpdater reloadAheadValueUpdater) {
		return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever,
				reloadAheadValueReloader, reloadAheadValueUpdater);
	}

}
