package blog.svenbayer.cache.refresh.ahead.service;

import blog.svenbayer.cache.refresh.ahead.model.ReloadAheadKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReloadAheadValueReloader {

    private static final Logger logger = LoggerFactory.getLogger(ReloadAheadValueReloader.class);

    private BeanFactory beanFactory;

    public ReloadAheadValueReloader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object reloadCacheForKey(ReloadAheadKey key) {
        try {
            logger.info("Starting re-population for parameters '{}'", key.getParameters());

            Object proxyBean = beanFactory.getBean(Class.forName(key.getInstanceName()));
            Object bean;
            if (proxyBean instanceof Advised) {
                bean = ((Advised) proxyBean).getTargetSource().getTarget();
            } else {
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
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("Could not find Class '" + clazzName + "' for parameter!", e);
                        }
                    })
                    .toArray(Class[]::new);
            Method method = bean.getClass().getMethod(key.getMethodName(), methodClazzes);
            Object cacheValue = method.invoke(bean, key.getParameters());
            logger.info("Finished re-population for parameters '{}' with value '{}'", key.getParameters(), cacheValue);
            return cacheValue;
        } catch (Exception e) {
            throw new IllegalStateException("ReloadError for key " + key, e);
        }
    }
}
