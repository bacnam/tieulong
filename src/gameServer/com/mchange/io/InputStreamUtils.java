/*     */ package com.mchange.io;
/*     */ 
/*     */ import com.mchange.util.RobustMessageLogger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InputStreamUtils
/*     */ {
/*     */   public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2, long paramLong) throws IOException {
/*     */     long l;
/*  50 */     for (l = 0L; l < paramLong; l++) {
/*     */       int i;
/*  52 */       if ((i = paramInputStream1.read()) != paramInputStream2.read())
/*  53 */         return false; 
/*  54 */       if (i < 0)
/*     */         break; 
/*     */     } 
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean compare(InputStream paramInputStream1, InputStream paramInputStream2) throws IOException {
/*  62 */     int i = 0;
/*  63 */     while (i) {
/*  64 */       if ((i = paramInputStream1.read()) != paramInputStream2.read())
/*  65 */         return false; 
/*  66 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(InputStream paramInputStream, int paramInt) throws IOException {
/*  71 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(paramInt); byte b; int i;
/*  72 */     for (b = 0, i = paramInputStream.read(); i >= 0 && b < paramInt; i = paramInputStream.read(), b++)
/*  73 */       byteArrayOutputStream.write(i); 
/*  74 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getBytes(InputStream paramInputStream) throws IOException {
/*  79 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  80 */     for (int i = paramInputStream.read(); i >= 0; ) { byteArrayOutputStream.write(i); i = paramInputStream.read(); }
/*  81 */      return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, String paramString) throws IOException, UnsupportedEncodingException {
/*  86 */     return new String(getBytes(paramInputStream), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream) throws IOException {
/*     */     try {
/*  92 */       return getContentsAsString(paramInputStream, System.getProperty("file.encoding", "8859_1"));
/*  93 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/*  95 */       throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, int paramInt, String paramString) throws IOException, UnsupportedEncodingException {
/* 102 */     return new String(getBytes(paramInputStream, paramInt), paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getContentsAsString(InputStream paramInputStream, int paramInt) throws IOException {
/*     */     try {
/* 108 */       return getContentsAsString(paramInputStream, paramInt, System.getProperty("file.encoding", "8859_1"));
/* 109 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/* 111 */       throw new InternalError("You have no default character encoding, and iso-8859-1 is unsupported?!?!");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static InputStream getEmptyInputStream() {
/* 117 */     return EMPTY_ISTREAM;
/*     */   }
/*     */   public static void attemptClose(InputStream paramInputStream) {
/* 120 */     attemptClose(paramInputStream, null);
/*     */   }
/*     */   
/*     */   public static void attemptClose(InputStream paramInputStream, RobustMessageLogger paramRobustMessageLogger) {
/*     */     try {
/* 125 */       paramInputStream.close();
/* 126 */     } catch (IOException iOException) {
/* 127 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(iOException, "IOException trying to close InputStream"); 
/* 128 */     } catch (NullPointerException nullPointerException) {
/* 129 */       if (paramRobustMessageLogger != null) paramRobustMessageLogger.log(nullPointerException, "NullPointerException trying to close InputStream"); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void skipFully(InputStream paramInputStream, long paramLong) throws EOFException, IOException {
/* 134 */     long l = 0L;
/* 135 */     while (l < paramLong) {
/*     */       
/* 137 */       long l1 = paramInputStream.skip(paramLong - l);
/* 138 */       if (l1 > 0L) {
/* 139 */         l += l1;
/*     */         continue;
/*     */       } 
/* 142 */       int i = paramInputStream.read();
/* 143 */       if (paramInputStream.read() < 0) {
/* 144 */         throw new EOFException("Skipped only " + l + " bytes to end of file.");
/*     */       }
/* 146 */       l++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   private static InputStream EMPTY_ISTREAM = new ByteArrayInputStream(new byte[0]);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/InputStreamUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */