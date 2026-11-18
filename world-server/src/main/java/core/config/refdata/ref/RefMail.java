package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefMail extends RefBaseWorld {
    @RefField(
        iskey = true
    )
    public int id;
    public String Title;
    public String Content;
    public int RewardId;
    public int ExistTime;
    public int PickUpExistTime;

    @Override
    public boolean Assert() {
        return RefAssert.inRef(this.RewardId, RefReward.class, 0);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
