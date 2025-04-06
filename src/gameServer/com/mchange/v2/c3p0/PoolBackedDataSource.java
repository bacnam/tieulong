/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource;
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
/*    */ public final class PoolBackedDataSource
/*    */   extends AbstractPoolBackedDataSource
/*    */   implements PooledDataSource
/*    */ {
/*    */   public PoolBackedDataSource(boolean autoregister) {
/* 43 */     super(autoregister);
/*    */   }
/*    */   public PoolBackedDataSource() {
/* 46 */     this(true);
/*    */   }
/*    */   
/*    */   public PoolBackedDataSource(String configName) {
/* 50 */     this();
/* 51 */     initializeNamedConfig(configName, false);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/PoolBackedDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */