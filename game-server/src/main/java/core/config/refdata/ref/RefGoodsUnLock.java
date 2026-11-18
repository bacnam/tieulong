package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ConstEnum.GoodsUnLockType;
import com.zhonglian.server.common.utils.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefGoodsUnLock extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int ID;
    public GoodsUnLockType UnLockType;
    public int Value;
    public String Desc;
    @RefField(
        isfield = false
    )
    public static Map<GoodsUnLockType, List<RefGoodsUnLock>> typeValue = new HashMap<>();

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        GoodsUnLockType[] var5;
        for (GoodsUnLockType type : var5 = GoodsUnLockType.values()) {
            typeValue.put(type, Lists.newArrayList());
        }

        for (RefGoodsUnLock ref : ((RefContainer<RefGoodsUnLock>) all).values()) {
            typeValue.get(ref.UnLockType).add(ref);
        }

        return true;
    }
}
