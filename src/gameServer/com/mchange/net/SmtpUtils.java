/*     */ package com.mchange.net;
/*     */ 
/*     */ import com.mchange.io.OutputStreamUtils;
/*     */ import com.mchange.io.ReaderUtils;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ public final class SmtpUtils
/*     */ {
/*     */   private static final String ENC = "8859_1";
/*     */   private static final String CRLF = "\r\n";
/*     */   private static final String CHARSET = "charset";
/*  60 */   private static final int CHARSET_LEN = "charset".length();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_SMTP_PORT = 25;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendMail(InetAddress paramInetAddress, int paramInt, String paramString, String[] paramArrayOfString, Properties paramProperties, byte[] paramArrayOfbyte) throws IOException, SmtpException {
/*  70 */     Socket socket = null;
/*  71 */     DataOutputStream dataOutputStream = null;
/*  72 */     BufferedReader bufferedReader = null;
/*     */ 
/*     */     
/*     */     try {
/*  76 */       socket = new Socket(paramInetAddress, paramInt);
/*  77 */       dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
/*  78 */       bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));
/*  79 */       ensureResponse(bufferedReader, 200, 300);
/*  80 */       dataOutputStream.writeBytes("HELO " + socket.getLocalAddress().getHostName() + "\r\n");
/*     */       
/*  82 */       dataOutputStream.flush();
/*  83 */       ensureResponse(bufferedReader, 200, 300);
/*  84 */       dataOutputStream.writeBytes("MAIL FROM: " + paramString + "\r\n");
/*     */       
/*  86 */       dataOutputStream.flush();
/*  87 */       ensureResponse(bufferedReader, 200, 300);
/*  88 */       for (int i = paramArrayOfString.length; --i >= 0; ) {
/*     */         
/*  90 */         dataOutputStream.writeBytes("RCPT TO: " + paramArrayOfString[i] + "\r\n");
/*     */         
/*  92 */         dataOutputStream.flush();
/*  93 */         ensureResponse(bufferedReader, 200, 300);
/*     */       } 
/*  95 */       dataOutputStream.writeBytes("DATA\r\n");
/*     */       
/*  97 */       dataOutputStream.flush();
/*  98 */       ensureResponse(bufferedReader, 300, 400);
/*     */       
/* 100 */       for (Enumeration<String> enumeration = paramProperties.keys(); enumeration.hasMoreElements(); ) {
/*     */         
/* 102 */         String str1 = enumeration.nextElement();
/* 103 */         String str2 = paramProperties.getProperty(str1);
/* 104 */         dataOutputStream.writeBytes(str1 + ": " + str2 + "\r\n");
/*     */       } 
/* 106 */       dataOutputStream.writeBytes("\r\n");
/* 107 */       dataOutputStream.write(paramArrayOfbyte);
/* 108 */       dataOutputStream.writeBytes("\r\n.\r\n");
/* 109 */       dataOutputStream.flush();
/* 110 */       ensureResponse(bufferedReader, 200, 300);
/* 111 */       dataOutputStream.writeBytes("QUIT\r\n");
/* 112 */       dataOutputStream.flush();
/*     */     }
/* 114 */     catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/* 116 */       unsupportedEncodingException.printStackTrace();
/* 117 */       throw new InternalError("8859_1 not supported???");
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       OutputStreamUtils.attemptClose(dataOutputStream);
/* 122 */       ReaderUtils.attemptClose(bufferedReader);
/* 123 */       SocketUtils.attemptClose(socket);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String encodingFromContentType(String paramString) {
/* 129 */     int i = paramString.indexOf("charset");
/* 130 */     if (i >= 0) {
/*     */       
/* 132 */       String str = paramString.substring(i + CHARSET_LEN);
/* 133 */       str = str.trim();
/*     */ 
/*     */       
/* 136 */       if (str.charAt(0) != '=') return encodingFromContentType(str); 
/* 137 */       str = str.substring(1).trim();
/* 138 */       int j = str.indexOf(';');
/* 139 */       if (j >= 0)
/* 140 */         str = str.substring(0, j); 
/* 141 */       return str;
/*     */     } 
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] bytesFromBodyString(String paramString1, String paramString2) throws UnsupportedEncodingException {
/* 148 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 149 */     PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, paramString2));
/* 150 */     printWriter.print(paramString1);
/* 151 */     printWriter.flush();
/* 152 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void ensureResponse(BufferedReader paramBufferedReader, int paramInt1, int paramInt2) throws IOException, SmtpException {
/* 160 */     String str = paramBufferedReader.readLine();
/*     */ 
/*     */     
/*     */     try {
/* 164 */       int i = Integer.parseInt(str.substring(0, 3));
/* 165 */       for (; str.charAt(3) == '-'; str = paramBufferedReader.readLine());
/* 166 */       if (i < paramInt1 || i >= paramInt2) {
/* 167 */         throw new SmtpException(i, str);
/*     */       }
/* 169 */     } catch (NumberFormatException numberFormatException) {
/* 170 */       throw new SmtpException("Bad SMTP response while mailing document!");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 177 */       InetAddress inetAddress = InetAddress.getByName("mailhub.mchange.com");
/* 178 */       byte b = 25;
/* 179 */       String str = "octavia@mchange.com";
/* 180 */       String[] arrayOfString = { "swaldman@mchange.com", "sw-lists@mchange.com" };
/*     */       
/* 182 */       Properties properties = new Properties();
/* 183 */       properties.put("From", "goolash@mchange.com");
/* 184 */       properties.put("To", "garbage@mchange.com");
/* 185 */       properties.put("Subject", "Test test test AGAIN...");
/*     */       
/* 187 */       byte[] arrayOfByte = "This is a test AGAIN! Imagine that!".getBytes("8859_1");
/*     */       
/* 189 */       sendMail(inetAddress, b, str, arrayOfString, properties, arrayOfByte);
/*     */     }
/* 191 */     catch (Exception exception) {
/* 192 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/SmtpUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */