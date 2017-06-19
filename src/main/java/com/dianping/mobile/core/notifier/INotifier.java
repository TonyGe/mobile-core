package com.dianping.mobile.core.notifier;

public interface INotifier extends Runnable {
	
	long getInterval();
	
	long getLastRunTime();

}
