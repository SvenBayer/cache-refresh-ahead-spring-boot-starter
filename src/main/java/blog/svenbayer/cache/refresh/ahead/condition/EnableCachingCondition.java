package blog.svenbayer.cache.refresh.ahead.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Objects;

public class EnableCachingCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = Objects.requireNonNull(context.getBeanFactory());
        boolean isEnableCaching = beanFactory.getBeanNamesForAnnotation(EnableCaching.class).length > 0;
        if (isEnableCaching) {
            return new ConditionOutcome(true, "Caching is enabled!");
        } else {
            return new ConditionOutcome(false, "Caching is disabled!");
        }
    }
}
