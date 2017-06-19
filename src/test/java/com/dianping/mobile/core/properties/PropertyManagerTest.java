package com.dianping.mobile.core.properties;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.mobile.core.exception.ApplicationRuntimeException;

public class PropertyManagerTest {

	@Test
	public void testGetProperty() {
		String test = PropertyManager.get("test", "test");
		Assert.assertEquals(test, "test property");
		String test2 = PropertyManager.get("test", "test");
		Assert.assertEquals(test2, "test property");
	}

	@Test
	public void testException() {
		boolean hasException = false;
		try {
			PropertyManager.get("exception", "exception");
		} catch (ApplicationRuntimeException e) {
			hasException = true;
		}
		
		Assert.assertTrue(hasException);

	}

}
