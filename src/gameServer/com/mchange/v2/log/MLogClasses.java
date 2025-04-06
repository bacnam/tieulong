/*    */ package com.mchange.v2.log;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MLogClasses
/*    */ {
/*    */   static final String LOG4J_CNAME = "com.mchange.v2.log.log4j.Log4jMLog";
/*    */   static final String SLF4J_CNAME = "com.mchange.v2.log.slf4j.Slf4jMLog";
/*    */   static final String JDK14_CNAME = "com.mchange.v2.log.jdk14logging.Jdk14MLog";
/* 46 */   static final String[] SEARCH_CLASSNAMES = new String[] { "com.mchange.v2.log.log4j.Log4jMLog", "com.mchange.v2.log.slf4j.Slf4jMLog", "com.mchange.v2.log.jdk14logging.Jdk14MLog" };
/*    */   
/*    */   static final Map<String, String> ALIASES;
/*    */ 
/*    */   
/*    */   static {
/* 52 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 53 */     hashMap.put("log4j", "com.mchange.v2.log.log4j.Log4jMLog");
/* 54 */     hashMap.put("slf4j", "com.mchange.v2.log.slf4j.Slf4jMLog");
/* 55 */     hashMap.put("jdk14", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
/* 56 */     hashMap.put("jul", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
/* 57 */     hashMap.put("java.util.logging", "com.mchange.v2.log.jdk14logging.Jdk14MLog");
/* 58 */     hashMap.put("fallback", "com.mchange.v2.log.FallbackMLog");
/* 59 */     ALIASES = Collections.unmodifiableMap(hashMap);
/*    */   }
/*    */ 
/*    */   
/*    */   static String resolveIfAlias(String paramString) {
/* 64 */     String str = ALIASES.get(paramString.toLowerCase());
/* 65 */     if (str == null) str = paramString; 
/* 66 */     return str;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/MLogClasses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */