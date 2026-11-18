package core.config.refdata.ref;

import business.player.feature.character.PowerUtils;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.utils.CommMath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefWarSpiritLv extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int Level;
    public int Star;
    public String Name;
    public int Exp;
    public List<Attribute> AttrTypeList;
    public List<Integer> AttrValueList;
    public int UniformId;
    public int UniformCount;
    public int GainExp;
    public List<Integer> ExpCrit;
    public List<Integer> ExpCritWeight;
    public static Map<Integer, List<RefWarSpiritLv>> sameLevel = new HashMap<>();
    public int Power = 0;

    @Override
    public boolean Assert() {
        if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList)) {
            return false;
        } else if (!RefAssert.listSize(this.ExpCrit, this.ExpCritWeight)) {
            return false;
        } else {
            if (sameLevel.get(this.Level) == null) {
                sameLevel.put(this.Level, new ArrayList<>());
            }

            if (this.id != 0) {
                sameLevel.get(this.Level).add(this);
                this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
            }

            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }

    public int getCrit() {
        int index = CommMath.getRandomIndexByRate(this.ExpCritWeight);
        return this.ExpCrit.get(index);
    }
}
