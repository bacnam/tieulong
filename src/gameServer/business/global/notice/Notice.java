/*    */ package business.global.notice;
/*    */ 
/*    */ public class Notice
/*    */ {
/*    */   public long id;
/*    */   public String content;
/*    */   public int clientType;
/*    */   public int beginTime;
/*    */   public int endTime;
/*    */   public int interval;
/*    */   public int lastSendTime;
/*    */   
/*    */   public long getId() {
/* 14 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(long id) {
/* 18 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 22 */     return this.content;
/*    */   }
/*    */   
/*    */   public void setContent(String content) {
/* 26 */     this.content = content;
/*    */   }
/*    */   
/*    */   public int getClientType() {
/* 30 */     return this.clientType;
/*    */   }
/*    */   
/*    */   public void setClientType(int clientType) {
/* 34 */     this.clientType = clientType;
/*    */   }
/*    */   
/*    */   public int getBeginTime() {
/* 38 */     return this.beginTime;
/*    */   }
/*    */   
/*    */   public void setBeginTime(int beginTime) {
/* 42 */     this.beginTime = beginTime;
/*    */   }
/*    */   
/*    */   public int getEndTime() {
/* 46 */     return this.endTime;
/*    */   }
/*    */   
/*    */   public void setEndTime(int endTime) {
/* 50 */     this.endTime = endTime;
/*    */   }
/*    */   
/*    */   public int getInterval() {
/* 54 */     return this.interval;
/*    */   }
/*    */   
/*    */   public void setInterval(int interval) {
/* 58 */     this.interval = interval;
/*    */   }
/*    */   
/*    */   public int getLastSendTime() {
/* 62 */     return this.lastSendTime;
/*    */   }
/*    */   
/*    */   public void setLastSendTime(int lastSendTime) {
/* 66 */     this.lastSendTime = lastSendTime;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/notice/Notice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */