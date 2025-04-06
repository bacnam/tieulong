/*    */ package org.apache.mina.core.filterchain;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IoFilterChainBuilder
/*    */ {
/* 43 */   public static final IoFilterChainBuilder NOOP = new IoFilterChainBuilder()
/*    */     {
/*    */       public void buildFilterChain(IoFilterChain chain) throws Exception {}
/*    */ 
/*    */       
/*    */       public String toString() {
/* 49 */         return "NOOP";
/*    */       }
/*    */     };
/*    */   
/*    */   void buildFilterChain(IoFilterChain paramIoFilterChain) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/IoFilterChainBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */