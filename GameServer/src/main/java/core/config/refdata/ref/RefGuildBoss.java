package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.Maps;

import java.util.Map;

public class RefGuildBoss
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<ConstEnum.BuffType, RefGuildBoss> buffMap = Maps.newMap();
    @RefField(iskey = true)
    public int id;
    public String Name;
    public int NeedGuildLevel;
    public int UniformId;
    public int UniformCount;
    public int MailId;
    public int MonsterId;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

