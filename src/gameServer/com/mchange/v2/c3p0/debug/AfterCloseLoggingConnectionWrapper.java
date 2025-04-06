/*    */ package com.mchange.v2.c3p0.debug;
/*    */ 
/*    */ import com.mchange.v2.log.MLevel;
/*    */ import com.mchange.v2.log.MLog;
/*    */ import com.mchange.v2.log.MLogger;
/*    */ import com.mchange.v2.reflect.ReflectUtils;
/*    */ import com.mchange.v2.sql.filter.FilterConnection;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLWarning;
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
/*    */ public class AfterCloseLoggingConnectionWrapper
/*    */   extends FilterConnection
/*    */ {
/* 47 */   static final MLogger logger = MLog.getLogger(AfterCloseLoggingConnectionWrapper.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public static Connection wrap(Connection inner) {
/*    */     try {
/* 53 */       Constructor<Connection> ctor = ReflectUtils.findProxyConstructor(AfterCloseLoggingConnectionWrapper.class.getClassLoader(), Connection.class);
/* 54 */       return ctor.newInstance(new Object[] { new AfterCloseLoggingInvocationHandler(inner) });
/*    */     }
/* 56 */     catch (Exception e) {
/*    */       
/* 58 */       if (logger.isLoggable(MLevel.SEVERE)) {
/* 59 */         logger.log(MLevel.SEVERE, "An unexpected Exception occured while trying to instantiate a dynamic proxy.", e);
/*    */       }
/* 61 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static class AfterCloseLoggingInvocationHandler
/*    */     implements InvocationHandler
/*    */   {
/*    */     final Connection inner;
/* 69 */     volatile SQLWarning closeStackTrace = null;
/*    */     
/*    */     AfterCloseLoggingInvocationHandler(Connection inner) {
/* 72 */       this.inner = inner;
/*    */     }
/*    */     
/*    */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 76 */       if ("close".equals(method.getName()) && this.closeStackTrace == null) {
/* 77 */         this.closeStackTrace = new SQLWarning("DEBUG STACK TRACE -- " + this.inner + ".close() first-call stack trace.");
/* 78 */       } else if (this.closeStackTrace != null) {
/*    */         
/* 80 */         if (AfterCloseLoggingConnectionWrapper.logger.isLoggable(MLevel.INFO))
/* 81 */           AfterCloseLoggingConnectionWrapper.logger.log(MLevel.INFO, String.format("Method '%s' called after call to Connection close().", new Object[] { method })); 
/* 82 */         if (AfterCloseLoggingConnectionWrapper.logger.isLoggable(MLevel.FINE)) {
/*    */           
/* 84 */           AfterCloseLoggingConnectionWrapper.logger.log(MLevel.FINE, "After-close() method call stack trace:", new SQLWarning("DEBUG STACK TRACE -- ILLEGAL use of " + this.inner + " after call to close()."));
/* 85 */           AfterCloseLoggingConnectionWrapper.logger.log(MLevel.FINE, "Original close() call stack trace:", this.closeStackTrace);
/*    */         } 
/*    */       } 
/*    */       
/* 89 */       return method.invoke(this.inner, args);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/debug/AfterCloseLoggingConnectionWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */