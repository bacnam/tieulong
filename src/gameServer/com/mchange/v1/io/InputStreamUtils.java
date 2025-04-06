/*     */ package com.mchange.v1.io;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
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
/*     */ public final class InputStreamUtils
/*     */ {
/*  43 */   private static final MLogger logger = MLog.getLogger(InputStreamUtils.class);
/*     */ 
/*     */   
/*     */   public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2, long paramLong) throws IOException {
/*     */     long l;
/*  48 */     for (l = 0L; l < paramLong; l++) {
/*     */       int i;
/*  50 */       if ((i = paramInputStream1.read()) != paramInputStream2.read())
/*  51 */         return false; 
/*  52 */       if (i < 0)
/*     */         break; 
/*     */     } 
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2) throws IOException {
/*  60 */     int i = 0;
/*  61 */     while (i) {
/*  62 */       if ((i = paramInputStream1.read()) != paramInputStream2.read())
/*  63 */         return false; 
/*  64 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(InputStream paramInputStream, int paramInt) throws IOException {
/*  69 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(paramInt); byte b; int i;
/*  70 */     for (b = 0, i = paramInputStream.read(); i >= 0 && b < paramInt; i = paramInputStream.read(), b++)
/*  71 */       byteArrayOutputStream.write(i); 
/*  72 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(InputStream paramInputStream) throws IOException {
/*  77 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  78 */     for (int i = paramInputStream.read(); i >= 0; ) { byteArrayOutputStream.write(i); i = paramInputStream.read(); }
/*  79 */      return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, String paramString) throws IOException, UnsupportedEncodingException {
/*  84 */     return new String(getBytes(paramInputStream), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream) throws IOException {
/*     */     try {
/*  90 */       return getContentsAsString(paramInputStream, System.getProperty("file.encoding", "8859_1"));
/*  91 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/*  93 */       throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, int paramInt, String paramString) throws IOException, UnsupportedEncodingException {
/* 100 */     return new String(getBytes(paramInputStream, paramInt), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, int paramInt) throws IOException {
/*     */     try {
/* 106 */       return getContentsAsString(paramInputStream, paramInt, System.getProperty("file.encoding", "8859_1"));
/* 107 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/* 109 */       throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static InputStream getEmptyInputStream() {
/* 115 */     return EMPTY_ISTREAM;
/*     */   }
/*     */   
/*     */   public static void attemptClose(InputStream paramInputStream) {
/*     */     try {
/* 120 */       if (paramInputStream != null) paramInputStream.close(); 
/* 121 */     } catch (IOException iOException) {
/*     */ 
/*     */       
/* 124 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 125 */         logger.log(MLevel.WARNING, "InputStream close FAILED.", iOException);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void skipFully(InputStream paramInputStream, long paramLong) throws EOFException, IOException {
/* 131 */     long l = 0L;
/* 132 */     while (l < paramLong) {
/*     */       
/* 134 */       long l1 = paramInputStream.skip(paramLong - l);
/* 135 */       if (l1 > 0L) {
/* 136 */         l += l1;
/*     */         continue;
/*     */       } 
/* 139 */       int i = paramInputStream.read();
/* 140 */       if (paramInputStream.read() < 0) {
/* 141 */         throw new EOFException("Skipped only " + l + " bytes to end of file.");
/*     */       }
/* 143 */       l++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   private static InputStream EMPTY_ISTREAM = new ByteArrayInputStream(new byte[0]);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/io/InputStreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */