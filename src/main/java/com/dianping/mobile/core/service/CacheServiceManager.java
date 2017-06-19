package com.dianping.mobile.core.service;

import com.dianping.mobile.core.notifier.AbstractNotifierJob;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class CacheServiceManager implements Runnable {

    private static final long MINIMUM_INTERVAL = 60 * 1000;

//	private final AvatarLogger log = AvatarLoggerFactory.getLogger(toString());

    private final Logger log = Logger.getLogger(this.getClass());

    private Map<String, Map<String, AbstractNotifierJob>> tasks = new HashMap<String, Map<String, AbstractNotifierJob>>();

    private long interval = MINIMUM_INTERVAL;

    private Executor executor = new Executor() {

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    };

    private boolean isStarted = true;

    // private boolean isPaused = false;

    public void start() {
        if (!isStarted) {
            throw new RuntimeException(
                    "cannot start an already stopped CacheServiceManager");
        }
        new Thread(this).start();
    }

    public void stop() {
        isStarted = false;
        synchronized (this) {
            tasks.clear();
            this.notifyAll();
        }
    }

    public void register(String category, String name, AbstractNotifierJob task) {
        synchronized (this) {
            Map<String, AbstractNotifierJob> jobs = tasks.get(category);
            if (jobs == null) {
                jobs = new HashMap<String, AbstractNotifierJob>();
                tasks.put(category, jobs);
            }
            jobs.put(name, task);
            this.notifyAll();
        }
    }

    public void unregister(String category, String name) {
        synchronized (this) {
            Map<String, AbstractNotifierJob> jobs = tasks.get(category);
            if (jobs != null) {
                jobs.remove(name);
            }
        }
    }

    public void unregister(String category) {
        synchronized (this) {
            Map<String, AbstractNotifierJob> jobs = tasks.get(category);
            if (jobs != null) {
                jobs.clear();
            }
        }

    }

    public boolean reload(String category) {
        synchronized (this) {
            Map<String, AbstractNotifierJob> jobs = tasks.get(category);
            if (jobs != null) {
                for (AbstractNotifierJob job : jobs.values()) {
                    job.setLastRunTime(Long.MIN_VALUE);
                }
                this.notifyAll();
                return true;
            }
        }
        return false;
    }

    public boolean reload(String category, String name) {
        synchronized (this) {
            Map<String, AbstractNotifierJob> jobs = tasks.get(category);
            if (jobs != null) {
                AbstractNotifierJob job = jobs.get(name);
                if (job != null) {
                    job.setLastRunTime(Long.MIN_VALUE);
                    this.notifyAll();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (isStarted) {
            long minInterval = Long.MAX_VALUE;
            synchronized (this) {
                for (String category : tasks.keySet()) {
                    Map<String, AbstractNotifierJob> categoryJobs = tasks
                            .get(category);
                    for (AbstractNotifierJob job : categoryJobs.values()) {
                        if (job.getInterval() < minInterval) {
                            minInterval = job.getInterval();
                        }
                        if (job.getInterval() + job.getLastRunTime() <= System
                                .currentTimeMillis()) {
                            executor.execute(job);
                        }
                    }
                }
                if (minInterval < interval
                        || minInterval == Long.MAX_VALUE) {
                    minInterval = interval;
                }
                try {
                    this.wait(minInterval);
                } catch (InterruptedException e) {
                    log.error("main thread has been interrupted", e);
                }
            }
        }
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

}
