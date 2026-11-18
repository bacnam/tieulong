package business.global.refresh;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
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
            @Override
            public void run() {
                int wait = RefreshMgr.INTERVAL;

                try {
                    wait = RefreshMgr.this.process();
                } catch (Exception var3) {
                    CommLog.error("RefreshMgr.init", (Throwable)var3);
                }

                SyncTaskManager.task(this, wait);
            }
        }, INTERVAL);
    }

    public int process() {
        int curSec = CommTime.nowSecond();

        try {
            this.checkGlobal(curSec);
            return 20000;
        } catch (Exception var3) {
            throw var3;
        }
    }

    public void checkGlobal(int curSec) {
        WorldDailyRefreshContainer.getInstance().process(curSec);
    }

    public void reload() {
        WorldDailyRefreshContainer.getInstance().reload();
    }
}
