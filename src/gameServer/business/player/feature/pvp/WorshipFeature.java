/*    */ package business.player.feature.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.Feature;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import core.database.game.bo.WorshipBO;
/*    */ 
/*    */ public class WorshipFeature extends Feature {
/*    */   private WorshipBO bo;
/*    */   
/*    */   public WorshipFeature(Player player) {
/* 12 */     super(player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadDB() {
/* 19 */     this.bo = (WorshipBO)BM.getBM(WorshipBO.class).findOne("pid", Long.valueOf(getPid()));
/*    */   }
/*    */   
/*    */   public WorshipBO getOrCreate() {
/* 23 */     WorshipBO bo = this.bo;
/* 24 */     if (bo != null) {
/* 25 */       return bo;
/*    */     }
/* 27 */     synchronized (this) {
/* 28 */       bo = this.bo;
/* 29 */       if (bo != null) {
/* 30 */         return bo;
/*    */       }
/* 32 */       bo = new WorshipBO();
/* 33 */       bo.setPid(this.player.getPid());
/* 34 */       bo.insert();
/* 35 */       this.bo = bo;
/*    */     } 
/* 37 */     return bo;
/*    */   }
/*    */   
/*    */   public int addTimes(int ranktype) {
/* 41 */     WorshipBO bo = getOrCreate();
/* 42 */     bo.saveWorshipTimes(ranktype, this.bo.getWorshipTimes(ranktype) + 1);
/* 43 */     return getTimes(ranktype);
/*    */   }
/*    */   
/*    */   public int getTimes(int ranktype) {
/* 47 */     WorshipBO bo = getOrCreate();
/* 48 */     return bo.getWorshipTimes(ranktype);
/*    */   }
/*    */   
/*    */   public void beWorshiped(int ranktype) {
/* 52 */     WorshipBO bo = getOrCreate();
/* 53 */     bo.saveBeWorshipTimes(ranktype, this.bo.getBeWorshipTimes(ranktype) + 1);
/*    */   }
/*    */   
/*    */   public WorshipBO getBO() {
/* 57 */     return getOrCreate();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/pvp/WorshipFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */