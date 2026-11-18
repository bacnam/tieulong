package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import java.util.List;

public class RefGuildSkillLevel extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int SkillID;
    public int SkillLevel;
    public int ResearchCost;
    public int NeedGuildLevel;
    public List<Integer> UpgradeCostList;
    public List<Integer> CostItemList;

    @Override
    public boolean Assert() {
        return !RefAssert.inRef(this.SkillID, RefGuildSkill.class) ? false : RefAssert.listSize(this.UpgradeCostList, this.CostItemList);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
