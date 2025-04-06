/*     */ package business.global.refresh;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTask;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import java.util.LinkedList;
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
/*     */ public class RefreshMgr
/*     */ {
/*  22 */   public static int INTERVAL = 3000;
/*  23 */   private static RefreshMgr instance = new RefreshMgr();
/*  24 */   public List<Long> cids = new LinkedList<>();
/*     */   
/*     */   public static RefreshMgr getInstance() {
/*  27 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  35 */     SyncTaskManager.task(new SyncTask()
/*     */         {
/*     */           public void run()
/*     */           {
/*  39 */             int wait = RefreshMgr.INTERVAL;
/*     */             try {
/*  41 */               wait = RefreshMgr.this.process();
/*  42 */             } catch (Exception e) {
/*  43 */               CommLog.error("RefreshMgr.init", e);
/*     */             } 
/*  45 */             SyncTaskManager.task(this, wait);
/*     */           }
/*  47 */         }INTERVAL);
/*     */   }
/*     */   
/*     */   public void gm_reset() {
/*  51 */     WorldDailyRefreshContainer.getInstance().gm_reset();
/*  52 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  53 */       if (player.isDailyRefreshLoaded())
/*  54 */         player.getDailyRefreshFeature().gm_reset(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reload() {
/*  59 */     WorldDailyRefreshContainer.getInstance().reload();
/*  60 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  61 */       if (player.isDailyRefreshLoaded()) {
/*  62 */         player.getDailyRefreshFeature().reload();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int process() {
/*  71 */     int curSec = CommTime.nowSecond();
/*     */ 
/*     */     
/*  74 */     if (this.cids.size() > 0) {
/*  75 */       int cnt = 0;
/*  76 */       while (this.cids.size() > 0 && cnt < 10) {
/*  77 */         cnt++;
/*  78 */         Long cid = this.cids.remove(0);
/*     */         try {
/*  80 */           checkPlayer(cid.longValue(), curSec);
/*  81 */         } catch (Exception e) {
/*  82 */           CommLog.error("refresh cid:{}", cid, e);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  91 */         checkGlobal(curSec);
/*  92 */       } catch (Exception e) {
/*  93 */         throw e;
/*     */       } 
/*     */       
/*  96 */       this.cids.addAll(PlayerMgr.getInstance().getOnlinePlayersCid());
/*     */     } 
/*     */     
/*  99 */     if (this.cids.size() > 0) {
/* 100 */       return 0;
/*     */     }
/* 102 */     int wait = 20;
/* 103 */     int next = (curSec + 20) % 60;
/*     */     
/* 105 */     if (next != 0 && next < 20) {
/* 106 */       wait = 20 - next;
/*     */     }
/* 108 */     return wait * 1000;
/*     */   }
/*     */   
/*     */   public void checkGlobal(int curSec) {
/* 112 */     WorldDailyRefreshContainer.getInstance().process(curSec);
/*     */   }
/*     */   
/*     */   public void checkPlayer(long cid, int curSec) {
/* 116 */     SyncTaskManager.task(() -> {
/*     */           Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(paramLong);
/*     */           if (player == null)
/*     */             return; 
/*     */           player.getDailyRefreshFeature().process(paramInt);
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/refresh/RefreshMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */