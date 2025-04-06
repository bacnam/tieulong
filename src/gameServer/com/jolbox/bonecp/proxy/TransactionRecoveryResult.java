/*    */ package com.jolbox.bonecp.proxy;
/*    */ 
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
/*    */ public class TransactionRecoveryResult
/*    */ {
/*    */   private Object result;
/* 33 */   private Map<Object, Object> replaceTarget = new HashMap<Object, Object>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getResult() {
/* 39 */     return this.result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setResult(Object result) {
/* 45 */     this.result = result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<Object, Object> getReplaceTarget() {
/* 51 */     return this.replaceTarget;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/proxy/TransactionRecoveryResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */