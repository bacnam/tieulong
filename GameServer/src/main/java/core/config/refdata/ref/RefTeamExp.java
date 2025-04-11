package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefTeamExp
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int UpExp;
    public int EnergyToExpRate;
    public int AddCurrEnergy;
    public int MaxEnergy;
    public int TokensToGoldRate;
    public int MaxTokens;
    public int TotalBattleNumber;
    public int TotalLittleFriend;
    public int LevelRewardID;

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        if (all.size() == 0) {
            CommLog.error("[teanExp]配表不能为空");
            return false;
        }
        return true;
    }
}

