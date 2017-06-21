package com.dianping.mobile.core.util;

import com.dianping.mobile.core.exception.ApplicationDecryptException;
import com.dianping.mobile.core.exception.ApplicationEncryptException;
import org.junit.Test;

import java.security.Key;

import static org.junit.Assert.assertEquals;

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
