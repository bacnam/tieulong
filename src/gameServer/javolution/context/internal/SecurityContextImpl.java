/*    */ package javolution.context.internal;
/*    */ 
/*    */ import javolution.context.AbstractContext;
/*    */ import javolution.context.SecurityContext;
/*    */ import javolution.util.FastCollection;
/*    */ import javolution.util.FastTable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SecurityContextImpl
/*    */   extends SecurityContext
/*    */ {
/* 19 */   private FastTable<Action> actions = new FastTable();
/*    */ 
/*    */   
/*    */   public boolean isGranted(SecurityContext.Permission<?> permission) {
/* 23 */     boolean isGranted = true;
/* 24 */     for (Action a : this.actions) {
/* 25 */       if (a.permission.implies(permission))
/* 26 */         isGranted = a.grant; 
/*    */     } 
/* 28 */     return isGranted;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void grant(SecurityContext.Permission<?> permission, Object certificate) throws SecurityException {
/* 34 */     Action a = new Action();
/* 35 */     a.grant = true;
/* 36 */     a.permission = permission;
/* 37 */     this.actions.add(a);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void revoke(SecurityContext.Permission<?> permission, Object certificate) throws SecurityException {
/* 43 */     Action a = new Action();
/* 44 */     a.grant = false;
/* 45 */     a.permission = permission;
/* 46 */     this.actions.add(a);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SecurityContext inner() {
/* 51 */     SecurityContextImpl ctx = new SecurityContextImpl();
/* 52 */     ctx.actions.addAll((FastCollection)this.actions);
/* 53 */     return ctx;
/*    */   }
/*    */   
/*    */   private static class Action {
/*    */     boolean grant;
/*    */     SecurityContext.Permission<?> permission;
/*    */     
/*    */     private Action() {}
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/internal/SecurityContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */