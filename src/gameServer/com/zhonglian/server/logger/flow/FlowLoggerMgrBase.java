/*    */ package com.zhonglian.server.logger.flow;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class FlowLoggerMgrBase<Logger extends FlowLoggerBase>
/*    */ {
/* 14 */   private List<Logger> loggers = new ArrayList<>();
/* 15 */   private Map<String, Method> methods = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean init(Class<Logger> baselogger, String loggerPath) {
/* 19 */     List<Class<?>> loggersClass = CommClass.getAllClassByInterface(baselogger, loggerPath);
/*    */     
/* 21 */     Method[] baseMethod = Object.class.getDeclaredMethods(); byte b; int i;
/*    */     Method[] arrayOfMethod1;
/* 23 */     for (i = (arrayOfMethod1 = baselogger.getMethods()).length, b = 0; b < i; ) { Method method = arrayOfMethod1[b];
/* 24 */       if (!isInSuperType(method, baseMethod)) {
/*    */ 
/*    */         
/* 27 */         if (this.methods.containsKey(method.getName())) {
/* 28 */           CommLog.error("FlowLogger 重复定义了log 方法[{}]", method);
/* 29 */           System.exit(-1);
/*    */         } 
/* 31 */         this.methods.put(method.getName(), method);
/*    */       }  b++; }
/*    */     
/* 34 */     for (Class<?> cs : loggersClass) {
/* 35 */       FlowLoggerBase flowLoggerBase; Logger logger = null;
/*    */       try {
/* 37 */         flowLoggerBase = CommClass.forName(cs.getName()).newInstance();
/* 38 */       } catch (Exception e) {
/* 39 */         CommLog.error("初始化SDK日志失败，原因{}", e.getMessage(), e);
/*    */       } 
/* 41 */       if (flowLoggerBase == null || !flowLoggerBase.isOpen()) {
/*    */         continue;
/*    */       }
/* 44 */       this.loggers.add((Logger)flowLoggerBase);
/* 45 */       CommLog.info("注册SDK日志 {} ", flowLoggerBase.getClass().getSimpleName());
/*    */     } 
/* 47 */     return true; } private boolean isInSuperType(Method method, Method[] baseMethod) {
/*    */     byte b;
/*    */     int i;
/*    */     Method[] arrayOfMethod;
/* 51 */     for (i = (arrayOfMethod = baseMethod).length, b = 0; b < i; ) { Method m = arrayOfMethod[b];
/* 52 */       if (method.getName().equals(m.getName()))
/* 53 */         return true;  b++; }
/*    */     
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   protected void log(String method, Object... params) {
/* 59 */     for (FlowLoggerBase flowLoggerBase : this.loggers) {
/* 60 */       if (!flowLoggerBase.isOpen()) {
/*    */         continue;
/*    */       }
/*    */       
/*    */       try {
/* 65 */         ((Method)this.methods.get(method)).invoke(flowLoggerBase, params);
/*    */       }
/* 67 */       catch (Exception e) {
/* 68 */         CommLog.error("{} 记录 {} 时发生异常,信息:{}", new Object[] { flowLoggerBase.getClass().getSimpleName(), method, e.getMessage(), e });
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/flow/FlowLoggerMgrBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */