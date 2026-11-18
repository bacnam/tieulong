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

public class StoreManager {
    private static StoreManager instance = new StoreManager();
    public Map<StoreType, Integer> GainFreeInterval = Maps.newConcurrentMap();
    private Map<StoreType, Class<? extends PlayerStore>> stores = new HashMap<>();

    public static StoreManager getInstance() {
        return instance;
    }

    private StoreManager() {
    }

    public void init() {
        this.initGainFreeTime();

        for (Class<?> clz : CommClass.getClasses(PlayerStore.class.getPackage().getName())) {
            try {
                Class<? extends PlayerStore> clazz = (Class<? extends PlayerStore>)clz;
                Store types = clz.getAnnotation(Store.class);
                if (types == null || types.value().length == 0) {
                    CommLog.error("[{}]的类型没有相关商城类型，请检查", clz.getSimpleName());
                    System.exit(0);
                }

                StoreType[] var9;
                for (StoreType type : var9 = types.value()) {
                    if (this.stores.containsKey(type)) {
                        String preClass = this.stores.get(type).getClass().getSimpleName();
                        CommLog.error("[{},{}]重复定义[{}]类型的商城", preClass, clazz.getSimpleName());
                        System.exit(0);
                    }
                }
            } catch (Exception var11) {
                CommLog.error("商城[{}]初始化失败", clz.getSimpleName(), var11);
                System.exit(0);
            }
        }
    }

    public void initGainFreeTime() {
        this.GainFreeInterval.clear();
        List<RefDailyPlayerRefresh> allList = new ArrayList<>(RefDataMgr.getAll(RefDailyPlayerRefresh.class).values());
        allList.stream().filter(b -> b.EventTypes.toString().contains("GainStoreRefreshTimes")).forEach(x -> {
            for (Integer typeId : x.EventValues) {
                StoreType storeType = StoreType.values()[typeId];
                if (this.GainFreeInterval.get(storeType) != null) {
                    CommLog.error("商店枚举{}已经配置了每隔时间增加次数的定时器,不允许重复添加", storeType);
                }

                this.GainFreeInterval.put(StoreType.values()[typeId], x.getInterval());
            }
        });
    }

    public int nextGainFreeCountDown(StoreType storeType, int nowTimes) {
        int maxTimes = RefStoreRefresh.FreeStoreTimes.get(storeType);
        return nowTimes >= maxTimes ? -1 : this.GainFreeInterval.get(storeType) - CommTime.getTodaySecond() % this.GainFreeInterval.get(storeType);
    }
}
