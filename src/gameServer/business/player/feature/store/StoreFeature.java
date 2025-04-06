/*     */ package business.player.feature.store;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.StoreType;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefStoreRefresh;
/*     */ import core.database.game.bo.PlayerGoodsBO;
/*     */ import core.database.game.bo.StoreRecordBO;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class StoreFeature
/*     */   extends Feature {
/*     */   private Map<StoreType, PlayerStore> stores;
/*     */   
/*     */   public StoreFeature(Player player) {
/*  20 */     super(player);
/*     */ 
/*     */     
/*  23 */     this.stores = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  28 */     List<PlayerGoodsBO> boList = BM.getBM(PlayerGoodsBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  29 */     for (PlayerGoodsBO bo : boList) {
/*  30 */       StoreType storeType = StoreType.values()[bo.getStoreType()];
/*  31 */       PlayerStore playerStore = this.stores.get(storeType);
/*  32 */       if (playerStore == null) {
/*  33 */         playerStore = new PlayerStore(storeType, this.player);
/*  34 */         this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
/*     */       } 
/*  36 */       playerStore.addGoods(bo);
/*     */     } 
/*  38 */     initRefresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerStore getOrCreate(StoreType storeType) {
/*  48 */     PlayerStore playerStore = this.stores.get(storeType);
/*  49 */     if (playerStore != null) {
/*  50 */       return playerStore;
/*     */     }
/*  52 */     synchronized (this) {
/*  53 */       playerStore = this.stores.get(storeType);
/*  54 */       if (playerStore != null) {
/*  55 */         return playerStore;
/*     */       }
/*  57 */       this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
/*     */     } 
/*  59 */     return playerStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doAddFreeRefreshTimes(List<Integer> typeIdList, int times) {
/*  70 */     StoreRecordBO bo = ((StoreRecord)this.player.getFeature(StoreRecord.class)).getOrCreate();
/*     */     
/*  72 */     typeIdList.forEach(typeId -> {
/*     */           int maxTimes = ((Integer)RefStoreRefresh.FreeStoreTimes.get(StoreType.values()[typeId.intValue()])).intValue();
/*     */           int nowTimes = paramStoreRecordBO.getFreeRefreshTimes(typeId.intValue()) + paramInt;
/*     */           if (nowTimes > maxTimes) {
/*     */             nowTimes = maxTimes;
/*     */           }
/*     */           paramStoreRecordBO.setFreeRefreshTimes(typeId.intValue(), nowTimes);
/*     */           if (nowTimes >= maxTimes) {
/*     */             paramStoreRecordBO.setFlushIcon(typeId.intValue(), 1);
/*     */           }
/*     */         });
/*  83 */     bo.saveAll();
/*     */     
/*  85 */     typeIdList.forEach(typeId -> this.player.pushProto("store.RefreshInfo", getOrCreate(StoreType.values()[typeId.intValue()]).refreshInfo()));
/*     */ 
/*     */     
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doAutoRefresh() {
/*     */     try {
/*  98 */       for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
/*  99 */         if (!ref.AutoRefresh) {
/*     */           continue;
/*     */         }
/* 102 */         StoreType storeType = ref.id;
/*     */         
/* 104 */         PlayerStore playerStore = getOrCreate(storeType);
/*     */         
/* 106 */         playerStore.doAutoRefresh();
/*     */         
/* 108 */         ((StoreRecord)this.player.getFeature(StoreRecord.class)).doAutoRefresh(storeType);
/*     */         
/* 110 */         this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());
/*     */         
/* 112 */         this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
/*     */       } 
/* 114 */     } catch (Exception e) {
/*     */       
/* 116 */       e.printStackTrace();
/*     */     } 
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean initRefresh() {
/* 127 */     for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
/* 128 */       if (ref.AutoRefresh) {
/*     */         continue;
/*     */       }
/* 131 */       StoreType storeType = ref.id;
/*     */       
/* 133 */       PlayerStore playerStore = getOrCreate(storeType);
/*     */       
/* 135 */       playerStore.doAutoRefresh();
/*     */       
/* 137 */       ((StoreRecord)this.player.getFeature(StoreRecord.class)).doAutoRefresh(storeType);
/*     */       
/* 139 */       this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());
/*     */       
/* 141 */       this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
/*     */     } 
/* 143 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/store/StoreFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */