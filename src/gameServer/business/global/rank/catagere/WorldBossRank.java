/*    */ package business.global.rank.catagere;
/*    */ 
/*    */ import business.global.rank.Ranks;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Ranks({RankType.WorldBoss1, RankType.WorldBoss2, RankType.WorldBoss3, RankType.WorldBoss4})
/*    */ public class WorldBossRank
/*    */   extends NormalRank
/*    */ {
/*    */   public WorldBossRank(RankType type) {
/* 14 */     super(type);
/*    */   }
/*    */   
/*    */   protected boolean filter(long ownerid) {
/* 18 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/catagere/WorldBossRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */