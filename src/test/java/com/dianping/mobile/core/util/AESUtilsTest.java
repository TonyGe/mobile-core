package com.dianping.mobile.core.util;

import static org.junit.Assert.assertEquals;

import java.security.Key;

import org.junit.Test;

import com.dianping.mobile.core.exception.ApplicationDecryptException;
import com.dianping.mobile.core.exception.ApplicationEncryptException;

public class AESUtilsTest {
	
	private static final String SOURCE = "aes encrypt/decrypt test";
	
	@Test
	public void test() throws ApplicationDecryptException, ApplicationEncryptException {
		Key key = AESUtils.genKey();
		String base64Key = Base64Utils.encode(key.getEncoded());
		byte[] encryptedData = AESUtils.encrypt(SOURCE.getBytes(), base64Key);
		byte[] decryptedData = AESUtils.decrypt(encryptedData, base64Key);
		String result = new String(decryptedData);
		assertEquals(result, SOURCE);
	}

}
