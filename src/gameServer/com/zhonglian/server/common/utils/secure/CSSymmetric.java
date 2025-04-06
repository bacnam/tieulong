/*    */ package com.zhonglian.server.common.utils.secure;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CSSymmetric
/*    */ {
/*    */   public static String encrypt(String strPlain, String strKey) {
/* 14 */     if (strPlain == null) {
/* 15 */       return null;
/*    */     }
/*    */     
/* 18 */     if (strKey == null) {
/* 19 */       return strPlain;
/*    */     }
/*    */     
/* 22 */     byte[] byarrPlain = strPlain.getBytes();
/* 23 */     byte[] byarrKey = SecureUtils.stringHex2ByteArray(strKey);
/* 24 */     int iPlainCount = byarrPlain.length;
/* 25 */     int iKeyCount = byarrKey.length;
/* 26 */     byte[] byarrCipher = new byte[iPlainCount];
/* 27 */     for (int i = 0; i < iPlainCount; i++) {
/* 28 */       byarrCipher[i] = (byte)(byarrPlain[i] ^ byarrKey[i % iKeyCount]);
/*    */     }
/*    */     
/* 31 */     return SecureUtils.byteArray2StringHex(byarrCipher);
/*    */   }
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
/*    */   public static String decrypt(String strCipher, String strKey) {
/* 44 */     if (strCipher == null) {
/* 45 */       return null;
/*    */     }
/*    */     
/* 48 */     if (strKey == null) {
/* 49 */       return strCipher;
/*    */     }
/*    */     
/* 52 */     byte[] byarrCipher = SecureUtils.stringHex2ByteArray(strCipher);
/* 53 */     byte[] byarrKey = SecureUtils.stringHex2ByteArray(strKey);
/* 54 */     int iCipherCount = byarrCipher.length;
/* 55 */     int iKeyCount = byarrKey.length;
/* 56 */     byte[] byarrPlain = new byte[iCipherCount];
/* 57 */     for (int i = 0; i < iCipherCount; i++) {
/* 58 */       byarrPlain[i] = (byte)(byarrCipher[i] ^ byarrKey[i % iKeyCount]);
/*    */     }
/*    */     
/* 61 */     return new String(byarrPlain);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/CSSymmetric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */