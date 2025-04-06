/*    */ package business.global.guild.rank.catagere;
/*    */ 
/*    */ import business.global.guild.GuildRank;
/*    */ import business.global.guild.GuildRanks;
/*    */ import business.player.RobotManager;
/*    */ import com.zhonglian.server.common.enums.GuildRankType;
/*    */ import core.database.game.bo.GuildRankRecordBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GuildRanks({GuildRankType.GuildBoss})
/*    */ public class GuildNormalRank
/*    */   extends GuildRank
/*    */ {
/*    */   public GuildNormalRank(GuildRankType type) {
/* 17 */     super(type, (left, right) -> (left.getValue() != right.getValue()) ? ((right.getValue() > left.getValue()) ? 1 : -1) : ((left.getExt1() != right.getExt1()) ? ((right.getExt1() > left.getExt1()) ? 1 : -1) : (left.getUpdateTime() - right.getUpdateTime())));
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
/* 30 */     return RobotManager.getInstance().isRobot(ownerid);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/rank/catagere/GuildNormalRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */