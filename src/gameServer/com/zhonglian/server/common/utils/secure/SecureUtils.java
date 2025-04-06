/*     */ package com.zhonglian.server.common.utils.secure;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecureUtils
/*     */ {
/*     */   public static final String byteArray2StringHex(byte[] byteArray) {
/*  24 */     StringBuilder buf = new StringBuilder(byteArray.length * 2);
/*     */ 
/*     */     
/*  27 */     for (int i = 0; i < byteArray.length; i++) {
/*  28 */       if ((byteArray[i] & 0xFF) < 16)
/*     */       {
/*  30 */         buf.append("0");
/*     */       }
/*     */       
/*  33 */       buf.append(Long.toString((byteArray[i] & 0xFF), 16));
/*     */     } 
/*  35 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] stringHex2ByteArray(String str) {
/*  45 */     if (str == null) {
/*  46 */       return null;
/*     */     }
/*  48 */     int iCount = str.length() / 2;
/*  49 */     byte[] byarrRet = new byte[iCount];
/*  50 */     for (int i = 0; i < iCount; i++) {
/*  51 */       byarrRet[i] = (byte)(hex2Int(str.charAt(i * 2)) * 16 + hex2Int(str.charAt(i * 2 + 1)));
/*     */     }
/*     */     
/*  54 */     return byarrRet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] string2ByteArray(String str) {
/*  64 */     if (str == null) {
/*  65 */       return null;
/*     */     }
/*     */     
/*  68 */     return str.getBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int hex2Int(char ch) {
/*  78 */     if (ch >= 'a' && ch <= 'f')
/*  79 */       return ch - 97 + 10; 
/*  80 */     if (ch >= '0' && ch <= '9') {
/*  81 */       return ch - 48;
/*     */     }
/*     */     
/*  84 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String decryptConfigFile(String strConfigFile) {
/*  95 */     String strConfigFileTmp = null;
/*     */     
/*  97 */     InputStream isEncrypt = null;
/*     */     try {
/*  99 */       isEncrypt = new FileInputStream(strConfigFile);
/* 100 */     } catch (FileNotFoundException e1) {
/* 101 */       CommLog.error(null, e1);
/*     */     } 
/*     */     
/* 104 */     if (isEncrypt == null) {
/* 105 */       return strConfigFileTmp;
/*     */     }
/*     */ 
/*     */     
/* 109 */     InputStreamReader isr = null;
/*     */     try {
/* 111 */       isr = new InputStreamReader(isEncrypt, "UTF-8");
/* 112 */     } catch (UnsupportedEncodingException e1) {
/* 113 */       CommLog.error(null, e1);
/*     */     } 
/*     */     
/* 116 */     if (isr == null) {
/* 117 */       return strConfigFileTmp;
/*     */     }
/*     */     
/* 120 */     BufferedReader br = null;
/*     */     
/* 122 */     br = new BufferedReader(isr);
/*     */ 
/*     */     
/*     */     try {
/* 126 */       StringBuilder sbContent = new StringBuilder();
/* 127 */       String strLine = br.readLine();
/* 128 */       while (strLine != null) {
/* 129 */         sbContent.append(strLine);
/* 130 */         strLine = br.readLine();
/*     */       } 
/*     */       
/* 133 */       sbContent.deleteCharAt(0);
/*     */       
/* 135 */       CSTea tea = new CSTea();
/* 136 */       String strPlainText = tea.decrypt(stringHex2ByteArray(sbContent.toString()), CSTea.CONFIG_FILE_KEY);
/*     */       
/* 138 */       strConfigFileTmp = String.valueOf(strConfigFile) + ".tmp";
/*     */ 
/*     */       
/* 141 */       File file = new File(strConfigFileTmp);
/* 142 */       if (file.isFile() && file.exists()) {
/* 143 */         file.delete();
/*     */       }
/*     */       
/* 146 */       FileOutputStream fosTemp = new FileOutputStream(strConfigFileTmp);
/* 147 */       fosTemp.write(strPlainText.getBytes());
/* 148 */       fosTemp.close();
/* 149 */     } catch (IOException e) {
/* 150 */       CommLog.error(null, e);
/*     */     } 
/*     */     
/* 153 */     return strConfigFileTmp;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/SecureUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */