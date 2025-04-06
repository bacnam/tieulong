/*     */ package com.zhonglian.server.common;
/*     */ 
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ 
/*     */ public class Config
/*     */ {
/*     */   public static int GameID() {
/*   8 */     return 3;
/*     */   }
/*     */   
/*     */   public static String getPlatform() {
/*  12 */     return System.getProperty("Platform", "");
/*     */   }
/*     */ 
/*     */   
/*  16 */   private static Integer serverIid = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ServerID() {
/*  22 */     if (serverIid == null) {
/*  23 */       serverIid = Integer.getInteger("ServerID_Int");
/*  24 */       if (serverIid == null) {
/*  25 */         throw new UnsupportedOperationException("ServerID_Int 不能为空");
/*     */       }
/*     */     } 
/*  28 */     return serverIid.intValue();
/*     */   }
/*     */   
/*  31 */   private static String serverSid = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ServerIDStr() {
/*  37 */     if (serverSid == null) {
/*  38 */       serverSid = System.getProperty("ServerID_Str");
/*  39 */       if (serverSid == null || serverSid.trim().isEmpty()) {
/*  40 */         throw new UnsupportedOperationException("ServerID_Str 不能为空");
/*     */       }
/*     */     } 
/*  43 */     return serverSid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String DBUrl() {
/*  51 */     return System.getProperty("DB_URL");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String DBUser() {
/*  58 */     return System.getProperty("DB_USER");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String DBPassword() {
/*  65 */     return System.getProperty("DB_PASSWORD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String LogDBUrl() {
/*  72 */     return System.getProperty("LOGDB_URL");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String LogDBUser() {
/*  79 */     return System.getProperty("LOGDB_USER");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String LogDBPassword() {
/*  86 */     return System.getProperty("LOGDB_PASSWORD");
/*     */   }
/*     */ 
/*     */   
/*  90 */   private static int startTime = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getServerStartTime() {
/*  96 */     return startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setStartTime(int starttime) {
/* 105 */     startTime = starttime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getInitialID() {
/* 114 */     return ServerID() * 100000000000L + 100000000L + Random.nextInt(100000000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String GmHeartBeatAddr() {
/* 123 */     String baseurl = System.getProperty("downConfUrl");
/* 124 */     if (baseurl == null || baseurl.isEmpty()) {
/* 125 */       return null;
/*     */     }
/* 127 */     return System.getProperty("GmHeartBeatAddr", "http://" + baseurl + "/gm/gameStatus");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String PhpMargueeNoticeAddr() {
/* 136 */     return System.getProperty("PhpMargueeNoticeAddr");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String PhpExchangeCodeAddr() {
/* 145 */     return System.getProperty("PhpExchangeCodeAddr");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */