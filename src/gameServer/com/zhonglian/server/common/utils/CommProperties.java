/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommProperties
/*    */ {
/* 23 */   private static Set<String> loadedProperties = new HashSet<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean load(String file) {
/* 32 */     return load(file, true);
/*    */   }
/*    */   
/*    */   public static boolean load(String file, boolean userlogger) {
/*    */     try {
/* 37 */       Exception exception2, exception1 = null;
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
/*    */ 
/*    */     
/*    */     }
/* 51 */     catch (Exception e) {
/* 52 */       if (userlogger) {
/* 53 */         CommLog.error("加载properties[{}]配置错误：", file, e);
/*    */       } else {
/* 55 */         System.err.println("加载properties[" + file + "]配置错误：");
/* 56 */         e.printStackTrace();
/*    */       } 
/* 58 */       return false;
/*    */     } 
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void printPropertis() {
/* 67 */     for (String filepath : loadedProperties) {
/* 68 */       CommLog.info("");
/* 69 */       CommLog.info("print properties in file:{}", filepath); try {
/* 70 */         Exception exception2, exception1 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 81 */       catch (Exception e) {
/* 82 */         CommLog.error("file({}) load failed", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */