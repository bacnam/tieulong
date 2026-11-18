package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.SacrificeType;

public class RefGuildSacrifice extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public SacrificeType id;
    public int CostItemID;
    public int CostCount;
    public int SacrificeProgress;
    public int GuildExp;
    public int GuildDonate;
    public int Critical;
    public int CriticalValue;
    public String SacrificeName;

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.CostItemID, RefUniformItem.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
