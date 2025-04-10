package business.global.store;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.player.feature.store.PlayerStore;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyPlayerRefresh;
import core.config.refdata.ref.RefStoreRefresh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoreManager
{
private static StoreManager instance = new StoreManager(); public Map<StoreType, Integer> GainFreeInterval; private Map<StoreType, Class<? extends PlayerStore>> stores;

public static StoreManager getInstance() {
return instance;
}

private StoreManager() {
this.GainFreeInterval = Maps.newConcurrentMap();

this.stores = new HashMap<>();
}

public void init() {
initGainFreeTime();
Set<Class<?>> clazzs = CommClass.getClasses(PlayerStore.class.getPackage().getName());
for (Class<?> clz : clazzs) {

try {
Class<? extends PlayerStore> clazz = (Class)clz;
Store types = clazz.<Store>getAnnotation(Store.class);
if (types == null || (types.value()).length == 0) {
CommLog.error("[{}]的类型没有相关商城类型，请检查", clazz.getSimpleName());
System.exit(0);
}  byte b; int i; StoreType[] arrayOfStoreType;
for (i = (arrayOfStoreType = types.value()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
if (this.stores.containsKey(type)) {
String preClass = ((Class)this.stores.get(type)).getClass().getSimpleName();
CommLog.error("[{},{}]重复定义[{}]类型的商城", preClass, clazz.getSimpleName());
System.exit(0);
} 

b++; }

} catch (Exception e) {
CommLog.error("商城[{}]初始化失败", clz.getSimpleName(), e);
System.exit(0);
} 
} 
}

public void initGainFreeTime() {
this.GainFreeInterval.clear();
List<RefDailyPlayerRefresh> allList = new ArrayList<>(RefDataMgr.getAll(RefDailyPlayerRefresh.class).values());
allList.stream().filter(b -> b.EventTypes.toString().contains("GainStoreRefreshTimes")).forEach(x -> {
for (Integer typeId : x.EventValues) {
StoreType storeType = StoreType.values()[typeId.intValue()];
if (this.GainFreeInterval.get(storeType) != null) {
CommLog.error("商店枚举{}已经配置了每隔时间增加次数的定时器,不允许重复添加", storeType);
}
this.GainFreeInterval.put(StoreType.values()[typeId.intValue()], Integer.valueOf(x.getInterval()));
} 
});
}

public int nextGainFreeCountDown(StoreType storeType, int nowTimes) {
int maxTimes = ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue();
if (nowTimes >= maxTimes) {
return -1;
}
return ((Integer)this.GainFreeInterval.get(storeType)).intValue() - CommTime.getTodaySecond() % ((Integer)this.GainFreeInterval.get(storeType)).intValue();
}
}

