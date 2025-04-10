package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.List;

public class RefTreasure
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public NumberRange LevelRange;
public ArrayList<Integer> NormalIdList;
public ArrayList<Integer> NormalCountList;
public ArrayList<Integer> NormalWeightList;
public ArrayList<Integer> FixedTenIdList;
public ArrayList<Integer> FixedTenCountList;
public ArrayList<Integer> FixedTenWeightList;
public ArrayList<Integer> LeastIdList;
public ArrayList<Integer> LeastTenCountList;
public ArrayList<Integer> LeastTenWeightList;
public int UniformId;
public int UniformCount;
public int Price;
public int TenPrice;

public boolean Assert() {
if (!RefAssert.listSize(this.NormalIdList, this.NormalCountList, new List[] { this.NormalWeightList })) {
CommLog.error("normal");
return false;
} 

if (!RefAssert.listSize(this.FixedTenIdList, this.FixedTenCountList, new List[] { this.FixedTenWeightList })) {
CommLog.error("fix");
return false;
} 
if (!RefAssert.listSize(this.LeastIdList, this.LeastTenCountList, new List[] { this.LeastTenWeightList })) {
CommLog.error("least");
return false;
} 
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static RefTreasure getByLevel(int level) {
for (RefTreasure ref : RefDataMgr.getAll(RefTreasure.class).values()) {
if (ref.LevelRange.within(level)) {
return ref;
}
} 

return null;
}
}

