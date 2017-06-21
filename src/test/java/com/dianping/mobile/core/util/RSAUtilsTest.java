package com.dianping.mobile.core.util;

import org.junit.Test;

import java.security.Key;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RSAUtilsTest {

    private static final String SOURCE = "rsa encrypt/decrypt test";

    @Test
    public void testEncryptByPrivateKey() throws Exception {
        Map<String, Key> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        byte[] data = SOURCE.getBytes();

        byte[] encryptedDataByPrivateKey = RSAUtils.encryptByPrivateKey(data, privateKey);
        byte[] decryptedDataByPublicKey = RSAUtils.decryptByPublicKey(encryptedDataByPrivateKey, publicKey);
        String result = new String(decryptedDataByPublicKey);
        assertEquals(SOURCE, result);
    }

    @Test
    public void testEncryptByPublicKey() throws Exception {
        Map<String, Key> keyMap = RSAUtils.genKeyPair();
        String publicKey = RSAUtils.getPublicKey(keyMap);
        String privateKey = RSAUtils.getPrivateKey(keyMap);
        byte[] data = SOURCE.getBytes();

        byte[] encryptedDataByPrivateKey = RSAUtils.encryptByPublicKey(data, publicKey);
        byte[] decryptedDataByPublicKey = RSAUtils.decryptByPrivateKey(encryptedDataByPrivateKey, privateKey);
        String result = new String(decryptedDataByPublicKey);
        assertEquals(SOURCE, result);
    }

}
