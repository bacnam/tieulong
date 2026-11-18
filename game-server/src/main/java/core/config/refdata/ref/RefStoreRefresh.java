package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.StoreType;
import java.util.HashMap;
import java.util.Map;

public class RefStoreRefresh extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public StoreType id;
    public int UniformId;
    public int Count;
    public int StoreFreeRefreshTimes;
    public boolean ManualRefresh;
    public boolean AutoRefresh;
    public boolean ShowNextRefreshTime;
    @RefField(
        isfield = false
    )
    public static Map<StoreType, Integer> FreeStoreTimes = new HashMap<>();

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.UniformId, RefUniformItem.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        StoreType[] var5;
        for (StoreType type : var5 = StoreType.values()) {
            FreeStoreTimes.put(type, 10);
        }

        for (RefStoreRefresh ref : ((RefContainer<RefStoreRefresh>) all).values()) {
            FreeStoreTimes.put(ref.id, ref.StoreFreeRefreshTimes);
        }

        return true;
    }
}
