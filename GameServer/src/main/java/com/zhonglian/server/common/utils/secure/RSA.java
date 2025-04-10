package com.zhonglian.server.common.utils.secure;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class RSA
{
private static String RSA = "RSA";

public KeyPair getKeyPair(int RsaSize) {
KeyPair kPair = null;
try {
KeyPairGenerator kP = KeyPairGenerator.getInstance("RSA");
kP.initialize(RsaSize);
kPair = kP.genKeyPair();
} catch (NoSuchAlgorithmException e) {
e.printStackTrace();
} 
return kPair;
}

public String RSA_encode(String str, PublicKey pKey) {
return str;
}

public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
try {
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(2, privateKey);
return cipher.doFinal(encryptedData);
} catch (Exception e) {
System.out.println("------------------------decryptData error: " + e.toString());
return null;
} 
}

public static byte[] encryptData(byte[] data, PublicKey publicKey) {
try {
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

cipher.init(1, publicKey);

return cipher.doFinal(data);
} catch (Exception e) {
System.out.println("------------------------encryptData error: " + e.toString());
e.printStackTrace();
return null;
} 
}

public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
byte[] buffer = Base64.decode(publicKeyStr);
KeyFactory keyFactory = KeyFactory.getInstance(RSA);
X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
return keyFactory.generatePublic(keySpec);
}

public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
byte[] buffer = Base64.decode(privateKeyStr);
PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
KeyFactory keyFactory = KeyFactory.getInstance(RSA);
return keyFactory.generatePrivate(keySpec);
}

public static String getKeyString(Key key) throws Exception {
byte[] keyBytes = key.getEncoded();
return Base64.encode(keyBytes);
}

public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
BigInteger bigIntModulus = new BigInteger(modulus);
BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
KeyFactory keyFactory = KeyFactory.getInstance(RSA);
PublicKey publicKey = keyFactory.generatePublic(keySpec);
return publicKey;
}

public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
BigInteger bigIntModulus = new BigInteger(modulus);
BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
KeyFactory keyFactory = KeyFactory.getInstance(RSA);
PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
return privateKey;
}
}

