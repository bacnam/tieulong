/*    */ package business.global.rank.catagere;
/*    */ 
/*    */ import business.global.rank.Ranks;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Ranks({RankType.Guild})
/*    */ public class GuildRank
/*    */   extends NormalRank
/*    */ {
/*    */   public GuildRank(RankType type) {
/* 13 */     super(type);
/*    */   }
/*    */   
/*    */   protected boolean filter(long ownerid) {
/* 17 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/rank/catagere/GuildRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */