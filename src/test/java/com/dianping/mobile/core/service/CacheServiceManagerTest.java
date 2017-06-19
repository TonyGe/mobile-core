package com.dianping.mobile.core.service;

import org.junit.Test;

import com.dianping.mobile.core.notifier.AbstractNotifierJob;

import junit.framework.Assert;

public class CacheServiceManagerTest {
	
	class TestJob extends AbstractNotifierJob {
		
		private int counter = 0;
		
		TestJob() {
			setInterval(600);
		}

		@Override
		protected void doNotification() {
			counter ++;
		}
		
		public int getCounter() {
			return counter;
		}
		
	}
	
	@Test
	public void testRestart() {
		CacheServiceManager manager = new CacheServiceManager();
		manager.start();
		manager.stop();
		boolean result = false;
		try {
			manager.start();
		} catch (RuntimeException re) {
			result = true;
		}
		Assert.assertTrue(result);
	}
	
	@Test
	public void testRun() {
		CacheServiceManager manager = new CacheServiceManager();
		manager.setInterval(500);
		manager.start();
		TestJob job = new TestJob();
		manager.register("test", "test", job);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.unregister("test", "test");
		manager.stop();
		Assert.assertTrue(job.getCounter() > 0);
		
	}

}
