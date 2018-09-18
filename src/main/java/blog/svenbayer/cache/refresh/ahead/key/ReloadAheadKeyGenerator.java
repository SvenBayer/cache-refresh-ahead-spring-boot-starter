package blog.svenbayer.cache.refresh.ahead.key;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Conditional(value = EnableCachingCondition.class)
@Component("reloadAheadKeyGenerator")
public class ReloadAheadKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object instance, Method method, Object... parameters) {
        String instanceName = instance.getClass().getName();
        String methodName = method.getName();
        String[] parameterClazzNames = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterClazzNames[i] = parameters[i].getClass().getName();
        }
        return new ReloadAheadKey(instanceName, methodName, parameters, parameterClazzNames);
    }
}