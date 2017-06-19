package com.dianping.mobile.core.util;

import com.dianping.mobile.core.exception.ApplicationDecryptException;
import com.dianping.mobile.core.exception.ApplicationEncryptException;
import com.dianping.mobile.core.exception.ApplicationRuntimeException;
import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtils {

    public static final String KEY_ALGORITHM_AES = "AES";
    public static final String CIPHER_ALGORITHM_AES = "AES/ECB/PKCS5Padding";

    private static final Logger log = Logger.getLogger(AESUtils.class);

    public static Key genKey() {
        KeyGenerator kgen;
        try {
            kgen = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
            kgen.init(128, new SecureRandom());
            return kgen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationRuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] key) throws ApplicationDecryptException {
        try {
            SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_AES);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, sks);// 初始化  
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (NoSuchPaddingException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (InvalidKeyException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (IllegalBlockSizeException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (BadPaddingException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        }
    }

    public static byte[] decrypt(byte[] encryptedData, String key) throws ApplicationDecryptException {
        return decrypt(encryptedData, Base64Utils.decode(key));
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws ApplicationEncryptException {
        try {
            SecretKeySpec sks = new SecretKeySpec(key, KEY_ALGORITHM_AES);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_AES);// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, sks);// 初始化  
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (NoSuchPaddingException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (InvalidKeyException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (IllegalBlockSizeException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (BadPaddingException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        }
    }

    public static byte[] encrypt(byte[] data, String key) throws ApplicationEncryptException {
        return encrypt(data, Base64Utils.decode(key));
    }

}
