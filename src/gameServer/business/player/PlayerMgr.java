/*     */ package business.player;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTask;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import BaseThread.BaseMutexObject;
/*     */ import business.player.feature.AccountFeature;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.common.utils.Random;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefPlayerInit;
/*     */ import core.config.refdata.ref.RefPlayerName;
/*     */ import core.config.refdata.ref.RefRobot;
/*     */ import core.database.game.bo.MarryBO;
/*     */ import core.database.game.bo.PlayerBO;
/*     */ import core.network.client2game.ClientSession;
/*     */ import core.network.game2zone.ZoneConnector;
/*     */ import core.server.ServerConfig;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import proto.gamezone.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerMgr
/*     */ {
/*  46 */   private final String EmptyPlayerPid = "";
/*     */   
/*  48 */   public final Map<Long, Player> _onlinePlayersByCid = Maps.newConcurrentMap();
/*     */   
/*  50 */   public final Map<Long, Player> pidDatas = Maps.newConcurrentMap();
/*     */   
/*  52 */   public final Map<String, Map<Integer, Player>> openidSidDatas = Maps.newConcurrentMap();
/*     */   
/*  54 */   public final Map<String, Player> nameDatas = Maps.newConcurrentMap();
/*     */   
/*  56 */   public final Map<String, JsonObject> playerJsons = Maps.newConcurrentMap();
/*     */ 
/*     */   
/*  59 */   public final Map<String, Player> sexPlayers = Maps.newConcurrentMap();
/*     */ 
/*     */   
/*  62 */   private final BaseMutexObject _lock = new BaseMutexObject();
/*     */   
/*     */   public void lock() {
/*  65 */     this._lock.lock();
/*     */   }
/*     */   
/*     */   public void unlock() {
/*  69 */     this._lock.unlock();
/*     */   }
/*     */   
/*  72 */   private static PlayerMgr instance = new PlayerMgr(); private BlockingQueue<Player> toNotifyWorld;
/*     */   
/*     */   public static PlayerMgr getInstance() {
/*  75 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  83 */     CommLog.info("[PlayerMgr.init] load player begin...]");
/*     */     
/*  85 */     lock();
/*  86 */     List<PlayerBO> allPlayers = BM.getBM(PlayerBO.class).findAll();
/*  87 */     for (PlayerBO bo : allPlayers) {
/*  88 */       if (!bo.getName().matches("s\\d+\\..+")) {
/*  89 */         bo.saveName("s" + bo.getSid() + "." + bo.getName());
/*     */       }
/*  91 */       regPlayer(new Player(bo));
/*     */     } 
/*  93 */     unlock();
/*  94 */     CommLog.info("[PlayerMgr.init] load player success, count: {}", Integer.valueOf(this.pidDatas.size()));
/*     */ 
/*     */     
/*  97 */     List<MarryBO> allMarry = BM.getBM(MarryBO.class).findAll();
/*  98 */     for (MarryBO bo : allMarry) {
/*  99 */       if (bo.getSex() != 0) {
/* 100 */         Player player = getPlayer(bo.getPid());
/* 101 */         this.sexPlayers.put(player.getName(), player);
/*     */       } 
/*     */     } 
/* 104 */     notifyAll2World();
/* 105 */     startSync2Zone();
/*     */   }
/*     */ 
/*     */   
/*     */   private void regPlayer(Player player) {
/* 110 */     lock();
/* 111 */     long playerCid = player.getPlayerBO().getId();
/* 112 */     this.pidDatas.put(Long.valueOf(playerCid), player);
/* 113 */     this.nameDatas.put(player.getName(), player);
/* 114 */     if (!player.getOpenId().isEmpty()) {
/* 115 */       Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
/* 116 */       if (sidDatas == null) {
/* 117 */         sidDatas = Maps.newConcurrentMap();
/* 118 */         this.openidSidDatas.put(player.getOpenId(), sidDatas);
/*     */       } 
/* 120 */       sidDatas.put(Integer.valueOf(player.getPlayerBO().getSid()), player);
/*     */     } 
/* 122 */     unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   private void unregPlayer(Player player) {
/* 127 */     lock();
/* 128 */     long playerCid = player.getPlayerBO().getId();
/* 129 */     this.pidDatas.remove(Long.valueOf(playerCid));
/* 130 */     this.nameDatas.remove(player.getPlayerBO().getName());
/* 131 */     this._onlinePlayersByCid.remove(Long.valueOf(playerCid));
/* 132 */     if (!player.getOpenId().isEmpty()) {
/* 133 */       Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
/* 134 */       if (sidDatas != null) {
/* 135 */         sidDatas.remove(Integer.valueOf(player.getPlayerBO().getSid()));
/*     */       }
/*     */     } 
/* 138 */     unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void regOnlinePlayer(Player player) {
/* 143 */     lock();
/* 144 */     this._onlinePlayersByCid.put(Long.valueOf(player.getPid()), player);
/* 145 */     unlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregOnlinePlayer(Player player) {
/* 150 */     lock();
/* 151 */     this._onlinePlayersByCid.remove(Long.valueOf(player.getPid()));
/* 152 */     unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player getOnlinePlayerByCid(long cid) {
/* 162 */     return this._onlinePlayersByCid.get(Long.valueOf(cid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player getPlayer(long pid) {
/* 172 */     return this.pidDatas.get(Long.valueOf(pid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player getPlayer(String openid, int sid) {
/* 183 */     if (openid == null || openid.length() <= 0) {
/* 184 */       return null;
/*     */     }
/* 186 */     if ("LEGEND_ROBOT".equals(openid)) {
/* 187 */       return null;
/*     */     }
/* 189 */     Map<Integer, Player> sidDatas = this.openidSidDatas.get(openid);
/* 190 */     if (sidDatas != null) {
/* 191 */       return sidDatas.get(Integer.valueOf(sid));
/*     */     }
/* 193 */     return null;
/*     */   }
/*     */   
/*     */   public List<Player> searchPlayer(String openid, int serverid) {
/* 197 */     List<Player> rtn = new ArrayList<>();
/* 198 */     if (openid == null || openid.length() <= 0) {
/* 199 */       return rtn;
/*     */     }
/* 201 */     List<String> names = new ArrayList<>(this.nameDatas.keySet());
/* 202 */     for (String name : names) {
/* 203 */       if (name.indexOf(openid) != -1) {
/* 204 */         rtn.add(this.nameDatas.get(name));
/*     */       }
/*     */     } 
/* 207 */     return rtn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player getPlayer(String name) {
/* 218 */     return this.nameDatas.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Player> getAllPlayers() {
/* 227 */     return Lists.newArrayList(this.pidDatas.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Player> getOnlinePlayers() {
/* 236 */     return Lists.newArrayList(this._onlinePlayersByCid.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Long> getOnlinePlayersCid() {
/* 245 */     return Lists.newArrayList(this._onlinePlayersByCid.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player createPlayer(PlayerBO newBO) {
/* 255 */     Player ret = null;
/* 256 */     lock();
/*     */     
/* 258 */     if (!this.nameDatas.containsKey(newBO.getName())) {
/*     */ 
/*     */       
/* 261 */       Player player = getPlayer(newBO.getOpenId(), newBO.getSid());
/* 262 */       if (player == null)
/*     */         
/*     */         try {
/*     */           
/* 266 */           boolean suc = newBO.insert_sync();
/* 267 */           if (suc) {
/* 268 */             ret = new Player(newBO);
/* 269 */             regPlayer(ret);
/*     */           } 
/* 271 */         } catch (Exception e) {
/* 272 */           CommLog.error("数据库中已经存在名为{}的玩家", newBO.getName());
/*     */         }  
/*     */     } 
/* 275 */     unlock();
/*     */     
/* 277 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delPlayer(long cid) {
/* 286 */     lock();
/*     */     
/* 288 */     Player player = getPlayer(cid);
/* 289 */     if (player == null) {
/* 290 */       unlock();
/*     */       
/*     */       return;
/*     */     } 
/* 294 */     if (player.getOpenId().isEmpty()) {
/* 295 */       Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
/* 296 */       if (sidDatas != null) {
/* 297 */         sidDatas.remove(Integer.valueOf(player.getPlayerBO().getSid()));
/*     */       }
/* 299 */       player.getPlayerBO().saveOpenId("");
/*     */     } 
/*     */     
/* 302 */     unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void linkPlayer(String openId, int serverid, long pid) {
/* 313 */     Player player = getPlayer(pid);
/* 314 */     if (player == null) {
/*     */       return;
/*     */     }
/* 317 */     lock();
/* 318 */     unregPlayer(player);
/* 319 */     player.getPlayerBO().saveOpenId(openId);
/* 320 */     player.getPlayerBO().saveSid(serverid);
/* 321 */     regPlayer(player);
/* 322 */     unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean connectPlayer(ClientSession session, Player player) {
/* 333 */     ClientSession oldSession = player.getClientSession();
/*     */     
/* 335 */     if (oldSession == session) {
/* 336 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 340 */     if (oldSession != null)
/*     */     {
/* 342 */       oldSession.losePlayer();
/*     */     }
/*     */     
/* 345 */     Player oldPlayer = session.getPlayer();
/* 346 */     if (oldPlayer != null)
/*     */     {
/* 348 */       oldPlayer.loseSession();
/*     */     }
/*     */ 
/*     */     
/* 352 */     session.bindPlayer(player);
/* 353 */     player.bindSession(session);
/* 354 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPlayerRename(long cid, String oldName, String newName) {
/* 365 */     lock();
/*     */     
/* 367 */     Player data = getPlayer(cid);
/* 368 */     if (data != null) {
/* 369 */       this.nameDatas.remove(oldName);
/* 370 */       this.nameDatas.put(newName, data);
/*     */     } 
/*     */     
/* 373 */     unlock();
/*     */   }
/*     */   
/*     */   public void cachePlayerJson(String openid, JsonObject player_info) {
/* 377 */     this.playerJsons.put(openid, player_info);
/*     */   }
/*     */   
/*     */   public JsonObject getPlayerJson(String openid) {
/* 381 */     return this.playerJsons.get(openid);
/*     */   }
/*     */   
/*     */   public void removePlayerJson(long pid) {
/* 385 */     this.playerJsons.remove(Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String output() {
/* 389 */     StringBuilder sBuilder = new StringBuilder();
/* 390 */     lock();
/*     */     
/* 392 */     sBuilder.append(String.format("online:%s, all:%s\n", new Object[] { Integer.valueOf(this._onlinePlayersByCid.size()), Integer.valueOf(this.pidDatas.size()) }));
/* 393 */     sBuilder.append(String.format("%20s %10s %10s %10s %10s %10s\n", new Object[] { "cid", "sid", "pid", "isOnline", "lv", "name" }));
/* 394 */     for (Player player : this.pidDatas.values()) {
/* 395 */       sBuilder.append(String.format("%20s %10s %10s %10s %10s %10s\n", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(player.getSid()), player.getOpenId(), Boolean.valueOf(player.isOnline()), 
/* 396 */               Integer.valueOf(player.getLv()), player.getName() }));
/*     */     } 
/* 398 */     sBuilder.append(String.format("online:%s, all:%s\n", new Object[] { Integer.valueOf(this._onlinePlayersByCid.size()), Integer.valueOf(this.pidDatas.size()) }));
/*     */     
/* 400 */     unlock();
/* 401 */     return sBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRandomName() {
/* 410 */     List<RefPlayerName> rolenameList = new ArrayList<>(RefDataMgr.getAll(RefPlayerName.class).values());
/* 411 */     int canSeek = 10000;
/*     */     while (true) {
/* 413 */       int preIdx = Random.nextInt(rolenameList.size());
/* 414 */       int midIdx = Random.nextInt(rolenameList.size());
/* 415 */       int suffIdx = Random.nextInt(rolenameList.size());
/* 416 */       String str = String.valueOf(((RefPlayerName)rolenameList.get(preIdx)).PrefixName) + ((RefPlayerName)rolenameList.get(midIdx)).MidName + ((RefPlayerName)rolenameList.get(suffIdx)).SuffixName;
/* 417 */       if (!this.nameDatas.containsKey(str)) {
/* 418 */         return str;
/*     */       }
/* 420 */       if (canSeek-- <= 0) {
/* 421 */         return str;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Player createRobot(RefRobot init) {
/* 433 */     String roleName = getRandomName();
/* 434 */     lock();
/*     */     try {
/* 436 */       PlayerBO bo = new PlayerBO();
/* 437 */       bo.setName(roleName);
/* 438 */       bo.setOpenId("LEGEND_ROBOT");
/* 439 */       bo.setSid(ServerConfig.ServerID());
/* 440 */       bo.setLv(init.Level);
/* 441 */       bo.setDungeonLv(Math.min(init.DungeonLevel, 1));
/* 442 */       bo.setVipLevel(init.VIP);
/* 443 */       Player player = createPlayer(bo);
/* 444 */       if (player == null) {
/* 445 */         return null;
/*     */       }
/* 447 */       RobotManager.getInstance().setRobot(player.getPid(), player);
/* 448 */       ((PlayerBase)player.<PlayerBase>getFeature(PlayerBase.class)).onCreate(init);
/* 449 */       return player;
/* 450 */     } catch (Exception e) {
/* 451 */       CommLog.error("创建角色失败出现异常", e);
/* 452 */       return null;
/*     */     } finally {
/* 454 */       unlock();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorCode createRole(Player player, String name, int selectedId) {
/* 469 */     if (player == null) {
/* 470 */       return ErrorCode.Player_NotFound;
/*     */     }
/* 472 */     lock();
/*     */     
/* 474 */     try { if (this.nameDatas.containsKey(name)) {
/* 475 */         return ErrorCode.Player_AlreadyExist;
/*     */       }
/* 477 */       player.getPlayerBO().saveName(name);
/* 478 */       player.getPlayerBO().saveIcon(selectedId);
/* 479 */       this.nameDatas.put(player.getName(), player); }
/*     */     
/* 481 */     catch (Exception e)
/* 482 */     { CommLog.error("创建角色失败出现异常", e);
/* 483 */       return ErrorCode.Unknown; }
/*     */     finally
/* 485 */     { unlock(); }  unlock();
/*     */     
/* 487 */     return ErrorCode.Success;
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
/*     */ 
/*     */   
/*     */   public ErrorCode createPlayer(ClientSession session, String openid, int sid, String name, int selectedId) {
/* 501 */     Player player = null;
/* 502 */     lock();
/*     */     
/* 504 */     try { PlayerBO bo = new PlayerBO();
/* 505 */       bo.setOpenId(openid);
/* 506 */       bo.setSid(sid);
/* 507 */       bo.setName(name);
/* 508 */       bo.setIcon(selectedId);
/* 509 */       bo.setCreateTime(CommTime.nowSecond());
/* 510 */       initPlayerBO(bo, 0);
/* 511 */       player = createPlayer(bo);
/* 512 */       if (player == null) {
/* 513 */         return ErrorCode.Player_AlreadyExist;
/*     */       } }
/*     */     
/* 516 */     catch (Exception e)
/* 517 */     { CommLog.error("创建角色失败出现异常", e);
/* 518 */       return ErrorCode.Unknown; }
/*     */     finally
/* 520 */     { unlock(); }  unlock();
/*     */     
/* 522 */     connectPlayer(session, player);
/* 523 */     ((AccountFeature)player.<AccountFeature>getFeature(AccountFeature.class)).updateCreateRole();
/* 524 */     return ErrorCode.Success;
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
/*     */ 
/*     */   
/*     */   public ErrorCode createPlayerFirst(ClientSession session, String openid, int sid) {
/* 538 */     Player player = null;
/* 539 */     lock();
/*     */     try {
/* 541 */       PlayerBO bo = new PlayerBO();
/* 542 */       bo.setOpenId(openid);
/* 543 */       bo.setSid(sid);
/* 544 */       bo.setName("大侠小虾米" + openid);
/* 545 */       bo.setCreateTime(CommTime.nowSecond());
/* 546 */       initPlayerBO(bo, 0);
/* 547 */       player = createPlayer(bo);
/* 548 */       if (player == null) {
/* 549 */         return ErrorCode.Player_AlreadyExist;
/*     */       }
/* 551 */     } catch (Exception e) {
/* 552 */       CommLog.error("创建角色失败出现异常", e);
/* 553 */       return ErrorCode.Unknown;
/*     */     } finally {
/* 555 */       unlock();
/*     */     } 
/* 557 */     connectPlayer(session, player);
/* 558 */     ((AccountFeature)player.<AccountFeature>getFeature(AccountFeature.class)).updateCreateRole();
/* 559 */     return ErrorCode.Success;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initPlayerBO(PlayerBO bo, int initType) {
/* 569 */     RefPlayerInit playerInit = (RefPlayerInit)RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(initType));
/* 570 */     if (playerInit == null) {
/* 571 */       playerInit = (RefPlayerInit)RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(0));
/*     */     }
/* 573 */     bo.setDungeonLv(playerInit.DungeonLevel);
/* 574 */     bo.setLv(playerInit.Level);
/* 575 */     bo.setExp(playerInit.Exp);
/* 576 */     bo.setCrystal(playerInit.Crystal);
/* 577 */     bo.setGold(playerInit.Gold);
/* 578 */     bo.setVipLevel(playerInit.VipLevel);
/* 579 */     bo.setGmLevel(playerInit.GmLevel);
/* 580 */     bo.setWarspiritLv(playerInit.WarspiritLv);
/*     */   }
/*     */   
/*     */   public void notifyAll2World() {
/* 584 */     if (!"on".equalsIgnoreCase(System.getProperty("GameServer.ZoneOpen"))) {
/*     */       return;
/*     */     }
/* 587 */     List<Player> playerList = new ArrayList<>(this.pidDatas.values());
/* 588 */     for (Player t : playerList)
/* 589 */       tryNotify(t); 
/*     */   }
/*     */   
/*     */   private PlayerMgr() {
/* 593 */     this.toNotifyWorld = new LinkedBlockingQueue<>();
/*     */     this._lock.reduceMutexLevel(1);
/*     */   } public void tryNotify(Player data) {
/* 596 */     int levelLimit = RefDataMgr.getFactor("ZoneServer_LevelLimit", 30);
/* 597 */     if (data.getLv() < levelLimit) {
/*     */       return;
/*     */     }
/* 600 */     if (!this.toNotifyWorld.contains(data)) {
/*     */       try {
/* 602 */         this.toNotifyWorld.put(data);
/* 603 */       } catch (InterruptedException e) {
/* 604 */         CommLog.error(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void tryNotifyNoLimit(Player data) {
/* 610 */     if (!this.toNotifyWorld.contains(data)) {
/*     */       try {
/* 612 */         this.toNotifyWorld.put(data);
/* 613 */       } catch (InterruptedException e) {
/* 614 */         CommLog.error(e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startSync2Zone() {
/* 623 */     SyncTaskManager.task(new SyncTask()
/*     */         {
/*     */           public void run() {
/* 626 */             if (!ZoneConnector.getInstance().isLogined()) {
/* 627 */               SyncTaskManager.task(this, 2000);
/*     */               return;
/*     */             } 
/* 630 */             List<Player.PlayerInfo> sendList = new ArrayList<>();
/*     */             
/* 632 */             for (int index = 1; index <= 100 && 
/* 633 */               PlayerMgr.this.toNotifyWorld.size() > 0; index++) {
/*     */ 
/*     */               
/*     */               try {
/*     */                 
/* 638 */                 Player data = PlayerMgr.this.toNotifyWorld.take();
/*     */                 
/* 640 */                 if (data.getPid() != -1L) {
/* 641 */                   sendList.add(((PlayerBase)data.<PlayerBase>getFeature(PlayerBase.class)).zoneProto());
/*     */                 }
/* 643 */               } catch (InterruptedException e) {
/* 644 */                 CommLog.error(e.getMessage(), e);
/*     */               } 
/*     */             } 
/* 647 */             if (sendList.size() > 0) {
/* 648 */               ZoneConnector.getInstance().notifyMessage("base.playerinfo", new Player.G_PlayerInfo(sendList));
/*     */             }
/* 650 */             int todeal = PlayerMgr.this.toNotifyWorld.size();
/* 651 */             if (todeal > 0) {
/* 652 */               SyncTaskManager.task(this);
/*     */             } else {
/* 654 */               SyncTaskManager.task(this, 5000);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releasPlayer(int activeTime) {
/* 666 */     for (Player player : getAllPlayers()) {
/* 667 */       player.releaseFeature(activeTime);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> randomLoadOnlinePlayersSync(int count, Collection<Long> exceptCids) {
/* 673 */     List<Player> randomOnlinePlayers = Lists.newArrayList();
/* 674 */     for (Player player : this.pidDatas.values()) {
/* 675 */       if (exceptCids.contains(Long.valueOf(player.getPid()))) {
/*     */         continue;
/*     */       }
/* 678 */       randomOnlinePlayers.add(player);
/*     */     } 
/* 680 */     if (randomOnlinePlayers.size() <= count) {
/* 681 */       return randomOnlinePlayers;
/*     */     }
/* 683 */     List<Player> listPlayers = Lists.newArrayList();
/* 684 */     int randIndex = CommMath.randomInt(randomOnlinePlayers.size() - count);
/* 685 */     for (int i = randIndex; i < randomOnlinePlayers.size(); i++) {
/* 686 */       Player player = randomOnlinePlayers.get(i);
/* 687 */       listPlayers.add(player);
/* 688 */       if (listPlayers.size() == count) {
/*     */         break;
/*     */       }
/*     */     } 
/* 692 */     return listPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Player> randomLoadSexPlayers(int sex, int count, Collection<Long> exceptCids) {
/* 697 */     List<Player> randomOnlinePlayers = Lists.newArrayList();
/* 698 */     for (Player player : this.sexPlayers.values()) {
/*     */       
/* 700 */       if (exceptCids.contains(Long.valueOf(player.getPid()))) {
/*     */         continue;
/*     */       }
/*     */       
/* 704 */       if (((MarryFeature)player.<MarryFeature>getFeature(MarryFeature.class)).getSex() == sex) {
/*     */         continue;
/*     */       }
/*     */       
/* 708 */       if (((MarryFeature)player.<MarryFeature>getFeature(MarryFeature.class)).isMarried()) {
/*     */         continue;
/*     */       }
/*     */       
/* 712 */       randomOnlinePlayers.add(player);
/*     */     } 
/* 714 */     if (randomOnlinePlayers.size() <= count) {
/* 715 */       return randomOnlinePlayers;
/*     */     }
/* 717 */     List<Player> listPlayers = Lists.newArrayList();
/* 718 */     Collections.shuffle(randomOnlinePlayers);
/* 719 */     for (int i = 0; i < randomOnlinePlayers.size(); i++) {
/* 720 */       Player player = randomOnlinePlayers.get(i);
/* 721 */       listPlayers.add(player);
/* 722 */       if (listPlayers.size() == count) {
/*     */         break;
/*     */       }
/*     */     } 
/* 726 */     return listPlayers;
/*     */   }
/*     */ 
/*     */   
/*     */   public void kickoutAllPlayer() {
/* 731 */     for (Player player : getInstance().getOnlinePlayers()) {
/* 732 */       player.pushProto("kickout", "");
/* 733 */       player.loseSession();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/PlayerMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */