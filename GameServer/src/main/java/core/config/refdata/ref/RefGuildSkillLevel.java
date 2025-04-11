package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

import java.util.List;

public class RefGuildSkillLevel
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int SkillID;
    public int SkillLevel;
    public int ResearchCost;
    public int NeedGuildLevel;
    public List<Integer> UpgradeCostList;
    public List<Integer> CostItemList;

    public boolean Assert() {
        if (!RefAssert.inRef(Integer.valueOf(this.SkillID), RefGuildSkill.class, new Object[0])) {
            return false;
        }

        if (!RefAssert.listSize(this.UpgradeCostList, this.CostItemList, new List[0])) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

