/*      */ package business.global.guild;
/*      */ 
/*      */ import BaseTask.SyncTask.SyncTaskManager;
/*      */ import business.global.battle.SimulatBattle;
/*      */ import business.global.gmmail.MailCenter;
/*      */ import business.player.Player;
/*      */ import business.player.PlayerMgr;
/*      */ import business.player.RobotManager;
/*      */ import business.player.feature.PlayerCurrency;
/*      */ import business.player.feature.features.PlayerRecord;
/*      */ import business.player.feature.guild.GuildMemberFeature;
/*      */ import business.player.item.Reward;
/*      */ import com.zhonglian.server.common.db.BM;
/*      */ import com.zhonglian.server.common.enums.ConstEnum;
/*      */ import com.zhonglian.server.common.enums.FightResult;
/*      */ import com.zhonglian.server.common.enums.PrizeType;
/*      */ import com.zhonglian.server.common.utils.CommTime;
/*      */ import com.zhonglian.server.common.utils.Maps;
/*      */ import com.zhonglian.server.logger.flow.ItemFlow;
/*      */ import com.zhonglian.server.websocket.def.ErrorCode;
/*      */ import com.zhonglian.server.websocket.exception.WSException;
/*      */ import core.config.refdata.RefDataMgr;
/*      */ import core.config.refdata.ref.RefCrystalPrice;
/*      */ import core.config.refdata.ref.RefGuildWarCenter;
/*      */ import core.config.refdata.ref.RefGuildWarPersonReward;
/*      */ import core.database.game.bo.GuildwarGuildResultBO;
/*      */ import core.database.game.bo.GuildwarResultBO;
/*      */ import core.database.game.bo.GuildwarapplyBO;
/*      */ import core.database.game.bo.GuildwarpuppetBO;
/*      */ import core.network.proto.Fight;
/*      */ import core.network.proto.GuildWarCenterInfo;
/*      */ import core.network.proto.GuildWarFightProtol;
/*      */ import core.network.proto.Player;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GuildWarMgr
/*      */ {
/*   54 */   private static GuildWarMgr instance = new GuildWarMgr();
/*      */   
/*      */   public static GuildWarMgr getInstance() {
/*   57 */     return instance;
/*      */   }
/*      */ 
/*      */   
/*   61 */   public Map<Integer, List<Guild>> guildWarApplyer = Maps.newConcurrentHashMap();
/*   62 */   public Map<Integer, Guild> guildWarCenterOwner = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   65 */   public Map<Integer, List<Guild>> guildWarAtk = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   68 */   private int road = 3;
/*      */ 
/*      */   
/*   71 */   private Map<Integer, Integer> fightTime = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   74 */   private Map<Integer, List<GuildwarResultBO>> fightResult = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   77 */   public Map<Long, Fight.Battle> fightBattle = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   80 */   public Map<Long, List<GuildwarResultBO>> guildAllResulet = Maps.newConcurrentHashMap();
/*      */   
/*   82 */   private Map<Integer, List<GuildwarGuildResultBO>> guildFightResult = Maps.newConcurrentHashMap();
/*      */   
/*   84 */   private Map<Integer, Center> fighters = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   87 */   public Map<Integer, List<Player>> watchPlayers = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   90 */   public Map<Long, List<Double>> playersHP = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   93 */   public Map<Integer, Guild> historyWinner = Maps.newConcurrentHashMap();
/*      */ 
/*      */   
/*   96 */   Map<Integer, Map<Long, Report>> reports = new HashMap<>();
/*      */ 
/*      */   
/*      */   private static class Report
/*      */   {
/*      */     Guild guild;
/*  102 */     Map<Long, Integer> puppet = new HashMap<>();
/*      */     int total;
/*      */     
/*      */     private Report() {} }
/*      */   
/*      */   private static class Center { Guild atkGuild;
/*      */     Guild defGuild;
/*  109 */     List<Player> atkplayers = new ArrayList<>();
/*  110 */     List<Player> defplayers = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Center(List<Player> atkplayers, Guild atkGuild, List<Player> defplayers, Guild defGuild) {
/*  117 */       this.atkplayers = atkplayers;
/*  118 */       this.atkGuild = atkGuild;
/*  119 */       this.defplayers = defplayers;
/*  120 */       this.defGuild = defGuild;
/*      */     }
/*      */     
/*      */     private Center() {} }
/*      */   
/*      */   public static class Road { List<Player> atkplayers;
/*      */     List<Player> defplayers;
/*      */     List<Player> deadatkplayers;
/*      */     List<Player> deaddefplayers;
/*  129 */     long Winner = 0L;
/*      */     int overTime;
/*      */     boolean isOver = false;
/*      */     int begin;
/*      */     
/*      */     public Road() {
/*  135 */       this.atkplayers = new Vector<>();
/*  136 */       this.defplayers = new Vector<>();
/*  137 */       this.deadatkplayers = new Vector<>();
/*  138 */       this.deaddefplayers = new Vector<>();
/*      */     }
/*      */     
/*      */     public long getWinner() {
/*  142 */       return this.Winner;
/*      */     }
/*      */     
/*      */     public void setWinner(long winner) {
/*  146 */       this.Winner = winner;
/*      */     }
/*      */     
/*      */     public List<Player> getAtkplayers() {
/*  150 */       return this.atkplayers;
/*      */     }
/*      */     
/*      */     public void setAtkplayers(List<Player> atkplayers) {
/*  154 */       this.atkplayers = atkplayers;
/*      */     }
/*      */     
/*      */     public List<Player> getDefplayers() {
/*  158 */       return this.defplayers;
/*      */     }
/*      */     
/*      */     public void setDefplayers(List<Player> defplayers) {
/*  162 */       this.defplayers = defplayers;
/*      */     }
/*      */     
/*      */     public int getOverTime() {
/*  166 */       return this.overTime;
/*      */     }
/*      */     
/*      */     public void setOverTime(int overTime) {
/*  170 */       this.overTime = overTime;
/*      */     }
/*      */     
/*      */     public List<Player> getDeadatkplayers() {
/*  174 */       return this.deadatkplayers;
/*      */     }
/*      */     
/*      */     public void setDeadatkplayers(List<Player> deadatkplayers) {
/*  178 */       this.deadatkplayers = deadatkplayers;
/*      */     }
/*      */     
/*      */     public List<Player> getDeaddefplayers() {
/*  182 */       return this.deaddefplayers;
/*      */     }
/*      */     
/*      */     public void setDeaddefplayers(List<Player> deaddefplayers) {
/*  186 */       this.deaddefplayers = deaddefplayers;
/*      */     }
/*      */     
/*      */     public boolean isOver() {
/*  190 */       return this.isOver;
/*      */     }
/*      */     
/*      */     public void setOver(boolean isOver) {
/*  194 */       this.isOver = isOver;
/*      */     }
/*      */     
/*      */     public int getBegin() {
/*  198 */       return this.begin;
/*      */     }
/*      */     
/*      */     public void setBegin(int begin) {
/*  202 */       this.begin = begin;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*  207 */   public Map<Integer, List<Road>> guildWarRoad = Maps.newConcurrentHashMap();
/*      */   
/*      */   public void dailyRefresh() {
/*      */     try {
/*  211 */       for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  212 */         this.guildWarApplyer.put(Integer.valueOf(ref.id), new LinkedList<>());
/*  213 */         this.guildWarAtk.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */ 
/*      */         
/*  216 */         this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */         
/*  218 */         this.watchPlayers.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */         
/*  220 */         for (int i = 0; i < this.road; i++) {
/*  221 */           ((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
/*      */         }
/*      */       } 
/*  224 */       this.guildFightResult.clear();
/*  225 */       this.fightResult.clear();
/*  226 */       this.guildAllResulet.clear();
/*  227 */       this.fightBattle.clear();
/*  228 */     } catch (Exception e) {
/*      */       
/*  230 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void init() {
/*  236 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  237 */       this.guildWarApplyer.put(Integer.valueOf(ref.id), new LinkedList<>());
/*  238 */       this.guildWarAtk.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */ 
/*      */       
/*  241 */       this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */       
/*  243 */       this.watchPlayers.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */       
/*  245 */       for (int i = 0; i < this.road; i++) {
/*  246 */         ((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  252 */     List<GuildwarapplyBO> guildwarapplys = BM.getBM(GuildwarapplyBO.class).findAllBySort("applyTime", true, 0);
/*  253 */     for (GuildwarapplyBO guildwarapply : guildwarapplys) {
/*  254 */       Guild guild = GuildMgr.getInstance().getGuild(guildwarapply.getGuildId());
/*  255 */       if (guild == null) {
/*  256 */         guildwarapply.del();
/*      */         continue;
/*      */       } 
/*  259 */       if (guildwarapply.getWinCenterId() != 0) {
/*  260 */         this.historyWinner.put(Integer.valueOf(guildwarapply.getWinCenterId()), guild);
/*      */       }
/*  262 */       if (guildwarapply.getApplyTime() > CommTime.getTodayZeroClockS()) {
/*  263 */         guild.guildwarCenter = guildwarapply;
/*  264 */         ((List<Guild>)this.guildWarApplyer.get(Integer.valueOf(guildwarapply.getCenterId()))).add(guild);
/*      */       } 
/*      */     } 
/*      */     
/*  268 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*      */       
/*  270 */       if (this.historyWinner.get(Integer.valueOf(ref.id)) != null) {
/*  271 */         this.guildWarCenterOwner.put(Integer.valueOf(ref.id), this.historyWinner.get(Integer.valueOf(ref.id)));
/*      */       }
/*  273 */       int centerId = ref.id;
/*  274 */       Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/*  275 */       if (owner == null) {
/*      */         
/*  277 */         ((List)this.guildWarAtk.get(Integer.valueOf(ref.id))).addAll(this.guildWarApplyer.get(Integer.valueOf(ref.id)));
/*      */         continue;
/*      */       } 
/*  280 */       boolean flag = false;
/*  281 */       for (Guild tmp_guild : this.guildWarApplyer.get(Integer.valueOf(centerId))) {
/*  282 */         if (owner != tmp_guild && !flag) {
/*      */           continue;
/*      */         }
/*  285 */         if (owner == tmp_guild) {
/*  286 */           flag = true;
/*      */           continue;
/*      */         } 
/*  289 */         ((List<Guild>)this.guildWarAtk.get(Integer.valueOf(ref.id))).add(tmp_guild);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  295 */     if (CommTime.getTodayHour() >= 19) {
/*  296 */       Start();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Guild getAtkGuild(int centerId) {
/*  303 */     return (((List)this.guildWarAtk.get(Integer.valueOf(centerId))).size() > 0) ? ((List<Guild>)this.guildWarAtk.get(Integer.valueOf(centerId))).get(0) : null;
/*      */   }
/*      */   
/*      */   public void applyGuildWar(int centerId, Guild guild) throws WSException {
/*  307 */     if (((List)this.guildWarApplyer.get(Integer.valueOf(centerId))).size() >= RefDataMgr.getFactor("MaxGuildWarApply", 6)) {
/*  308 */       throw new WSException(ErrorCode.GuildWar_AlreadyApply, "据点申请已满");
/*      */     }
/*  310 */     synchronized (this) {
/*  311 */       ((List<Guild>)this.guildWarApplyer.get(Integer.valueOf(centerId))).add(guild);
/*  312 */       ((List<Guild>)this.guildWarAtk.get(Integer.valueOf(centerId))).add(guild);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prepareFight() {
/*  320 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  321 */       if (!ref.isOpenTime()) {
/*      */         continue;
/*      */       }
/*  324 */       if (getAtkGuild(ref.id) == null) {
/*      */         continue;
/*      */       }
/*  327 */       int centerId = ref.id;
/*      */       
/*  329 */       Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/*  330 */       List<Player> defList = new ArrayList<>();
/*  331 */       List<Player> atkList = new ArrayList<>();
/*  332 */       List<Player> defpuppets = new ArrayList<>();
/*  333 */       List<Player> atkpuppets = new ArrayList<>();
/*      */ 
/*      */       
/*  336 */       this.guildWarRoad.put(Integer.valueOf(ref.id), new LinkedList<>());
/*      */       
/*  338 */       for (int i = 0; i < this.road; i++) {
/*  339 */         ((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).add(new Road());
/*      */       }
/*      */ 
/*      */       
/*  343 */       this.fightResult.put(Integer.valueOf(centerId), new ArrayList<>());
/*      */ 
/*      */       
/*  346 */       this.fighters.put(Integer.valueOf(centerId), new Center(null));
/*      */       
/*  348 */       if (owner == null) {
/*      */         
/*  350 */         defList = RobotManager.getInstance().getRandomPlayers(14);
/*      */       } else {
/*      */         
/*  353 */         defList = owner.getGuildWarPlayer();
/*  354 */         defpuppets = owner.createPuppets();
/*      */       } 
/*      */       
/*  357 */       Guild atkGuild = getAtkGuild(ref.id);
/*  358 */       while (atkGuild.getGuildWarPlayer().size() == 0) {
/*      */         
/*  360 */         ((List)this.guildWarApplyer.get(Integer.valueOf(ref.id))).remove(atkGuild);
/*  361 */         ((List)this.guildWarAtk.get(Integer.valueOf(ref.id))).remove(atkGuild);
/*  362 */         atkGuild = getAtkGuild(ref.id);
/*  363 */         if (atkGuild == null) {
/*      */           break;
/*      */         }
/*      */       } 
/*  367 */       if (atkGuild == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  371 */       atkList = atkGuild.getGuildWarPlayer();
/*      */       
/*  373 */       atkpuppets = atkGuild.createPuppets();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  378 */       Map<Long, Report> reportmap = new HashMap<>();
/*  379 */       Report defreport = new Report(null);
/*  380 */       defreport.guild = owner;
/*  381 */       defreport.total = defList.size() + defpuppets.size();
/*  382 */       if (owner != null) {
/*  383 */         for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : owner.puppets.entrySet()) {
/*  384 */           defreport.puppet.put(entry.getKey(), Integer.valueOf(((List)entry.getValue()).size()));
/*      */         }
/*  386 */         reportmap.put(Long.valueOf(owner.getGuildId()), defreport);
/*      */       } else {
/*  388 */         reportmap.put(Long.valueOf(0L), defreport);
/*      */       } 
/*      */ 
/*      */       
/*  392 */       Report atkreport = new Report(null);
/*  393 */       atkreport.guild = atkGuild;
/*  394 */       atkreport.total = atkList.size() + atkpuppets.size();
/*  395 */       for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : atkGuild.puppets.entrySet()) {
/*  396 */         atkreport.puppet.put(entry.getKey(), Integer.valueOf(((List)entry.getValue()).size()));
/*      */       }
/*  398 */       reportmap.put(Long.valueOf(atkGuild.getGuildId()), atkreport);
/*  399 */       this.reports.put(Integer.valueOf(centerId), reportmap);
/*      */ 
/*      */       
/*  402 */       this.fighters.put(Integer.valueOf(centerId), new Center(atkList, atkGuild, defList, owner, null));
/*      */       
/*      */       int j;
/*  405 */       for (j = 0; j < defList.size(); j++) {
/*  406 */         int index = j % this.road;
/*  407 */         ((Road)((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getDefplayers().add(defList.get(j));
/*      */       } 
/*      */ 
/*      */       
/*  411 */       for (j = 0; j < defpuppets.size(); j++) {
/*  412 */         int index = j % this.road;
/*  413 */         ((Road)((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getDefplayers().add(defpuppets.get(j));
/*      */       } 
/*      */ 
/*      */       
/*  417 */       for (j = 0; j < atkList.size(); j++) {
/*  418 */         int index = j % this.road;
/*  419 */         ((Road)((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getAtkplayers().add(atkList.get(j));
/*      */       } 
/*      */ 
/*      */       
/*  423 */       for (j = 0; j < atkpuppets.size(); j++) {
/*  424 */         int index = j % this.road;
/*  425 */         ((Road)((List<Road>)this.guildWarRoad.get(Integer.valueOf(ref.id))).get(index)).getAtkplayers().add(atkpuppets.get(j));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginFightByCenterId(int centerId) {
/*  433 */     this.fightTime.put(Integer.valueOf(centerId), Integer.valueOf(CommTime.nowSecond()));
/*  434 */     for (int i = 0; i < this.road; i++) {
/*  435 */       Road road = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  436 */       road.setBegin(CommTime.nowSecond());
/*  437 */       if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
/*  438 */         road.setOverTime(CommTime.nowSecond());
/*      */       }
/*  440 */       SyncTaskManager.task(() -> SyncTaskManager.schedule(GuildWarConfig.oneFightTime(), ()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean roadFight(Road road, int centerId) {
/*  449 */     synchronized (this) {
/*  450 */       road.setBegin(CommTime.nowSecond());
/*      */       
/*  452 */       if (atkResult(centerId, road)) {
/*  453 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  457 */       if (timeOver(centerId, road)) {
/*  458 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  462 */       if (road.getWinner() != 0L) {
/*  463 */         return false;
/*      */       }
/*  465 */       Player atkPlayer = null;
/*  466 */       Player defPlayer = null;
/*  467 */       if (road.getAtkplayers().size() > 0) {
/*  468 */         atkPlayer = road.getAtkplayers().get(0);
/*      */       }
/*  470 */       if (road.getDefplayers().size() > 0) {
/*  471 */         defPlayer = road.getDefplayers().get(0);
/*      */       }
/*  473 */       if (atkPlayer == null || defPlayer == null) {
/*  474 */         if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() + 2 > GuildWarConfig.overTime()) {
/*  475 */           if (atkPlayer == null) {
/*  476 */             if (RobotManager.getInstance().isRobot(defPlayer.getPid())) {
/*  477 */               road.setWinner(-1L);
/*      */             } else {
/*  479 */               road.setWinner(((GuildMemberFeature)defPlayer.getFeature(GuildMemberFeature.class)).getGuildID());
/*      */             } 
/*  481 */             Optional<Road> find = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
/*  482 */             if (find.isPresent()) {
/*  483 */               ((Road)find.get()).getDefplayers().addAll(road.getDefplayers());
/*  484 */               road.getDefplayers().clear();
/*      */             } 
/*      */           } else {
/*      */             
/*  488 */             road.setWinner(((GuildMemberFeature)atkPlayer.getFeature(GuildMemberFeature.class)).getGuildID());
/*  489 */             Optional<Road> find = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).stream().parallel().filter(x -> (x != paramRoad1 && x.getWinner() == 0L)).findAny();
/*  490 */             if (find.isPresent()) {
/*  491 */               ((Road)find.get()).getAtkplayers().addAll(road.getAtkplayers());
/*  492 */               road.getAtkplayers().clear();
/*      */             } 
/*      */           } 
/*  495 */           return false;
/*  496 */         }  if (road.getOverTime() == 0) {
/*  497 */           road.setOverTime(CommTime.nowSecond());
/*      */         }
/*      */       } else {
/*      */         
/*  501 */         SimulatBattle battle = new SimulatBattle(atkPlayer, defPlayer);
/*  502 */         battle.initHp(this.playersHP.get(Long.valueOf(atkPlayer.getPid())), this.playersHP.get(Long.valueOf(defPlayer.getPid())));
/*  503 */         battle.initInspire();
/*  504 */         battle.fight();
/*  505 */         FightResult result = battle.getResult();
/*  506 */         GuildwarResultBO resultbo = new GuildwarResultBO();
/*  507 */         resultbo.setAtkpid(atkPlayer.getPid());
/*  508 */         resultbo.setDefpid(defPlayer.getPid());
/*  509 */         resultbo.setResult(result.ordinal());
/*  510 */         resultbo.setCenterId(centerId);
/*  511 */         resultbo.setFightTime(CommTime.nowSecond());
/*  512 */         resultbo.insert();
/*  513 */         ((List<GuildwarResultBO>)this.fightResult.get(Integer.valueOf(centerId))).add(resultbo);
/*  514 */         this.fightBattle.put(Long.valueOf(resultbo.getId()), battle.proto());
/*  515 */         Player lose = null;
/*  516 */         Player winner = null;
/*  517 */         if (result == FightResult.Win) {
/*  518 */           lose = road.getDefplayers().remove(0);
/*  519 */           winner = atkPlayer;
/*  520 */           road.getDeaddefplayers().add(lose);
/*      */         } 
/*  522 */         if (result == FightResult.Lost) {
/*  523 */           lose = road.getAtkplayers().remove(0);
/*  524 */           winner = defPlayer;
/*  525 */           road.getDeadatkplayers().add(lose);
/*      */         } 
/*  527 */         if (result == FightResult.Draw) {
/*  528 */           Player lose1 = road.getDefplayers().remove(0);
/*  529 */           road.getDeaddefplayers().add(lose1);
/*  530 */           Player lose2 = road.getAtkplayers().remove(0);
/*  531 */           road.getDeadatkplayers().add(lose2);
/*      */         } 
/*  533 */         if (lose != null && winner != null) {
/*      */           
/*  535 */           this.playersHP.put(Long.valueOf(winner.getPid()), battle.getWinnerHp());
/*      */           
/*  537 */           if (GuildWarConfig.puppetPlayer.class.isInstance(lose)) {
/*  538 */             Guild guild = ((GuildMemberFeature)lose.getFeature(GuildMemberFeature.class)).getGuild();
/*  539 */             guild.removePuppet(lose);
/*      */           } 
/*      */         } 
/*      */         
/*  543 */         if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
/*  544 */           road.setOverTime(CommTime.nowSecond());
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  550 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void dealWarFinish(int centerId, GuildwarGuildResultBO result) {
/*  555 */     synchronized (this) {
/*  556 */       for (int i = 0; i < this.road; i++) {
/*  557 */         Road road1 = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  558 */         road1.isOver = true;
/*      */       } 
/*  560 */       personReward(this.fightResult.get(Integer.valueOf(centerId)), centerId);
/*  561 */       this.fighters.remove(Integer.valueOf(centerId));
/*  562 */       this.playersHP.clear();
/*      */       
/*  564 */       for (Player player : this.watchPlayers.get(Integer.valueOf(centerId))) {
/*  565 */         player.pushProto("warFinish", "");
/*      */       }
/*  567 */       checkReward(centerId);
/*  568 */       roportAll(centerId, result);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void roportAll(int centerId, GuildwarGuildResultBO result) {
/*  574 */     Map<Long, Report> map = this.reports.get(Integer.valueOf(centerId));
/*  575 */     RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
/*      */ 
/*      */     
/*  578 */     long defId = result.getDefGuildId();
/*  579 */     if (defId != 0L) {
/*  580 */       Report report1 = map.get(Long.valueOf(defId));
/*  581 */       Guild guild = GuildMgr.getInstance().getGuild(defId);
/*      */       
/*  583 */       String str1 = guild.getName();
/*      */       
/*  585 */       String str2 = String.valueOf(((Report)map.get(Long.valueOf(defId))).total);
/*      */       
/*  587 */       String str3 = String.valueOf(getdeadDef(centerId));
/*      */       
/*  589 */       String EnemyGuild = GuildMgr.getInstance().getGuild(result.getAtkGuildId()).getName();
/*      */       
/*  591 */       String str4 = String.valueOf(((Report)map.get(Long.valueOf(result.getAtkGuildId()))).total);
/*      */       
/*  593 */       String str5 = String.valueOf(getdeadAtk(centerId));
/*      */       
/*  595 */       SyncTaskManager.task(() -> {
/*      */             int mailId = 0;
/*      */             if (paramGuildwarGuildResultBO.getResult() == FightResult.Win.ordinal()) {
/*      */               mailId = paramRefGuildWarCenter.FailMail;
/*      */             } else if (paramGuildwarGuildResultBO.getResult() == FightResult.Lost.ordinal()) {
/*      */               mailId = paramRefGuildWarCenter.MailId;
/*      */             } 
/*      */             for (Long pid : paramGuild.getMembers()) {
/*      */               int puppet = 0;
/*      */               int kill = 0;
/*      */               try {
/*      */                 if (paramReport.puppet.get(pid) != null) {
/*      */                   puppet = ((Integer)paramReport.puppet.get(pid)).intValue();
/*      */                 }
/*  609 */               } catch (Exception exception) {}
/*      */ 
/*      */               
/*      */               kill = getKillNum(paramInt, pid.longValue());
/*      */ 
/*      */               
/*      */               MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[] { paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString() });
/*      */             } 
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/*  621 */     long guildId = result.getAtkGuildId();
/*      */     
/*  623 */     Report report = map.get(Long.valueOf(guildId));
/*      */     
/*  625 */     Guild defGuild = GuildMgr.getInstance().getGuild(guildId);
/*      */     
/*  627 */     String myGuild = defGuild.getName();
/*      */     
/*  629 */     String total = String.valueOf(((Report)map.get(Long.valueOf(guildId))).total);
/*      */     
/*  631 */     String dead = String.valueOf(getdeadAtk(centerId));
/*      */     
/*  633 */     String enemyTotal = String.valueOf(((Report)map.get(Long.valueOf(result.getDefGuildId()))).total);
/*      */     
/*  635 */     String enemyDead = String.valueOf(getdeadDef(centerId));
/*      */     
/*  637 */     SyncTaskManager.task(() -> {
/*      */           String EnemyGuild = "";
/*      */           
/*      */           if (GuildMgr.getInstance().getGuild(paramGuildwarGuildResultBO.getDefGuildId()) == null) {
/*      */             EnemyGuild = "神秘帮派";
/*      */           } else {
/*      */             EnemyGuild = GuildMgr.getInstance().getGuild(paramGuildwarGuildResultBO.getDefGuildId()).getName();
/*      */           } 
/*      */           int mailId = 0;
/*      */           if (paramGuildwarGuildResultBO.getResult() == FightResult.Win.ordinal()) {
/*      */             mailId = paramRefGuildWarCenter.MailId;
/*      */           } else if (paramGuildwarGuildResultBO.getResult() == FightResult.Lost.ordinal()) {
/*      */             mailId = paramRefGuildWarCenter.FailMail;
/*      */           } 
/*      */           for (Long pid : paramGuild.getMembers()) {
/*      */             int puppet = 0;
/*      */             int kill = 0;
/*      */             try {
/*      */               if (paramReport.puppet.get(pid) != null) {
/*      */                 puppet = ((Integer)paramReport.puppet.get(pid)).intValue();
/*      */               }
/*  658 */             } catch (Exception exception) {}
/*      */             kill = getKillNum(paramInt, pid.longValue());
/*      */             MailCenter.getInstance().sendMail(pid.longValue(), mailId, new String[] { paramString1, paramString2, paramString3, EnemyGuild, paramString4, paramString5, (new StringBuilder(String.valueOf(puppet))).toString(), (new StringBuilder(String.valueOf(kill))).toString() });
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean atkResult(int centerId, Road road) {
/*  670 */     synchronized (this) {
/*  671 */       if (road.isOver) {
/*  672 */         return true;
/*      */       }
/*  674 */       int atknum = getleftAtk(centerId);
/*  675 */       int defnum = getleftDef(centerId);
/*  676 */       if (atknum == 0 || defnum == 0) {
/*  677 */         GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
/*  678 */         if (getAtkGuild(centerId) != null)
/*  679 */           bo.setAtkGuildId(getAtkGuild(centerId).getGuildId()); 
/*  680 */         if (this.guildWarCenterOwner.get(Integer.valueOf(centerId)) != null)
/*  681 */           bo.setDefGuildId(((Guild)this.guildWarCenterOwner.get(Integer.valueOf(centerId))).getGuildId()); 
/*  682 */         bo.setCenterId(centerId);
/*  683 */         bo.setFightTime(CommTime.nowSecond());
/*  684 */         if (atknum == 0) {
/*  685 */           bo.setResult(FightResult.Lost.ordinal());
/*      */         }
/*  687 */         if (defnum == 0) {
/*  688 */           bo.setResult(FightResult.Win.ordinal());
/*      */         }
/*  690 */         bo.insert();
/*      */ 
/*      */         
/*  693 */         this.guildAllResulet.put(Long.valueOf(bo.getId()), new ArrayList<>(this.fightResult.get(Integer.valueOf(centerId))));
/*      */         
/*  695 */         if (this.guildFightResult.get(Integer.valueOf(centerId)) == null) {
/*  696 */           List<GuildwarGuildResultBO> list = new ArrayList<>();
/*  697 */           list.add(bo);
/*  698 */           this.guildFightResult.put(Integer.valueOf(centerId), list);
/*      */         } else {
/*  700 */           ((List<GuildwarGuildResultBO>)this.guildFightResult.get(Integer.valueOf(centerId))).add(bo);
/*      */         } 
/*      */         
/*  703 */         Guild guild = ((List<Guild>)this.guildWarAtk.get(Integer.valueOf(centerId))).remove(0);
/*  704 */         if (defnum == 0) {
/*  705 */           this.guildWarCenterOwner.put(Integer.valueOf(centerId), guild);
/*      */         }
/*  707 */         dealWarFinish(centerId, bo);
/*  708 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  712 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean timeOver(int centerId, Road road) {
/*  717 */     synchronized (this) {
/*  718 */       if (road.isOver) {
/*  719 */         return true;
/*      */       }
/*      */       
/*  722 */       if (CommTime.nowSecond() - ((Integer)this.fightTime.get(Integer.valueOf(centerId))).intValue() > GuildWarConfig.fightTime() - GuildWarConfig.restTime()) {
/*  723 */         GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
/*  724 */         bo.setAtkGuildId(getAtkGuild(centerId).getGuildId());
/*  725 */         if (this.guildWarCenterOwner.get(Integer.valueOf(centerId)) != null)
/*  726 */           bo.setDefGuildId(((Guild)this.guildWarCenterOwner.get(Integer.valueOf(centerId))).getGuildId()); 
/*  727 */         bo.setCenterId(centerId);
/*  728 */         bo.setFightTime(CommTime.nowSecond());
/*  729 */         bo.setResult(FightResult.Lost.ordinal());
/*  730 */         bo.insert();
/*  731 */         if (this.guildFightResult.get(Integer.valueOf(centerId)) == null) {
/*  732 */           List<GuildwarGuildResultBO> list = new ArrayList<>();
/*  733 */           list.add(bo);
/*  734 */           this.guildFightResult.put(Integer.valueOf(centerId), list);
/*      */         } else {
/*  736 */           ((List<GuildwarGuildResultBO>)this.guildFightResult.get(Integer.valueOf(centerId))).add(bo);
/*      */         } 
/*  738 */         ((List)this.guildWarAtk.get(Integer.valueOf(centerId))).remove(0);
/*  739 */         dealWarFinish(centerId, bo);
/*  740 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  744 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean fight() {
/*  749 */     int guilds = 0;
/*  750 */     for (List<Guild> list : this.guildWarAtk.values()) {
/*  751 */       guilds += list.size();
/*      */     }
/*  753 */     if (guilds == 0) {
/*  754 */       return false;
/*      */     }
/*      */     
/*  757 */     prepareFight();
/*  758 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  759 */       if (!ref.isOpenTime()) {
/*      */         continue;
/*      */       }
/*  762 */       if (getAtkGuild(ref.id) == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  766 */       beginFightByCenterId(ref.id);
/*      */     } 
/*      */     
/*  769 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void Start() {
/*      */     try {
/*  775 */       fight();
/*  776 */       SyncTaskManager.schedule(GuildWarConfig.fightTime() * 1000, () -> fight());
/*      */     
/*      */     }
/*  779 */     catch (Exception e) {
/*      */       
/*  781 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void guildWarEvent() {
/*      */     try {
/*  788 */       checkTakeReward();
/*  789 */       Start();
/*  790 */     } catch (Exception e) {
/*      */       
/*  792 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkTakeReward() {
/*  797 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  798 */       if (!ref.isOpenTime()) {
/*      */         continue;
/*      */       }
/*  801 */       if (getAtkGuild(ref.id) != null) {
/*      */         continue;
/*      */       }
/*      */       
/*  805 */       Guild owner = this.guildWarCenterOwner.get(Integer.valueOf(ref.id));
/*  806 */       if (owner == null) {
/*      */         continue;
/*      */       }
/*      */       
/*  810 */       for (Long pid : owner.getMembers()) {
/*  811 */         MailCenter.getInstance().sendMail(pid.longValue(), ref.TakeMail, new String[0]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GuildWarCenterInfo> getCenterInfo() {
/*  818 */     List<GuildWarCenterInfo> listInfo = new ArrayList<>();
/*  819 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/*  820 */       listInfo.add(centerInfo(ref));
/*      */     }
/*  822 */     return listInfo;
/*      */   }
/*      */   
/*      */   public GuildWarCenterInfo centerInfo(RefGuildWarCenter ref) {
/*  826 */     int centerId = ref.id;
/*  827 */     GuildWarCenterInfo info = new GuildWarCenterInfo();
/*      */     
/*  829 */     info.setCenterId(centerId);
/*      */ 
/*      */     
/*  832 */     info.setOpen(ref.isOpenTime());
/*      */ 
/*      */     
/*  835 */     List<Guild.GuildSummary> list = new ArrayList<>();
/*  836 */     for (Guild guild : this.guildWarApplyer.get(Integer.valueOf(centerId))) {
/*  837 */       Guild.GuildSummary summary = new Guild.GuildSummary();
/*  838 */       summary.setGuildId(guild.getGuildId());
/*  839 */       summary.setGuildName(guild.getName());
/*  840 */       list.add(summary);
/*      */     } 
/*  842 */     info.setAtkGuild(list);
/*      */ 
/*      */     
/*  845 */     Guild guildowner = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/*      */     
/*  847 */     if (guildowner != null && guildowner.guildwarCenter != null && guildowner.guildwarCenter.getWinCenterId() == centerId) {
/*  848 */       info.setWinner(new Guild.GuildSummary(guildowner));
/*      */     }
/*      */     
/*  851 */     if (guildowner != null) {
/*  852 */       info.setOwner(new Guild.GuildSummary(guildowner));
/*      */     } else {
/*  854 */       Guild.GuildSummary summary = new Guild.GuildSummary();
/*  855 */       summary.setGuildName("神秘帮会");
/*  856 */       Player.showModle modle = new Player.showModle();
/*  857 */       modle.name = "神秘帮主";
/*  858 */       summary.setPresident(modle);
/*  859 */       info.setOwner(summary);
/*      */     } 
/*      */ 
/*      */     
/*  863 */     List<GuildWarCenterInfo.GuildwarGuildResultRecord> resultlist = new ArrayList<>();
/*  864 */     if (this.guildFightResult.get(Integer.valueOf(centerId)) != null) {
/*  865 */       for (GuildwarGuildResultBO bo : this.guildFightResult.get(Integer.valueOf(centerId))) {
/*  866 */         resultlist.add(new GuildWarCenterInfo.GuildwarGuildResultRecord(bo));
/*      */       }
/*      */     }
/*  869 */     info.setResult(resultlist);
/*      */ 
/*      */     
/*  872 */     info.setAtkingGuild(new Guild.GuildSummary(getAtkGuild(centerId)));
/*  873 */     return info;
/*      */   }
/*      */   
/*      */   public GuildWarFightProtol getFightInfo(int centerId, Player player) throws WSException {
/*  877 */     GuildWarFightProtol info = new GuildWarFightProtol();
/*  878 */     info.setCenterId(centerId);
/*  879 */     Guild guildatk = getAtkGuild(centerId);
/*  880 */     if (this.fighters.get(Integer.valueOf(centerId)) == null) {
/*  881 */       throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始或已结束");
/*      */     }
/*  883 */     if (guildatk != null) {
/*  884 */       info.setAtk(new Guild.GuildSummary(guildatk));
/*      */     } else {
/*  886 */       Guild.GuildSummary summary = new Guild.GuildSummary();
/*  887 */       summary.setGuildId(0L);
/*  888 */       summary.setGuildName("");
/*  889 */       info.setAtk(summary);
/*      */     } 
/*      */     
/*  892 */     Guild guilddef = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/*  893 */     if (guilddef != null) {
/*  894 */       info.setDef(new Guild.GuildSummary(guilddef));
/*      */     } else {
/*  896 */       Guild.GuildSummary summary = new Guild.GuildSummary();
/*  897 */       summary.setGuildId(-1L);
/*  898 */       summary.setGuildName("神秘帮会");
/*  899 */       info.setDef(summary);
/*      */     } 
/*  901 */     List<GuildWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
/*  902 */     guildInfo.add(new GuildWarFightProtol.GuildInfo(info.getAtk().getGuildName(), getleftAtk(centerId), 
/*  903 */           ((Center)this.fighters.get(Integer.valueOf(centerId))).atkplayers.size() - getleftAtk(centerId), getAtkPuppetNum(centerId)));
/*  904 */     guildInfo.add(new GuildWarFightProtol.GuildInfo(info.getDef().getGuildName(), getleftDef(centerId), 
/*  905 */           ((Center)this.fighters.get(Integer.valueOf(centerId))).defplayers.size() - getleftDef(centerId), getDefPuppetNum(centerId)));
/*  906 */     info.setGuildInfo(guildInfo);
/*  907 */     List<GuildWarFightProtol.ResultRecord> result = new ArrayList<>();
/*  908 */     for (GuildwarResultBO bo : this.fightResult.get(Integer.valueOf(centerId))) {
/*  909 */       result.add(new GuildWarFightProtol.ResultRecord(bo));
/*      */     }
/*  911 */     info.setResultInfo(result);
/*  912 */     info.setEndTime(nextRefreshTime());
/*  913 */     info.setKillnum(getKillNum(centerId, player.getPid()));
/*  914 */     List<GuildWarFightProtol.RoadSummry> summarylist = new ArrayList<>();
/*  915 */     for (Road road : this.guildWarRoad.get(Integer.valueOf(centerId))) {
/*  916 */       summarylist.add(new GuildWarFightProtol.RoadSummry(road));
/*      */     }
/*  918 */     info.setRoad(summarylist);
/*  919 */     for (Road road : this.guildWarRoad.get(Integer.valueOf(centerId))) {
/*  920 */       if (road.getDeadatkplayers().contains(player) || road.getDeaddefplayers().contains(player)) {
/*  921 */         info.setDead(true);
/*      */         break;
/*      */       } 
/*      */     } 
/*  925 */     info.setRebirthTime(((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
/*  926 */     List<Player> listplayer = this.watchPlayers.get(Integer.valueOf(centerId));
/*  927 */     if (!listplayer.contains(player)) {
/*  928 */       listplayer.add(player);
/*      */     }
/*      */     
/*  931 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextRefreshTime() {
/*  940 */     int time = CommTime.getTodayZeroClockS();
/*  941 */     time += 68400;
/*      */     while (true) {
/*  943 */       time += GuildWarConfig.fightTime();
/*  944 */       if (time > CommTime.nowSecond()) {
/*  945 */         return time - GuildWarConfig.restTime();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getleftAtk(int centerId) {
/*  951 */     int num = 0;
/*  952 */     for (int i = 0; i < this.road; i++) {
/*  953 */       Road road = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  954 */       num += road.getAtkplayers().size();
/*      */     } 
/*  956 */     return num;
/*      */   }
/*      */   
/*      */   public int getleftDef(int centerId) {
/*  960 */     int num = 0;
/*  961 */     for (int i = 0; i < this.road; i++) {
/*  962 */       Road road = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  963 */       num += road.getDefplayers().size();
/*      */     } 
/*  965 */     return num;
/*      */   }
/*      */   
/*      */   public int getdeadAtk(int centerId) {
/*  969 */     int num = 0;
/*  970 */     for (int i = 0; i < this.road; i++) {
/*  971 */       Road road = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  972 */       num += road.getDeadatkplayers().size();
/*      */     } 
/*  974 */     return num;
/*      */   }
/*      */   
/*      */   public int getdeadDef(int centerId) {
/*  978 */     int num = 0;
/*  979 */     for (int i = 0; i < this.road; i++) {
/*  980 */       Road road = ((List<Road>)this.guildWarRoad.get(Integer.valueOf(centerId))).get(i);
/*  981 */       num += road.getDeaddefplayers().size();
/*      */     } 
/*  983 */     return num;
/*      */   }
/*      */   
/*      */   public int getKillNum(int centerId, long pid) {
/*  987 */     int num = 0;
/*  988 */     List<GuildwarResultBO> list = this.fightResult.get(Integer.valueOf(centerId));
/*  989 */     for (GuildwarResultBO bo : list) {
/*  990 */       if (bo.getAtkpid() == pid && bo.getResult() == FightResult.Win.ordinal()) {
/*  991 */         num++;
/*      */       }
/*      */     } 
/*  994 */     return num;
/*      */   }
/*      */   
/*      */   public int getAtkPuppetNum(int centerId) {
/*  998 */     int num = 0;
/*  999 */     for (Player player : ((Center)this.fighters.get(Integer.valueOf(centerId))).atkplayers) {
/* 1000 */       if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
/* 1001 */         num++;
/*      */       }
/*      */     } 
/* 1004 */     return num;
/*      */   }
/*      */   
/*      */   public int getDefPuppetNum(int centerId) {
/* 1008 */     int num = 0;
/* 1009 */     for (Player player : ((Center)this.fighters.get(Integer.valueOf(centerId))).defplayers) {
/* 1010 */       if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
/* 1011 */         num++;
/*      */       }
/*      */     } 
/* 1014 */     return num;
/*      */   }
/*      */   
/*      */   public void checkReward(int centerId) {
/* 1018 */     Guild guild = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/* 1019 */     if (guild != null && ((List)this.guildWarAtk.get(Integer.valueOf(centerId))).size() == 0) {
/* 1020 */       if (guild.guildwarCenter.getWinCenterId() != 0) {
/*      */         return;
/*      */       }
/* 1023 */       RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
/* 1024 */       for (Long pid : guild.getMembers()) {
/* 1025 */         MailCenter.getInstance().sendMail(pid.longValue(), ref.TakeMail, new String[0]);
/*      */       }
/* 1027 */       guild.guildwarCenter.saveWinCenterId(centerId);
/* 1028 */       this.historyWinner.put(Integer.valueOf(centerId), guild);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void personReward(List<GuildwarResultBO> list, int centerId) {
/* 1034 */     List<Road> roads = this.guildWarRoad.get(Integer.valueOf(centerId));
/* 1035 */     int atkwin = 0;
/* 1036 */     int defwin = 0;
/* 1037 */     for (Road road : roads) {
/* 1038 */       if (road.getWinner() == 0L)
/*      */         continue; 
/* 1040 */       if (road.getAtkplayers().size() != 0) {
/* 1041 */         atkwin++; continue;
/* 1042 */       }  if (road.getDefplayers().size() != 0) {
/* 1043 */         defwin++;
/*      */       }
/*      */     } 
/*      */     
/* 1047 */     Map<Long, List<GuildwarResultBO>> map = Maps.newConcurrentHashMap();
/* 1048 */     for (GuildwarResultBO bo : list) {
/* 1049 */       if (bo.getResult() == FightResult.Win.ordinal()) {
/* 1050 */         if (map.get(Long.valueOf(bo.getAtkpid())) != null) {
/* 1051 */           ((List<GuildwarResultBO>)map.get(Long.valueOf(bo.getAtkpid()))).add(bo); continue;
/*      */         } 
/* 1053 */         List<GuildwarResultBO> tmp_list = new ArrayList<>();
/* 1054 */         tmp_list.add(bo);
/* 1055 */         map.put(Long.valueOf(bo.getAtkpid()), tmp_list);
/*      */       } 
/*      */     } 
/*      */     
/* 1059 */     for (Map.Entry<Long, List<GuildwarResultBO>> entry : map.entrySet()) {
/* 1060 */       int num = 1;
/* 1061 */       Player player = PlayerMgr.getInstance().getPlayer(((Long)entry.getKey()).longValue());
/* 1062 */       if (((Center)this.fighters.get(Integer.valueOf(centerId))).atkplayers.contains(player)) {
/* 1063 */         num = Math.max(num, atkwin);
/* 1064 */       } else if (((Center)this.fighters.get(Integer.valueOf(centerId))).defplayers.contains(player)) {
/* 1065 */         num = Math.max(num, defwin);
/*      */       } 
/* 1067 */       Reward rewardall = new Reward();
/* 1068 */       Reward reward = RefGuildWarPersonReward.getReward(((List)entry.getValue()).size());
/* 1069 */       for (int i = 0; i < num; i++) {
/* 1070 */         rewardall.combine(reward);
/*      */       }
/* 1072 */       MailCenter.getInstance().sendMail(player.getPid(), "GM", "帮派战个人奖励", "在帮派战中你奋勇杀敌，获得以下奖励", rewardall, new String[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public GuildWarFightProtol rebirth(Player player, int centerId) throws WSException {
/* 1078 */     Center center = this.fighters.get(Integer.valueOf(centerId));
/*      */     
/* 1080 */     if (center == null) {
/* 1081 */       throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
/*      */     }
/*      */     
/* 1084 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 1085 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*      */     
/* 1087 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth);
/* 1088 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
/* 1089 */     if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
/* 1090 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", new Object[] { Integer.valueOf(prize.GuildWarRebirth) });
/*      */     }
/* 1092 */     currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
/* 1093 */     recorder.addValue(ConstEnum.DailyRefresh.GuildWarRebirth);
/* 1094 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 1095 */     GuildwarpuppetBO bo = new GuildwarpuppetBO();
/* 1096 */     bo.setPid(player.getPid());
/* 1097 */     bo.setGuildId(guild.getGuildId());
/* 1098 */     bo.savePuppetId(curTimes);
/* 1099 */     bo.setApplyTime(CommTime.nowSecond());
/* 1100 */     bo.insert();
/* 1101 */     if (guild.puppets.get(Long.valueOf(player.getPid())) != null) {
/* 1102 */       ((List<GuildwarpuppetBO>)guild.puppets.get(Long.valueOf(player.getPid()))).add(bo);
/*      */     } else {
/* 1104 */       List<GuildwarpuppetBO> list1 = new ArrayList<>();
/* 1105 */       list1.add(bo);
/* 1106 */       guild.puppets.put(Long.valueOf(player.getPid()), list1);
/*      */     } 
/*      */ 
/*      */     
/* 1110 */     Report report = (Report)((Map)this.reports.get(Integer.valueOf(centerId))).get(Long.valueOf(guild.getGuildId()));
/* 1111 */     report.total++;
/* 1112 */     int num = 0;
/* 1113 */     if (report.puppet.get(Long.valueOf(player.getPid())) != null) {
/* 1114 */       num = ((Integer)report.puppet.get(Long.valueOf(player.getPid()))).intValue();
/*      */     }
/* 1116 */     num++;
/* 1117 */     report.puppet.put(Long.valueOf(player.getPid()), Integer.valueOf(num));
/*      */     
/* 1119 */     List<Road> list = this.guildWarRoad.get(Integer.valueOf(centerId));
/* 1120 */     List<Road> tmp_list = new ArrayList<>(list);
/* 1121 */     Collections.shuffle(tmp_list);
/* 1122 */     if (center.defplayers.contains(player)) {
/* 1123 */       Optional<Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
/* 1124 */       if (find.isPresent()) {
/* 1125 */         GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
/* 1126 */         p_player.setPuppet_id(curTimes);
/* 1127 */         p_player.setIs_puppet(true);
/* 1128 */         ((Road)find.get()).getDefplayers().add(p_player);
/*      */       } 
/*      */     } 
/* 1131 */     if (center.atkplayers.contains(player)) {
/* 1132 */       Optional<Road> find = tmp_list.stream().filter(x -> (x.getWinner() == 0L)).findAny();
/* 1133 */       if (find.isPresent()) {
/* 1134 */         GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
/* 1135 */         p_player.setPuppet_id(curTimes);
/* 1136 */         p_player.setIs_puppet(true);
/* 1137 */         ((Road)find.get()).getAtkplayers().add(p_player);
/*      */       } 
/*      */     } 
/*      */     
/* 1141 */     SyncTaskManager.task(() -> {
/*      */           for (Player player_tmp : this.watchPlayers.get(Integer.valueOf(paramInt))) {
/*      */             if (player_tmp == paramPlayer)
/*      */               continue; 
/*      */             try {
/*      */               player_tmp.pushProto("guildwarRebirth", getFightInfo(paramInt, player_tmp));
/* 1147 */             } catch (WSException e) {
/*      */               e.printStackTrace();
/*      */             } 
/*      */           } 
/*      */         });
/*      */ 
/*      */     
/* 1154 */     return getFightInfo(centerId, player);
/*      */   }
/*      */ 
/*      */   
/*      */   public int isFighting(Guild guild) {
/* 1159 */     for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
/* 1160 */       int centerId = 0;
/* 1161 */       if (this.fighters.get(Integer.valueOf(ref.id)) != null) {
/* 1162 */         centerId = ref.id;
/*      */       }
/* 1164 */       if (centerId == 0) {
/*      */         continue;
/*      */       }
/* 1167 */       Guild Def = this.guildWarCenterOwner.get(Integer.valueOf(centerId));
/* 1168 */       Guild Atk = getAtkGuild(centerId);
/*      */       
/* 1170 */       if (Def != null && Def == guild) {
/* 1171 */         return centerId;
/*      */       }
/* 1173 */       if (Atk != null && Atk == guild) {
/* 1174 */         return centerId;
/*      */       }
/*      */     } 
/*      */     
/* 1178 */     return 0;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/GuildWarMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */