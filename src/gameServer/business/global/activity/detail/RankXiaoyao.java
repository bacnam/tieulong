/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import core.config.refdata.ref.RefOpenServerRankReward;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.database.game.bo.ActivityRecordBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RankXiaoyao
/*    */   extends RankActivity
/*    */ {
/*    */   public RankXiaoyao(ActivityBO bo) {
/* 25 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 30 */     for (Record record : RankManager.getInstance().getRankList(RankType.XiaoYaoPower, RankManager.getInstance().getRankSize(RankType.XiaoYaoPower))) {
/* 31 */       if (record == null)
/*    */         continue; 
/* 33 */       int rank = record.getRank();
/* 34 */       for (RefOpenServerRankReward ref : RefOpenServerRankReward.RankRewardByType.get(ConstEnum.RankRewardType.XiaoyaoRank)) {
/* 35 */         if (!ref.RankRange.within(rank))
/*    */           continue; 
/* 37 */         MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[] { (new StringBuilder(String.valueOf(rank))).toString() });
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 45 */     return ActivityType.XiaoyaoRank;
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
/* 56 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 57 */     bo.setPid(player.getPid());
/* 58 */     bo.setAid(this.bo.getId());
/* 59 */     bo.setActivity(getType().toString());
/* 60 */     int rank = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getBo().getPoint();
/* 61 */     bo.setExtInt(0, rank);
/* 62 */     bo.insert();
/* 63 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankXiaoyao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */