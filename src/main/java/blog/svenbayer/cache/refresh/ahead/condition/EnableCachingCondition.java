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

package blog.svenbayer.cache.refresh.ahead.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

/**
 * Condition is fullfilled if the @EnableCaching annotation is declared in the project.
 *
 * @author Sven Bayer
 */
public class EnableCachingCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		ConfigurableListableBeanFactory beanFactory = Objects
				.requireNonNull(context.getBeanFactory());
		boolean isEnableCaching = beanFactory
				.getBeanNamesForAnnotation(EnableCaching.class).length > 0;
		if (isEnableCaching) {
			return new ConditionOutcome(true, "Caching is enabled!");
		}
		else {
			return new ConditionOutcome(false, "Caching is disabled!");
		}
	}

}
