package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefDungeon extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int Exp;
    public int Gold;
    public int Drop;
    public int BossDrop;
    public int BossID;

    @Override
    public boolean Assert() {
        return !RefAssert.inRef(this.Drop, RefReward.class) ? false : RefAssert.inRef(this.BossDrop, RefReward.class);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
