package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import core.config.refdata.RefDataMgr;

public class RefCrystalPrice
        extends RefBaseGame {
    @RefField(isfield = false)
    private static int max = 0;
    @RefField(iskey = true)
    public int id;
    public int ArenaResetFightCD;
    public int ArenaResetRefreshCD;
    public int ArenaAddChallenge;
    public int DroiyanSearch;
    public int EquipInstanceAddChallenge;
    public int GemInstanceAddChallenge;
    public int MeridianInstanceAddChallenge;
    public int ActivityInstanceAddChallenge;
    public int PackageBuyTimes;
    public int StealGoldPrice;
    public int StealGoldGain;
    public int GuildBossBuyChallenge;
    public int GuildWarRebirth;
    public int GuildBossOpen;
    public int SacrificeCost;
    public int SacrificeExp;
    public int SacrificeDonate;
    public int LoversSend;
    public int GuildwarInspire;
    public int GuildwarInspireValue;
    public int LongnvDonate;
    public int LongnvCrystal;
    public int LongnvCrystalExp;
    public int LongnvRebirth;
    public int AutoFightWorldboss;

    public static RefCrystalPrice getPrize(int times) {
        if (times > max)
            times = max;
        return (RefCrystalPrice) RefDataMgr.get(RefCrystalPrice.class, Integer.valueOf(times));
    }

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        max = all.size() - 1;

        return true;
    }
}

