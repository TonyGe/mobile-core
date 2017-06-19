package com.dianping.mobile.core.util;

import com.dianping.mobile.core.exception.ApplicationDecryptException;
import com.dianping.mobile.core.exception.ApplicationEncryptException;
import com.dianping.mobile.core.exception.ApplicationRuntimeException;
import org.apache.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    /**
     * 密钥算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 加密算法RSA
     */
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1PADDING";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final Logger log = Logger.getLogger(RSAUtils.class);


    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     */
    public static Map<String, Key> genKeyPair() {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationRuntimeException(e);
        }
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Key> keyMap = new HashMap<String, Key>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws ApplicationDecryptException
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData,
                                             String privateKey) throws ApplicationDecryptException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet,
                            MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (InvalidKeySpecException e) {
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
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.warn(e);
            }
        }
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws ApplicationDecryptException
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData,
                                            String publicKey) throws ApplicationDecryptException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet,
                            MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen
                            - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationDecryptException(e);
        } catch (InvalidKeySpecException e) {
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
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.warn(e);
            }
        }
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws ApplicationEncryptException
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws ApplicationEncryptException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (InvalidKeySpecException e) {
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
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.warn(e);
            }
        }
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws ApplicationEncryptException
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws ApplicationEncryptException {
        ByteArrayOutputStream out = null;
        try {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (NoSuchAlgorithmException e) {
            log.warn(e);
            throw new ApplicationEncryptException(e);
        } catch (InvalidKeySpecException e) {
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
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.warn(e);
            }
        }
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    public static void main(String[] args) throws Exception {
        Map<String, Key> keyMap = genKeyPair();
        String publicKey = getPublicKey(keyMap);
        String privateKey = getPrivateKey(keyMap);
        System.out.println("public key:");
        System.out.println(publicKey);
        System.out.println("private key:");
        System.out.println(privateKey);
        String plainText = "test RSA encryption";
        byte[] data = plainText.getBytes();

        System.out.println("encrypt by private key:");
        byte[] encryptedDataByPrivateKey = encryptByPrivateKey(data,
                getPrivateKey(keyMap));
        System.out.println(Base64Utils.encode(encryptedDataByPrivateKey));
        byte[] decryptedDataByPublicKey = decryptByPublicKey(
                encryptedDataByPrivateKey, getPublicKey(keyMap));
        String result = new String(decryptedDataByPublicKey);
        System.out.println("result:" + result);
        assert (result.equals(plainText));

        System.out.println("encrypt by public key:");
        byte[] encryptedDataByPublicKey = encryptByPublicKey(data,
                getPublicKey(keyMap));
        System.out.println(Base64Utils.encode(encryptedDataByPublicKey));
        byte[] decryptedDataByPrivateKey = decryptByPrivateKey(
                encryptedDataByPublicKey, getPrivateKey(keyMap));
        result = new String(decryptedDataByPrivateKey);
        System.out.println("result:" + result);
        assert (result.equals(plainText));

//		String base64text = "DUFsmOAy6W1ys2bi+tizEdMVUxtFmplT00Do8KzIag7RNz0rRsqAfgz86YK4ueTTuVNqtap58dAKmYOJAODf5X4pCwSAzZqXKV31s1SZwDwgRb+aV4/QUFlSnUteF5VOGjoivJXmSgG1RP9YRm2bhGsBgbGr7+inlJ1Lxz0XKHs=";
//		byte[] data = Base64Utils.decode(base64text);
//		byte[] result = decryptByPrivateKey(data, privateKey);
//		System.out.println(new String(result));

    }

}