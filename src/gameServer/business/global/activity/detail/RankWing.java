/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.Character;
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
/*    */ public class RankWing
/*    */   extends RankActivity
/*    */ {
/*    */   public RankWing(ActivityBO bo) {
/* 25 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 30 */     for (Record record : RankManager.getInstance().getRankList(RankType.WingLevel, RankManager.getInstance().getRankSize(RankType.WingLevel))) {
/* 31 */       if (record == null)
/*    */         continue; 
/* 33 */       int rank = record.getRank();
/* 34 */       for (RankActivity.RankAward ref : this.rankrewardList) {
/* 35 */         if (!ref.rankrange.within(rank))
/*    */           continue; 
/* 37 */         MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 44 */     return ActivityType.WingRank;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConstEnum.VIPGiftType getAwardType() {
/* 49 */     return ConstEnum.VIPGiftType.WingRank;
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
/* 64 */     int wingLevel = 0;
/* 65 */     for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
/* 66 */       wingLevel += charac.getBo().getWing();
/*    */     }
/* 68 */     bo.setExtInt(0, wingLevel);
/* 69 */     bo.insert();
/* 70 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankWing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */