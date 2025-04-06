/*     */ package business.player.feature.features;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.AccumRechargeDay;
/*     */ import business.global.activity.detail.ActivityInstance;
/*     */ import business.global.activity.detail.DailyRecharge;
/*     */ import business.global.activity.detail.DailySign;
/*     */ import business.global.activity.detail.FortuneWheel;
/*     */ import business.global.activity.detail.NewFirstReward;
/*     */ import business.global.activity.detail.SignInOpenServer;
/*     */ import business.global.activity.detail.SignInPrize;
/*     */ import business.global.guild.Guild;
/*     */ import business.player.Player;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import business.player.feature.player.TitleFeature;
/*     */ import business.player.feature.pve.InstanceFeature;
/*     */ import business.player.feature.pvp.DroiyanFeature;
/*     */ import business.player.feature.pvp.StealGoldFeature;
/*     */ import business.player.feature.pvp.WorshipFeature;
/*     */ import business.player.feature.store.StoreFeature;
/*     */ import business.player.feature.store.StoreRecord;
/*     */ import business.player.feature.task.TaskActivityFeature;
/*     */ import business.player.feature.treasure.FindTreasureFeature;
/*     */ import business.player.feature.worldboss.WorldBossFeature;
/*     */ import com.zhonglian.server.common.mgr.daily.BaseDailyRefreshEvent;
/*     */ import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerDailyRefresh
/*     */   extends BaseDailyRefreshEvent
/*     */ {
/*     */   public static abstract class IPlayerDailyRefresh
/*     */   {
/*     */     public abstract void doEvent(Player param1Player, IDailyRefreshRef param1IDailyRefreshRef, int param1Int);
/*     */   }
/*     */   
/*     */   public PlayerDailyRefresh(IDailyRefreshRef ref, DailyRefreshFeature container) {
/*  50 */     super(ref, container);
/*     */   }
/*     */ 
/*     */   
/*     */   public void doEvent(int times) {
/*  55 */     DailyRefreshFeature containerP = (DailyRefreshFeature)this.dailyRefresh;
/*  56 */     Player player = containerP.getPlayer();
/*  57 */     player.lockIns();
/*     */     try {
/*  59 */       doEvent(player, times);
/*  60 */     } catch (Exception e) {
/*  61 */       CommLog.error("玩家{}执行刷新发生异常", Long.valueOf(player.getPid()), e);
/*     */     } 
/*  63 */     player.unlockIns();
/*     */   } private void doEvent(Player player, int times) {
/*     */     GuildMemberFeature guildMember;
/*     */     Guild guild;
/*  67 */     switch (this.ref.getEventTypes()) {
/*     */       
/*     */       case null:
/*  70 */         ((StoreRecord)player.getFeature(StoreRecord.class)).dailyRefresh();
/*     */         
/*  72 */         ((PlayerRecord)player.getFeature(PlayerRecord.class)).clearValue0();
/*     */         
/*  74 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).dailyRefresh();
/*     */         
/*  76 */         ((TitleFeature)player.getFeature(TitleFeature.class)).dailyRefresh();
/*     */         
/*  78 */         ((TaskActivityFeature)player.getFeature(TaskActivityFeature.class)).dailyRefresh();
/*     */         
/*  80 */         ((DailySign)ActivityMgr.getActivity(DailySign.class)).handDailyRefresh(player, times);
/*     */         
/*  82 */         ((SignInOpenServer)ActivityMgr.getActivity(SignInOpenServer.class)).handDailyRefresh(player);
/*     */         
/*  84 */         ((SignInPrize)ActivityMgr.getActivity(SignInPrize.class)).handDailyRefresh(player);
/*     */         
/*  86 */         ((ActivityInstance)ActivityMgr.getActivity(ActivityInstance.class)).handDailyRefresh(player);
/*     */         
/*  88 */         ((RechargeFeature)player.getFeature(RechargeFeature.class)).desRebateRemains(times);
/*     */         
/*  90 */         ((WorshipFeature)player.getFeature(WorshipFeature.class)).getBO().saveWorshipTimesAll(0);
/*     */         
/*  92 */         ((DailyRecharge)ActivityMgr.getActivity(DailyRecharge.class)).dailyRechargeRefresh(player);
/*     */         
/*  94 */         ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getBo().savePoint(0);
/*     */         
/*  96 */         ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getBo().saveWinTimes(0);
/*     */         
/*  98 */         ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dailyRefresh();
/*     */         
/* 100 */         ((InstanceFeature)player.getFeature(InstanceFeature.class)).dailyRefresh();
/*     */         
/* 102 */         ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).dailyEvent();
/*     */         
/* 104 */         ((StealGoldFeature)player.getFeature(StealGoldFeature.class)).dailyRefresh();
/*     */ 
/*     */ 
/*     */         
/* 108 */         ((NewFirstReward)ActivityMgr.getActivity(NewFirstReward.class)).dailyRefesh(player, times);
/*     */         
/* 110 */         ((FindTreasureFeature)player.getFeature(FindTreasureFeature.class)).getOrCreate().saveTimes(0);
/* 111 */         ((FindTreasureFeature)player.getFeature(FindTreasureFeature.class)).getOrCreate().saveTentimes(0);
/*     */         
/* 113 */         ((MarryFeature)player.getFeature(MarryFeature.class)).dailyRefresh();
/*     */         
/* 115 */         ((TitleFeature)player.getFeature(TitleFeature.class)).checkAllTitle();
/*     */         
/* 117 */         ((AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class)).dailyRefresh(player);
/*     */         
/* 119 */         ((FortuneWheel)ActivityMgr.getActivity(FortuneWheel.class)).dailyRefresh(player);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case Every_h2:
/* 147 */         guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 148 */         guild = guildMember.getGuild();
/* 149 */         if (guild != null && guild.isInOpenHour()) {
/* 150 */           guildMember.refreshChallengeTimes();
/*     */         }
/*     */         break;
/*     */       case Every_h3:
/* 154 */         ((StoreFeature)player.getFeature(StoreFeature.class)).doAutoRefresh();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/PlayerDailyRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */