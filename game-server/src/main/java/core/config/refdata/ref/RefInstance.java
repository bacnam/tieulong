package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import java.util.List;
import java.util.Map;

public class RefInstance extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public InstanceType Type;
    public int Material;
    public int RewardId;
    @RefField(
        iskey = false
    )
    public static Map<InstanceType, List<RefInstance>> instanceMap = Maps.newConcurrentMap();

    @Override
    public boolean Assert() {
        return this.Type == InstanceType.GuaidInstance || RefAssert.inRef(this.RewardId, RefReward.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        InstanceType[] var5;
        for (InstanceType instanceType : var5 = InstanceType.values()) {
            instanceMap.put(instanceType, Lists.newArrayList());
        }

        for (RefInstance ref : ((RefContainer<RefInstance>) all).values()) {
            instanceMap.get(ref.Type).add(ref);
        }

        return true;
    }
}
