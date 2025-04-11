package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.ConstEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefAchievement
        extends RefBaseGame {
    @RefField(isfield = false)
    public static Map<Achievement.AchievementType, RefAchievement> achieveName2ref = new HashMap<>();
    @RefField(iskey = true)
    public int id;
    public Achievement.AchievementType AchieveName;
    public ConstEnum.AchieveClassify AchieveType;
    public ArrayList<Integer> PrizeIDList;
    public ArrayList<Integer> FirstArgsList;
    public ArrayList<Integer> SecondArgsList;
    public int Condition;
    public ConstEnum.TaskClassify TaskType;
    public int IsConditionWork;
    public int ConditionPreTaskId;
    public ConstEnum.AchieveReset Reset;
    public int ActiveScore;
    public int Days;
    public boolean IsHide;

    public static RefAchievement getByType(Achievement.AchievementType type) {
        RefAchievement ref = achieveName2ref.get(type);
        if (ref == null) {
            CommLog.warn("RefAchievement not find type:" + type);
        }
        return ref;
    }

    public boolean Assert() {
        achieveName2ref.put(this.AchieveName, this);
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

