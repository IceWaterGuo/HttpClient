package com.ice.httpclient.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Desc:
 * Created by icewater on 2024/01/02.
 */
public class EncryptionUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";

    private EncryptionUtils() {
    }

    @FunctionalInterface
    interface WithSalt {
        /**
         * 加盐
         *
         * @param content 加密内容
         * @param salt    盐
         * @return 加盐密文
         */
        String withSalt(String content, String salt);
    }

    private static final WithSalt DEFAULT_WITH_SALT = new WithSalt() {
        @Override
        public String withSalt(String content, String salt) {
            return content + salt;
        }
    };

    @FunctionalInterface
    interface WithoutSalt {
        /**
         * 加盐
         *
         * @param content 加密内容
         * @param salt    盐
         * @return 加盐密文
         */
        String withoutSalt(String content, String salt);
    }

    private static final WithoutSalt DEFAULT_WITHOUT_SALT = new WithoutSalt() {
        @Override
        public String withoutSalt(String content, String salt) {
            if (StringUtils.isEmpty(content)) {
                return content;
            }
            if (content.endsWith(salt)) {
                return content.substring(0, salt.length());
            }
            throw new RuntimeException(content + salt);
        }
    };

    /**
     * RSA 对称加密
     */
    public static class RSA {
        private static final String ALGORITHM = "RSA";
        private static final String ALGORITHMS_SHA1 = "SHA1WithRSA";

        /**
         * 获取公钥
         * <p>
         * throw {@link NoSuchAlgorithmException} 找不到算法异常
         *
         * @param publicKey 公钥
         * @return 公钥
         */
        public static RSAPublicKey getPublicKey(String publicKey) {
            try {

                return (RSAPublicKey) KeyFactory.getInstance(ALGORITHM)
                        .generatePublic(new X509EncodedKeySpec(Base64.decode(publicKey,Base64.NO_WRAP)));
            } catch (Exception e) {
                throw new RuntimeException("RSA get Public Key failed!", e);
            }
        }

        /**
         * 使用公钥或者私钥加密
         * <p>
         * throw {@link NoSuchAlgorithmException} 无效的算法
         *
         * @param content 内容
         * @param key     公钥或者私钥
         * @return 密文
         */
        public static String encrypt(String content, Key key) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return Base64.encodeToString(cipher.doFinal(content.getBytes(DEFAULT_CHARSET)),Base64.NO_WRAP);
            } catch (Exception e) {
                throw new RuntimeException("RSA encrypt failed!", e);
            }
        }


        /**
         * 使用公钥加盐加密
         *
         * @param content   明文
         * @param publicKey 公钥
         * @return 密文
         */
        public static String encrypt(String content, String publicKey) {
            return RSA.encrypt(content, null, publicKey, null);
        }


        /**
         * 使用公钥加盐加密
         *
         * @param content   明文
         * @param key       盐
         * @param publicKey 公钥
         * @param withSalt  明文加盐
         * @return 密文
         */
        public static String encrypt(String content, String key, String publicKey, WithSalt withSalt) {
            return RSA.encrypt(withSalt != null ? withSalt.withSalt(content, key) : content, getPublicKey(publicKey));
        }
    }

    public static class AES {
        private static final String ALGORITHM = "AES";

        /**
         * 生成密钥
         */
        private static SecretKeySpec getSecretKeySpec(String secretKeyStr) {
            return new SecretKeySpec(Base64.decode(secretKeyStr,Base64.NO_WRAP), ALGORITHM);
        }

        //AES/CBC/PKCS5Padding
        public static String encryptWithUrlEncoder(String content, String secretKey) {
            Key key = getSecretKeySpec(secretKey);
            try {
                // 创建密码器
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                // 初始化
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return Base64.encodeToString(cipher.doFinal(content.getBytes(DEFAULT_CHARSET)),Base64.URL_SAFE).replace("\n","");
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 解密
         */
        public static String decryptWithUrlDecoder(String content, String secretKey) {
            Key key = getSecretKeySpec(secretKey);
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                // Base64.decode(content.getBytes(DEFAULT_CHARSET),Base64.CRLF)
                return new String(cipher.doFinal(Base64.decode(content,Base64.URL_SAFE)), StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
