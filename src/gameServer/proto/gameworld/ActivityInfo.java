/*    */ package proto.gameworld;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActivityInfo
/*    */ {
/*    */   public ActivityType type;
/*    */   public ActivityStatus status;
/*    */   public int beginTime;
/*    */   public int endTime;
/*    */   public int closeTime;
/*    */   
/*    */   public ActivityType getType() {
/* 17 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(ActivityType type) {
/* 21 */     this.type = type;
/*    */   }
/*    */   
/*    */   public ActivityStatus getStatus() {
/* 25 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(ActivityStatus status) {
/* 29 */     this.status = status;
/*    */   }
/*    */   
/*    */   public int getBeginTime() {
/* 33 */     return this.beginTime;
/*    */   }
/*    */   
/*    */   public void setBeginTime(int beginTime) {
/* 37 */     this.beginTime = beginTime;
/*    */   }
/*    */   
/*    */   public int getEndTime() {
/* 41 */     return this.endTime;
/*    */   }
/*    */   
/*    */   public void setEndTime(int endTime) {
/* 45 */     this.endTime = endTime;
/*    */   }
/*    */   
/*    */   public int getCloseTime() {
/* 49 */     return this.closeTime;
/*    */   }
/*    */   
/*    */   public void setCloseTime(int closeTime) {
/* 53 */     this.closeTime = closeTime;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/gameworld/ActivityInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */