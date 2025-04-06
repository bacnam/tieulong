/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JNDIUtil
/*    */ {
/*    */   public static Context getInitialContext() throws NamingException {
/* 30 */     return new InitialContext();
/*    */   }
/*    */   
/*    */   public static String lookup(Context ctx, String name) {
/* 34 */     if (ctx == null) {
/* 35 */       return null;
/*    */     }
/*    */     try {
/* 38 */       Object lookup = ctx.lookup(name);
/* 39 */       return (lookup == null) ? null : lookup.toString();
/* 40 */     } catch (NamingException e) {
/* 41 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/util/JNDIUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */