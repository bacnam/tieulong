package business.global.refresh;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.common.utils.CommTime;

import java.util.LinkedList;
import java.util.List;

public class RefreshMgr {
    public static int INTERVAL = 3000;
    private static RefreshMgr instance = new RefreshMgr();
    public List<Long> cids = new LinkedList<>();

    public static RefreshMgr getInstance() {
        return instance;
    }

    public void init() {
        SyncTaskManager.task(new SyncTask() {
            public void run() {
                int wait = RefreshMgr.INTERVAL;
                try {
                    wait = RefreshMgr.this.process();
                } catch (Exception e) {
                    CommLog.error("RefreshMgr.init", e);
                }
                SyncTaskManager.task(this, wait);
            }
        }, INTERVAL);
    }

    public void gm_reset() {
        WorldDailyRefreshContainer.getInstance().gm_reset();
        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            if (player.isDailyRefreshLoaded())
                player.getDailyRefreshFeature().gm_reset();
        }
    }

    public void reload() {
        WorldDailyRefreshContainer.getInstance().reload();
        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            if (player.isDailyRefreshLoaded()) {
                player.getDailyRefreshFeature().reload();
            }
        }
    }

    public int process() {
        int curSec = CommTime.nowSecond();

        if (this.cids.size() > 0) {
            int cnt = 0;
            while (this.cids.size() > 0 && cnt < 10) {
                cnt++;
                Long cid = this.cids.remove(0);
                try {
                    checkPlayer(cid.longValue(), curSec);
                } catch (Exception e) {
                    CommLog.error("refresh cid:{}", cid, e);
                }
            }
        } else {

            try {

                checkGlobal(curSec);
            } catch (Exception e) {
                throw e;
            }

            this.cids.addAll(PlayerMgr.getInstance().getOnlinePlayersCid());
        }

        if (this.cids.size() > 0) {
            return 0;
        }
        int wait = 20;
        int next = (curSec + 20) % 60;

        if (next != 0 && next < 20) {
            wait = 20 - next;
        }
        return wait * 1000;
    }

    public void checkGlobal(int curSec) {
        WorldDailyRefreshContainer.getInstance().process(curSec);
    }

    public void checkPlayer(long cid, int curSec) {
        SyncTaskManager.task(() -> {
            Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(paramLong);
            if (player == null)
                return;
            player.getDailyRefreshFeature().process(paramInt);
        });
    }
}

