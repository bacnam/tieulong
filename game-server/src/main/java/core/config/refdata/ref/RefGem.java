package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;

public class RefGem extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int Material;
    public int ATK;
    public int MaxHP;
    public int DEF;
    public int RGS;
    public int Hit;
    public int Dodge;
    public int Critical;
    public int Tenacity;

    public int getValue(Attribute attr) {
        switch (attr) {
            case MaxHP:
                return this.MaxHP;
            case ATK:
                return this.ATK;
            case DEF:
                return this.DEF;
            case RGS:
                return this.RGS;
            case Hit:
                return this.Hit;
            case Dodge:
                return this.Dodge;
            case Critical:
                return this.Critical;
            case Tenacity:
                return this.Tenacity;
            default:
                return 0;
        }
    }

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
