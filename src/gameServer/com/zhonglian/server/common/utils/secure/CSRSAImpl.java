/*    */ package com.zhonglian.server.common.utils.secure;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.math.BigInteger;
/*    */ import java.security.KeyFactory;
/*    */ import java.security.interfaces.RSAPrivateKey;
/*    */ import java.security.interfaces.RSAPublicKey;
/*    */ import java.security.spec.RSAPrivateKeySpec;
/*    */ import java.security.spec.RSAPublicKeySpec;
/*    */ import javax.crypto.Cipher;
/*    */ import org.apache.commons.codec.binary.Base64;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CSRSAImpl
/*    */ {
/*    */   public static String encryptPublic(String _sRSAModulus, String _sRSAPublic, String _sPlain) {
/* 26 */     BigInteger modulus = new BigInteger(_sRSAModulus, 10);
/* 27 */     BigInteger pubExp = new BigInteger(_sRSAPublic, 10);
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 32 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 33 */       RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
/* 34 */       RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(pubKeySpec);
/*    */       
/* 36 */       Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/* 37 */       cipher.init(1, key);
/*    */       
/* 39 */       byte[] cipherData = cipher.doFinal(_sPlain.getBytes());
/*    */       
/* 41 */       return Base64.encodeBase64String(cipherData);
/* 42 */     } catch (Exception e) {
/* 43 */       CommLog.error(e.getMessage(), e);
/*    */ 
/*    */       
/* 46 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String decryptPrivate(String _sRSAModulus, String _sRSAPrivate, String _sCipher) {
/* 58 */     BigInteger modulus = new BigInteger(_sRSAModulus, 10);
/* 59 */     BigInteger privExp = new BigInteger(_sRSAPrivate, 10);
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 64 */       KeyFactory keyFactory = KeyFactory.getInstance("RSA");
/* 65 */       RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(modulus, privExp);
/* 66 */       RSAPrivateKey key = (RSAPrivateKey)keyFactory.generatePrivate(privKeySpec);
/*    */       
/* 68 */       Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
/* 69 */       cipher.init(2, key);
/*    */       
/* 71 */       byte[] plainData = cipher.doFinal(Base64.decodeBase64(_sCipher));
/*    */       
/* 73 */       return new String(plainData);
/* 74 */     } catch (Exception e) {
/* 75 */       CommLog.error(e.getMessage(), e);
/*    */ 
/*    */       
/* 78 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/CSRSAImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */