/*    */ package com.mchange.v2.resourcepool;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ 
/*    */ 
/*    */ public class ResourcePoolEvent
/*    */   extends EventObject
/*    */ {
/*    */   Object resc;
/*    */   boolean checked_out_resource;
/*    */   int pool_size;
/*    */   int available_size;
/*    */   int removed_but_unreturned_size;
/*    */   
/*    */   public ResourcePoolEvent(ResourcePool pool, Object resc, boolean checked_out_resource, int pool_size, int available_size, int removed_but_unreturned_size) {
/* 55 */     super(pool);
/* 56 */     this.resc = resc;
/* 57 */     this.checked_out_resource = checked_out_resource;
/* 58 */     this.pool_size = pool_size;
/* 59 */     this.available_size = available_size;
/* 60 */     this.removed_but_unreturned_size = removed_but_unreturned_size;
/*    */   }
/*    */   
/*    */   public Object getResource() {
/* 64 */     return this.resc;
/*    */   }
/*    */   public boolean isCheckedOutResource() {
/* 67 */     return this.checked_out_resource;
/*    */   }
/*    */   public int getPoolSize() {
/* 70 */     return this.pool_size;
/*    */   }
/*    */   public int getAvailableSize() {
/* 73 */     return this.available_size;
/*    */   }
/*    */   public int getRemovedButUnreturnedSize() {
/* 76 */     return this.removed_but_unreturned_size;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/resourcepool/ResourcePoolEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */