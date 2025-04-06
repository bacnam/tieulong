/*     */ package com.mchange.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
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
/*     */ public class SmtpMailSender
/*     */   implements MailSender
/*     */ {
/*     */   InetAddress hostAddr;
/*     */   int port;
/*     */   
/*     */   public SmtpMailSender(InetAddress paramInetAddress, int paramInt) {
/*  49 */     this.hostAddr = paramInetAddress;
/*  50 */     this.port = paramInt;
/*     */   }
/*     */   
/*     */   public SmtpMailSender(InetAddress paramInetAddress) {
/*  54 */     this(paramInetAddress, 25);
/*     */   }
/*     */   public SmtpMailSender(String paramString, int paramInt) throws UnknownHostException {
/*  57 */     this(InetAddress.getByName(paramString), paramInt);
/*     */   }
/*     */   public SmtpMailSender(String paramString) throws UnknownHostException {
/*  60 */     this(paramString, 25);
/*     */   }
/*     */   
/*     */   public void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3, String paramString4) throws IOException, ProtocolException, UnsupportedEncodingException {
/*     */     String[] arrayOfString;
/*  65 */     if (paramArrayOfString1 == null || paramArrayOfString1.length < 1)
/*  66 */       throw new SmtpException("You must specify at least one recipient in the \"to\" field."); 
/*  67 */     Properties properties = new Properties();
/*  68 */     properties.put("From", paramString1);
/*  69 */     properties.put("To", makeRecipientString(paramArrayOfString1));
/*  70 */     properties.put("Subject", paramString2);
/*  71 */     properties.put("MIME-Version", "1.0");
/*  72 */     properties.put("Content-Type", "text/plain; charset=" + MimeUtils.normalEncoding(paramString4));
/*  73 */     properties.put("X-Generator", getClass().getName());
/*     */ 
/*     */     
/*  76 */     if (paramArrayOfString2 != null || paramArrayOfString3 != null) {
/*     */       
/*  78 */       int i = paramArrayOfString1.length + ((paramArrayOfString2 != null) ? paramArrayOfString2.length : 0) + ((paramArrayOfString3 != null) ? paramArrayOfString3.length : 0);
/*  79 */       arrayOfString = new String[i];
/*  80 */       int j = 0;
/*  81 */       System.arraycopy(paramArrayOfString1, 0, arrayOfString, j, paramArrayOfString1.length);
/*  82 */       j += paramArrayOfString1.length;
/*  83 */       if (paramArrayOfString2 != null) {
/*     */         
/*  85 */         System.arraycopy(paramArrayOfString2, 0, arrayOfString, j, paramArrayOfString2.length);
/*  86 */         j += paramArrayOfString2.length;
/*  87 */         properties.put("CC", makeRecipientString(paramArrayOfString2));
/*     */       } 
/*  89 */       if (paramArrayOfString3 != null)
/*  90 */         System.arraycopy(paramArrayOfString3, 0, arrayOfString, j, paramArrayOfString3.length); 
/*     */     } else {
/*  92 */       arrayOfString = paramArrayOfString1;
/*  93 */     }  SmtpUtils.sendMail(this.hostAddr, this.port, paramString1, arrayOfString, properties, paramString3.getBytes(paramString4));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMail(String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString2, String paramString3) throws IOException, ProtocolException {
/*     */     try {
/* 100 */       sendMail(paramString1, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramString2, paramString3, System.getProperty("file.encoding"));
/* 101 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 102 */       throw new InternalError("Default encoding [" + System.getProperty("file.encoding") + "] not supported???");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String makeRecipientString(String[] paramArrayOfString) {
/* 107 */     StringBuffer stringBuffer = new StringBuffer(256); byte b; int i;
/* 108 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       
/* 110 */       if (b != 0) stringBuffer.append(", "); 
/* 111 */       stringBuffer.append(paramArrayOfString[b]);
/*     */     } 
/* 113 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 120 */       String[] arrayOfString1 = { "stevewaldman@uky.edu" };
/* 121 */       String[] arrayOfString2 = new String[0];
/* 122 */       String[] arrayOfString3 = { "stevewaldman@mac.com" };
/* 123 */       String str1 = "swaldman@mchange.com";
/* 124 */       String str2 = "Test SmtpMailSender Again";
/* 125 */       String str3 = "Wheeeee!!!";
/*     */       
/* 127 */       SmtpMailSender smtpMailSender = new SmtpMailSender("localhost");
/* 128 */       smtpMailSender.sendMail(str1, arrayOfString1, arrayOfString2, arrayOfString3, str2, str3);
/*     */     }
/* 130 */     catch (Exception exception) {
/* 131 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/SmtpMailSender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */