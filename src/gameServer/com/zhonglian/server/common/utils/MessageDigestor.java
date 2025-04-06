/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
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
/*    */ public class MessageDigestor
/*    */ {
/*    */   public static String md5(String msg) {
/*    */     try {
/* 22 */       return md5(msg.getBytes("UTF-8"));
/* 23 */     } catch (UnsupportedEncodingException e) {
/* 24 */       CommLog.error(e.getMessage(), e);
/* 25 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String md5(byte[] msg) {
/*    */     try {
/* 31 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 32 */       byte[] buf = md5.digest(msg);
/* 33 */       return ByteUtils.toHexAscii(buf);
/* 34 */     } catch (NoSuchAlgorithmException ex) {
/* 35 */       CommLog.error("MessageDigestor.md5", ex);
/* 36 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String sha256(String msg) {
/*    */     try {
/* 42 */       MessageDigest sha = MessageDigest.getInstance("SHA-256");
/*    */       
/* 44 */       byte[] bufOther = msg.getBytes("UTF-8");
/* 45 */       byte[] bufRes = sha.digest(bufOther);
/* 46 */       return ByteUtils.toHexAscii(bufRes);
/* 47 */     } catch (NoSuchAlgorithmException|UnsupportedEncodingException ex) {
/* 48 */       CommLog.error("MessageDigestor.sha256", ex);
/* 49 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/MessageDigestor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */