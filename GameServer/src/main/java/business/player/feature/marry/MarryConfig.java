package business.player.feature.marry;

import core.config.refdata.RefDataMgr;

public class MarryConfig {
    public static int getSendFlowerExp() {
        return RefDataMgr.getFactor("SendFlowerExp");
    }

    public static int getMarryDivorceApplyCostId() {
        return RefDataMgr.getFactor("MarryDivorceApplyCostId");
    }

    public static int getMarryDivorceApplyCostCount() {
        return RefDataMgr.getFactor("MarryDivorceApplyCostCount");
    }

    public static int getMarryDivorceForceApplyCost() {
        return RefDataMgr.getFactor("MarryDivorceForceApplyCost");
    }

    public static int getForceDivorceVip() {
        return RefDataMgr.getFactor("ForceDivorceVip");
    }

    public static int getMarryApplyTime() {
        return RefDataMgr.getFactor("MarryApplyTime");
    }

    public static int getMarryApplyCostId() {
        return RefDataMgr.getFactor("MarryApplyCostId");
    }

    public static int getMarryApplyCostCount() {
        return RefDataMgr.getFactor("MarryApplyCostCount");
    }

    public static int getMarryList() {
        return RefDataMgr.getFactor("MarryList");
    }

    public static int getMarryReceiveApply() {
        return RefDataMgr.getFactor("MarryReceiveApply");
    }

    public static int getMarryAllReward() {
        return RefDataMgr.getFactor("MarryAllReward");
    }

    public static int getMarryReward() {
        return RefDataMgr.getFactor("MarryReward", 400001);
    }
}

