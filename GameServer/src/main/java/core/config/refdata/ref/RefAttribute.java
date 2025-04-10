package core.config.refdata.ref;

import business.player.feature.character.PowerUtils;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.HashMap;
import java.util.Map;

public class RefAttribute
extends RefBaseGame {
@RefField(iskey = true)
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
@RefField(isfield = false)
public int Power = 0;

public boolean Assert() {
Map<Attribute, Integer> attr = new HashMap<>();
attr.put(Attribute.MaxHP, Integer.valueOf(this.MaxHP));
attr.put(Attribute.ATK, Integer.valueOf(this.ATK));
attr.put(Attribute.DEF, Integer.valueOf(this.DEF));
attr.put(Attribute.RGS, Integer.valueOf(this.RGS));
attr.put(Attribute.Hit, Integer.valueOf(this.Hit));
attr.put(Attribute.Dodge, Integer.valueOf(this.Dodge));
attr.put(Attribute.Critical, Integer.valueOf(this.Critical));
attr.put(Attribute.Tenacity, Integer.valueOf(this.Tenacity));
this.Power = PowerUtils.getPower(attr);
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

