/*    */ package business.global.store;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import business.player.feature.store.PlayerStore;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefDailyPlayerRefresh;
/*    */ import core.config.refdata.ref.RefStoreRefresh;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StoreManager
/*    */ {
/* 22 */   private static StoreManager instance = new StoreManager(); public Map<StoreType, Integer> GainFreeInterval; private Map<StoreType, Class<? extends PlayerStore>> stores;
/*    */   
/*    */   public static StoreManager getInstance() {
/* 25 */     return instance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private StoreManager() {
/* 32 */     this.GainFreeInterval = Maps.newConcurrentMap();
/*    */     
/* 34 */     this.stores = new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 39 */     initGainFreeTime();
/* 40 */     Set<Class<?>> clazzs = CommClass.getClasses(PlayerStore.class.getPackage().getName());
/* 41 */     for (Class<?> clz : clazzs) {
/*    */ 
/*    */       
/*    */       try {
/* 45 */         Class<? extends PlayerStore> clazz = (Class)clz;
/* 46 */         Store types = clazz.<Store>getAnnotation(Store.class);
/* 47 */         if (types == null || (types.value()).length == 0) {
/* 48 */           CommLog.error("[{}]的类型没有相关商城类型，请检查", clazz.getSimpleName());
/* 49 */           System.exit(0);
/*    */         }  byte b; int i; StoreType[] arrayOfStoreType;
/* 51 */         for (i = (arrayOfStoreType = types.value()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
/* 52 */           if (this.stores.containsKey(type)) {
/* 53 */             String preClass = ((Class)this.stores.get(type)).getClass().getSimpleName();
/* 54 */             CommLog.error("[{},{}]重复定义[{}]类型的商城", preClass, clazz.getSimpleName());
/* 55 */             System.exit(0);
/*    */           } 
/*    */           
/*    */           b++; }
/*    */       
/* 60 */       } catch (Exception e) {
/* 61 */         CommLog.error("商城[{}]初始化失败", clz.getSimpleName(), e);
/* 62 */         System.exit(0);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void initGainFreeTime() {
/* 68 */     this.GainFreeInterval.clear();
/* 69 */     List<RefDailyPlayerRefresh> allList = new ArrayList<>(RefDataMgr.getAll(RefDailyPlayerRefresh.class).values());
/* 70 */     allList.stream().filter(b -> b.EventTypes.toString().contains("GainStoreRefreshTimes")).forEach(x -> {
/*    */           for (Integer typeId : x.EventValues) {
/*    */             StoreType storeType = StoreType.values()[typeId.intValue()];
/*    */             if (this.GainFreeInterval.get(storeType) != null) {
/*    */               CommLog.error("商店枚举{}已经配置了每隔时间增加次数的定时器,不允许重复添加", storeType);
/*    */             }
/*    */             this.GainFreeInterval.put(StoreType.values()[typeId.intValue()], Integer.valueOf(x.getInterval()));
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int nextGainFreeCountDown(StoreType storeType, int nowTimes) {
/* 89 */     int maxTimes = ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue();
/* 90 */     if (nowTimes >= maxTimes) {
/* 91 */       return -1;
/*    */     }
/* 93 */     return ((Integer)this.GainFreeInterval.get(storeType)).intValue() - CommTime.getTodaySecond() % ((Integer)this.GainFreeInterval.get(storeType)).intValue();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/store/StoreManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */