package core.config.refdata.ref;

import com.google.common.collect.Lists;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Achievement;

import java.util.List;
import java.util.Map;

public class RefRecharge
        extends RefBaseGame {
    @RefField(isfield = false)
    public static List<Integer> rechargePrice = Lists.newArrayList();
    @RefField(iskey = true)
    public String id;
    public int Price;
    public int First;
    public int Crystal;
    public int VipExp;
    public Achievement.AchievementType RebateAchievement;
    public int RebateDays;
    public int RepurchaseDaysLimit;
    public int AddPackage;
    public int CostFreeCount;
    public String Icon;
    public String BgTexture;
    public String Title;
    public String ContentBefore;
    public String ContentAfter;
    public String LimitDesc;
    public String ThridProductId;
    @RefField(isfield = false)
    private Map<String, String> _thirdProductId;

    public String getThridProductId(String chennal) {
        String idString = this._thirdProductId.get(chennal);
        return (idString == null) ? "" : idString;
    }

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

