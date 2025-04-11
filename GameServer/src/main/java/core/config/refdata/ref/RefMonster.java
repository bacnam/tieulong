package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

import java.util.List;

public class RefMonster
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public String Name;
    public int Level;
    public int MaxHP;
    public int ATK;
    public int DEF;
    public int RGS;
    public int Hit;
    public int Dodge;
    public int Critical;
    public int Tenacity;
    public int RNG;
    public int SPD;
    public List<Integer> SkillList;
    public List<Integer> BuffList;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

