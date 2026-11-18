package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.LongnvSacrificeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;

public class RefLongnvSacrifice extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public LongnvSacrificeType id;
    public int CostItemID;
    public int Exp;
    public int Critical;
    public int CriticalValue;
    public String SacrificeName;
    public int Limit;
    public DailyRefresh RefreshType;

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.CostItemID, RefUniformItem.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
