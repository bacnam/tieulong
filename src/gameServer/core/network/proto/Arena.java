/*    */ package core.network.proto;
/*    */ 
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.features.RechargeFeature;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import core.database.game.bo.PlayerBO;
/*    */ 
/*    */ public class Arena
/*    */ {
/*    */   public static class CompetitorInfo
/*    */     extends Player.Summary
/*    */   {
/*    */     static PlayerMgr playerMgr;
/*    */     int rank;
/*    */     
/*    */     public CompetitorInfo(Competitor competitor) {
/* 20 */       if (playerMgr == null) {
/* 21 */         playerMgr = PlayerMgr.getInstance();
/*    */       }
/*    */       
/* 24 */       Player player = playerMgr.getPlayer(competitor.getPid());
/* 25 */       PlayerBO bo = player.getPlayerBO();
/* 26 */       this.pid = bo.getId();
/* 27 */       this.name = bo.getName();
/* 28 */       this.lv = bo.getLv();
/* 29 */       this.icon = bo.getIcon();
/* 30 */       this.vipLv = bo.getVipLevel();
/* 31 */       this.power = ((CharFeature)player.getFeature(CharFeature.class)).getPower();
/* 32 */       this.rank = competitor.getBo().getRank();
/*    */ 
/*    */       
/* 35 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/* 36 */       int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
/* 37 */       this.MonthCard = (monthNum > 0);
/* 38 */       int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
/* 39 */       this.YearCard = (yearNum == -1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Arena.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */