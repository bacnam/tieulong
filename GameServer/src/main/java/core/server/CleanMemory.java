package core.server;

import BaseServer._ACleanMemory;
import business.player.PlayerMgr;
import com.zhonglian.server.common.utils.CommTime;

public class CleanMemory
        extends _ACleanMemory {
    private static CleanMemory instance = new CleanMemory();
    private int featureGCTime = 86400;

    public static CleanMemory GetInstance() {
        return instance;
    }

    public int getFeatureGCTime() {
        return this.featureGCTime;
    }

    public void setFeatureGCTime(int featureGCTime) {
        this.featureGCTime = featureGCTime;
    }

    public void run() {
        CommTime.RecentSec = CommTime.nowSecond();

        PlayerMgr.getInstance().releasPlayer(this.featureGCTime);
    }
}

