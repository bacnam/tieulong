/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.lang.ByteUtils;
/*     */ import com.mchange.util.PasswordManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HexAsciiMD5PropertiesPasswordManager
/*     */   implements PasswordManager
/*     */ {
/*     */   private static final String DIGEST_ALGORITHM = "MD5";
/*     */   private static final String PASSWORD_ENCODING = "8859_1";
/*     */   private static final String DEF_PASSWORD_PROP_PFX = "password";
/*     */   private static final String DEF_HEADER = "com.mchange.util.impl.HexAsciiMD5PropertiesPasswordManager data";
/*     */   private static final boolean DEBUG = true;
/*     */   SyncedProperties props;
/*     */   String pfx;
/*     */   MessageDigest md;
/*     */   
/*     */   public HexAsciiMD5PropertiesPasswordManager(File paramFile, String paramString, String[] paramArrayOfString) throws IOException {
/*  60 */     this(new SyncedProperties(paramFile, paramArrayOfString), paramString);
/*     */   }
/*     */   public HexAsciiMD5PropertiesPasswordManager(File paramFile, String paramString1, String paramString2) throws IOException {
/*  63 */     this(new SyncedProperties(paramFile, paramString2), paramString1);
/*     */   }
/*     */   public HexAsciiMD5PropertiesPasswordManager(File paramFile) throws IOException {
/*  66 */     this(paramFile, "password", "com.mchange.util.impl.HexAsciiMD5PropertiesPasswordManager data");
/*     */   }
/*     */ 
/*     */   
/*     */   private HexAsciiMD5PropertiesPasswordManager(SyncedProperties paramSyncedProperties, String paramString) throws IOException {
/*     */     try {
/*  72 */       this.props = paramSyncedProperties;
/*  73 */       this.pfx = paramString;
/*  74 */       this.md = MessageDigest.getInstance("MD5");
/*     */     }
/*  76 */     catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/*  77 */       throw new InternalError("MD5 is not supported???");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean validate(String paramString1, String paramString2) throws IOException {
/*     */     try {
/*  84 */       String str = this.props.getProperty((this.pfx != null) ? (this.pfx + '.' + paramString1) : paramString1);
/*  85 */       byte[] arrayOfByte1 = ByteUtils.fromHexAscii(str);
/*  86 */       byte[] arrayOfByte2 = this.md.digest(paramString2.getBytes("8859_1"));
/*  87 */       return Arrays.equals(arrayOfByte1, arrayOfByte2);
/*     */     }
/*  89 */     catch (NumberFormatException numberFormatException) {
/*  90 */       throw new IOException("Password file corrupted! [contains invalid hex ascii string]");
/*  91 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/*  93 */       unsupportedEncodingException.printStackTrace();
/*  94 */       throw new InternalError("8859_1is an unsupported encoding???");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean updatePassword(String paramString1, String paramString2, String paramString3) throws IOException {
/* 100 */     if (!validate(paramString1, paramString2)) return false; 
/* 101 */     this.props.put(this.pfx + '.' + paramString1, ByteUtils.toHexAscii(this.md.digest(paramString3.getBytes("8859_1"))));
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/HexAsciiMD5PropertiesPasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */