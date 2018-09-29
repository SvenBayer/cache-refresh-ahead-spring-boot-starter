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

package blog.svenbayer.cache.refresh.ahead.redis;

import blog.svenbayer.cache.refresh.ahead.redis.service.RedisCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.redis.service.RedisValueUpdater;
import blog.svenbayer.cache.refresh.ahead.redis.transformer.RedisKeyTransformerService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadCacheRetriever;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadKeyRetriever;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueReloader;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadValueUpdater;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Configuration for Redis reload ahead cache.
 *
 * @author Sven Bayer
 */
@AutoConfigureAfter
@Import(ReloadAheadValueReloader.class)
@ConditionalOnClass(name = {
		"org.springframework.data.redis.connection.RedisConnectionFactory",
		"org.springframework.data.redis.cache.RedisCacheManager" })
@Configuration
public class RedisCacheRefreshAheadConfiguration {

	@Bean
	public ReloadAheadValueReloader reloadAheadService(BeanFactory beanFactory) {
		return new ReloadAheadValueReloader(beanFactory);
	}

	@Bean
	public ReloadAheadCacheRetriever redisCacheRetriever(
			RedisCacheManager redisCacheManager) {
		return new RedisCacheRetriever(redisCacheManager);
	}

	@Bean
	public ReloadAheadKeyRetriever redisKeyRetriever(
			RedisConnectionFactory redisConnectionFactory,
			RedisKeyTransformerService redisKeyTransformerService) {
		return new RedisKeyRetriever(redisConnectionFactory, redisKeyTransformerService);
	}

	@Bean
	public ReloadAheadValueUpdater redisValueUpdater() {
		return new RedisValueUpdater();
	}

	@Bean
	public RedisKeyTransformerService redisKeyTransformerService() {
		return new RedisKeyTransformerService();
	}

	@Bean
	public ReloadAheadService reloadAheadService(
			RedisCacheRetriever reloadAheadCacheRetriever,
			RedisKeyRetriever reloadAheadKeyRetriever,
			ReloadAheadValueReloader reloadAheadValueReloader,
			RedisValueUpdater reloadAheadValueUpdater) {
		return new ReloadAheadService(reloadAheadCacheRetriever, reloadAheadKeyRetriever,
				reloadAheadValueReloader, reloadAheadValueUpdater);
	}

}
