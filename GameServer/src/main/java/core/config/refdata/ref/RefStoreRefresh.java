package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.StoreType;

import java.util.HashMap;
import java.util.Map;

public class RefStoreRefresh
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<StoreType, Integer> FreeStoreTimes = new HashMap<>();
    @RefField(iskey = true)
    public StoreType id;
    public int UniformId;
    public int Count;
    public int StoreFreeRefreshTimes;
    public boolean ManualRefresh;
    public boolean AutoRefresh;
    public boolean ShowNextRefreshTime;

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.UniformId), RefUniformItem.class, new Object[0])) {
            return false;
        }

        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        byte b;
        int i;
        StoreType[] arrayOfStoreType;
        for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) {
            StoreType type = arrayOfStoreType[b];
            FreeStoreTimes.put(type, Integer.valueOf(10));
            b++;
        }

        for (RefStoreRefresh ref : all.values()) {
            FreeStoreTimes.put(ref.id, Integer.valueOf(ref.StoreFreeRefreshTimes));
        }
        return true;
    }
}

