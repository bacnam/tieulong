package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefWingActive
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int Level;
    public int Price;
    public int Discount;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

