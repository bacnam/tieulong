package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.AchieveClassify;
import com.zhonglian.server.common.enums.ConstEnum.AchieveReset;
import com.zhonglian.server.common.enums.ConstEnum.TaskClassify;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefAchievement extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public AchievementType AchieveName;
    public AchieveClassify AchieveType;
    public ArrayList<Integer> PrizeIDList;
    public ArrayList<Integer> FirstArgsList;
    public ArrayList<Integer> SecondArgsList;
    public int Condition;
    public TaskClassify TaskType;
    public int IsConditionWork;
    public int ConditionPreTaskId;
    public AchieveReset Reset;
    public int ActiveScore;
    public int Days;
    public boolean IsHide;
    @RefField(
        isfield = false
    )
    public static Map<AchievementType, RefAchievement> achieveName2ref = new HashMap<>();

    public static RefAchievement getByType(AchievementType type) {
        RefAchievement ref = achieveName2ref.get(type);
        if (ref == null) {
            CommLog.warn("RefAchievement not find type:" + type);
        }

        return ref;
    }

    @Override
    public boolean Assert() {
        achieveName2ref.put(this.AchieveName, this);
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
