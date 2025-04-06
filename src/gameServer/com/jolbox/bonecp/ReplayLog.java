/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class ReplayLog
/*    */ {
/*    */   private Object target;
/*    */   private Method method;
/*    */   private Object[] args;
/*    */   
/*    */   public ReplayLog(Object target, Method method, Object[] args) {
/* 42 */     this.target = target;
/* 43 */     this.method = method;
/* 44 */     this.args = args;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Method getMethod() {
/* 50 */     return this.method;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMethod(Method method) {
/* 56 */     this.method = method;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object[] getArgs() {
/* 63 */     return this.args;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setArgs(Object[] args) {
/* 69 */     this.args = args;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getTarget() {
/* 75 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTarget(Object target) {
/* 81 */     this.target = target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return ((this.target == null) ? "" : this.target.getClass().getName()) + "." + ((this.method == null) ? "" : this.method.getName()) + " with args " + ((this.args == null) ? "null" : (String)this.args);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ReplayLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */