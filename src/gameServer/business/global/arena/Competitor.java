/*    */ package business.global.arena;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.database.game.bo.ArenaCompetitorBO;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class Competitor
/*    */ {
/*    */   ArenaCompetitorBO bo;
/* 11 */   List<Competitor> opponents = null;
/* 12 */   int refreshTime = 0;
/* 13 */   int lastFightTime = 0;
/*    */   
/*    */   public Competitor(ArenaCompetitorBO bo) {
/* 16 */     this.bo = bo;
/*    */   }
/*    */   
/*    */   public ArenaCompetitorBO getBo() {
/* 20 */     return this.bo;
/*    */   }
/*    */   
/*    */   public List<Competitor> getOpponents() {
/* 24 */     if (this.opponents == null) {
/* 25 */       this.opponents = ArenaManager.getInstance().getOpponents(this.bo.getRank());
/*    */     }
/* 27 */     return this.opponents;
/*    */   }
/*    */   
/*    */   public int getRefreshCD() {
/* 31 */     return Math.max(this.refreshTime + ArenaConfig.refreshCD() - CommTime.nowSecond(), 0);
/*    */   }
/*    */   
/*    */   public long getPid() {
/* 35 */     return this.bo.getPid();
/*    */   }
/*    */   
/*    */   public Competitor getOpponent(long targetPid) {
/* 39 */     if (this.opponents == null) {
/* 40 */       return null;
/*    */     }
/* 42 */     for (Competitor opp : this.opponents) {
/* 43 */       if (opp.getPid() == targetPid) {
/* 44 */         return opp;
/*    */       }
/*    */     } 
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public int getFightCD() {
/* 51 */     return Math.max(this.lastFightTime + ArenaConfig.fightCD() - CommTime.nowSecond(), 0);
/*    */   }
/*    */   
/*    */   public int getRank() {
/* 55 */     return this.bo.getRank();
/*    */   }
/*    */   
/*    */   public void resetOpponents() {
/* 59 */     this.opponents = null;
/*    */   }
/*    */   
/*    */   public void setFightCD(int cd) {
/* 63 */     this.lastFightTime = CommTime.nowSecond() - ArenaConfig.fightCD() + cd;
/*    */   }
/*    */   
/*    */   public void setRefreshCD(int cd) {
/* 67 */     this.refreshTime = CommTime.nowSecond() - ArenaConfig.refreshCD() + cd;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/arena/Competitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */