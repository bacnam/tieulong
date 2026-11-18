package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefCrystalPrice extends RefBaseWorld {
    @RefField(
        iskey = true
    )
    public int id;
    public int ZoneArenaBuyFightCount;

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
