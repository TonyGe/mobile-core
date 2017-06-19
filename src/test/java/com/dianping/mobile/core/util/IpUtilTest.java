package com.dianping.mobile.core.util;

import junit.framework.Assert;

import org.junit.Test;

public class IpUtilTest {
	
	@Test
	public void testGetFirstNoLoopbackAddress() {
		Assert.assertNotNull(IpUtil.getFirstNoLoopbackAddress());
	}

}
