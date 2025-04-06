/*    */ package com.mchange.v2.codegen.intfc;
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
/*    */ 
/*    */ public final class ReflectiveDelegationPolicy
/*    */ {
/* 40 */   public static final ReflectiveDelegationPolicy USE_MAIN_DELEGATE_INTERFACE = new ReflectiveDelegationPolicy();
/* 41 */   public static final ReflectiveDelegationPolicy USE_RUNTIME_CLASS = new ReflectiveDelegationPolicy();
/*    */   
/*    */   Class delegateClass;
/*    */   
/*    */   private ReflectiveDelegationPolicy() {
/* 46 */     this.delegateClass = null;
/*    */   }
/*    */   
/*    */   public ReflectiveDelegationPolicy(Class paramClass) {
/* 50 */     if (paramClass == null)
/* 51 */       throw new IllegalArgumentException("Class for reflective delegation cannot be null!"); 
/* 52 */     this.delegateClass = paramClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     if (this == USE_MAIN_DELEGATE_INTERFACE)
/* 58 */       return "[ReflectiveDelegationPolicy: Reflectively delegate via the main delegate interface.]"; 
/* 59 */     if (this == USE_RUNTIME_CLASS) {
/* 60 */       return "[ReflectiveDelegationPolicy: Reflectively delegate via the runtime class of the delegate object.]";
/*    */     }
/* 62 */     return "[ReflectiveDelegationPolicy: Reflectively delegate via " + this.delegateClass.getName() + ".]";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/intfc/ReflectiveDelegationPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */