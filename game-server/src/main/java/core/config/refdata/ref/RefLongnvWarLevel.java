package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefLongnvWarLevel extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int RobotLevel;
    public int Amount;
    public String GuildName;

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
