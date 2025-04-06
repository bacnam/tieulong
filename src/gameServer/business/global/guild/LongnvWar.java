/*     */ package business.global.guild;
/*     */ 
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.global.battle.SimulatBattle;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.rank.Record;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.RobotManager;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.guild.GuildMemberFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.config.refdata.ref.RefLongnvLevel;
/*     */ import core.config.refdata.ref.RefLongnvWarLevel;
/*     */ import core.config.refdata.ref.RefLongnvWarPersonReward;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.database.game.bo.LongnvApplyBO;
/*     */ import core.database.game.bo.LongnvResultBO;
/*     */ import core.database.game.bo.LongnvWarResultBO;
/*     */ import core.database.game.bo.LongnvwarpuppetBO;
/*     */ import core.network.proto.Fight;
/*     */ import core.network.proto.LongnvWarFightProtol;
/*     */ import core.network.proto.LongnvWarInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LongnvWar
/*     */ {
/*     */   Guild guild;
/*     */   LongnvWarResultBO bo;
/*     */   List<GuildWarMgr.Road> warRoad;
/*     */   private int road;
/*     */   int fightTime;
/*     */   public Map<Long, List<Double>> playersHP;
/*     */   public List<LongnvResultBO> fightResult;
/*     */   public Map<Long, Fight.Battle> fightBattle;
/*     */   public final Map<Long, List<LongnvwarpuppetBO>> puppets;
/*     */   public List<Player> joinPlayers;
/*     */   int total;
/*     */   int enemyTotal;
/*     */   Map<Long, Integer> playerPuppet;
/*     */   
/*     */   public LongnvWar(Guild guild) {
/*  68 */     this.warRoad = new ArrayList<>();
/*     */     
/*  70 */     this.road = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.playersHP = Maps.newConcurrentHashMap();
/*     */     
/*  78 */     this.fightResult = new ArrayList<>();
/*     */     
/*  80 */     this.fightBattle = Maps.newConcurrentHashMap();
/*     */ 
/*     */     
/*  83 */     this.puppets = Maps.newConcurrentHashMap();
/*     */ 
/*     */     
/*  86 */     this.joinPlayers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     this.playerPuppet = new HashMap<>();
/*     */     this.guild = guild;
/*     */     init();
/*     */   } public void init() {
/*  98 */     List<LongnvApplyBO> applys = BM.getBM(LongnvApplyBO.class).findAll("guildId", Long.valueOf(this.guild.getGuildId()));
/*  99 */     for (LongnvApplyBO apply : applys) {
/* 100 */       if (apply.getApplyTime() < CommTime.getTodayZeroClockS()) {
/* 101 */         apply.del();
/*     */         continue;
/*     */       } 
/* 104 */       Player player = PlayerMgr.getInstance().getPlayer(apply.getPid());
/* 105 */       this.joinPlayers.add(player);
/*     */     } 
/*     */ 
/*     */     
/* 109 */     List<LongnvwarpuppetBO> puppets = BM.getBM(LongnvwarpuppetBO.class).findAll();
/* 110 */     for (LongnvwarpuppetBO puppet : puppets) {
/* 111 */       Guild guild = GuildMgr.getInstance().getGuild(puppet.getGuildId());
/* 112 */       if (guild == null) {
/* 113 */         puppet.del();
/*     */         continue;
/*     */       } 
/* 116 */       if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
/* 117 */         puppet.del();
/*     */         continue;
/*     */       } 
/* 120 */       if (this.puppets.get(Long.valueOf(puppet.getPid())) != null) {
/* 121 */         ((List<LongnvwarpuppetBO>)this.puppets.get(Long.valueOf(puppet.getPid()))).add(puppet); continue;
/*     */       } 
/* 123 */       List<LongnvwarpuppetBO> list = new ArrayList<>();
/* 124 */       list.add(puppet);
/* 125 */       this.puppets.put(Long.valueOf(puppet.getPid()), list);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     List<LongnvWarResultBO> results = BM.getBM(LongnvWarResultBO.class).findAll("guildId", Long.valueOf(this.guild.getGuildId()));
/* 132 */     for (LongnvWarResultBO result : results) {
/* 133 */       if (result.getChallengeTime() < CommTime.getTodayZeroClockS()) {
/* 134 */         result.del();
/*     */         continue;
/*     */       } 
/* 137 */       this.bo = result;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void NPCchallenge() {
/* 142 */     LongnvWarResultBO bo = new LongnvWarResultBO();
/* 143 */     bo.setGuildId(this.guild.getGuildId());
/* 144 */     bo.setLevel(this.guild.getLongnvLevel());
/* 145 */     bo.setChallengeTime(CommTime.nowSecond());
/* 146 */     bo.insert();
/* 147 */     this.bo = bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepareFight() {
/* 155 */     this.warRoad.clear();
/* 156 */     for (int i = 0; i < this.road; i++) {
/* 157 */       this.warRoad.add(new GuildWarMgr.Road());
/*     */     }
/*     */     
/* 160 */     List<Player> defList = new ArrayList<>();
/* 161 */     List<Player> atkList = new ArrayList<>();
/*     */     
/* 163 */     List<Player> defpuppets = new ArrayList<>();
/*     */     
/* 165 */     RefLongnvWarLevel ref = (RefLongnvWarLevel)RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));
/* 166 */     if (ref.RobotLevel != 0) {
/* 167 */       List<Player> players = RobotManager.getInstance().getLvlPlayers(ref.RobotLevel);
/*     */       
/* 169 */       if (players == null) {
/* 170 */         players = RobotManager.getInstance().getLvlPlayers(38);
/*     */       }
/* 172 */       atkList.addAll(players.subList(0, ref.Amount));
/*     */     } else {
/*     */       
/* 175 */       long guildid = RankManager.getInstance().getPlayerId(RankType.Guild, 1);
/* 176 */       List<Long> members = GuildMgr.getInstance().getGuild(guildid).getMembers();
/* 177 */       List<Record> record = RankManager.getInstance().getRankList(RankType.Power, ref.Amount);
/* 178 */       for (Record r : record) {
/* 179 */         if (r == null) {
/*     */           continue;
/*     */         }
/* 182 */         members.add(Long.valueOf(r.getPid()));
/*     */       } 
/* 184 */       for (Long pid : members) {
/* 185 */         Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
/* 186 */         atkList.add(player);
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     defList.addAll(this.joinPlayers);
/* 191 */     defpuppets = createPuppets();
/*     */     
/* 193 */     for (Map.Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
/* 194 */       this.playerPuppet.put(entry.getKey(), Integer.valueOf(((List)entry.getValue()).size()));
/*     */     }
/*     */ 
/*     */     
/* 198 */     this.total = defList.size() + defpuppets.size();
/* 199 */     this.enemyTotal = atkList.size();
/*     */     
/*     */     int j;
/* 202 */     for (j = 0; j < defList.size(); j++) {
/* 203 */       int index = j % this.road;
/* 204 */       ((GuildWarMgr.Road)this.warRoad.get(index)).getDefplayers().add(defList.get(j));
/*     */     } 
/*     */ 
/*     */     
/* 208 */     for (j = 0; j < defpuppets.size(); j++) {
/* 209 */       int index = j % this.road;
/* 210 */       ((GuildWarMgr.Road)this.warRoad.get(index)).getDefplayers().add(defpuppets.get(j));
/*     */     } 
/*     */ 
/*     */     
/* 214 */     for (j = 0; j < atkList.size(); j++) {
/* 215 */       int index = j % this.road;
/* 216 */       ((GuildWarMgr.Road)this.warRoad.get(index)).getAtkplayers().add(atkList.get(j));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fight() {
/* 222 */     prepareFight();
/* 223 */     beginFight();
/*     */   }
/*     */   
/*     */   public void Start() {
/* 227 */     if (this.guild.getLevel() < RefDataMgr.getFactor("LongnvUnlockLv", 5)) {
/*     */       return;
/*     */     }
/* 230 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.LongnvUnderAttack, this.guild, new String[0]);
/* 231 */     dailyRefresh();
/* 232 */     NPCchallenge();
/* 233 */     SyncTaskManager.schedule(LongnvWarConfig.challengeCD() * 1000, () -> {
/*     */           fight();
/*     */           return false;
/*     */         });
/*     */   }
/*     */   
/*     */   public void Restart() {
/* 240 */     if (this.bo == null) {
/*     */       return;
/*     */     }
/* 243 */     if (this.bo.getResult() != 0 || this.bo.getChallengeTime() < CommTime.getTodayZeroClockS()) {
/*     */       return;
/*     */     }
/* 246 */     int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
/* 247 */     SyncTaskManager.schedule(Math.max(0, time) * 1000, () -> {
/*     */           try {
/*     */             fight();
/* 250 */           } catch (Exception e) {
/*     */             return false;
/*     */           } 
/*     */           return false;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginFight() {
/* 260 */     this.fightTime = CommTime.nowSecond();
/* 261 */     for (int i = 0; i < this.road; i++) {
/* 262 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 263 */       road.setBegin(CommTime.nowSecond());
/* 264 */       if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
/* 265 */         road.setOverTime(CommTime.nowSecond());
/*     */       }
/* 267 */       SyncTaskManager.task(() -> SyncTaskManager.schedule(LongnvWarConfig.oneFightTime(), ()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean roadFight(GuildWarMgr.Road road) {
/* 276 */     synchronized (this) {
/* 277 */       road.setBegin(CommTime.nowSecond());
/*     */       
/* 279 */       if (atkResult(road)) {
/* 280 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 284 */       if (timeOver(road)) {
/* 285 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 289 */       if (road.getWinner() != 0L) {
/* 290 */         return false;
/*     */       }
/* 292 */       Player atkPlayer = null;
/* 293 */       Player defPlayer = null;
/* 294 */       if (road.getAtkplayers().size() > 0) {
/* 295 */         atkPlayer = road.getAtkplayers().get(0);
/*     */       }
/* 297 */       if (road.getDefplayers().size() > 0) {
/* 298 */         defPlayer = road.getDefplayers().get(0);
/*     */       }
/* 300 */       if (atkPlayer == null || defPlayer == null) {
/* 301 */         if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() > LongnvWarConfig.overTime()) {
/* 302 */           if (atkPlayer == null) {
/* 303 */             road.setWinner(2L);
/* 304 */             Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
/* 305 */             if (find.isPresent()) {
/* 306 */               ((GuildWarMgr.Road)find.get()).getDefplayers().addAll(road.getDefplayers());
/* 307 */               road.getDefplayers().clear();
/*     */             } 
/*     */           } else {
/* 310 */             road.setWinner(1L);
/* 311 */             Optional<GuildWarMgr.Road> find = this.warRoad.stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
/* 312 */             if (find.isPresent()) {
/* 313 */               ((GuildWarMgr.Road)find.get()).getAtkplayers().addAll(road.getAtkplayers());
/* 314 */               road.getAtkplayers().clear();
/*     */             } 
/*     */           } 
/* 317 */           return false;
/* 318 */         }  if (road.getOverTime() == 0) {
/* 319 */           road.setOverTime(CommTime.nowSecond());
/*     */         }
/*     */       } else {
/*     */         
/* 323 */         SimulatBattle battle = new SimulatBattle(atkPlayer, defPlayer);
/* 324 */         battle.initHp(this.playersHP.get(Long.valueOf(atkPlayer.getPid())), this.playersHP.get(Long.valueOf(defPlayer.getPid())));
/* 325 */         battle.initInspire();
/* 326 */         battle.fight();
/* 327 */         FightResult result = battle.getResult();
/* 328 */         LongnvResultBO resultbo = new LongnvResultBO();
/* 329 */         resultbo.setAtkpid(atkPlayer.getPid());
/* 330 */         resultbo.setDefpid(defPlayer.getPid());
/* 331 */         resultbo.setResult(result.ordinal());
/* 332 */         resultbo.setFightTime(CommTime.nowSecond());
/* 333 */         resultbo.insert();
/* 334 */         this.fightResult.add(resultbo);
/* 335 */         this.fightBattle.put(Long.valueOf(resultbo.getId()), battle.proto());
/* 336 */         Player lose = null;
/* 337 */         Player winner = null;
/* 338 */         if (result == FightResult.Win) {
/* 339 */           lose = road.getDefplayers().remove(0);
/* 340 */           winner = atkPlayer;
/* 341 */           road.getDeaddefplayers().add(lose);
/*     */         } 
/* 343 */         if (result == FightResult.Lost) {
/* 344 */           lose = road.getAtkplayers().remove(0);
/* 345 */           winner = defPlayer;
/* 346 */           road.getDeadatkplayers().add(lose);
/*     */         } 
/* 348 */         if (result == FightResult.Draw) {
/* 349 */           Player lose1 = road.getDefplayers().remove(0);
/* 350 */           road.getDeaddefplayers().add(lose1);
/* 351 */           Player lose2 = road.getAtkplayers().remove(0);
/* 352 */           road.getDeadatkplayers().add(lose2);
/*     */         } 
/* 354 */         if (lose != null && winner != null) {
/*     */           
/* 356 */           this.playersHP.put(Long.valueOf(winner.getPid()), battle.getWinnerHp());
/*     */           
/* 358 */           if (GuildWarConfig.puppetPlayer.class.isInstance(lose)) {
/* 359 */             removePuppet(lose);
/*     */           }
/*     */         } 
/*     */         
/* 363 */         if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
/* 364 */           road.setOverTime(CommTime.nowSecond());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePuppet(Player puppet) {
/* 376 */     List<LongnvwarpuppetBO> puppets = this.puppets.get(Long.valueOf(puppet.getPid()));
/* 377 */     LongnvwarpuppetBO find = null;
/* 378 */     if (puppets != null) {
/* 379 */       GuildWarConfig.puppetPlayer puppetplayer = (GuildWarConfig.puppetPlayer)puppet;
/* 380 */       for (LongnvwarpuppetBO bo : puppets) {
/* 381 */         if (bo.getPuppetId() == puppetplayer.getPuppet_id()) {
/* 382 */           find = bo;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 387 */     if (find != null) {
/* 388 */       puppets.remove(find);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean atkResult(GuildWarMgr.Road road) {
/* 394 */     synchronized (this) {
/* 395 */       if (road.isOver) {
/* 396 */         return true;
/*     */       }
/* 398 */       int atknum = getleftAtk();
/* 399 */       int defnum = getleftDef();
/* 400 */       if (atknum == 0 || defnum == 0) {
/* 401 */         this.bo.setFightTime(CommTime.nowSecond());
/* 402 */         if (atknum == 0) {
/* 403 */           this.bo.setResult(FightResult.Win.ordinal());
/* 404 */           RefLongnvWarLevel ref = (RefLongnvWarLevel)RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));
/* 405 */           NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.LongnvWin, new String[] { this.guild.getName(), ref.GuildName });
/*     */         } 
/* 407 */         if (defnum == 0) {
/* 408 */           this.bo.setResult(FightResult.Lost.ordinal());
/*     */         }
/* 410 */         this.bo.saveAll();
/*     */         
/* 412 */         dealWarFinish();
/* 413 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 417 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean timeOver(GuildWarMgr.Road road) {
/* 422 */     synchronized (this) {
/* 423 */       if (road.isOver) {
/* 424 */         return true;
/*     */       }
/*     */       
/* 427 */       if (CommTime.nowSecond() - this.fightTime > LongnvWarConfig.fightTime()) {
/* 428 */         this.bo.setFightTime(CommTime.nowSecond());
/* 429 */         this.bo.setResult(FightResult.Win.ordinal());
/* 430 */         this.bo.saveAll();
/* 431 */         dealWarFinish();
/* 432 */         return true;
/*     */       } 
/*     */     } 
/* 435 */     return false;
/*     */   }
/*     */   
/*     */   public int getleftAtk() {
/* 439 */     int num = 0;
/* 440 */     for (int i = 0; i < this.road; i++) {
/* 441 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 442 */       num += road.getAtkplayers().size();
/*     */     } 
/* 444 */     return num;
/*     */   }
/*     */   
/*     */   public int getDeadAtk() {
/* 448 */     int num = 0;
/* 449 */     for (int i = 0; i < this.road; i++) {
/* 450 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 451 */       num += road.getDeadatkplayers().size();
/*     */     } 
/* 453 */     return num;
/*     */   }
/*     */   
/*     */   public int getleftDef() {
/* 457 */     int num = 0;
/* 458 */     for (int i = 0; i < this.road; i++) {
/* 459 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 460 */       num += road.getDefplayers().size();
/*     */     } 
/* 462 */     return num;
/*     */   }
/*     */   
/*     */   public int getDeadDef() {
/* 466 */     int num = 0;
/* 467 */     for (int i = 0; i < this.road; i++) {
/* 468 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 469 */       num += road.getDeaddefplayers().size();
/*     */     } 
/* 471 */     return num;
/*     */   }
/*     */   
/*     */   public int getdeadAtk() {
/* 475 */     int num = 0;
/* 476 */     for (int i = 0; i < this.road; i++) {
/* 477 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 478 */       num += road.getDeadatkplayers().size();
/*     */     } 
/* 480 */     return num;
/*     */   }
/*     */   
/*     */   public int getdeadDef() {
/* 484 */     int num = 0;
/* 485 */     for (int i = 0; i < this.road; i++) {
/* 486 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 487 */       num += road.getDeaddefplayers().size();
/*     */     } 
/* 489 */     return num;
/*     */   }
/*     */   
/*     */   private void dealWarFinish() {
/* 493 */     synchronized (this) {
/* 494 */       for (int i = 0; i < this.road; i++) {
/* 495 */         GuildWarMgr.Road road1 = this.warRoad.get(i);
/* 496 */         road1.isOver = true;
/*     */       } 
/* 498 */       personReward(this.fightResult);
/*     */       
/* 500 */       this.playersHP.clear();
/* 501 */       this.guild.broadcast("warFinish", "");
/* 502 */       roportAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void roportAll() {
/* 508 */     String myGuild = this.guild.getName();
/*     */     
/* 510 */     String total = String.valueOf(this.total);
/*     */     
/* 512 */     String dead = String.valueOf(getdeadDef());
/*     */     
/* 514 */     String enemyTotal = String.valueOf(this.enemyTotal);
/*     */     
/* 516 */     String enemyDead = String.valueOf(getdeadAtk());
/*     */     
/* 518 */     SyncTaskManager.task(() -> {
/*     */           RefLongnvWarLevel ref = (RefLongnvWarLevel)RefDataMgr.get(RefLongnvWarLevel.class, Integer.valueOf(this.guild.bo.getLnwarLevel() + 1));
/*     */           
/*     */           String EnemyGuild = ref.GuildName;
/*     */           
/*     */           int mailId = LongnvWarConfig.ResultMail();
/*     */           
/*     */           String result = "";
/*     */           
/*     */           if (this.bo.getResult() == FightResult.Win.ordinal()) {
/*     */             result = "胜利";
/*     */           } else if (this.bo.getResult() == FightResult.Lost.ordinal()) {
/*     */             result = "失败";
/*     */           } 
/*     */           for (Long pid : this.guild.getMembers()) {
/*     */             int puppet = 0;
/*     */             int kill = 0;
/*     */             try {
/*     */               if (this.playerPuppet.get(pid) != null) {
/*     */                 puppet = ((Integer)this.playerPuppet.get(pid)).intValue();
/*     */               }
/* 539 */             } catch (Exception exception) {}
/*     */             kill = getKillNum(pid.longValue());
/*     */             MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[] { paramString1, paramString2, paramString3, EnemyGuild, paramString4, paramString5, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString(), result });
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void personReward(List<LongnvResultBO> list) {
/* 551 */     List<GuildWarMgr.Road> roads = this.warRoad;
/*     */     
/* 553 */     int defwin = 0;
/* 554 */     for (GuildWarMgr.Road road : roads) {
/* 555 */       if (road.getWinner() == 0L)
/*     */         continue; 
/* 557 */       if (road.getDefplayers().size() != 0) {
/* 558 */         defwin++;
/*     */       }
/*     */     } 
/*     */     
/* 562 */     Map<Long, List<LongnvResultBO>> map = Maps.newConcurrentHashMap();
/* 563 */     for (LongnvResultBO bo : list) {
/* 564 */       if (bo.getResult() == FightResult.Lost.ordinal()) {
/* 565 */         if (map.get(Long.valueOf(bo.getDefpid())) != null) {
/* 566 */           ((List<LongnvResultBO>)map.get(Long.valueOf(bo.getDefpid()))).add(bo); continue;
/*     */         } 
/* 568 */         List<LongnvResultBO> tmp_list = new ArrayList<>();
/* 569 */         tmp_list.add(bo);
/* 570 */         map.put(Long.valueOf(bo.getDefpid()), tmp_list);
/*     */       } 
/*     */     } 
/*     */     
/* 574 */     for (Map.Entry<Long, List<LongnvResultBO>> entry : map.entrySet()) {
/* 575 */       int num = 1;
/* 576 */       Player player = PlayerMgr.getInstance().getPlayer(((Long)entry.getKey()).longValue());
/* 577 */       num = Math.max(num, defwin);
/* 578 */       Reward rewardall = new Reward();
/* 579 */       Reward reward = RefLongnvWarPersonReward.getReward(((List)entry.getValue()).size());
/* 580 */       for (int i = 0; i < num; i++) {
/* 581 */         rewardall.combine(reward);
/*     */       }
/* 583 */       MailCenter.getInstance().sendMail(player.getPid(), "GM", "守卫龙女个人奖励", "在龙女守卫战中你奋勇杀敌，获得以下奖励", rewardall, new String[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> createPuppets() {
/* 589 */     List<Player> puppets = new ArrayList<>();
/* 590 */     for (Map.Entry<Long, List<LongnvwarpuppetBO>> entry : this.puppets.entrySet()) {
/* 591 */       Player player = PlayerMgr.getInstance().getPlayer(((Long)entry.getKey()).longValue());
/* 592 */       for (LongnvwarpuppetBO bo : entry.getValue()) {
/* 593 */         GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
/* 594 */         p_player.setPuppet_id(bo.getPuppetId());
/* 595 */         p_player.setIs_puppet(true);
/* 596 */         puppets.add(p_player);
/*     */       } 
/*     */     } 
/* 599 */     return puppets;
/*     */   }
/*     */   
/*     */   public void applyLongnvWar(Player player) throws WSException {
/* 603 */     if (this.bo == null) {
/* 604 */       throw new WSException(ErrorCode.Longnv_NotFoundFight, "尚未有帮派宣战");
/*     */     }
/* 606 */     int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
/* 607 */     if (time < 0) {
/* 608 */       throw new WSException(ErrorCode.Longnv_NotFoundFight, "报名时间已过");
/*     */     }
/* 610 */     if (!this.joinPlayers.contains(player)) {
/* 611 */       LongnvApplyBO apply = new LongnvApplyBO();
/* 612 */       apply.setPid(player.getPid());
/* 613 */       apply.setGuildId(this.guild.getGuildId());
/* 614 */       apply.setApplyTime(CommTime.nowSecond());
/* 615 */       apply.insert();
/* 616 */       this.joinPlayers.add(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LongnvWarInfo getWarInfo(Player player) throws WSException {
/* 621 */     if (this.bo == null) {
/* 622 */       return null;
/*     */     }
/*     */     
/* 625 */     LongnvWarInfo info = new LongnvWarInfo();
/* 626 */     info.level = this.bo.getLevel();
/* 627 */     info.robotLevel = this.guild.bo.getLnwarLevel();
/* 628 */     info.leftTime = Math.max(0, this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond());
/* 629 */     info.isApply = this.joinPlayers.contains(player);
/* 630 */     info.pickTimes = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).bo.getLongnvPickReward();
/* 631 */     info.result = this.bo.getResult();
/* 632 */     info.puppet = getPuppetInfo(player);
/* 633 */     info.leftRebirthTime = 86400 - CommTime.nowSecond() - this.bo.getChallengeTime();
/* 634 */     return info;
/*     */   }
/*     */   
/*     */   public int getAtkPuppetNum() {
/* 638 */     int num = 0;
/* 639 */     for (int i = 0; i < this.road; i++) {
/* 640 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 641 */       for (Player player : road.atkplayers) {
/* 642 */         if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
/* 643 */           num++;
/*     */         }
/*     */       } 
/*     */     } 
/* 647 */     return num;
/*     */   }
/*     */   
/*     */   public int getDefPuppetNum() {
/* 651 */     int num = 0;
/* 652 */     for (int i = 0; i < this.road; i++) {
/* 653 */       GuildWarMgr.Road road = this.warRoad.get(i);
/* 654 */       for (Player player : road.defplayers) {
/* 655 */         if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
/* 656 */           num++;
/*     */         }
/*     */       } 
/*     */     } 
/* 660 */     return num;
/*     */   }
/*     */   
/*     */   public boolean isWarOver() {
/* 664 */     int overRoad = 0;
/* 665 */     for (GuildWarMgr.Road road : this.warRoad) {
/* 666 */       if (road.isOver) {
/* 667 */         overRoad++;
/*     */       }
/*     */     } 
/* 670 */     if (overRoad == this.road) {
/* 671 */       return true;
/*     */     }
/*     */     
/* 674 */     return false;
/*     */   }
/*     */   
/*     */   public LongnvWarFightProtol getFightInfo(Player player) throws WSException {
/* 678 */     LongnvWarFightProtol info = new LongnvWarFightProtol();
/* 679 */     if (this.warRoad.size() == 0) {
/* 680 */       throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始");
/*     */     }
/* 682 */     if (isWarOver()) {
/* 683 */       throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
/*     */     }
/*     */     
/* 686 */     Guild.GuildSummary summary = new Guild.GuildSummary();
/* 687 */     summary.setGuildId(-1L);
/* 688 */     summary.setGuildName("神秘帮会");
/* 689 */     info.setAtk(summary);
/*     */     
/* 691 */     info.setDef(new Guild.GuildSummary(this.guild));
/*     */     
/* 693 */     List<LongnvWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
/* 694 */     guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getAtk().getGuildName(), getleftAtk(), getDeadAtk(), getAtkPuppetNum()));
/* 695 */     guildInfo.add(new LongnvWarFightProtol.GuildInfo(info.getDef().getGuildName(), getleftDef(), getDeadDef(), getDefPuppetNum()));
/* 696 */     info.setGuildInfo(guildInfo);
/* 697 */     List<LongnvWarFightProtol.ResultRecord> result = new ArrayList<>();
/* 698 */     for (LongnvResultBO bo : this.fightResult) {
/* 699 */       result.add(new LongnvWarFightProtol.ResultRecord(bo));
/*     */     }
/* 701 */     info.setResultInfo(result);
/* 702 */     info.setEndTime(nextRefreshTime());
/* 703 */     info.setKillnum(getKillNum(player.getPid()));
/* 704 */     List<LongnvWarFightProtol.RoadSummry> summarylist = new ArrayList<>();
/* 705 */     for (GuildWarMgr.Road road : this.warRoad) {
/* 706 */       summarylist.add(new LongnvWarFightProtol.RoadSummry(road));
/*     */     }
/* 708 */     info.setRoad(summarylist);
/* 709 */     for (GuildWarMgr.Road road : this.warRoad) {
/* 710 */       if (road.getDeadatkplayers().contains(player) || road.getDeaddefplayers().contains(player)) {
/* 711 */         info.setDead(true);
/*     */         break;
/*     */       } 
/*     */     } 
/* 715 */     info.setRebirthTime(((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.LongnvRebirth));
/* 716 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextRefreshTime() {
/* 725 */     int time = CommTime.getTodayZeroClockS();
/* 726 */     time = time + 39600 + LongnvWarConfig.challengeCD();
/* 727 */     time += GuildWarConfig.fightTime();
/* 728 */     return time;
/*     */   }
/*     */   
/*     */   public int getKillNum(long pid) {
/* 732 */     int num = 0;
/* 733 */     List<LongnvResultBO> list = this.fightResult;
/* 734 */     for (LongnvResultBO bo : list) {
/* 735 */       if (bo.getDefpid() == pid && bo.getResult() == FightResult.Lost.ordinal()) {
/* 736 */         num++;
/*     */       }
/*     */     } 
/* 739 */     return num;
/*     */   }
/*     */ 
/*     */   
/*     */   public Guild.rebirth rebirth(Player player) throws WSException {
/* 744 */     if (!this.joinPlayers.contains(player)) {
/* 745 */       throw new WSException(ErrorCode.Longnv_NotApply, "玩家未报名");
/*     */     }
/* 747 */     int time = this.bo.getChallengeTime() + LongnvWarConfig.challengeCD() - CommTime.nowSecond();
/* 748 */     if (time < 0) {
/* 749 */       rebirthFighting(player);
/* 750 */       PlayerRecord playerRecord = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 751 */       return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), playerRecord.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
/*     */     } 
/* 753 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 754 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*     */     
/* 756 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth);
/* 757 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
/* 758 */     if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
/* 759 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", new Object[] { Integer.valueOf(prize.LongnvRebirth) });
/*     */     }
/* 761 */     currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
/* 762 */     recorder.addValue(ConstEnum.DailyRefresh.LongnvRebirth);
/* 763 */     LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
/* 764 */     bo.setPid(player.getPid());
/* 765 */     bo.setGuildId(this.guild.getGuildId());
/* 766 */     bo.setPuppetId(curTimes);
/* 767 */     bo.setApplyTime(CommTime.nowSecond());
/* 768 */     bo.insert();
/* 769 */     if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
/* 770 */       ((List<LongnvwarpuppetBO>)this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
/*     */     } else {
/* 772 */       List<LongnvwarpuppetBO> list = new ArrayList<>();
/* 773 */       list.add(bo);
/* 774 */       this.puppets.put(Long.valueOf(player.getPid()), list);
/*     */     } 
/*     */     
/* 777 */     this.guild.broadcast("beforeLongnvPuppet", Integer.valueOf(getTotalPuppet()), player.getPid());
/*     */     
/* 779 */     return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalPuppet() {
/* 784 */     int num = 0;
/* 785 */     for (List<LongnvwarpuppetBO> list : this.puppets.values()) {
/* 786 */       num += list.size();
/*     */     }
/*     */     
/* 789 */     return num;
/*     */   }
/*     */   
/*     */   public int getPersonPuppet(long pid) {
/* 793 */     int num = 0;
/* 794 */     List<LongnvwarpuppetBO> list = this.puppets.get(Long.valueOf(pid));
/* 795 */     if (list != null) {
/* 796 */       num = list.size();
/*     */     }
/* 798 */     return num;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongnvWarFightProtol rebirthFighting(Player player) throws WSException {
/* 803 */     if (isWarOver()) {
/* 804 */       throw new WSException(ErrorCode.Longnv_NotFoundFight, "战斗已结束");
/*     */     }
/*     */     
/* 807 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 808 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*     */     
/* 810 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth);
/* 811 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
/* 812 */     if (!currency.check(PrizeType.Crystal, prize.LongnvRebirth)) {
/* 813 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", new Object[] { Integer.valueOf(prize.LongnvRebirth) });
/*     */     }
/* 815 */     currency.consume(PrizeType.Crystal, prize.LongnvRebirth, ItemFlow.Longnv_Rebirth);
/* 816 */     recorder.addValue(ConstEnum.DailyRefresh.LongnvRebirth);
/* 817 */     List<GuildWarMgr.Road> list = this.warRoad;
/* 818 */     List<GuildWarMgr.Road> tmp_list = new ArrayList<>(list);
/* 819 */     Collections.shuffle(tmp_list);
/* 820 */     Optional<GuildWarMgr.Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
/* 821 */     if (find.isPresent()) {
/* 822 */       GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
/* 823 */       p_player.setPuppet_id(curTimes);
/* 824 */       p_player.setIs_puppet(true);
/* 825 */       ((GuildWarMgr.Road)find.get()).getDefplayers().add(p_player);
/*     */     } 
/* 827 */     LongnvwarpuppetBO bo = new LongnvwarpuppetBO();
/* 828 */     bo.setPid(player.getPid());
/* 829 */     bo.setGuildId(this.guild.getGuildId());
/* 830 */     bo.setPuppetId(curTimes);
/* 831 */     bo.setApplyTime(CommTime.nowSecond());
/* 832 */     bo.insert();
/* 833 */     if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
/* 834 */       ((List<LongnvwarpuppetBO>)this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
/*     */     } else {
/* 836 */       List<LongnvwarpuppetBO> listp = new ArrayList<>();
/* 837 */       listp.add(bo);
/* 838 */       this.puppets.put(Long.valueOf(player.getPid()), listp);
/*     */     } 
/*     */ 
/*     */     
/* 842 */     this.total++;
/*     */     
/* 844 */     int num = 0;
/* 845 */     if (this.playerPuppet.get(Long.valueOf(player.getPid())) != null) {
/* 846 */       num = ((Integer)this.playerPuppet.get(Long.valueOf(player.getPid()))).intValue();
/*     */     }
/* 848 */     num++;
/* 849 */     this.playerPuppet.put(Long.valueOf(player.getPid()), Integer.valueOf(num));
/*     */     
/* 851 */     SyncTaskManager.task(() -> {
/*     */           for (Long pid : this.guild.getMembers()) {
/*     */             Player player_tmp = PlayerMgr.getInstance().getPlayer(pid.longValue());
/*     */             if (player_tmp == paramPlayer)
/*     */               continue; 
/*     */             try {
/*     */               player_tmp.pushProto("longnvRebirth", getFightInfo(player_tmp));
/* 858 */             } catch (WSException e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 865 */     return getFightInfo(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/* 870 */     if (this.bo == null) {
/*     */       return;
/*     */     }
/* 873 */     int level = this.guild.bo.getLnwarLevel();
/*     */     
/* 875 */     if (this.bo.getResult() == FightResult.Win.ordinal()) {
/* 876 */       int max = RefDataMgr.getAll(RefLongnvWarLevel.class).size();
/* 877 */       int now = Math.min(level + 1, max);
/* 878 */       this.guild.bo.saveLnwarLevel(now);
/*     */     } 
/*     */     
/* 881 */     if (this.bo.getResult() == FightResult.Lost.ordinal()) {
/* 882 */       int now = Math.max(0, level - 1);
/* 883 */       this.guild.bo.saveLnwarLevel(now);
/*     */     } 
/*     */     
/* 886 */     for (GuildMemberFeature feature : this.guild.getAllMemberFeatures()) {
/* 887 */       feature.bo.saveLongnvPickReward(0);
/*     */     }
/*     */     
/* 890 */     this.puppets.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Guild.rebirth getPuppetInfo(Player player) {
/* 895 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 896 */     return new Guild.rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.LongnvRebirth));
/*     */   }
/*     */   
/*     */   public Reward pickReward(Player player) throws WSException {
/* 900 */     if (!LongnvWarConfig.pickRewardTime()) {
/* 901 */       throw new WSException(ErrorCode.Longnv_NotFoundFight, "不在领奖时间内");
/*     */     }
/* 903 */     if (this.bo == null || this.bo.getResult() != 1) {
/* 904 */       throw new WSException(ErrorCode.Longnv_NotWin, "没有成功守卫龙女");
/*     */     }
/* 906 */     int picktime = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).bo.getLongnvPickReward();
/* 907 */     if (picktime != 0) {
/* 908 */       throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
/*     */     }
/* 910 */     ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).bo.saveLongnvPickReward(picktime + 1);
/* 911 */     RefLongnvLevel reflevel = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(this.bo.getLevel()));
/* 912 */     RefReward ref = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(reflevel.RewardId));
/* 913 */     Reward reward = ref.genReward();
/* 914 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Longnv_Win);
/* 915 */     return reward;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/LongnvWar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */