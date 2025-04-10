package core.config.refdata.ref;

import business.player.item.Reward;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import core.config.refdata.RefDataMgr;

public class RefLongnvWarPersonReward
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public NumberRange Num;
public int Reward;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static Reward getReward(int num) {
for (RefLongnvWarPersonReward ref : RefDataMgr.getAll(RefLongnvWarPersonReward.class).values()) {
if (ref.Num.within(num)) {
return ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Reward))).genReward();
}
} 
return null;
}
}

