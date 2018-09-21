package blog.svenbayer.cache.refresh.ahead.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service("reloadAheadKeyGenerator")
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