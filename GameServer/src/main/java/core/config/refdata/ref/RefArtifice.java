package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.utils.Maps;

import java.util.List;
import java.util.Map;

public class RefArtifice
        extends RefBaseGame {
    public static Map<Integer, RefArtifice> maxLevelMap = Maps.newConcurrentHashMap();
    @RefField(iskey = true)
    public int id;
    public int Stars;
    public int Level;
    public List<Integer> UniformId;
    public List<Integer> UniformCount;
    public int Rate;
    public int Star;
    public int ATK;
    public int MaxHP;
    public int DEF;
    public int RGS;
    public int Hit;
    public int Dodge;
    public int Critical;
    public int Tenacity;
    public int Timemin;
    public int TimeMax;

    public int getValue(Attribute attr) {
        switch (attr) {
            case null:
                return this.ATK;
            case MaxHP:
                return this.MaxHP;
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
        }
        return 0;
    }

    public boolean Assert() {
        RefArtifice ref = maxLevelMap.get(Integer.valueOf(this.Level));

        if (ref == null) {
            maxLevelMap.put(Integer.valueOf(this.Level), this);
        } else if (ref != null && ref.Stars < this.Stars) {
            maxLevelMap.put(Integer.valueOf(this.Level), this);
        }

        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

