/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.database.game.bo.ActivityRecordBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankLevel
/*    */   extends RankActivity
/*    */ {
/*    */   public RankLevel(ActivityBO bo) {
/* 23 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 28 */     for (Record record : RankManager.getInstance().getRankList(RankType.Level, RankManager.getInstance().getRankSize(RankType.Level))) {
/* 29 */       if (record == null)
/*    */         continue; 
/* 31 */       int rank = record.getRank();
/* 32 */       for (RankActivity.RankAward ref : this.rankrewardList) {
/* 33 */         if (!ref.rankrange.within(rank))
/*    */           continue; 
/* 35 */         MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 43 */     return ActivityType.LevelRank;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConstEnum.VIPGiftType getAwardType() {
/* 48 */     return ConstEnum.VIPGiftType.LevelRank;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 59 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 60 */     bo.setPid(player.getPid());
/* 61 */     bo.setAid(this.bo.getId());
/* 62 */     bo.setActivity(getType().toString());
/* 63 */     int rank = player.getLv();
/* 64 */     bo.setExtInt(0, rank);
/* 65 */     bo.insert();
/* 66 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */