package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefMail
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public String Title;
    public String Content;
    public int RewardId;
    public int ExistTime;
    public int PickUpExistTime;

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.RewardId), RefReward.class, new Object[]{Integer.valueOf(0)})) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

