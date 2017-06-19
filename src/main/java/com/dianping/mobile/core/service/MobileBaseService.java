/**
 *
 */
package com.dianping.mobile.core.service;

import com.dianping.mobile.core.cache.annotation.CacheTask;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author kewen.yao
 */
public abstract class MobileBaseService implements CacheConfigurable {
    private final Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "serviceCacheFamily")
    protected String serviceCacheFamily;

    private static final CacheServiceManager manager = new CacheServiceManager();

    static {
        manager.start();
    }

    @PostConstruct
    protected void init() {

    }

    @PostConstruct
    protected final void initCache() {
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(CacheTask.class)) {
                CacheTask anno = method.getAnnotation(CacheTask.class);
                manager.register(this.getClass().getName(), anno.name(), new CacheNotifierJob(this, method, anno.cacheType()));
            }
        }
    }

    @Override
    public final boolean reloadCache() {
        return manager.reload(this.getClass().getName());
    }

    @Override
    public final boolean reloadCache(String name) {
        return manager.reload(this.getClass().getName(), name);
    }

    @PreDestroy
    protected final void destroyCache() {
        manager.unregister(this.getClass().getName());
    }

}
