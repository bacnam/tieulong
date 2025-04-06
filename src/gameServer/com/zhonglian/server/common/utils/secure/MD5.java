/*    */ package com.zhonglian.server.common.utils.secure;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.security.MessageDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MD5
/*    */ {
/*    */   public static String md5(String inStr) {
/* 13 */     MessageDigest md5 = null;
/* 14 */     byte[] byteArray = null;
/*    */     try {
/* 16 */       md5 = MessageDigest.getInstance("MD5");
/* 17 */       byteArray = inStr.getBytes("utf-8");
/* 18 */     } catch (Exception e) {
/* 19 */       CommLog.error("生成MD5串错误");
/* 20 */       return "";
/*    */     } 
/* 22 */     byte[] md5Bytes = md5.digest(byteArray);
/* 23 */     StringBuffer hexValue = new StringBuffer();
/* 24 */     for (int i = 0; i < md5Bytes.length; i++) {
/* 25 */       int val = md5Bytes[i] & 0xFF;
/* 26 */       if (val < 16)
/* 27 */         hexValue.append("0"); 
/* 28 */       hexValue.append(Integer.toHexString(val));
/*    */     } 
/* 30 */     return hexValue.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/MD5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */