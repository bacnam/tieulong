package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;

public class RefTreasureWarspirit extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public NumberRange LevelRange;
    public ArrayList<Integer> NormalIdList;
    public ArrayList<Integer> NormalCountList;
    public ArrayList<Integer> NormalWeightList;
    public ArrayList<Integer> FixedTenIdList;
    public ArrayList<Integer> FixedTenCountList;
    public ArrayList<Integer> FixedTenWeightList;
    public ArrayList<Integer> LeastIdList;
    public ArrayList<Integer> LeastTenCountList;
    public ArrayList<Integer> LeastTenWeightList;
    public int UniformId;
    public int UniformCount;
    public int Price;
    public int TenPrice;

    @Override
    public boolean Assert() {
        if (!RefAssert.listSize(this.NormalIdList, this.NormalCountList, this.NormalWeightList)) {
            CommLog.error("normal");
            return false;
        } else if (!RefAssert.listSize(this.FixedTenIdList, this.FixedTenCountList, this.FixedTenWeightList)) {
            CommLog.error("fix");
            return false;
        } else if (!RefAssert.listSize(this.LeastIdList, this.LeastTenCountList, this.LeastTenWeightList)) {
            CommLog.error("least");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }

    public static RefTreasureWarspirit getByLevel(int level) {
        for (RefTreasureWarspirit ref : RefDataMgr.getAll(RefTreasureWarspirit.class).values()) {
            if (ref.LevelRange.within(level)) {
                return ref;
            }
        }

        return null;
    }
}
