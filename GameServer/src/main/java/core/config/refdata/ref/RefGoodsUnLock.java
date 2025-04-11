package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefGoodsUnLock
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<ConstEnum.GoodsUnLockType, List<RefGoodsUnLock>> typeValue = new HashMap<>();
    @RefField(iskey = true)
    public int ID;
    public ConstEnum.GoodsUnLockType UnLockType;
    public int Value;
    public String Desc;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        byte b;
        int i;
        ConstEnum.GoodsUnLockType[] arrayOfGoodsUnLockType;
        for (i = (arrayOfGoodsUnLockType = ConstEnum.GoodsUnLockType.values()).length, b = 0; b < i; ) {
            ConstEnum.GoodsUnLockType type = arrayOfGoodsUnLockType[b];
            typeValue.put(type, Lists.newArrayList());
            b++;
        }

        for (RefGoodsUnLock ref : all.values()) {
            ((List<RefGoodsUnLock>) typeValue.get(ref.UnLockType)).add(ref);
        }
        return true;
    }
}

