package business.player.feature.character;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerUtils
{
private static Map<Attribute, Double> factors = new HashMap<>();
private static double pwrFacoter = RefDataMgr.getFactor("Power_Factor", 650) / 10000.0D;

static {
factors.put(Attribute.MaxHP, Double.valueOf(RefDataMgr.getFactor("Power_MaxHP", 6900) / 10000.0D));
factors.put(Attribute.ATK, Double.valueOf(RefDataMgr.getFactor("Power_ATK", 40000) / 10000.0D));
factors.put(Attribute.DEF, Double.valueOf(RefDataMgr.getFactor("Power_DEF", 110000) / 10000.0D));
factors.put(Attribute.RGS, Double.valueOf(RefDataMgr.getFactor("Power_RGS", 110000) / 10000.0D));
factors.put(Attribute.Hit, Double.valueOf(RefDataMgr.getFactor("Power_Hit", 30000) / 10000.0D));
factors.put(Attribute.Dodge, Double.valueOf(RefDataMgr.getFactor("Power_Dodge", 130000) / 10000.0D));
factors.put(Attribute.Critical, Double.valueOf(RefDataMgr.getFactor("Power_Critical", 90000) / 10000.0D));
factors.put(Attribute.Tenacity, Double.valueOf(RefDataMgr.getFactor("Power_Tenacity", 30000) / 10000.0D));
}

public static int getPower(Map<Attribute, Integer> attr) {
int power = 0;
for (Map.Entry<Attribute, Integer> pair : attr.entrySet()) {
power += getPower(pair.getKey(), ((Integer)pair.getValue()).intValue());
}
return power;
}

public static int getPower(List<Attribute> attrType, List<Integer> attrValue) {
Map<Attribute, Integer> map = new HashMap<>();
for (int i = 0; i < attrType.size(); i++) {
map.put(attrType.get(i), attrValue.get(i));
}
return getPower(map);
}

public static int getPower(Attribute strengthenType, int value) {
Double factor = factors.get(strengthenType);
return (factor == null) ? 0 : (int)(value * factor.doubleValue() * pwrFacoter);
}
}

