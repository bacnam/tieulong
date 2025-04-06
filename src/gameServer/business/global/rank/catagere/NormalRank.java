/*    */ package business.global.rank.catagere;
/*    */ 
/*    */ import business.global.rank.Rank;
/*    */ import business.global.rank.Ranks;
/*    */ import business.player.RobotManager;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import core.database.game.bo.RankRecordBO;
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
/*    */ @Ranks({RankType.Droiyan, RankType.Level, RankType.WingLevel, RankType.Dungeon, RankType.Power, RankType.TianLongPower, RankType.GuMuPower, RankType.XiaoYaoPower, RankType.DrawPoint, RankType.Lovers, RankType.Artifice})
/*    */ public class NormalRank
/*    */   extends Rank
/*    */ {
/*    */   public NormalRank(RankType type) {
/* 27 */     super(type, (left, right) -> (left.getValue() != right.getValue()) ? ((right.getValue() > left.getValue()) ? 1 : -1) : ((left.getExt1() != right.getExt1()) ? ((right.getExt1() > left.getExt1()) ? 1 : -1) : (left.getUpdateTime() - right.getUpdateTime())));
/*    */   }
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
/*    */   protected boolean filter(long ownerid) {
/* 40 */     return RobotManager.getInstance().isRobot(ownerid);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/catagere/NormalRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */