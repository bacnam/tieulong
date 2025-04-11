package core.config.refdata.ref;

import com.google.common.collect.Lists;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

import java.util.ArrayList;
import java.util.List;

public class RefItem
        extends RefBaseGame {
    public static List<RefItem> refineStone = Lists.newArrayList();
    @RefField(iskey = true)
    public int ID;
    public String Name;
    public String IconID;
    public boolean CanUse;
    public boolean UseNow;
    public ArrayList<Integer> RewardList;
    public String Description;
    public int SuccessRatio;
    public int CarryExp;

    public static List<RefItem> getRefineStoneList() {
        return refineStone;
    }

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

