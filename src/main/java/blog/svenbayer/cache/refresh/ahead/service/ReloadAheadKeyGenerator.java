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

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Generates the cache key of a method and parameters.
 *
 * @author Sven Bayer
 */
public class ReloadAheadKeyGenerator implements KeyGenerator {

	public Object generate(Object instance, Method method, Object... parameters) {
		String instanceName = instance.getClass().getName();
		String methodName = method.getName();
		String[] parameterClazzNames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterClazzNames[i] = parameters[i].getClass().getName();
		}
		return new ReloadAheadKey(instanceName, methodName, parameters,
				parameterClazzNames);
	}

}
