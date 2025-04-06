/*    */ package business.global.refresh;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import com.zhonglian.server.common.mgr.daily.IDailyRefresh;
/*    */ import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDailyWorldRefresh;
/*    */ import core.database.game.bo.DailyWorldRefreshBO;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldDailyRefreshContainer
/*    */   extends IDailyRefresh<WorldDailyRefresh>
/*    */ {
/* 27 */   private static WorldDailyRefreshContainer instance = new WorldDailyRefreshContainer();
/*    */   
/*    */   public static WorldDailyRefreshContainer getInstance() {
/* 30 */     return instance;
/*    */   }
/*    */   
/* 33 */   private DailyWorldRefreshBO bo = null;
/*    */   
/*    */   public WorldDailyRefreshContainer() {
/* 36 */     init();
/*    */   }
/*    */   
/*    */   public void init() {
/*    */     try {
/* 41 */       List<DailyWorldRefreshBO> bos = BM.getBM(DailyWorldRefreshBO.class).findAll();
/* 42 */       if (bos.size() == 0) {
/* 43 */         this.bo = new DailyWorldRefreshBO();
/* 44 */         this.bo.setServerStart(CommTime.getTodayZeroClockS());
/* 45 */         this.bo.insert();
/*    */       } else {
/*    */         
/* 48 */         this.bo = bos.get(0);
/*    */       } 
/* 50 */     } catch (Exception e) {
/* 51 */       CommLog.error("load daily world refresh db error:", e);
/*    */     } 
/*    */     
/* 54 */     reload();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void gm_reset() {
/* 60 */     BM.getBM(DailyWorldRefreshBO.class).delAll();
/* 61 */     init();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void reload() {
/* 67 */     CommLog.warn("WorldDailyRefreshMgr reload!!!");
/*    */     
/* 69 */     this.refreshList.clear();
/* 70 */     this.refreshMap.clear();
/*    */     
/*    */     try {
/* 73 */       for (RefDailyWorldRefresh data : RefDataMgr.getAll(RefDailyWorldRefresh.class).values()) {
/* 74 */         if (data.Index > this.bo.getIndexLastTimeSize()) {
/* 75 */           CommLog.error("reload RefDailyWorldRefresh index:{} > {}", Integer.valueOf(data.Index), Integer.valueOf(this.bo.getIndexLastTimeSize()));
/*    */           
/*    */           continue;
/*    */         } 
/* 79 */         WorldDailyRefresh dailyRefresh = new WorldDailyRefresh((IDailyRefreshRef)data, this);
/* 80 */         dailyRefresh.fixTime();
/* 81 */         this.refreshList.add(dailyRefresh);
/* 82 */         this.refreshMap.put(dailyRefresh.ref.getEventTypes(), dailyRefresh);
/*    */       } 
/* 84 */     } catch (Exception e) {
/* 85 */       CommLog.error("load daily world refresh db error:", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLastRefreshTime(int index) {
/* 91 */     return this.bo.getIndexLastTime(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLastRefreshTime(int index, int nextRefreshTime) {
/* 96 */     this.bo.saveIndexLastTime(index, nextRefreshTime);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/refresh/WorldDailyRefreshContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */