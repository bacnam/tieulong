/*    */ package core.network.proto;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ 
/*    */ public class StoreRefresh {
/*    */   StoreType storeType;
/*    */   int nextRefreshTime;
/*    */   int remainsec;
/*    */   int freeRefreshTimes;
/*    */   int paidRefreshTimes;
/*    */   
/*    */   public StoreType getStoreType() {
/* 13 */     return this.storeType;
/*    */   }
/*    */   
/*    */   public void setStoreType(StoreType storeType) {
/* 17 */     this.storeType = storeType;
/*    */   }
/*    */   
/*    */   public int getNextRefreshTime() {
/* 21 */     return this.nextRefreshTime;
/*    */   }
/*    */   
/*    */   public void setNextRefreshTime(int nextRefreshTime) {
/* 25 */     this.nextRefreshTime = nextRefreshTime;
/*    */   }
/*    */   
/*    */   public int getRemainsec() {
/* 29 */     return this.remainsec;
/*    */   }
/*    */   
/*    */   public void setRemainsec(int remainsec) {
/* 33 */     this.remainsec = remainsec;
/*    */   }
/*    */   
/*    */   public int getFreeRefreshTimes() {
/* 37 */     return this.freeRefreshTimes;
/*    */   }
/*    */   
/*    */   public void setFreeRefreshTimes(int freeRefreshTimes) {
/* 41 */     this.freeRefreshTimes = freeRefreshTimes;
/*    */   }
/*    */   
/*    */   public int getPaidRefreshTimes() {
/* 45 */     return this.paidRefreshTimes;
/*    */   }
/*    */   
/*    */   public void setPaidRefreshTimes(int paidRefreshTimes) {
/* 49 */     this.paidRefreshTimes = paidRefreshTimes;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/StoreRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */