package com.dianping.mobile.core.service;

import com.dianping.mobile.core.cache.MobileCacheKey.CacheType;
import com.dianping.mobile.core.notifier.AbstractNotifierJob;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

public class CacheNotifierJob extends AbstractNotifierJob {
    private final Logger log = Logger.getLogger(this.getClass());

    private static final long INTERVAL_WEB = 4L * 24 * 60 * 60 * 1000;
    private static final long INTERVAL_MC_10M = 10L * 60 * 1000;
    private static final long INTERVAL_MC = 1000L * 24 * 60 * 60 * 1000;

    private MobileBaseService service;
    private Method method;

    public CacheNotifierJob(MobileBaseService service, Method method, CacheType cacheType) {
        this.service = service;
        this.method = method;
        long i = 0;
        switch (cacheType) {
            case WEB_CACHE:
                i = INTERVAL_WEB;
                break;
            case MEMCACHE:
                i = INTERVAL_MC;
                break;
            case MEMCACHE_10M:
                i = INTERVAL_MC_10M;
                break;
            default:
                break;
        }
        //i * (0.8-0.9)
        //randomize interval in case too many jobs run at the same time
        i *= (0.9 - (Math.random() * 0.1));
        setInterval(i);
    }

    @Override
    protected void doNotification() {
        try {
            method.setAccessible(true);
            method.invoke(service);
        } catch (Exception e) {
            log.warn("load cache error!", e);
        }
    }

}
