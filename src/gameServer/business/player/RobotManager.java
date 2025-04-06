/*     */ package business.player;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseThread.BaseMutexObject;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRobot;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class RobotManager
/*     */ {
/*  30 */   private final Map<Long, Player> pidDatas = Maps.newConcurrentMap();
/*     */   
/*  32 */   private final Map<Integer, List<Player>> lvCidDatas = Maps.newConcurrentMap();
/*     */   
/*  34 */   private final BaseMutexObject _lock = new BaseMutexObject();
/*     */   
/*  36 */   private static RobotManager instance = new RobotManager();
/*     */   
/*     */   public static RobotManager getInstance() {
/*  39 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lock() {
/*  46 */     this._lock.lock();
/*     */   }
/*     */   
/*     */   public void unlock() {
/*  50 */     this._lock.unlock();
/*     */   }
/*     */   
/*     */   public void init() {
/*  54 */     reload();
/*     */   }
/*     */   
/*     */   public Map<Long, Player> getAll() {
/*  58 */     return this.pidDatas;
/*     */   }
/*     */   
/*     */   public Player getRandomPlayer() {
/*  62 */     List<Player> players = new ArrayList<>();
/*  63 */     players.addAll(this.pidDatas.values());
/*  64 */     Player player = (Player)CommMath.randomOne(players);
/*  65 */     return player;
/*     */   }
/*     */   
/*     */   public List<Player> getRandomPlayers(int size) {
/*  69 */     List<Player> players = new ArrayList<>();
/*  70 */     players.addAll(this.pidDatas.values());
/*  71 */     List<Player> player = CommMath.getRandomListByCnt(players, size);
/*  72 */     return player;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reload() {
/*  77 */     this.lvCidDatas.clear();
/*  78 */     this.pidDatas.clear();
/*     */ 
/*     */     
/*  81 */     for (Player player : PlayerMgr.getInstance().getAllPlayers()) {
/*  82 */       if (!player.isVirtualPlayer())
/*     */         continue; 
/*  84 */       regVPlayer(player);
/*     */     } 
/*     */     
/*  87 */     int totalVPlayerCnt = 0;
/*  88 */     List<RefRobot> vPlayerInits = new ArrayList<>(RefDataMgr.getAll(RefRobot.class).values());
/*  89 */     for (RefRobot init : vPlayerInits) {
/*  90 */       totalVPlayerCnt += init.Count;
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (this.pidDatas.size() >= totalVPlayerCnt) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     CommLog.info("[RobotManager.init] 机器人数量不足，开始创建机器人... 大概需要1min 请稍等");
/*     */     
/* 100 */     Collections.sort(vPlayerInits, new Comparator<RefRobot>()
/*     */         {
/*     */           public int compare(RefRobot o1, RefRobot o2) {
/* 103 */             return o2.Level - o1.Level;
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 108 */     for (RefRobot init : vPlayerInits) {
/* 109 */       List<Player> players = this.lvCidDatas.get(Integer.valueOf(init.Level));
/* 110 */       if (players == null) {
/* 111 */         players = new ArrayList<>();
/* 112 */         this.lvCidDatas.put(Integer.valueOf(init.Level), players);
/*     */       } 
/*     */       
/* 115 */       if (players.size() >= init.Count) {
/*     */         continue;
/*     */       }
/*     */       
/* 119 */       createRobot(init);
/*     */     } 
/* 121 */     CommLog.info("[RobotManager.init]机器人创建完毕，当前总量: {}", Integer.valueOf(this.pidDatas.size()));
/* 122 */     CommLog.info("[RobotManager.init]机器人创建完后并未入库请入库完毕后再停服重启!!否则会导致数据丢失!!");
/*     */   }
/*     */   
/*     */   public void createRobot(RefRobot init) {
/* 126 */     CommLog.info("[RobotManager.init] 创建机器人lv:{},数量:{}, 机器人创建完后并未入库请入库完毕后再停服重启!!!", Integer.valueOf(init.Level), Integer.valueOf(init.Count));
/* 127 */     for (int i = 0; i < init.Count; i++) {
/* 128 */       Player player = PlayerMgr.getInstance().createRobot(init);
/* 129 */       if (player == null)
/*     */         return; 
/* 131 */       regVPlayer(player);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void regVPlayer(Player player) {
/* 136 */     int lv = player.getPlayerBO().getLv();
/* 137 */     List<Player> lvInfoMap = this.lvCidDatas.get(Integer.valueOf(lv));
/* 138 */     if (lvInfoMap == null) {
/* 139 */       lvInfoMap = new ArrayList<>();
/* 140 */       this.lvCidDatas.put(Integer.valueOf(lv), lvInfoMap);
/*     */     } 
/* 142 */     lvInfoMap.add(player);
/* 143 */     this.pidDatas.put(Long.valueOf(player.getPid()), player);
/*     */   }
/*     */   
/*     */   public List<Player> getLvlPlayers(int teamLevel) {
/* 147 */     return this.lvCidDatas.get(Integer.valueOf(teamLevel));
/*     */   }
/*     */   
/*     */   public boolean isRobot(long pid) {
/* 151 */     return this.pidDatas.containsKey(Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public void setRobot(long pid, Player player) {
/* 155 */     this.pidDatas.put(Long.valueOf(pid), player);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/RobotManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */