package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefVIP extends RefBaseWorld {
    @RefField(
        iskey = true
    )
    public int id;
    public int DailyBuyZoneArenaAttackTimes;

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
