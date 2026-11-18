package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefLanguage extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public String id;
    public String CN;

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
