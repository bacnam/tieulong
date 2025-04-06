/*    */ package business.global.refresh;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.AllPeopleReward;
/*    */ import business.global.activity.detail.Lottery;
/*    */ import business.global.activity.detail.SignInSeven;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.common.mgr.daily.BaseDailyRefreshEvent;
/*    */ import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWorldBoss;
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
/*    */ public class WorldDailyRefresh
/*    */   extends BaseDailyRefreshEvent
/*    */ {
/*    */   public WorldDailyRefresh(IDailyRefreshRef ref, WorldDailyRefreshContainer container) {
/* 32 */     super(ref, container);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doEvent(int times) {
/* 37 */     switch (this.ref.getEventTypes()) {
/*    */       
/*    */       case null:
/* 40 */         ArenaManager.getInstance().settle();
/*    */         
/* 42 */         RankManager.getInstance().settle(RankType.Droiyan, true);
/*    */         
/* 44 */         GuildMgr.getInstance().dailyEvent();
/*    */         
/* 46 */         ((Lottery)ActivityMgr.getActivity(Lottery.class)).dailyRefresh();
/*    */         
/* 48 */         GuildWarMgr.getInstance().dailyRefresh();
/*    */         
/* 50 */         ((SignInSeven)ActivityMgr.getActivity(SignInSeven.class)).handDailyRefresh();
/*    */         break;
/*    */ 
/*    */       
/*    */       case Day_h4:
/* 55 */         for (RefWorldBoss ref : RefDataMgr.getAll(RefWorldBoss.class).values()) {
/* 56 */           WorldBossMgr.getInstance().dailyRefreshWorldBoss(ref.id);
/*    */         }
/*    */         break;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       case Day_h11:
/* 65 */         GuildMgr.getInstance().longnvEvent();
/*    */         break;
/*    */       case Day_h12:
/* 68 */         WorldBossMgr.getInstance().autoFight(1);
/*    */         break;
/*    */       case Day_h13:
/* 71 */         WorldBossMgr.getInstance().sendRankReward(1, false);
/*    */         break;
/*    */       case Day_h15:
/* 74 */         WorldBossMgr.getInstance().autoFight(2);
/*    */         break;
/*    */       case Day_h16:
/* 77 */         WorldBossMgr.getInstance().sendRankReward(2, false);
/*    */         break;
/*    */       case Day_h18:
/* 80 */         WorldBossMgr.getInstance().autoFight(3);
/*    */         break;
/*    */       case Day_h19:
/* 83 */         WorldBossMgr.getInstance().sendRankReward(3, false);
/* 84 */         GuildWarMgr.getInstance().guildWarEvent();
/*    */         break;
/*    */ 
/*    */       
/*    */       case Day_h21:
/* 89 */         WorldBossMgr.getInstance().autoFight(4);
/*    */         break;
/*    */       case Day_h22:
/* 92 */         WorldBossMgr.getInstance().sendRankReward(4, false);
/*    */         break;
/*    */       case Week_d1h0:
/* 95 */         ((AllPeopleReward)ActivityMgr.getActivity(AllPeopleReward.class)).weekEvent();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/refresh/WorldDailyRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */