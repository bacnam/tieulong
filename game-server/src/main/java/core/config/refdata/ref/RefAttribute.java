package core.config.refdata.ref;

import business.player.feature.character.PowerUtils;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.HashMap;
import java.util.Map;

public class RefAttribute extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public String id;
    public int charId;
    public int lv;
    public int MaxHP;
    public int ATK;
    public int DEF;
    public int RGS;
    public int Hit;
    public int Dodge;
    public int Critical;
    public int Tenacity;
    @RefField(
        isfield = false
    )
    public int Power = 0;

    @Override
    public boolean Assert() {
        Map<Attribute, Integer> attr = new HashMap<>();
        attr.put(Attribute.MaxHP, this.MaxHP);
        attr.put(Attribute.ATK, this.ATK);
        attr.put(Attribute.DEF, this.DEF);
        attr.put(Attribute.RGS, this.RGS);
        attr.put(Attribute.Hit, this.Hit);
        attr.put(Attribute.Dodge, this.Dodge);
        attr.put(Attribute.Critical, this.Critical);
        attr.put(Attribute.Tenacity, this.Tenacity);
        this.Power = PowerUtils.getPower(attr);
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
