/*    */ package org.slf4j.helpers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ public class SubstituteLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/* 43 */   final ConcurrentMap<String, SubstituteLogger> loggers = new ConcurrentHashMap<String, SubstituteLogger>();
/*    */   
/*    */   public Logger getLogger(String name) {
/* 46 */     SubstituteLogger logger = this.loggers.get(name);
/* 47 */     if (logger == null) {
/* 48 */       logger = new SubstituteLogger(name);
/* 49 */       SubstituteLogger oldLogger = this.loggers.putIfAbsent(name, logger);
/* 50 */       if (oldLogger != null)
/* 51 */         logger = oldLogger; 
/*    */     } 
/* 53 */     return logger;
/*    */   }
/*    */   
/*    */   public List<String> getLoggerNames() {
/* 57 */     return new ArrayList<String>(this.loggers.keySet());
/*    */   }
/*    */   
/*    */   public List<SubstituteLogger> getLoggers() {
/* 61 */     return new ArrayList<SubstituteLogger>(this.loggers.values());
/*    */   }
/*    */   
/*    */   public void clear() {
/* 65 */     this.loggers.clear();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/SubstituteLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */