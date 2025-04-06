/*     */ package business.global.fight;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankArena;
/*     */ import business.global.arena.ArenaConfig;
/*     */ import business.global.arena.ArenaManager;
/*     */ import business.global.arena.Competitor;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import core.database.game.bo.ArenaFightRecordBO;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArenaFight
/*     */   extends Fight<Reward>
/*     */ {
/*     */   Competitor atk;
/*     */   Competitor def;
/*     */   Map<Integer, Fighter> deffender;
/*     */   Map<Integer, Fighter> attacker;
/*     */   
/*     */   public ArenaFight(Competitor atk, Competitor def) {
/*  35 */     this.atk = atk;
/*  36 */     this.def = def;
/*     */     
/*  38 */     Player atk0 = PlayerMgr.getInstance().getPlayer(atk.getPid());
/*  39 */     this.attacker = ((CharFeature)atk0.getFeature(CharFeature.class)).getFighters();
/*     */     
/*  41 */     Player def0 = PlayerMgr.getInstance().getPlayer(def.getPid());
/*  42 */     this.deffender = ((CharFeature)def0.getFeature(CharFeature.class)).getFighters();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeSettle(FightResult result) {}
/*     */ 
/*     */   
/*     */   protected void onCheckError() {
/*  51 */     ((PlayerRecord)PlayerMgr.getInstance().getPlayer(this.atk.getPid())
/*  52 */       .getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.ArenaChallenge, -1);
/*     */   }
/*     */   
/*     */   public long getAtkPid() {
/*  56 */     return this.atk.getPid();
/*     */   }
/*     */   
/*     */   protected Reward onLost() {
/*  60 */     Reward reward = new Reward();
/*  61 */     reward.add(PrizeType.ArenaToken, ArenaConfig.loserToken());
/*     */     
/*  63 */     save(FightResult.Lost);
/*  64 */     Player player = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
/*  65 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ArenaFight);
/*  66 */     return reward;
/*     */   }
/*     */   
/*     */   protected Reward onWin() {
/*  70 */     Reward reward = new Reward();
/*  71 */     if (this.atk.getRank() > this.def.getRank()) {
/*  72 */       Player playerAta = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
/*  73 */       Player playerDef = PlayerMgr.getInstance().getPlayer(this.def.getPid());
/*  74 */       if (this.def.getRank() == 1) {
/*  75 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.ArenaFirstChange, new String[] { playerAta.getName(), playerDef.getName() });
/*     */       }
/*  77 */       ArenaManager.getInstance().swithRank(this.atk.getRank(), this.def.getRank());
/*     */       
/*  79 */       ((RankArena)ActivityMgr.getActivity(RankArena.class)).UpdateMaxRequire_cost(playerAta, this.atk.getRank());
/*  80 */       ((RankArena)ActivityMgr.getActivity(RankArena.class)).UpdateMaxRequire_cost(playerDef, this.def.getRank());
/*     */     } 
/*  82 */     reward.add(PrizeType.ArenaToken, ArenaConfig.winnerToken());
/*  83 */     reward.add(PrizeType.Gold, ArenaConfig.winnerGold());
/*  84 */     save(FightResult.Win);
/*  85 */     Player player = PlayerMgr.getInstance().getPlayer(this.atk.getPid());
/*  86 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ArenaFight);
/*  87 */     return reward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getDeffenders() {
/*  92 */     return this.deffender;
/*     */   }
/*     */   
/*     */   private void save(FightResult result) {
/*  96 */     ArenaFightRecordBO bo = new ArenaFightRecordBO();
/*  97 */     bo.setAtkPid(this.atk.getPid());
/*  98 */     bo.setAtkRank(this.atk.getRank());
/*  99 */     bo.setDefPid(this.def.getPid());
/* 100 */     bo.setDefRank(this.def.getRank());
/* 101 */     bo.setBeginTime(this.beginTime);
/* 102 */     bo.setResult(result.ordinal());
/* 103 */     bo.setEndTime(CommTime.nowSecond());
/* 104 */     bo.insert();
/* 105 */     ArenaManager.getInstance().addFightRecord(bo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getAttackers() {
/* 110 */     return this.attacker;
/*     */   }
/*     */ 
/*     */   
/*     */   public int fightTime() {
/* 115 */     return ArenaConfig.fightTime();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/ArenaFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */