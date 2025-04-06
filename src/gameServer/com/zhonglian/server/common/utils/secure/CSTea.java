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
/*    */ 
/*    */ public class CSTea
/*    */ {
/* 14 */   public static String CONFIG_FILE_KEY = "789f5645f68bd5a481963ffa458fac58";
/*    */ 
/*    */   
/* 17 */   private int TEA_TIMES = 32;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String encrypt(String strPlainText, String strKey) {
/* 27 */     CSTeaImpl teaImpl = new CSTeaImpl();
/* 28 */     byte[] byarrPlainText = strPlainText.getBytes();
/* 29 */     int iPaddingCount = 8 - byarrPlainText.length % 8;
/* 30 */     byte[] byarrEncrypt = new byte[byarrPlainText.length + iPaddingCount];
/* 31 */     byarrEncrypt[0] = (byte)iPaddingCount;
/* 32 */     System.arraycopy(byarrPlainText, 0, byarrEncrypt, iPaddingCount, byarrPlainText.length);
/* 33 */     byte[] byarrCipherText = new byte[byarrEncrypt.length];
/* 34 */     for (int offset = 0; offset < byarrCipherText.length; offset += 8) {
/* 35 */       byte[] tempEncrpt = teaImpl.encrypt(byarrEncrypt, offset, _generateKey(strKey), this.TEA_TIMES);
/* 36 */       System.arraycopy(tempEncrpt, 0, byarrCipherText, offset, 8);
/*    */     } 
/* 38 */     return SecureUtils.byteArray2StringHex(byarrCipherText);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String decrypt(byte[] byarrCipher, String strKey) {
/* 49 */     CSTeaImpl teaImpl = new CSTeaImpl();
/* 50 */     byte[] byarrDecrypt = null;
/* 51 */     byte[] byarrDecryptTmp = new byte[byarrCipher.length];
/* 52 */     for (int offset = 0; offset < byarrCipher.length; offset += 8) {
/* 53 */       byarrDecrypt = teaImpl.decrypt(byarrCipher, offset, _generateKey(strKey), this.TEA_TIMES);
/* 54 */       System.arraycopy(byarrDecrypt, 0, byarrDecryptTmp, offset, 8);
/*    */     } 
/*    */     
/* 57 */     int iPadding = byarrDecryptTmp[0];
/* 58 */     return new String(byarrDecryptTmp, iPadding, byarrDecrypt.length - iPadding);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int[] _generateKey(String strKey) {
/* 68 */     int[] iarrRet = new int[4];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 74 */     String strKey1 = strKey.substring(0, 8);
/* 75 */     String strKey2 = strKey.substring(8, 16);
/* 76 */     String strKey3 = strKey.substring(16, 24);
/* 77 */     String strKey4 = strKey.substring(24, 32);
/*    */     
/* 79 */     iarrRet[0] = Long.valueOf(strKey1, 16).intValue();
/* 80 */     iarrRet[1] = Long.valueOf(strKey2, 16).intValue();
/* 81 */     iarrRet[2] = Long.valueOf(strKey3, 16).intValue();
/* 82 */     iarrRet[3] = Long.valueOf(strKey4, 16).intValue();
/*    */     
/* 84 */     return iarrRet;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/CSTea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */