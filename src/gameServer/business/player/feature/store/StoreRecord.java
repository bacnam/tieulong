/*     */ package business.player.feature.store;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.StoreType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.config.refdata.ref.RefStoreRefresh;
/*     */ import core.database.game.bo.StoreRecordBO;
/*     */ 
/*     */ public class StoreRecord
/*     */   extends Feature
/*     */ {
/*     */   public StoreRecordBO storeRecord;
/*     */   
/*     */   public StoreRecord(Player owner) {
/*  17 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  22 */     this.storeRecord = (StoreRecordBO)BM.getBM(StoreRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StoreRecordBO getOrCreate() {
/*  31 */     StoreRecordBO record = this.storeRecord;
/*  32 */     if (record != null) {
/*  33 */       return record;
/*     */     }
/*  35 */     synchronized (this) {
/*  36 */       record = this.storeRecord;
/*  37 */       if (record != null) {
/*  38 */         return record;
/*     */       }
/*  40 */       record = new StoreRecordBO();
/*  41 */       record.setPid(this.player.getPid()); byte b; int i; StoreType[] arrayOfStoreType;
/*  42 */       for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType storeType = arrayOfStoreType[b];
/*  43 */         record.setFreeRefreshTimes(storeType.ordinal(), ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue()); b++; }
/*     */       
/*  45 */       record.insert();
/*  46 */       this.storeRecord = record;
/*     */     } 
/*  48 */     return record;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/*  56 */       StoreRecordBO record = getOrCreate();
/*  57 */       record.setPaidRefreshTimesAll(0);
/*  58 */       record.setBuyTimesAll(0); byte b; int i; StoreType[] arrayOfStoreType;
/*  59 */       for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType storeType = arrayOfStoreType[b];
/*  60 */         record.setFreeRefreshTimes(storeType.ordinal(), ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue()); b++; }
/*     */       
/*  62 */       record.saveAll();
/*  63 */     } catch (Exception e) {
/*     */       
/*  65 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFreeRefreshTimes(StoreType storeType) {
/*  76 */     return getOrCreate().getFreeRefreshTimes(storeType.ordinal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPaidRefreshTimes(StoreType storeType) {
/*  86 */     return getOrCreate().getPaidRefreshTimes(storeType.ordinal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBuyTimes(StoreType storeType) {
/*  96 */     return getOrCreate().getBuyTimes(storeType.ordinal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlushIconByIndex(StoreType storeType) {
/* 106 */     return getOrCreate().getFlushIcon(storeType.ordinal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAutoRefresh(StoreType storeType) {
/* 115 */     int index = storeType.ordinal();
/* 116 */     if (index <= StoreType.None.ordinal()) {
/*     */       return;
/*     */     }
/* 119 */     StoreRecordBO record = getOrCreate();
/*     */     
/* 121 */     record.saveFlushIcon(index, 1);
/*     */     
/* 123 */     record.saveLastRefreshTime(index, CommTime.nowSecond());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandleRefresh(StoreType storeType, boolean useFreeRefresh) {
/* 133 */     int index = storeType.ordinal();
/* 134 */     if (index <= StoreType.None.ordinal()) {
/*     */       return;
/*     */     }
/* 137 */     StoreRecordBO record = getOrCreate();
/*     */     
/* 139 */     if (useFreeRefresh) {
/* 140 */       record.saveFreeRefreshTimes(index, getFreeRefreshTimes(storeType) - 1);
/*     */     } else {
/* 142 */       record.savePaidRefreshTimes(index, getPaidRefreshTimes(storeType) + 1);
/*     */     } 
/*     */     
/* 145 */     record.saveLastRefreshTime(index, CommTime.nowSecond());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doBuyGoods(StoreType storeType) {
/* 154 */     int index = storeType.ordinal();
/* 155 */     if (index <= StoreType.None.ordinal()) {
/*     */       return;
/*     */     }
/* 158 */     StoreRecordBO record = getOrCreate();
/*     */     
/* 160 */     record.saveBuyTimes(index, getBuyTimes(storeType) + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doSaveFlushIcon(StoreType storeType, int value) {
/* 170 */     getOrCreate().saveFlushIcon(storeType.ordinal(), value);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/store/StoreRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */