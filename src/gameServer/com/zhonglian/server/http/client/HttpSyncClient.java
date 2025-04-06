/*     */ package com.zhonglian.server.http.client;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Map;
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
/*     */ public class HttpSyncClient
/*     */ {
/*     */   public static String sendGet(String url, String param) {
/*  27 */     String result = "";
/*  28 */     BufferedReader in = null;
/*     */     try {
/*  30 */       String urlNameString = String.valueOf(url) + "?" + param;
/*  31 */       URL realUrl = new URL(urlNameString);
/*     */       
/*  33 */       URLConnection connection = realUrl.openConnection();
/*     */       
/*  35 */       connection.setRequestProperty("accept", "*/*");
/*  36 */       connection.setRequestProperty("connection", "Keep-Alive");
/*  37 */       connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
/*     */       
/*  39 */       connection.connect();
/*     */       
/*  41 */       in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/*     */       String line;
/*  43 */       while ((line = in.readLine()) != null) {
/*  44 */         result = String.valueOf(result) + line;
/*     */       }
/*  46 */     } catch (Exception e) {
/*  47 */       CommLog.error("发送GET请求[{}]出现异常！", url, e);
/*     */     } finally {
/*     */       try {
/*  50 */         if (in != null) {
/*  51 */           in.close();
/*     */         }
/*  53 */       } catch (Exception exception) {}
/*     */     } 
/*     */     
/*  56 */     return result;
/*     */   }
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
/*     */   public static String sendPost(String url, String param) {
/*  69 */     PrintWriter out = null;
/*  70 */     BufferedReader in = null;
/*  71 */     String result = "";
/*     */     try {
/*  73 */       URL realUrl = new URL(url);
/*     */       
/*  75 */       URLConnection conn = realUrl.openConnection();
/*     */       
/*  77 */       conn.setRequestProperty("accept", "*/*");
/*  78 */       conn.setRequestProperty("connection", "Keep-Alive");
/*  79 */       conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
/*     */       
/*  81 */       conn.setDoOutput(true);
/*  82 */       conn.setDoInput(true);
/*     */       
/*  84 */       out = new PrintWriter(conn.getOutputStream());
/*     */       
/*  86 */       out.print(param);
/*     */       
/*  88 */       out.flush();
/*     */       
/*  90 */       in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */       String line;
/*  92 */       while ((line = in.readLine()) != null) {
/*  93 */         result = String.valueOf(result) + line;
/*     */       }
/*  95 */     } catch (Exception e) {
/*  96 */       CommLog.error("发送 POST请求[{}]出现异常！", url, e);
/*     */     } finally {
/*     */       try {
/*  99 */         if (out != null) {
/* 100 */           out.close();
/*     */         }
/* 102 */         if (in != null) {
/* 103 */           in.close();
/*     */         }
/* 105 */       } catch (IOException iOException) {}
/*     */     } 
/*     */     
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   public static String sendPost(String url, Map<String, Object> param) {
/* 112 */     StringBuilder sBuilder = new StringBuilder();
/* 113 */     for (Map.Entry<String, Object> pair : param.entrySet()) {
/*     */       try {
/* 115 */         sBuilder.append(pair.getKey()).append('=').append(URLEncoder.encode(pair.getValue().toString(), "utf-8")).append('&');
/* 116 */       } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */     } 
/*     */     
/* 119 */     if (sBuilder.length() > 0) {
/* 120 */       sBuilder.deleteCharAt(sBuilder.length() - 1);
/*     */     }
/* 122 */     return sendPost(url, sBuilder.toString());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/client/HttpSyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */