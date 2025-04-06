/*     */ package org.apache.mina.proxy.handlers.http.digest;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashMap;
/*     */ import javax.security.sasl.AuthenticationException;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.ByteUtilities;
/*     */ import org.apache.mina.proxy.utils.StringUtilities;
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
/*     */ public class DigestUtilities
/*     */ {
/*  42 */   public static final String SESSION_HA1 = DigestUtilities.class + ".SessionHA1";
/*     */   
/*     */   private static MessageDigest md5;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  49 */       md5 = MessageDigest.getInstance("MD5");
/*  50 */     } catch (NoSuchAlgorithmException e) {
/*  51 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final String[] SUPPORTED_QOPS = new String[] { "auth", "auth-int" };
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
/*     */   public static String computeResponseValue(IoSession session, HashMap<String, String> map, String method, String pwd, String charsetName, String body) throws AuthenticationException, UnsupportedEncodingException {
/*     */     byte[] hA1, hA2, hFinal;
/*  75 */     boolean isMD5Sess = "md5-sess".equalsIgnoreCase(StringUtilities.getDirectiveValue(map, "algorithm", false));
/*     */     
/*  77 */     if (!isMD5Sess || session.getAttribute(SESSION_HA1) == null) {
/*     */       
/*  79 */       StringBuilder stringBuilder = new StringBuilder();
/*  80 */       stringBuilder.append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "username", true))).append(':');
/*     */ 
/*     */       
/*  83 */       String realm = StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "realm", false));
/*  84 */       if (realm != null) {
/*  85 */         stringBuilder.append(realm);
/*     */       }
/*     */       
/*  88 */       stringBuilder.append(':').append(pwd);
/*     */       
/*  90 */       if (isMD5Sess) {
/*     */         byte[] prehA1;
/*  92 */         synchronized (md5) {
/*  93 */           md5.reset();
/*  94 */           prehA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
/*     */         } 
/*     */         
/*  97 */         stringBuilder = new StringBuilder();
/*  98 */         stringBuilder.append(ByteUtilities.asHex(prehA1));
/*  99 */         stringBuilder.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "nonce", true)));
/*     */         
/* 101 */         stringBuilder.append(':').append(StringUtilities.stringTo8859_1(StringUtilities.getDirectiveValue(map, "cnonce", true)));
/*     */ 
/*     */         
/* 104 */         synchronized (md5) {
/* 105 */           md5.reset();
/* 106 */           hA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
/*     */         } 
/*     */         
/* 109 */         session.setAttribute(SESSION_HA1, hA1);
/*     */       } else {
/* 111 */         synchronized (md5) {
/* 112 */           md5.reset();
/* 113 */           hA1 = md5.digest(stringBuilder.toString().getBytes(charsetName));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 117 */       hA1 = (byte[])session.getAttribute(SESSION_HA1);
/*     */     } 
/*     */     
/* 120 */     StringBuilder sb = new StringBuilder(method);
/* 121 */     sb.append(':');
/* 122 */     sb.append(StringUtilities.getDirectiveValue(map, "uri", false));
/*     */     
/* 124 */     String qop = StringUtilities.getDirectiveValue(map, "qop", false);
/* 125 */     if ("auth-int".equalsIgnoreCase(qop)) {
/* 126 */       byte[] hEntity; ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/*     */ 
/*     */       
/* 129 */       synchronized (md5) {
/* 130 */         md5.reset();
/* 131 */         hEntity = md5.digest(body.getBytes(proxyIoSession.getCharsetName()));
/*     */       } 
/* 133 */       sb.append(':').append(hEntity);
/*     */     } 
/*     */ 
/*     */     
/* 137 */     synchronized (md5) {
/* 138 */       md5.reset();
/* 139 */       hA2 = md5.digest(sb.toString().getBytes(charsetName));
/*     */     } 
/*     */     
/* 142 */     sb = new StringBuilder();
/* 143 */     sb.append(ByteUtilities.asHex(hA1));
/* 144 */     sb.append(':').append(StringUtilities.getDirectiveValue(map, "nonce", true));
/* 145 */     sb.append(":00000001:");
/*     */     
/* 147 */     sb.append(StringUtilities.getDirectiveValue(map, "cnonce", true));
/* 148 */     sb.append(':').append(qop).append(':');
/* 149 */     sb.append(ByteUtilities.asHex(hA2));
/*     */ 
/*     */     
/* 152 */     synchronized (md5) {
/* 153 */       md5.reset();
/* 154 */       hFinal = md5.digest(sb.toString().getBytes(charsetName));
/*     */     } 
/*     */     
/* 157 */     return ByteUtilities.asHex(hFinal);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/digest/DigestUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */