package com.dianping.mobile.core.notifier;

import org.apache.log4j.Logger;


public abstract class AbstractNotifierJob implements INotifier {

    private static final long DEFAULT_INTERVAL = 10L * 60 * 1000;

    private final Logger log = Logger.getLogger(this.getClass());

    private long interval = DEFAULT_INTERVAL;
    private long lastRunTime = Long.MIN_VALUE;

    public AbstractNotifierJob() {
    }

    protected abstract void doNotification();

    @Override
    public void run() {
        doNotification();
        setLastRunTime(System.currentTimeMillis());
    }

    @Override
    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public long getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(long lastRunTime) {
        this.lastRunTime = lastRunTime;
    }


}
