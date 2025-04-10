package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.Maps;
import java.util.ArrayList;
import java.util.Map;

public class RefDailyActive
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public NumberRange LvlRange;
public ArrayList<Integer> Condition;
public ArrayList<Integer> RewardID;
@RefField(isfield = false)
public static Map<Integer, Integer> level2refId = Maps.newConcurrentHashMap();

public boolean Assert() {
if (!RefAssert.inRef(this.RewardID, RefReward.class, new Object[0])) {
return false;
}
if (!RefAssert.listSize(this.Condition, this.RewardID, new java.util.List[0])) {
return false;
}

for (int index = this.LvlRange.getLow(); index <= this.LvlRange.getTop(); index++) {
level2refId.put(Integer.valueOf(index), Integer.valueOf(this.id));
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

