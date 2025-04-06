/*    */ package ch.qos.logback.classic.selector.servlet;
/*    */ 
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.selector.ContextSelector;
/*    */ import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
/*    */ import ch.qos.logback.classic.util.JNDIUtil;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
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
/*    */ public class ContextDetachingSCL
/*    */   implements ServletContextListener
/*    */ {
/*    */   public void contextDestroyed(ServletContextEvent servletContextEvent) {
/* 33 */     String loggerContextName = null;
/*    */     
/*    */     try {
/* 36 */       Context ctx = JNDIUtil.getInitialContext();
/* 37 */       loggerContextName = JNDIUtil.lookup(ctx, "java:comp/env/logback/context-name");
/* 38 */     } catch (NamingException ne) {}
/*    */ 
/*    */     
/* 41 */     if (loggerContextName != null) {
/* 42 */       System.out.println("About to detach context named " + loggerContextName);
/*    */       
/* 44 */       ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
/* 45 */       if (selector == null) {
/* 46 */         System.out.println("Selector is null, cannot detach context. Skipping.");
/*    */         return;
/*    */       } 
/* 49 */       LoggerContext context = selector.getLoggerContext(loggerContextName);
/* 50 */       if (context != null) {
/* 51 */         Logger logger = context.getLogger("ROOT");
/* 52 */         logger.warn("Stopping logger context " + loggerContextName);
/* 53 */         selector.detachLoggerContext(loggerContextName);
/*    */         
/* 55 */         context.stop();
/*    */       } else {
/* 57 */         System.out.println("No context named " + loggerContextName + " was found.");
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void contextInitialized(ServletContextEvent arg0) {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/selector/servlet/ContextDetachingSCL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */