package com.zhonglian.server.common.utils.secure;

import BaseCommon.CommLog;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class CSRSAImpl {
    public static String encryptPublic(String _sRSAModulus, String _sRSAPublic, String _sPlain) {
        BigInteger modulus = new BigInteger(_sRSAModulus, 10);
        BigInteger pubExp = new BigInteger(_sRSAPublic, 10);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, key);

            byte[] cipherData = cipher.doFinal(_sPlain.getBytes());

            return Base64.encodeBase64String(cipherData);
        } catch (Exception e) {
            CommLog.error(e.getMessage(), e);

            return null;
        }
    }

    public static String decryptPrivate(String _sRSAModulus, String _sRSAPrivate, String _sCipher) {
        BigInteger modulus = new BigInteger(_sRSAModulus, 10);
        BigInteger privExp = new BigInteger(_sRSAPrivate, 10);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(modulus, privExp);
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, key);

            byte[] plainData = cipher.doFinal(Base64.decodeBase64(_sCipher));

            return new String(plainData);
        } catch (Exception e) {
            CommLog.error(e.getMessage(), e);

            return null;
        }
    }
}

