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

import blog.svenbayer.cache.refresh.ahead.exception.ReloadAheadException;
import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Loads the updated value for a cache key.
 *
 * @author Sven Bayer
 */
public class ReloadAheadValueReloader {

	private static final Logger logger = LoggerFactory
			.getLogger(ReloadAheadValueReloader.class);

	private BeanFactory beanFactory;

	public ReloadAheadValueReloader(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Object reloadCacheForKey(ReloadAheadKey key) {
		try {
			logger.info("Starting re-population for parameters '{}'",
					key.getParameters());

			Object proxyBean = this.beanFactory
					.getBean(Class.forName(key.getInstanceName()));
			Object bean;
			if (proxyBean instanceof Advised) {
				bean = ((Advised) proxyBean).getTargetSource().getTarget();
			}
			else {
				bean = proxyBean;
			}
			if (bean == null) {
				logger.warn("Bean for cache could not be resolved!");
				return null;
			}
			Class[] methodClazzes = Arrays.stream(key.getParameterClazzNames())
					.map(clazzName -> {
						try {
							return Class.forName(clazzName);
						}
						catch (ClassNotFoundException ex) {
							throw new ReloadAheadException("Could not find Class '"
									+ clazzName + "' for parameter!", ex);
						}
					}).toArray(Class[]::new);
			Method method = bean.getClass().getMethod(key.getMethodName(), methodClazzes);
			Object cacheValue = method.invoke(bean, key.getParameters());
			logger.info("Finished re-population for parameters '{}' with value '{}'",
					key.getParameters(), cacheValue);
			return cacheValue;
		}
		catch (Exception ex) {
			throw new ReloadAheadException("ReloadError for key " + key, ex);
		}
	}

}
