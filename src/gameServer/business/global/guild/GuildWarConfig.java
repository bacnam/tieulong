/*    */ package business.global.guild;
/*    */ 
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildWarCenter;
/*    */ import core.database.game.bo.PlayerBO;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildWarConfig
/*    */ {
/*    */   public static int overTime() {
/* 17 */     return RefDataMgr.getFactor("GuildWar_OverTime", 60);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int fightTime() {
/* 24 */     return RefDataMgr.getFactor("GuildWar_FightTime", 1800);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int restTime() {
/* 31 */     return RefDataMgr.getFactor("GuildWar_RestTime", 600);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int oneFightTime() {
/* 38 */     return RefDataMgr.getFactor("GuildWar_OneFight", 30000);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int winRewardMailId(int centerId) {
/* 45 */     RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
/* 46 */     return ref.MailId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int rebirthCD() {
/* 53 */     return RefDataMgr.getFactor("GuildWar_rebirthCD", 3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean applyTime() {
/* 60 */     int begin = RefDataMgr.getFactor("GuildWar_ApplyBegin", 12);
/* 61 */     int end = RefDataMgr.getFactor("GuildWar_ApplyEnd", 13);
/* 62 */     NumberRange range = new NumberRange(begin, end);
/* 63 */     return range.within(CommTime.getTodayHour());
/*    */   }
/*    */   
/*    */   public static class puppetPlayer extends Player {
/*    */     int puppet_id;
/*    */     boolean is_puppet;
/*    */     
/*    */     public puppetPlayer(PlayerBO playerBO) {
/* 71 */       super(playerBO);
/*    */     }
/*    */     
/*    */     public int getPuppet_id() {
/* 75 */       return this.puppet_id;
/*    */     }
/*    */     
/*    */     public void setPuppet_id(int puppet_id) {
/* 79 */       this.puppet_id = puppet_id;
/*    */     }
/*    */     
/*    */     public boolean isIs_puppet() {
/* 83 */       return this.is_puppet;
/*    */     }
/*    */     
/*    */     public void setIs_puppet(boolean is_puppet) {
/* 87 */       this.is_puppet = is_puppet;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildWarConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */