package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefNewTitle
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int level;
    public String Name;
    public int Quality;
    public int ActiveId;
    public int ActiveCount;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

