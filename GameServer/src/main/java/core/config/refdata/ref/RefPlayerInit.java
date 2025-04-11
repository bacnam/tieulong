package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefPlayerInit
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int GmLevel;
    public int VipLevel;
    public int Level;
    public int Exp;
    public int Gold;
    public int Crystal;
    public int SkillPoint;
    public int DungeonLevel;
    public int WarspiritLv;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

