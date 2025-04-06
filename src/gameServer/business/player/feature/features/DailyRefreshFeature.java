/*     */ package business.player.feature.features;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.mgr.daily.IDailyRefresh;
/*     */ import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefDailyPlayerRefresh;
/*     */ import core.database.game.bo.DailyPlayerRefreshBO;
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
/*     */ public class DailyRefreshFeature
/*     */   extends IDailyRefresh<PlayerDailyRefresh>
/*     */ {
/*  25 */   private DailyPlayerRefreshBO bo = null;
/*     */   
/*     */   private Player player;
/*     */   
/*     */   public DailyRefreshFeature(Player player) {
/*  30 */     this.player = player;
/*  31 */     init();
/*     */   }
/*     */   
/*     */   public void init() {
/*     */     try {
/*  36 */       this.bo = (DailyPlayerRefreshBO)BM.getBM(DailyPlayerRefreshBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*  37 */       if (this.bo == null) {
/*  38 */         this.bo = new DailyPlayerRefreshBO();
/*  39 */         this.bo.setPid(this.player.getPid());
/*  40 */         this.bo.insert();
/*     */       } 
/*  42 */     } catch (Exception e) {
/*  43 */       CommLog.error("load daily player refresh db error:", e);
/*     */     } 
/*     */     
/*  46 */     reload();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void gm_reset() {
/*  52 */     this.bo.del();
/*  53 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reload() {
/*  60 */     this.refreshList.clear();
/*  61 */     this.refreshMap.clear();
/*     */     
/*  63 */     boolean needSave = false;
/*     */     try {
/*  65 */       for (RefDailyPlayerRefresh data : RefDataMgr.getAll(RefDailyPlayerRefresh.class).values()) {
/*  66 */         if (data.Index > this.bo.getIndexLastTimeSize()) {
/*  67 */           CommLog.error("reload RefDailyPlayerRefresh index:{} > {}", Integer.valueOf(data.Index), Integer.valueOf(this.bo.getIndexLastTimeSize()));
/*     */           
/*     */           continue;
/*     */         } 
/*  71 */         PlayerDailyRefresh dailyRefresh = new PlayerDailyRefresh((IDailyRefreshRef)data, this);
/*     */         
/*  73 */         if (dailyRefresh.getLastRefreshTime() == 0) {
/*  74 */           needSave = true;
/*  75 */           this.bo.setIndexLastTime(data.Index, dailyRefresh.getInitLastSec());
/*     */         } else {
/*  77 */           dailyRefresh.fixTime();
/*     */         } 
/*  79 */         this.refreshList.add(dailyRefresh);
/*  80 */         this.refreshMap.put(dailyRefresh.ref.getEventTypes(), dailyRefresh);
/*     */       } 
/*  82 */     } catch (Exception e) {
/*  83 */       CommLog.error("load daily player refresh db error:", e);
/*     */     } 
/*     */     
/*  86 */     if (needSave) {
/*  87 */       this.bo.saveAll();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLastRefreshTime(int index) {
/*  92 */     return this.bo.getIndexLastTime(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLastRefreshTime(int index, int nextRefreshTime) {
/*  97 */     this.bo.saveIndexLastTime(index, nextRefreshTime);
/*     */   }
/*     */   
/*     */   public Player getPlayer() {
/* 101 */     return this.player;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/DailyRefreshFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */