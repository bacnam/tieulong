/*     */ package business.global.worldboss;
/*     */ 
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.global.battle.detail.WorldbossBattle;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.rank.Record;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.RobotManager;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.worldboss.WorldBossConfig;
/*     */ import business.player.feature.worldboss.WorldBossFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.config.refdata.ref.RefLanguage;
/*     */ import core.config.refdata.ref.RefWorldBoss;
/*     */ import core.config.refdata.ref.RefWorldBossDamageReward;
/*     */ import core.database.game.bo.WorldBossBO;
/*     */ import core.database.game.bo.WorldBossChallengeBO;
/*     */ import core.database.game.bo.WorldBossKillRecordBO;
/*     */ import core.network.proto.WorldBossInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldBossMgr
/*     */ {
/*  54 */   private static WorldBossMgr instance = new WorldBossMgr();
/*     */   
/*     */   public static WorldBossMgr getInstance() {
/*  57 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<WorldBossBO> boList;
/*     */   
/*  63 */   public List<WorldBossKillRecordBO> killRecords = Lists.newConcurrentList();
/*     */   
/*  65 */   public Map<WorldBossBO, List<Player>> joinPlayers = new HashMap<>();
/*     */   
/*  67 */   public Map<WorldBossBO, List<Player>> fightingPlayers = new HashMap<>();
/*     */   
/*  69 */   public List<Player> robotPlayers = new ArrayList<>();
/*     */   
/*  71 */   public List<Long> autoPlayers = new Vector<>();
/*     */   
/*     */   boolean robotauto = false;
/*     */   
/*     */   public void init() {
/*  76 */     this.boList = null;
/*  77 */     this.killRecords.clear();
/*  78 */     this.joinPlayers.clear();
/*  79 */     this.fightingPlayers.clear();
/*     */     
/*  81 */     List<WorldBossBO> bossList = BM.getBM(WorldBossBO.class).findAll();
/*  82 */     if (bossList != null && bossList.size() > 0) {
/*  83 */       this.boList = bossList;
/*     */     }
/*     */     
/*  86 */     if (this.boList == null) {
/*  87 */       this.boList = new ArrayList<>();
/*  88 */       for (RefWorldBoss ref : RefDataMgr.getAll(RefWorldBoss.class).values()) {
/*  89 */         WorldBossBO bo = new WorldBossBO();
/*  90 */         bo.setBossId(ref.id);
/*  91 */         bo.setBossHp(ref.MaxHP);
/*  92 */         bo.setBossMaxHp(ref.MaxHP);
/*  93 */         bo.setIsDead(false);
/*  94 */         bo.setBossLevel(1L);
/*  95 */         bo.setReviveTime(0L);
/*  96 */         bo.insert_sync();
/*  97 */         this.boList.add(bo);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     for (WorldBossKillRecordBO bo : BM.getBM(WorldBossKillRecordBO.class).findAll()) {
/* 104 */       this.killRecords.add(bo);
/*     */     }
/*     */ 
/*     */     
/* 108 */     this.robotPlayers = RobotManager.getInstance().getRandomPlayers(RefDataMgr.getFactor("MaxWorldBossRobot", 15));
/*     */ 
/*     */     
/* 111 */     for (WorldBossChallengeBO bo : BM.getBM(WorldBossChallengeBO.class).findAll()) {
/* 112 */       if (!bo.getAutoChallenge()) {
/*     */         continue;
/*     */       }
/* 115 */       this.autoPlayers.add(Long.valueOf(bo.getPid()));
/*     */     } 
/*     */     
/* 118 */     for (WorldBossBO bo : this.boList) {
/* 119 */       this.joinPlayers.put(bo, new ArrayList<>());
/* 120 */       this.fightingPlayers.put(bo, new ArrayList<>());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldBossBO getBO(int bossId) {
/* 131 */     for (WorldBossBO bo : this.boList) {
/* 132 */       if (bo.getBossId() == bossId) {
/* 133 */         return bo;
/*     */       }
/*     */     } 
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldBossKillRecordBO getKillRecord(long bossId) {
/* 146 */     WorldBossKillRecordBO killRecordBO = null;
/* 147 */     for (WorldBossKillRecordBO bo : this.killRecords) {
/* 148 */       if (bo.getBossId() == bossId && CommTime.nowSecond() - bo.getDeathTime() < 86400) {
/* 149 */         killRecordBO = bo;
/*     */       }
/*     */     } 
/* 152 */     return killRecordBO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorldBossBO> getBOList() {
/* 162 */     return this.boList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Player> getPlayerList(WorldBossBO boss) {
/* 172 */     return this.fightingPlayers.get(boss);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Player> getJoinPlayerList(WorldBossBO boss) {
/* 181 */     return this.joinPlayers.get(boss);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int killBossNum() {
/* 190 */     int killNum = 0;
/* 191 */     synchronized (this.killRecords) {
/* 192 */       killNum = this.killRecords.size();
/*     */     } 
/* 194 */     return killNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearKillRecords(int bossId) {
/* 201 */     for (WorldBossKillRecordBO bo : this.killRecords) {
/* 202 */       if (bo.getBossId() != bossId) {
/*     */         continue;
/*     */       }
/* 205 */       this.killRecords.remove(bo);
/* 206 */       if (CommTime.nowSecond() - bo.getDeathTime() > 259200) {
/* 207 */         bo.del();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearPlayers(int bossId) {
/* 216 */     WorldBossBO boss = getBO(bossId);
/* 217 */     List<Player> joinPlayers = getJoinPlayerList(boss);
/* 218 */     List<Player> fightPlayers = getPlayerList(boss);
/* 219 */     if (joinPlayers != null)
/* 220 */       joinPlayers.clear(); 
/* 221 */     if (fightPlayers != null) {
/* 222 */       fightPlayers.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void limitHP(WorldBossBO bo) {
/* 230 */     if (bo.getBossHp() == bo.getBossMaxHp()) {
/*     */       return;
/*     */     }
/*     */     
/* 234 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf((int)bo.getBossId()));
/* 235 */     WorldBossKillRecordBO killRecord = getKillRecord(bo.getBossId());
/* 236 */     if (killRecord != null)
/* 237 */     { if (killRecord.getFightTime() > RefDataMgr.getFactor("WorldBossFightTime", 1200)) {
/* 238 */         bo.setBossMaxHp((long)((float)bo.getBossMaxHp() / ref.DownMultiple));
/* 239 */       } else if (killRecord.getFightTime() < RefDataMgr.getFactor("WorldBossFightTime", 600)) {
/* 240 */         if ((float)bo.getBossMaxHp() > 9.223372E18F / ref.UpMultiple) {
/* 241 */           bo.setBossMaxHp(Long.MAX_VALUE);
/*     */         } else {
/* 243 */           bo.setBossMaxHp((long)((float)bo.getBossMaxHp() * ref.UpMultiple));
/*     */         } 
/*     */       }  }
/* 246 */     else { bo.setBossMaxHp((long)((float)bo.getBossMaxHp() / ref.DownMultiple)); }
/*     */ 
/*     */     
/* 249 */     long hp = bo.getBossMaxHp();
/* 250 */     hp = hp / 1000000L * 1000000L;
/* 251 */     hp = Math.max(hp, RefDataMgr.getFactor("WorldBossMinHp", 100000));
/* 252 */     bo.setBossMaxHp(hp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefreshWorldBoss(int bossId) {
/*     */     try {
/* 265 */       int index = bossId - 1;
/* 266 */       WorldBossBO bo = getBOList().get(index);
/*     */       
/* 268 */       limitHP(bo);
/*     */       
/* 270 */       bo.setBossHp(bo.getBossMaxHp());
/* 271 */       bo.setBossLevel(1L);
/* 272 */       bo.setIsDead(false);
/* 273 */       bo.setReviveTime(CommTime.nowSecond());
/* 274 */       bo.setLastKillCid(0L);
/* 275 */       bo.saveAll();
/*     */       
/* 277 */       clearKillRecords(bossId);
/*     */       
/* 279 */       RankType type = getRankType(bossId);
/* 280 */       RankManager.getInstance().clear(type);
/*     */ 
/*     */       
/* 283 */       clearPlayers(bossId);
/*     */       
/* 285 */       for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 286 */         player.pushProto("bossRefresh", bo);
/*     */       }
/* 288 */     } catch (Exception e) {
/*     */       
/* 290 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public RankType getRankType(int bossId) {
/* 295 */     RankType type = null;
/* 296 */     switch (bossId) {
/*     */       case 1:
/* 298 */         type = RankType.WorldBoss1;
/*     */         break;
/*     */       case 2:
/* 301 */         type = RankType.WorldBoss2;
/*     */         break;
/*     */       case 3:
/* 304 */         type = RankType.WorldBoss3;
/*     */         break;
/*     */       case 4:
/* 307 */         type = RankType.WorldBoss4;
/*     */         break;
/*     */     } 
/*     */     
/* 311 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendRankReward(int bossId, boolean isKill) {
/*     */     try {
/* 321 */       WorldBossBO bo = getBO(bossId);
/* 322 */       if (bo.getIsDead() && !isKill) {
/*     */         return;
/*     */       }
/* 325 */       RankType type = getRankType(bossId);
/* 326 */       List<Record> records = RankManager.getInstance().getRankList(type, 999999);
/* 327 */       for (Record record : records) {
/* 328 */         if (record == null)
/*     */           continue; 
/* 330 */         RefWorldBossDamageReward ref = RefWorldBossDamageReward.getReward(bossId, record.getRank());
/* 331 */         if (ref != null) {
/* 332 */           MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[] { String.valueOf(record.getRank()) });
/*     */         }
/*     */       } 
/* 335 */     } catch (Exception e) {
/*     */       
/* 337 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doEnterWorldBoss(Player player, WorldBossBO worldBoss) {
/* 348 */     List<Player> players = this.joinPlayers.get(worldBoss);
/* 349 */     if (players == null) {
/* 350 */       List<Player> playerL = new ArrayList<>();
/* 351 */       playerL.add(player);
/* 352 */       this.joinPlayers.put(worldBoss, playerL);
/*     */     }
/* 354 */     else if (!players.contains(player)) {
/* 355 */       players.add(player);
/* 356 */       this.joinPlayers.put(worldBoss, players);
/*     */     } 
/*     */ 
/*     */     
/* 360 */     List<Player> fightPlayers = this.fightingPlayers.get(worldBoss);
/* 361 */     if (fightPlayers == null) {
/* 362 */       List<Player> playerL = new ArrayList<>();
/* 363 */       playerL.add(player);
/* 364 */       this.fightingPlayers.put(worldBoss, playerL);
/*     */     }
/* 366 */     else if (!fightPlayers.contains(player)) {
/* 367 */       fightPlayers.add(player);
/* 368 */       this.fightingPlayers.put(worldBoss, fightPlayers);
/*     */     } 
/*     */ 
/*     */     
/* 372 */     for (Player tmpPlayer : this.fightingPlayers.get(worldBoss)) {
/* 373 */       if (tmpPlayer != null && tmpPlayer != player) {
/* 374 */         tmpPlayer.pushProto("newWorldBossPlayer", ((PlayerBase)player.getFeature(PlayerBase.class)).fightInfo());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doLeaveWorldBoss(Player player, int bossId) {
/* 384 */     WorldBossBO boss = getBO(bossId);
/* 385 */     if (this.fightingPlayers.get(boss) != null) {
/* 386 */       ((List)this.fightingPlayers.get(boss)).remove(player);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void broadCastProtoToPlayers(WorldBossBO boss) {
/* 395 */     for (Player player : this.joinPlayers.get(boss)) {
/* 396 */       player.pushProto("worldbossDead", new WorldBossInfo(boss));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addWorldBossKillRecord(int bossId, Player killPlayer, Reward reward) {
/* 408 */     WorldBossBO bo = getBO(bossId);
/* 409 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/*     */     
/* 411 */     long beginTime = CommTime.getTodayHourMS(ref.BeginTime) / 1000L;
/*     */     
/* 413 */     WorldBossKillRecordBO kill = new WorldBossKillRecordBO();
/* 414 */     kill.setBossId(bo.getBossId());
/* 415 */     kill.setBossLevel(bo.getBossLevel());
/* 416 */     kill.setKillerPid(killPlayer.getPid());
/* 417 */     kill.setKillerName(killPlayer.getName());
/* 418 */     kill.setKillerLevel(killPlayer.getLv());
/* 419 */     kill.setKillerIcon(killPlayer.getPlayerBO().getIcon());
/* 420 */     kill.setRewardItemId(reward.uniformItemIds());
/* 421 */     kill.saveRewardItemCount(reward.uniformItemCounts());
/* 422 */     kill.setDeathTime((int)bo.getDeadTime());
/* 423 */     kill.setFightTime(bo.getDeadTime() - beginTime);
/* 424 */     kill.insert();
/* 425 */     this.killRecords.add(kill);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtWorldBoss(Player player, long damage, int bossId) throws WSException {
/* 437 */     boolean isKill = false;
/* 438 */     synchronized (this) {
/* 439 */       WorldBossBO bo = getBO(bossId);
/* 440 */       if (bo.getBossHp() == bo.getBossMaxHp() && !this.robotauto) {
/* 441 */         this.robotauto = true;
/* 442 */         robotFight(bossId);
/*     */       } 
/*     */       
/* 445 */       if (bo.getIsDead()) {
/* 446 */         throw new WSException(ErrorCode.WorldBoss_IsDeath, "Boss已被消灭");
/*     */       }
/* 448 */       long leftHp = bo.getBossHp();
/* 449 */       bo.setBossHp(leftHp - damage);
/* 450 */       if (bo.getBossHp() <= 0L) {
/* 451 */         damage = leftHp;
/* 452 */         bo.setDeadTime(CommTime.nowSecond());
/* 453 */         bo.setBossHp(0L);
/* 454 */         bo.setIsDead(true);
/* 455 */         bo.setLastKillCid(player.getPid());
/* 456 */         isKill = true;
/*     */       } 
/* 458 */       bo.saveAll();
/*     */       
/* 460 */       WorldBossFeature feature = (WorldBossFeature)player.getFeature(WorldBossFeature.class);
/* 461 */       feature.updateChallengeDamage(feature.getOrCreate(), damage, bossId);
/*     */       
/* 463 */       if (isKill) {
/* 464 */         broadCastProtoToPlayers(bo);
/* 465 */         RefWorldBoss boss = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 466 */         RefLanguage reflan = (RefLanguage)RefDataMgr.get(RefLanguage.class, boss.Name);
/* 467 */         NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.KillWorldBoss, new String[] { player.getName(), reflan.CN });
/* 468 */         sendRankReward(bossId, isKill);
/*     */       } 
/*     */     } 
/* 471 */     return isKill;
/*     */   }
/*     */ 
/*     */   
/*     */   public void robotFight(int bossId) {
/* 476 */     SyncTaskManager.schedule(30000, () -> robotDamage(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean robotDamage(int bossId) {
/* 482 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 483 */     WorldBossBO bo = getBO(bossId);
/* 484 */     double limit = bo.getBossMaxHp() * 0.05D;
/* 485 */     if (!ref.isInOpenHour() || bo.getIsDead() || bo.getBossHp() < limit) {
/* 486 */       this.robotauto = false;
/* 487 */       return false;
/*     */     } 
/* 489 */     for (Player player : this.robotPlayers) {
/*     */       try {
/* 491 */         long damage = Random.nextLong(10000L);
/* 492 */         doHurtWorldBoss(player, damage, bossId);
/* 493 */       } catch (WSException e) {
/* 494 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyAuto(Player player) throws WSException {
/* 502 */     synchronized (this) {
/* 503 */       if (this.autoPlayers.contains(Long.valueOf(player.getPid()))) {
/* 504 */         throw new WSException(ErrorCode.WorldBoss_AlreadyAuto, "自动挑战已申请");
/*     */       }
/* 506 */       ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(true);
/* 507 */       this.autoPlayers.add(Long.valueOf(player.getPid()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancelAuto(Player player) throws WSException {
/* 513 */     synchronized (this) {
/* 514 */       if (!this.autoPlayers.contains(Long.valueOf(player.getPid()))) {
/* 515 */         throw new WSException(ErrorCode.WorldBoss_NotAuto, "尚未申请过");
/*     */       }
/* 517 */       ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(false);
/* 518 */       this.autoPlayers.remove(Long.valueOf(player.getPid()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void autoFight(int bossId) {
/* 523 */     for (Long pid : this.autoPlayers) {
/* 524 */       Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
/* 525 */       ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveTotalDamage(bossId - 1, 0L);
/*     */     } 
/* 527 */     SyncTaskManager.schedule(WorldBossConfig.getFightCD() * 2 * 1000, () -> {
/*     */           RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(paramInt));
/*     */           WorldBossBO bo = getBO(paramInt);
/*     */           if (!ref.isInOpenHour() || bo.getIsDead())
/*     */             return false; 
/*     */           checkAuto();
/*     */           for (Long pid : this.autoPlayers) {
/*     */             Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
/*     */             autoDamage(player, paramInt);
/*     */           } 
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   public void checkAuto() {
/* 542 */     Iterator<Long> it = this.autoPlayers.iterator();
/* 543 */     while (it.hasNext()) {
/* 544 */       long pid = ((Long)it.next()).longValue();
/* 545 */       Player player = PlayerMgr.getInstance().getPlayer(pid);
/* 546 */       if (!((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge()) {
/* 547 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean autoDamage(Player player, int bossId) {
/* 553 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 554 */     WorldBossBO bo = getBO(bossId);
/* 555 */     if (!ref.isInOpenHour() || bo.getIsDead())
/* 556 */       return false; 
/*     */     try {
/* 558 */       PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 559 */       int times = record.getValue(ConstEnum.DailyRefresh.AutoFightWorldboss);
/* 560 */       int price = (RefCrystalPrice.getPrize(times)).AutoFightWorldboss;
/* 561 */       if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Gold, price, ItemFlow.AutoFightWorldboss)) {
/* 562 */         ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveAutoChallenge(false);
/* 563 */         return false;
/*     */       } 
/* 565 */       WorldbossBattle battle = new WorldbossBattle(player, ref.BossId);
/* 566 */       battle.init(bossId);
/* 567 */       battle.fight();
/* 568 */       long damage = battle.getDamage();
/*     */       
/* 570 */       boolean isKill = doHurtWorldBoss(player, damage, bossId);
/*     */       
/* 572 */       ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dealKillBoss(isKill, bossId);
/* 573 */       record.addValue(ConstEnum.DailyRefresh.AutoFightWorldboss);
/* 574 */       ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().saveLeaveFightTime((CommTime.nowSecond() + WorldBossConfig.getFightCD()));
/* 575 */     } catch (WSException e) {
/* 576 */       e.printStackTrace();
/*     */     } 
/* 578 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/worldboss/WorldBossMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */