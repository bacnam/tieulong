package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;

import java.util.List;
import java.util.Map;

public class RefInstance
        extends RefBaseGame {
    @RefField(iskey = false)
    public static Map<InstanceType, List<RefInstance>> instanceMap = Maps.newConcurrentMap();
    @RefField(iskey = true)
    public int id;
    public InstanceType Type;
    public int Material;
    public int RewardId;

    public boolean Assert() {
        if (this.Type != InstanceType.GuaidInstance && !RefAssert.inRef(Integer.valueOf(this.RewardId), RefReward.class, new Object[0])) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        byte b;
        int i;
        InstanceType[] arrayOfInstanceType;
        for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) {
            InstanceType instanceType = arrayOfInstanceType[b];
            instanceMap.put(instanceType, Lists.newArrayList());
            b++;
        }

        for (RefInstance ref : all.values()) {
            ((List<RefInstance>) instanceMap.get(ref.Type)).add(ref);
        }
        return true;
    }
}

