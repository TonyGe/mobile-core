/**
 * 
 */
package com.dianping.mobile.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author kewen.yao
 *
 */
public class ApiUtilTest {
	@Test
	public void testIsValidEmail() {
		String email1 = "qianhao.zhou@dianping.com";
		String email2 = "zhouqianhao@dianping.com";
		String invalid1 = "@dianping.com";
		String invalid2 = "zhou@dianping";
		String invalid3 = "zhou@.com";
		String invalid4 = "zhou.com";
		assertTrue(ApiUtil.isValidEmail(email1));
		assertTrue(ApiUtil.isValidEmail(email2));
		assertFalse(ApiUtil.isValidEmail(invalid1));
		assertFalse(ApiUtil.isValidEmail(invalid2));
		assertFalse(ApiUtil.isValidEmail(invalid3));
		assertFalse(ApiUtil.isValidEmail(invalid4));
	}

	@Test
	public void testParseFile() throws IOException {
		List<String> result = ApiUtil.parseFile("/testApiUtil.txt");
		assertEquals(2, result.size());
	}
	
	@Test
	public void testUnicodeToChinese() throws IOException {
		String chinese = "银行/ATM机";
		String unicode = ApiUtil.unicodeToChinese("\u94f6\u884c/ATM\u673a");
		assertEquals(chinese, unicode);
		
		chinese = "便利店";
		unicode = ApiUtil.unicodeToChinese("\u4fbf\u5229\u5e97");
		assertEquals(chinese, unicode);
	}
	
	@Test
	public void testUnicodeToChineseNull() {
		Assert.assertNull(ApiUtil.unicodeToChinese(null));
	}
	
	@Test
	public void testUnicodeToChineseException() {
		Assert.assertNull(ApiUtil.unicodeToChinese("abc\\u3ds"));
	}
	
	@Test
	public void testGetIp() {
		Pattern pattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
		Assert.assertTrue(pattern.matcher(ApiUtil.getIp()).matches());
		Assert.assertTrue(pattern.matcher(ApiUtil.getIp()).matches());
	}
	
	@Test
	public void testGetHash16() {
		String s = "IsMultiCategoryCity";
		int i = ApiUtil.getHash16(s);
		Assert.assertEquals(0x6ee1, i);
	}
}
