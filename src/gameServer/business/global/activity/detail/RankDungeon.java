/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.feature.pve.DungeonFeature;
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
/*    */ public class RankDungeon
/*    */   extends RankActivity
/*    */ {
/*    */   public RankDungeon(ActivityBO bo) {
/* 24 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 29 */     for (Record record : RankManager.getInstance().getRankList(RankType.Dungeon, RankManager.getInstance().getRankSize(RankType.Dungeon))) {
/* 30 */       if (record == null)
/*    */         continue; 
/* 32 */       int rank = record.getRank();
/* 33 */       for (RankActivity.RankAward ref : this.rankrewardList) {
/* 34 */         if (!ref.rankrange.within(rank))
/*    */           continue; 
/* 36 */         MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 44 */     return ActivityType.DungeonRank;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConstEnum.VIPGiftType getAwardType() {
/* 49 */     return ConstEnum.VIPGiftType.DungeonRank;
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
/* 60 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 61 */     bo.setPid(player.getPid());
/* 62 */     bo.setAid(this.bo.getId());
/* 63 */     bo.setActivity(getType().toString());
/* 64 */     int rank = ((DungeonFeature)player.getFeature(DungeonFeature.class)).getLevel();
/* 65 */     bo.setExtInt(0, rank);
/* 66 */     bo.insert();
/* 67 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankDungeon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */