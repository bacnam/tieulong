/*    */ package business.global.guild;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.database.game.bo.GuildRankRecordBO;
/*    */ 
/*    */ public class GuildRecord
/*    */ {
/*    */   int rank;
/*    */   GuildRankRecordBO recordBO;
/*    */   
/*    */   public GuildRecord(GuildRankRecordBO recordBO) {
/* 12 */     this.recordBO = recordBO;
/* 13 */     this.rank = 0;
/*    */   }
/*    */   
/*    */   public long saveValue(long value) {
/* 17 */     this.recordBO.saveValue(value);
/* 18 */     this.recordBO.saveUpdateTime(CommTime.nowSecond());
/* 19 */     return this.recordBO.getValue();
/*    */   }
/*    */   
/*    */   public GuildRankRecordBO copy() {
/* 23 */     GuildRankRecordBO recordBO = new GuildRankRecordBO();
/* 24 */     recordBO.setType(recordBO.getType());
/* 25 */     recordBO.setOwner(recordBO.getOwner());
/* 26 */     recordBO.setValue(recordBO.getValue());
/* 27 */     recordBO.setExt1(recordBO.getExt1());
/* 28 */     recordBO.setExt2(recordBO.getExt2());
/* 29 */     recordBO.setExt3(recordBO.getExt3());
/* 30 */     recordBO.setExt4(recordBO.getExt4());
/* 31 */     recordBO.setExt5(recordBO.getExt5());
/* 32 */     recordBO.setUpdateTime(CommTime.nowSecond());
/* 33 */     return recordBO;
/*    */   }
/*    */   
/*    */   public long getPid() {
/* 37 */     return this.recordBO.getOwner();
/*    */   }
/*    */   
/*    */   public int getRank() {
/* 41 */     return this.rank;
/*    */   }
/*    */   
/*    */   public long getValue() {
/* 45 */     return this.recordBO.getValue();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */