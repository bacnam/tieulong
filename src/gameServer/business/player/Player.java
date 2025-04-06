/*     */ package business.player;
/*     */ 
/*     */ import BaseCommon.CommClass;
/*     */ import BaseCommon.CommLog;
/*     */ import BaseThread.BaseMutexInstance;
/*     */ import BaseThread.BaseMutexObject;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.features.DailyRefreshFeature;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.PkgCacheMgr;
/*     */ import core.database.game.bo.PlayerBO;
/*     */ import core.network.client2game.ClientSession;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
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
/*     */ public class Player
/*     */ {
/*     */   public static final String VirtualPlayerOpenId = "LEGEND_ROBOT";
/*  37 */   protected final BaseMutexObject m_mutex = new BaseMutexObject();
/*  38 */   protected final BaseMutexInstance m_mutexIns = new BaseMutexInstance(); private final PlayerBO playerBO; private final boolean isVPlayer; private Hashtable<String, Feature> features;
/*     */   
/*     */   public void lockIns() {
/*  41 */     this.m_mutexIns.lock();
/*     */   }
/*     */   private ClientSession session; private DailyRefreshFeature dailyRefreshFeature; private Object dailyLockObject; private PkgCacheMgr pkgCacheMgr;
/*     */   public void unlockIns() {
/*  45 */     this.m_mutexIns.unlock();
/*     */   }
/*     */   
/*     */   public void lock() {
/*  49 */     this.m_mutex.lock();
/*     */   }
/*     */   
/*     */   public void unlock() {
/*  53 */     this.m_mutex.unlock();
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
/*     */   public PlayerBO getPlayerBO() {
/*  66 */     return this.playerBO;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  70 */     return this.playerBO.getId();
/*     */   }
/*     */   
/*     */   public String getOpenId() {
/*  74 */     return this.playerBO.getOpenId();
/*     */   }
/*     */   
/*     */   public int getSid() {
/*  78 */     return this.playerBO.getSid();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  82 */     return this.playerBO.getName();
/*     */   }
/*     */   
/*     */   public int getLv() {
/*  86 */     return this.playerBO.getLv();
/*     */   }
/*     */   
/*     */   public int getVipLevel() {
/*  90 */     return this.playerBO.getVipLevel();
/*     */   }
/*     */   
/*     */   public boolean isVirtualPlayer() {
/*  94 */     return this.isVPlayer;
/*     */   }
/*     */   
/*     */   public int getBanTime() {
/*  98 */     int expiredTime = this.playerBO.getBannedLoginExpiredTime();
/*  99 */     if (expiredTime == -1) {
/* 100 */       return -1;
/*     */     }
/* 102 */     return Math.max(0, expiredTime - CommTime.nowSecond());
/*     */   }
/*     */   
/*     */   public boolean isBanned() {
/* 106 */     int expiredTime = this.playerBO.getBannedLoginExpiredTime();
/* 107 */     return !(expiredTime != -1 && expiredTime <= CommTime.nowSecond());
/*     */   }
/*     */   
/*     */   public Player(PlayerBO playerBO) {
/* 111 */     this.features = new Hashtable<>();
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
/* 256 */     this.dailyLockObject = new Object();
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
/* 272 */     this.pkgCacheMgr = null;
/*     */     this.playerBO = playerBO;
/*     */     this.isVPlayer = "LEGEND_ROBOT".equals(playerBO.getOpenId());
/* 275 */     this.m_mutex.reduceMutexLevel(2); } public PkgCacheMgr getPacketCache() { if (this.pkgCacheMgr == null) {
/* 276 */       this.pkgCacheMgr = new PkgCacheMgr();
/*     */     }
/* 278 */     return this.pkgCacheMgr; }
/*     */ 
/*     */   
/*     */   public boolean isLoaded(Class<? extends Feature> clazz) {
/*     */     return (this.features.get(clazz.getName()) != null);
/*     */   }
/*     */   
/*     */   public <T extends Feature> T getFeature(Class<T> clazz) {
/*     */     Feature feature = getFeature(clazz.getName());
/*     */     feature.tryLoadDBData();
/*     */     return (T)feature;
/*     */   }
/*     */   
/*     */   private Feature getFeature(String featureName) {
/*     */     Feature feature = this.features.get(featureName);
/*     */     if (feature != null) {
/*     */       feature.updateLastActiveTime();
/*     */       return feature;
/*     */     } 
/*     */     synchronized (this.features) {
/*     */       feature = this.features.get(featureName);
/*     */       if (feature != null)
/*     */         return feature; 
/*     */       try {
/*     */         Class<?> cs = CommClass.forName(featureName);
/*     */         Constructor<Feature> constructor = (Constructor)cs.getConstructor(new Class[] { Player.class });
/*     */         feature = constructor.newInstance(new Object[] { this });
/*     */         this.features.put(featureName, feature);
/*     */         return feature;
/*     */       } catch (Exception e) {
/*     */         CommLog.error(e.getMessage(), e);
/*     */         System.exit(-1);
/*     */         return null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseFeature(int activeTime) {
/*     */     int curTime = CommTime.nowSecond();
/*     */     if (isVirtualPlayer())
/*     */       return; 
/*     */     if (isOnline())
/*     */       return; 
/*     */     if (getPlayerBO().getLastLogin() + activeTime > curTime)
/*     */       return; 
/*     */     lockIns();
/*     */     try {
/*     */       for (Feature feature : new ArrayList(this.features.values())) {
/*     */         if (feature.getLastActiveTime() + activeTime > curTime)
/*     */           continue; 
/*     */         this.features.remove(feature.getClass().getName());
/*     */       } 
/*     */     } catch (Exception e) {
/*     */       CommLog.error("释放玩家Feature时发生异常", e);
/*     */     } 
/*     */     unlockIns();
/*     */   }
/*     */   
/*     */   public ClientSession getClientSession() {
/*     */     return this.session;
/*     */   }
/*     */   
/*     */   public boolean isOnline() {
/*     */     return (this.session != null);
/*     */   }
/*     */   
/*     */   public void bindSession(ClientSession session) {
/*     */     lockIns();
/*     */     this.session = session;
/*     */     PlayerMgr.getInstance().regOnlinePlayer(this);
/*     */     this.playerBO.saveLastLogin(CommTime.nowSecond());
/*     */     ((PlayerBase)getFeature(PlayerBase.class)).onConnect();
/*     */     unlockIns();
/*     */   }
/*     */   
/*     */   public void loseSession() {
/*     */     PlayerMgr.getInstance().unregOnlinePlayer(this);
/*     */     this.session = null;
/*     */     ((PlayerBase)getFeature(PlayerBase.class)).onDisconnect();
/*     */   }
/*     */   
/*     */   public void pushProto(String operation, Object proto) {
/*     */     if (this.session != null && this.session.isPrint())
/*     */       CommLog.error(String.format("[notify][%s]：%s", new Object[] { operation, proto.toString() })); 
/*     */     if (this.session != null)
/*     */       this.session.notifyMessage(operation, proto); 
/*     */   }
/*     */   
/*     */   public void pushProperties(String name, int value) {
/*     */     List<core.network.proto.Player.Property> properties = new ArrayList<>();
/*     */     properties.add(new core.network.proto.Player.Property(name, value));
/*     */     pushProperties(properties);
/*     */   }
/*     */   
/*     */   public void pushProperties(String name0, int value0, String name1, int value1) {
/*     */     List<core.network.proto.Player.Property> properties = new ArrayList<>();
/*     */     properties.add(new core.network.proto.Player.Property(name0, value0));
/*     */     properties.add(new core.network.proto.Player.Property(name1, value1));
/*     */     pushProperties(properties);
/*     */   }
/*     */   
/*     */   public void pushProperties(List<core.network.proto.Player.Property> properties) {
/*     */     pushProto("playerchanged", properties);
/*     */   }
/*     */   
/*     */   public void pushMessage(String key, String... params) {
/*     */     ArrayList<String> p = new ArrayList<>();
/*     */     byte b;
/*     */     int i;
/*     */     String[] arrayOfString;
/*     */     for (i = (arrayOfString = params).length, b = 0; b < i; ) {
/*     */       String s = arrayOfString[b];
/*     */       p.add(s);
/*     */       b++;
/*     */     } 
/*     */     pushProto("systemMesage", "");
/*     */   }
/*     */   
/*     */   public void notify2Zone() {
/*     */     PlayerMgr.getInstance().tryNotify(this);
/*     */   }
/*     */   
/*     */   public boolean isDailyRefreshLoaded() {
/*     */     return (this.dailyRefreshFeature != null);
/*     */   }
/*     */   
/*     */   public DailyRefreshFeature getDailyRefreshFeature() {
/*     */     if (this.dailyRefreshFeature == null)
/*     */       synchronized (this.dailyLockObject) {
/*     */         if (this.dailyRefreshFeature == null)
/*     */           this.dailyRefreshFeature = new DailyRefreshFeature(this); 
/*     */       }  
/*     */     return this.dailyRefreshFeature;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/Player.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */