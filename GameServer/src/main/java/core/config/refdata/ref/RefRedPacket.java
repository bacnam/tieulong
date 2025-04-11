package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;

public class RefRedPacket
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public NumberRange Range;
    public int PickNum;
    public int Money;
    public NumberRange PickRange;
    public String Name;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

