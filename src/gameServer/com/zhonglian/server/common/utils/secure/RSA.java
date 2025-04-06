/*     */ package com.zhonglian.server.common.utils.secure;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.RSAPrivateKeySpec;
/*     */ import java.security.spec.RSAPublicKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import javax.crypto.Cipher;
/*     */ 
/*     */ public class RSA
/*     */ {
/*  20 */   private static String RSA = "RSA";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyPair getKeyPair(int RsaSize) {
/*  30 */     KeyPair kPair = null;
/*     */     try {
/*  32 */       KeyPairGenerator kP = KeyPairGenerator.getInstance("RSA");
/*  33 */       kP.initialize(RsaSize);
/*  34 */       kPair = kP.genKeyPair();
/*  35 */     } catch (NoSuchAlgorithmException e) {
/*  36 */       e.printStackTrace();
/*     */     } 
/*  38 */     return kPair;
/*     */   }
/*     */ 
/*     */   
/*     */   public String RSA_encode(String str, PublicKey pKey) {
/*  43 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
/*     */     try {
/*  57 */       Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/*  58 */       cipher.init(2, privateKey);
/*  59 */       return cipher.doFinal(encryptedData);
/*  60 */     } catch (Exception e) {
/*  61 */       System.out.println("------------------------decryptData error: " + e.toString());
/*  62 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encryptData(byte[] data, PublicKey publicKey) {
/*     */     try {
/*  78 */       Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/*     */       
/*  80 */       cipher.init(1, publicKey);
/*     */       
/*  82 */       return cipher.doFinal(data);
/*  83 */     } catch (Exception e) {
/*  84 */       System.out.println("------------------------encryptData error: " + e.toString());
/*  85 */       e.printStackTrace();
/*  86 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
/*  99 */     byte[] buffer = Base64.decode(publicKeyStr);
/* 100 */     KeyFactory keyFactory = KeyFactory.getInstance(RSA);
/* 101 */     X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
/* 102 */     return keyFactory.generatePublic(keySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
/* 114 */     byte[] buffer = Base64.decode(privateKeyStr);
/* 115 */     PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
/* 116 */     KeyFactory keyFactory = KeyFactory.getInstance(RSA);
/* 117 */     return keyFactory.generatePrivate(keySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getKeyString(Key key) throws Exception {
/* 126 */     byte[] keyBytes = key.getEncoded();
/* 127 */     return Base64.encode(keyBytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
/* 140 */     BigInteger bigIntModulus = new BigInteger(modulus);
/* 141 */     BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
/* 142 */     RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
/* 143 */     KeyFactory keyFactory = KeyFactory.getInstance(RSA);
/* 144 */     PublicKey publicKey = keyFactory.generatePublic(keySpec);
/* 145 */     return publicKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
/* 158 */     BigInteger bigIntModulus = new BigInteger(modulus);
/* 159 */     BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
/* 160 */     RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
/* 161 */     KeyFactory keyFactory = KeyFactory.getInstance(RSA);
/* 162 */     PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
/* 163 */     return privateKey;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/RSA.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */