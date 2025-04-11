package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefStore
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<StoreType, List<RefStore>> StoreByType = new HashMap<>();
    @RefField(iskey = true)
    public int ID;
    public StoreType StoreType;
    public NumberRange LevelRange;
    public int BuyLimit;
    public int PublicBuyLimit;
    public ArrayList<Long> GoodsIDList;
    public ArrayList<Integer> GoodsCountList;
    public ArrayList<Integer> GoodsWeightList;
    public boolean IsRandomCount;
    public boolean IsDailyRefresh;
    public int UnlockCondition;
    public int Sheet;

    public boolean Assert() {
        if (this.BuyLimit == 0) {
            this.BuyLimit = 1;
        }
        if (!RefAssert.listSize(this.GoodsIDList, this.GoodsCountList, new List[]{this.GoodsWeightList})) {
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
            StoreByType.put(type, Lists.newArrayList());
            b++;
        }

        for (RefStore ref : all.values()) {
            ((List<RefStore>) StoreByType.get(ref.StoreType)).add(ref);
        }
        return true;
    }
}

