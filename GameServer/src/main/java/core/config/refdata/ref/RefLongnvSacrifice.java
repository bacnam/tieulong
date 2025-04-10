package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.LongnvSacrificeType;

public class RefLongnvSacrifice extends RefBaseGame {
@RefField(iskey = true)
public LongnvSacrificeType id;
public int CostItemID;
public int Exp;
public int Critical;
public int CriticalValue;
public String SacrificeName;
public int Limit;
public ConstEnum.DailyRefresh RefreshType;

public boolean Assert() {
if (!RefAssert.inRef(Integer.valueOf(this.CostItemID), RefUniformItem.class, new Object[0])) {
return false;
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

