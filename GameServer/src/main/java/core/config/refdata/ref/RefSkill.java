package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

import java.util.List;

public class RefSkill
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public String Name;
    public String Describe;
    public double Attr;
    public double AttrAdd;
    public String Icon;
    public int Act;
    public int Effect;
    public int CD;
    public int Gold;
    public int GoldAdd;
    public int Require;
    public int CE;
    public String CastTarget;
    public String DefAttr;
    public String SettleType;
    public int Buff;
    public List<Integer> ClearBuffList;
    public int Priority;
    public String SelectTarget;
    public String SelectStrategy;
    public String SelectArea;
    public String SelectParam;
    public String Bullet;
    public int BulletSpeed;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

