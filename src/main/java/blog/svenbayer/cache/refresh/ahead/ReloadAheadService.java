package blog.svenbayer.cache.refresh.ahead;

import blog.svenbayer.cache.refresh.ahead.condition.EnableCachingCondition;
import blog.svenbayer.cache.refresh.ahead.key.ReloadAheadKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;

@Conditional(value = EnableCachingCondition.class)
@Service
public class ReloadAheadService {

    private static final Logger logger = LoggerFactory.getLogger(ReloadAheadService.class);

    private BeanFactory beanFactory;

    @Autowired
    public ReloadAheadService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object reloadAheadMethod(Object objectKey) {
        ReloadAheadKey key = (ReloadAheadKey) objectKey;
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
            throw new IllegalStateException("ReloadError for key " + objectKey, e);
        }
    }
}
