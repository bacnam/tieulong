/*     */ package business.player.feature.pvp;
/*     */ import business.global.arena.ArenaManager;
/*     */ import business.global.arena.Competitor;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import business.global.pvp.EncouterManager;
/*     */ import business.global.rank.RankManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.character.Character;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDroiyanTreasure;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ import core.database.game.bo.DroiyanBO;
/*     */ import core.database.game.bo.DroiyanRecordBO;
/*     */ import core.database.game.bo.DroiyanTreasureBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class DroiyanFeature extends Feature {
/*     */   private static final int MAX_FIGHTERS = 3;
/*     */   private DroiyanBO bo;
/*     */   private List<DroiyanTreasureBO> treasures;
/*     */   private List<DroiyanRecordBO> records;
/*     */   private List<Long> enemies;
/*     */   private Map<Integer, Integer> hp;
/*     */   
/*     */   public DroiyanFeature(Player player) {
/*  48 */     super(player);
/*     */ 
/*     */ 
/*     */     
/*  52 */     this.treasures = new ArrayList<>();
/*  53 */     this.records = new ArrayList<>();
/*  54 */     this.enemies = new ArrayList<>();
/*  55 */     this.hp = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void loadDB() {
/*  59 */     this.bo = (DroiyanBO)BM.getBM(DroiyanBO.class).findOne("pid", Long.valueOf(getPid()));
/*  60 */     if (this.bo == null) {
/*  61 */       this.bo = new DroiyanBO();
/*  62 */       this.bo.setPid(getPid());
/*  63 */       List<Long> list1 = ArenaManager.getInstance().randCompetitor(getPid(), 3);
/*  64 */       for (int i = 0; i < 3; i++) {
/*  65 */         this.bo.setFighters(i, ((Long)list1.get(i)).longValue());
/*     */       }
/*  67 */       this.bo.setLastSearchTime(CommTime.nowSecond());
/*  68 */       this.bo.insert();
/*     */       return;
/*     */     } 
/*  71 */     this.treasures = BM.getBM(DroiyanTreasureBO.class).findAll("pid", Long.valueOf(getPid()));
/*  72 */     this.records = BM.getBM(DroiyanRecordBO.class).findAll("pid", Long.valueOf(getPid()));
/*     */     
/*  74 */     List<DroiyanRecordBO> list = BM.getBM(DroiyanRecordBO.class).findAll("target", Long.valueOf(getPid()));
/*  75 */     this.enemies = new ArrayList<>();
/*  76 */     int time = CommTime.nowSecond() - 604800;
/*  77 */     for (DroiyanRecordBO bo : list) {
/*  78 */       if (bo.getTime() < time) {
/*  79 */         bo.del();
/*     */         continue;
/*     */       } 
/*  82 */       if (bo.getResult() == 1 && !bo.getRevenged())
/*  83 */         this.enemies.add(Long.valueOf(bo.getPid())); 
/*  84 */       if (this.enemies.size() >= RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/*  89 */     this.hp = new HashMap<>();
/*     */   }
/*     */   
/*     */   public DroiyanBO getBo() {
/*  93 */     return this.bo;
/*     */   }
/*     */   
/*     */   public void addDroiyan(long pid) {
/*  97 */     for (int i = 0; i < this.bo.getFightersSize(); i++) {
/*  98 */       if (this.bo.getFighters(i) == 0L) {
/*  99 */         this.bo.saveFighters(i, pid);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<DroiyanRecordBO> getRecords() {
/* 106 */     return this.records;
/*     */   }
/*     */   
/*     */   public boolean popDroiyan(long targetPid) {
/* 110 */     int count = 0;
/* 111 */     boolean find = false;
/* 112 */     for (int i = 0; i < this.bo.getFightersSize(); i++) {
/* 113 */       if (this.bo.getFighters(i) == 0L) {
/* 114 */         count++;
/* 115 */       } else if (this.bo.getFighters(i) == targetPid) {
/* 116 */         this.bo.saveFighters(i, 0L);
/* 117 */         find = true;
/*     */       } 
/*     */     } 
/* 120 */     if (count == 0) {
/* 121 */       this.bo.saveLastSearchTime(CommTime.nowSecond());
/*     */     }
/* 123 */     return find;
/*     */   }
/*     */   
/*     */   public DroiyanTreasureBO beRobbed() {
/* 127 */     return beRobbedByType("Normal");
/*     */   }
/*     */   
/*     */   public DroiyanTreasureBO beRevengeRobbed() {
/* 131 */     return beRobbedByType("Revenge");
/*     */   }
/*     */   
/*     */   public DroiyanTreasureBO beRobbedByType(String type) {
/* 135 */     int now = CommTime.nowSecond();
/* 136 */     Optional<DroiyanTreasureBO> find = Optional.ofNullable(null);
/*     */     
/* 138 */     if ((type.equals("Normal") && isRed()) || (type.equals("Revenge") && !isRed())) {
/*     */       
/* 140 */       List<DroiyanTreasureBO> findList = (List<DroiyanTreasureBO>)this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).collect(Collectors.toList());
/*     */ 
/*     */       
/* 143 */       List<Integer> rangeList = new ArrayList<>();
/* 144 */       List<RefDroiyanTreasure> refList = new ArrayList<>();
/* 145 */       for (DroiyanTreasureBO tmp_bo : findList) {
/* 146 */         RefDroiyanTreasure tmp_ref = (RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(tmp_bo.getTreasureId()));
/* 147 */         if (!refList.contains(tmp_ref))
/* 148 */           refList.add(tmp_ref); 
/*     */       } 
/* 150 */       for (RefDroiyanTreasure ref : refList) {
/* 151 */         String str; switch ((str = type).hashCode()) { case -1955878649: if (!str.equals("Normal"))
/*     */               continue; 
/* 153 */             rangeList.add(Integer.valueOf(ref.NormalWeight));
/*     */           case -1530471862:
/*     */             if (!str.equals("Revenge"))
/* 156 */               continue;  rangeList.add(Integer.valueOf(ref.RevengeWeight)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 164 */       int index = CommMath.getRandomIndexByRate(rangeList);
/* 165 */       if (index >= 0) {
/* 166 */         int id = ((RefDroiyanTreasure)refList.get(index)).id;
/* 167 */         find = findList.stream().filter(bo -> (bo.getTreasureId() == paramInt)).unordered().findAny();
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 172 */     else if (type.equals("Normal") && !isRed()) {
/* 173 */       find = this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).unordered().findAny();
/*     */     
/*     */     }
/* 176 */     else if (type.equals("Revenge") && isRed()) {
/*     */       
/* 178 */       List<DroiyanTreasureBO> findList = (List<DroiyanTreasureBO>)this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).collect(Collectors.toList());
/* 179 */       int quality = 0;
/* 180 */       for (DroiyanTreasureBO tmp_bo : findList) {
/* 181 */         RefDroiyanTreasure tmp_ref = (RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(tmp_bo.getTreasureId()));
/* 182 */         if (tmp_ref.Quality > quality)
/* 183 */           quality = tmp_ref.Quality; 
/*     */       } 
/* 185 */       int quality1 = quality;
/*     */       
/* 187 */       find = findList.stream().filter(bo -> (getquality(bo) == paramInt)).unordered().findAny();
/*     */     } 
/*     */     
/* 190 */     if (find == null || !find.isPresent()) {
/* 191 */       return null;
/*     */     }
/* 193 */     DroiyanTreasureBO bo = find.get();
/* 194 */     this.treasures.remove(bo);
/* 195 */     this.player.pushProto("robbedTreasure", Long.valueOf(bo.getId()));
/* 196 */     return bo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTreasure(DroiyanTreasureBO treasure) {
/* 201 */     this.treasures.add(treasure);
/*     */   }
/*     */   
/*     */   public DroiyanTreasureBO addTreasure(RefDroiyanTreasure ref) {
/* 205 */     DroiyanTreasureBO treasure = new DroiyanTreasureBO();
/* 206 */     treasure.setPid(getPid());
/* 207 */     treasure.setTreasureId(ref.id);
/* 208 */     treasure.setGainTime(CommTime.nowSecond());
/* 209 */     treasure.setExpireTime(ref.Time + CommTime.nowSecond());
/* 210 */     treasure.insert();
/* 211 */     this.treasures.add(treasure);
/* 212 */     return treasure;
/*     */   }
/*     */   
/*     */   public void addEnemy(long pid) {
/* 216 */     if (this.enemies.size() >= RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
/*     */       return;
/*     */     }
/* 219 */     this.enemies.add(Long.valueOf(pid));
/* 220 */     Player player = PlayerMgr.getInstance().getPlayer(pid);
/* 221 */     this.player.pushProto("newEnemy", ((PlayerBase)player.getFeature(PlayerBase.class)).summary());
/*     */   }
/*     */   
/*     */   public boolean isEnemy(long pid) {
/* 225 */     if (this.enemies.contains(Long.valueOf(pid))) {
/* 226 */       return true;
/*     */     }
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public void damage() {
/* 232 */     if (this.bo.getRed() < RefDataMgr.getFactor("Droiyan_Red", 80)) {
/*     */       return;
/*     */     }
/* 235 */     ((CharFeature)this.player.getFeature(CharFeature.class)).getAll().forEach((charid, character) -> {
/*     */           Integer curHp = this.hp.get(charid);
/*     */           if (curHp == null) {
/*     */             curHp = Integer.valueOf(100);
/*     */           }
/*     */           int damage = CommMath.randomInt(RefDataMgr.getFactor("Droiyan_RedDamageMin"), RefDataMgr.getFactor("Droiyan_RedDamageMax"));
/*     */           this.hp.put(charid, Integer.valueOf(Math.max(curHp.intValue() - damage, 0)));
/*     */         });
/*     */   }
/*     */   
/*     */   public void reborn() {
/* 246 */     ((CharFeature)this.player.getFeature(CharFeature.class)).getAll().forEach((charid, character) -> this.hp.put(charid, Integer.valueOf(100)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullDroiyan() {
/* 252 */     for (int i = 0; i < this.bo.getFightersSize(); i++) {
/* 253 */       if (this.bo.getFighters(i) == 0L) {
/* 254 */         return false;
/*     */       }
/*     */     } 
/* 257 */     return true;
/*     */   }
/*     */   
/*     */   public void checkDroiyan() {
/* 261 */     int interval = RefDataMgr.getFactor("Droiyan_SearchInterval", 1200);
/* 262 */     int times = (CommTime.nowSecond() - this.bo.getLastSearchTime()) / interval;
/* 263 */     if (times <= 0) {
/*     */       return;
/*     */     }
/* 266 */     List<Long> list = ranOpponent(getPid(), 4);
/* 267 */     list.removeAll(this.bo.getFightersAll());
/* 268 */     for (int i = 0; i < this.bo.getFightersSize() && times > 0; i++) {
/* 269 */       long cur = this.bo.getFighters(i);
/* 270 */       if (cur == 0L) {
/* 271 */         times--;
/* 272 */         this.bo.setFighters(i, ((Long)list.remove(0)).longValue());
/*     */       } 
/*     */     } 
/* 275 */     this.bo.setLastSearchTime(CommTime.nowSecond());
/* 276 */     this.bo.saveAll();
/*     */   }
/*     */   
/*     */   public long search() {
/* 280 */     List<Long> list = ranOpponent(getPid(), 4);
/* 281 */     list.removeAll(this.bo.getFightersAll());
/*     */     
/* 283 */     for (int i = 0; i < this.bo.getFightersSize(); i++) {
/* 284 */       long cur = this.bo.getFighters(i);
/* 285 */       if (cur == 0L) {
/* 286 */         this.bo.setFighters(i, ((Long)list.remove(0)).longValue());
/* 287 */         this.bo.setLastSearchTime(CommTime.nowSecond());
/* 288 */         this.bo.saveAll();
/* 289 */         return this.bo.getFighters(i);
/*     */       } 
/*     */     } 
/* 292 */     return 0L;
/*     */   }
/*     */   
/*     */   public List<Long> ranOpponent(long pid, int size) {
/* 296 */     List<Long> realPid = new ArrayList<>();
/*     */ 
/*     */     
/* 299 */     List<Long> treasurePid = new ArrayList<>();
/*     */     
/* 301 */     RankManager instance = RankManager.getInstance();
/* 302 */     int rank = instance.getRank(RankType.Power, pid);
/* 303 */     int offset = RefDataMgr.getFactor("DroiyanSearchOffset", 20);
/* 304 */     int begin = rank - offset;
/* 305 */     int end = rank + offset;
/* 306 */     List<Long> list = new ArrayList<>();
/* 307 */     int max = Math.min(instance.getRankSize(RankType.Power) - 1, end);
/*     */     
/* 309 */     for (int r = Math.max(1, begin); r <= max; r++) {
/* 310 */       if (r != rank) {
/*     */ 
/*     */         
/* 313 */         long tmp_pid = instance.getPlayerId(RankType.Power, r);
/*     */ 
/*     */         
/* 316 */         Player tmp_player = PlayerMgr.getInstance().getPlayer(tmp_pid);
/* 317 */         if (tmp_player != null) {
/* 318 */           if (((DroiyanFeature)tmp_player.getFeature(DroiyanFeature.class)).haveTreature()) {
/* 319 */             treasurePid.add(Long.valueOf(tmp_pid));
/*     */           } else {
/* 321 */             realPid.add(Long.valueOf(tmp_pid));
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 328 */     if (treasurePid.size() + realPid.size() < size) {
/* 329 */       Competitor competitor = ArenaManager.getInstance().getOrCreate(pid);
/* 330 */       int rankA = competitor.getRank();
/* 331 */       int offsetA = 50 + size * 2;
/*     */       
/* 333 */       int beginA = rankA - offsetA;
/* 334 */       int endA = rankA + offsetA;
/* 335 */       int maxA = Math.min(endA, ArenaManager.getInstance().getRank().size());
/* 336 */       for (int j = Math.max(1, beginA); j <= maxA; j++) {
/* 337 */         if (j != rankA)
/*     */         {
/*     */           
/* 340 */           list.add(Long.valueOf(((Competitor)ArenaManager.getInstance().getRank().get(j - 1)).getPid()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 345 */     Collections.shuffle(list);
/*     */     
/* 347 */     int pidSize = realPid.size() + treasurePid.size();
/*     */     
/* 349 */     for (int i = 0; i < size - pidSize; i++) {
/* 350 */       realPid.add(list.get(i));
/*     */     }
/*     */ 
/*     */     
/* 354 */     Collections.shuffle(treasurePid);
/* 355 */     Collections.shuffle(realPid);
/*     */     
/* 357 */     treasurePid.addAll(realPid);
/*     */     
/* 359 */     return treasurePid.subList(0, size);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean haveTreature() {
/* 364 */     int now = CommTime.nowSecond();
/* 365 */     for (DroiyanTreasureBO bo : this.treasures) {
/* 366 */       if (bo.getExpireTime() > now)
/* 367 */         return true; 
/*     */     } 
/* 369 */     return false;
/*     */   }
/*     */   
/*     */   public Reward openTreature(long treatureId) throws WSException {
/* 373 */     DroiyanTreasureBO find = null;
/* 374 */     for (DroiyanTreasureBO bo : this.treasures) {
/* 375 */       if (bo.getId() == treatureId) {
/* 376 */         find = bo;
/*     */         break;
/*     */       } 
/*     */     } 
/* 380 */     if (find == null) {
/* 381 */       throw new WSException(ErrorCode.Droiyan_TreasureNoFound, "%s玩家不持有宝物%s", new Object[] { Long.valueOf(getPid()), Long.valueOf(treatureId) });
/*     */     }
/* 383 */     if (find.getExpireTime() > CommTime.nowSecond()) {
/* 384 */       throw new WSException(ErrorCode.Droiyan_TreasureTimeLimit, "%s玩家宝物%s时间未到", new Object[] { Long.valueOf(getPid()), Long.valueOf(treatureId) });
/*     */     }
/* 386 */     PlayerRecord record = (PlayerRecord)this.player.getFeature(PlayerRecord.class);
/* 387 */     if (record.getValue(ConstEnum.DailyRefresh.OpenTreasure) >= ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).OpenTreasureTimes) {
/* 388 */       throw new WSException(ErrorCode.Droiyan_TreasureOpenTimes, "藏宝图开启次数已满");
/*     */     }
/*     */ 
/*     */     
/* 392 */     find.del();
/* 393 */     this.treasures.remove(find);
/*     */     
/* 395 */     RefDroiyanTreasure ref = (RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(find.getTreasureId()));
/* 396 */     Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Reward))).genReward();
/* 397 */     ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.DroiyanTreasure);
/* 398 */     EncouterManager.getInstance().addNews(EncouterManager.NewsType.Open, getPlayerName(), find.getTreasureId());
/*     */ 
/*     */     
/* 401 */     record.addValue(ConstEnum.DailyRefresh.OpenTreasure);
/*     */ 
/*     */ 
/*     */     
/* 405 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UseTreasure, new String[] { this.player.getName() });
/*     */     
/* 407 */     return reward;
/*     */   }
/*     */   
/*     */   public Map<Integer, Integer> getHpMap() {
/* 411 */     return this.hp;
/*     */   }
/*     */   
/*     */   public List<Long> getEnemies() {
/* 415 */     return this.enemies;
/*     */   }
/*     */   
/*     */   public boolean popEnemy(long targetPid) {
/* 419 */     return this.enemies.remove(Long.valueOf(targetPid));
/*     */   }
/*     */   
/*     */   public void setRevenged(long pid) {
/* 423 */     for (DroiyanRecordBO bo : this.records) {
/* 424 */       if (bo.getTarget() == pid) {
/* 425 */         bo.saveRevenged(true);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<DroiyanTreasureBO> getTreasures() {
/* 432 */     return this.treasures;
/*     */   }
/*     */   
/*     */   public void addRecored(DroiyanRecordBO recordBO) {
/* 436 */     this.records.add(recordBO);
/*     */   }
/*     */   
/*     */   public boolean isTreasureFull() {
/* 440 */     return (this.treasures.size() >= RefDataMgr.getFactor("MaxTreasureSize", 5));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRed() {
/* 445 */     return (this.bo.getRed() >= RefDataMgr.getFactor("Droiyan_MaxRed", 100));
/*     */   }
/*     */   
/*     */   public int getquality(DroiyanTreasureBO bo) {
/* 449 */     RefDroiyanTreasure ref = (RefDroiyanTreasure)RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(bo.getTreasureId()));
/* 450 */     return ref.Quality;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/pvp/DroiyanFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */