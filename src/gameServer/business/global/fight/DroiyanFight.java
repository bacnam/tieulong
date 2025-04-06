/*     */ package business.global.fight;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.RankDroiyan;
/*     */ import business.global.arena.ArenaConfig;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.pvp.EncouterManager;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.pvp.DroiyanFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDroiyanTreasure;
/*     */ import core.database.game.bo.DroiyanBO;
/*     */ import core.database.game.bo.DroiyanRecordBO;
/*     */ import core.database.game.bo.DroiyanTreasureBO;
/*     */ import core.network.client2game.handler.pvp.DroiyanTreasures;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DroiyanFight
/*     */   extends Fight<DroiyanRecordBO>
/*     */ {
/*     */   DroiyanFeature atk;
/*     */   DroiyanFeature def;
/*     */   Map<Integer, Fighter> deffender;
/*     */   Map<Integer, Fighter> attacker;
/*     */   boolean isRevenge = false;
/*     */   
/*     */   public DroiyanFight(DroiyanFeature atk, DroiyanFeature def, boolean revenge) {
/*  43 */     this.atk = atk;
/*  44 */     this.def = def;
/*  45 */     this.isRevenge = revenge;
/*     */     
/*  47 */     this.deffender = ((CharFeature)def.getPlayer().getFeature(CharFeature.class)).getFighters();
/*  48 */     this.attacker = ((CharFeature)atk.getPlayer().getFeature(CharFeature.class)).getFighters();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onCheckError() {
/*  53 */     if (this.isRevenge) {
/*  54 */       this.atk.addEnemy(this.def.getPid());
/*     */     } else {
/*  56 */       this.atk.addDroiyan(this.def.getPid());
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getAtkPid() {
/*  61 */     return this.atk.getPid();
/*     */   }
/*     */   
/*     */   public int getAtkRed() {
/*  65 */     return this.atk.getBo().getRed();
/*     */   }
/*     */ 
/*     */   
/*     */   protected DroiyanRecordBO onLost() {
/*  70 */     this.def.damage();
/*  71 */     this.atk.reborn();
/*     */     
/*  73 */     int time = CommTime.nowSecond();
/*  74 */     DroiyanRecordBO recordBO = new DroiyanRecordBO();
/*  75 */     recordBO.setPid(this.atk.getPid());
/*  76 */     recordBO.setTarget(this.def.getPid());
/*  77 */     recordBO.setResult(FightResult.Lost.ordinal());
/*  78 */     recordBO.setTargetName(this.def.getPlayerName());
/*  79 */     recordBO.setTime(time);
/*     */     
/*  81 */     DroiyanBO loserbo = this.atk.getBo();
/*  82 */     int red = loserbo.getRed() - RefDataMgr.getFactor("Droiyan_LostRed", 20);
/*  83 */     loserbo.setRed(Math.max(red, 0));
/*     */     
/*  85 */     loserbo.setWinTimes(0);
/*     */     
/*  87 */     if (this.isRevenge) {
/*  88 */       recordBO.setRevenged(true);
/*  89 */       this.def.setRevenged(this.atk.getPid());
/*     */     } else {
/*     */       
/*  92 */       recordBO.setRevenged(true);
/*     */     } 
/*  94 */     loserbo.saveAll();
/*  95 */     recordBO.insert();
/*  96 */     this.atk.addRecored(recordBO);
/*  97 */     return recordBO;
/*     */   }
/*     */   
/*     */   protected DroiyanRecordBO onWin() {
/* 101 */     Player winner = this.atk.getPlayer();
/* 102 */     DroiyanBO winnerbo = this.atk.getBo();
/*     */     
/* 104 */     this.atk.damage();
/* 105 */     this.def.reborn();
/*     */     
/* 107 */     int time = CommTime.nowSecond();
/* 108 */     DroiyanRecordBO recordBO = new DroiyanRecordBO();
/* 109 */     recordBO.setPid(this.atk.getPid());
/* 110 */     recordBO.setTarget(this.def.getPid());
/* 111 */     recordBO.setResult(FightResult.Win.ordinal());
/* 112 */     recordBO.setTargetName(this.def.getPlayerName());
/* 113 */     recordBO.setTime(time);
/*     */     
/* 115 */     int red = winnerbo.getRed() + RefDataMgr.getFactor("Droiyan_WinRed", 15);
/* 116 */     red = Math.min(red, RefDataMgr.getFactor("Droiyan_MaxRed", 100));
/* 117 */     winnerbo.setRed(red);
/* 118 */     if (!this.isRevenge) {
/*     */       
/* 120 */       int dlevel = this.def.getPlayer().getLv() - winner.getLv();
/* 121 */       int point = RefDataMgr.getFactor("Droiyan_WinRed", 15) + Math.max(dlevel, 0) + 2 * winnerbo.getWinTimes();
/* 122 */       winnerbo.setPoint(winnerbo.getPoint() + point);
/* 123 */       recordBO.setPoint(point);
/* 124 */       RankManager.getInstance().update(RankType.Droiyan, winnerbo.getPid(), winnerbo.getPoint());
/*     */ 
/*     */       
/* 127 */       ((RankDroiyan)ActivityMgr.getActivity(RankDroiyan.class)).UpdateMaxRequire_cost(winner, winnerbo.getPoint());
/*     */ 
/*     */       
/* 130 */       winnerbo.setWinTimes(winnerbo.getWinTimes() + 1);
/*     */     } 
/*     */     
/* 133 */     DroiyanTreasureBO robTreasure = null;
/* 134 */     int robRate = 0;
/*     */     
/* 136 */     if (this.def.isRed()) {
/* 137 */       robRate = RefDataMgr.getFactor("Droiyan_RedRobRate", 8000);
/*     */     }
/*     */     
/* 140 */     if (this.isRevenge) {
/* 141 */       robRate = RefDataMgr.getFactor("Droiyan_RevengeRobRate", 10000);
/*     */     }
/*     */     else {
/*     */       
/* 145 */       robRate = RefDataMgr.getFactor("Droiyan_RobRate", 3000);
/*     */     } 
/*     */     
/* 148 */     if (Random.nextInt(10000) < robRate) {
/* 149 */       if (this.isRevenge) {
/* 150 */         robTreasure = this.def.beRevengeRobbed();
/* 151 */       } else if (!this.isRevenge) {
/* 152 */         robTreasure = this.def.beRobbed();
/* 153 */       }  if (robTreasure != null)
/*     */       {
/* 155 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.RobTreasure, new String[] { this.atk.getPlayerName() });
/*     */       }
/*     */     } 
/* 158 */     if (robTreasure != null && !this.atk.isTreasureFull()) {
/* 159 */       robTreasure.setPid(this.atk.getPid());
/* 160 */       robTreasure.setGainTime(time);
/* 161 */       RefDroiyanTreasure ref = (RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(robTreasure.getTreasureId()));
/* 162 */       robTreasure.setExpireTime(ref.Time + time);
/* 163 */       this.atk.addTreasure(robTreasure);
/* 164 */       recordBO.setRob(robTreasure.getTreasureId());
/* 165 */       EncouterManager.getInstance().addNews(EncouterManager.NewsType.Drop, this.atk.getPlayerName(), robTreasure.getTreasureId());
/* 166 */       robTreasure.saveAll();
/* 167 */       pushTreasure(robTreasure);
/*     */     } 
/*     */ 
/*     */     
/* 171 */     RefDroiyanTreasure refTreasure = RefDroiyanTreasure.findTreature(Random.nextInt(10000));
/* 172 */     if (refTreasure != null && !this.atk.isTreasureFull()) {
/* 173 */       recordBO.setTreasure(refTreasure.id);
/* 174 */       DroiyanTreasureBO treasure = this.atk.addTreasure(refTreasure);
/* 175 */       EncouterManager.getInstance().addNews(EncouterManager.NewsType.Drop, this.atk.getPlayerName(), refTreasure.id);
/* 176 */       pushTreasure(treasure);
/*     */       
/* 178 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DropTreasure, new String[] { this.atk.getPlayerName() });
/*     */     } 
/*     */     
/* 181 */     Reward reward = new Reward();
/* 182 */     recordBO.setGold(RefDataMgr.getFactor("Droiyan_WinnerGold", 10000));
/* 183 */     recordBO.setExp(RefDataMgr.getFactor("Droiyan_WinnerExp", 5000));
/* 184 */     reward.add(PrizeType.Gold, recordBO.getGold());
/* 185 */     reward.add(PrizeType.Exp, recordBO.getExp());
/* 186 */     ((PlayerItem)winner.getFeature(PlayerItem.class)).gain(reward, ItemFlow.DroiyanFight);
/* 187 */     if (this.isRevenge) {
/* 188 */       recordBO.setRevenged(true);
/* 189 */       this.def.setRevenged(this.atk.getPid());
/*     */     } else {
/*     */       
/* 192 */       recordBO.setRevenged(false);
/* 193 */       this.def.addEnemy(this.atk.getPid());
/*     */     } 
/*     */ 
/*     */     
/* 197 */     RankManager.getInstance().update(RankType.Droiyan, getAtkPid(), winnerbo.getPoint());
/*     */ 
/*     */     
/* 200 */     if (winnerbo.getRed() >= RefDataMgr.getFactor("Droiyan_MaxRed", 100)) {
/* 201 */       NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DroiyanRedName, new String[] { this.atk.getPlayer().getName() });
/*     */     }
/*     */ 
/*     */     
/* 205 */     winnerbo.saveAll();
/* 206 */     recordBO.insert();
/* 207 */     this.atk.addRecored(recordBO);
/* 208 */     return recordBO;
/*     */   }
/*     */   
/*     */   private void pushTreasure(DroiyanTreasureBO bo) {
/* 212 */     this.atk.getPlayer().pushProto("addTreasure", new DroiyanTreasures.Treasure(bo));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getDeffenders() {
/* 217 */     return this.deffender;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Integer, Fighter> getAttackers() {
/* 222 */     return this.attacker;
/*     */   }
/*     */ 
/*     */   
/*     */   public int fightTime() {
/* 227 */     return ArenaConfig.fightTime();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/DroiyanFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */