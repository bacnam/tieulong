/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.RankActivity;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.RobotManager;
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
/*    */ public class RankArena
/*    */   extends RankActivity
/*    */ {
/*    */   public RankArena(ActivityBO bo) {
/* 26 */     super(bo);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnd() {
/* 31 */     for (Record record : RankManager.getInstance().getRankList(RankType.Arena, RankManager.getInstance().getRankSize(RankType.Arena))) {
/* 32 */       if (record == null)
/*    */         continue; 
/* 34 */       if (RobotManager.getInstance().isRobot(record.getPid()))
/*    */         continue; 
/* 36 */       int rank = record.getRank();
/* 37 */       for (RefOpenServerRankReward ref : RefOpenServerRankReward.RankRewardByType.get(ConstEnum.RankRewardType.ArenaRank)) {
/* 38 */         if (!ref.RankRange.within(rank))
/*    */           continue; 
/* 40 */         MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[] { (new StringBuilder(String.valueOf(rank))).toString() });
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 48 */     return ActivityType.ArenaRank;
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
/* 63 */     int rank = ArenaManager.getInstance().getOrCreate(player.getPid()).getRank();
/* 64 */     bo.setExtInt(0, rank);
/* 65 */     bo.insert();
/* 66 */     return bo;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/RankArena.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */