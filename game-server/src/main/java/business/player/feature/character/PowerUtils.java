package business.player.feature.character;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PowerUtils {
    private static Map<Attribute, Double> factors = new HashMap<>();
    private static double pwrFacoter = RefDataMgr.getFactor("Power_Factor", 650) / 10000.0;

    static {
        factors.put(Attribute.MaxHP, RefDataMgr.getFactor("Power_MaxHP", 6900) / 10000.0);
        factors.put(Attribute.ATK, RefDataMgr.getFactor("Power_ATK", 40000) / 10000.0);
        factors.put(Attribute.DEF, RefDataMgr.getFactor("Power_DEF", 110000) / 10000.0);
        factors.put(Attribute.RGS, RefDataMgr.getFactor("Power_RGS", 110000) / 10000.0);
        factors.put(Attribute.Hit, RefDataMgr.getFactor("Power_Hit", 30000) / 10000.0);
        factors.put(Attribute.Dodge, RefDataMgr.getFactor("Power_Dodge", 130000) / 10000.0);
        factors.put(Attribute.Critical, RefDataMgr.getFactor("Power_Critical", 90000) / 10000.0);
        factors.put(Attribute.Tenacity, RefDataMgr.getFactor("Power_Tenacity", 30000) / 10000.0);
    }

    public static int getPower(Map<Attribute, Integer> attr) {
        int power = 0;

        for (Entry<Attribute, Integer> pair : attr.entrySet()) {
            power += getPower(pair.getKey(), pair.getValue());
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
        return factor == null ? 0 : (int)(value * factor * pwrFacoter);
    }
}
