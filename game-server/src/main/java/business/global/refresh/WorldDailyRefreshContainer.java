package business.global.refresh;

import BaseCommon.CommLog;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.mgr.daily.IDailyRefresh;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyWorldRefresh;
import core.database.game.bo.DailyWorldRefreshBO;
import java.util.List;

public class WorldDailyRefreshContainer extends IDailyRefresh<WorldDailyRefresh> {
    private static WorldDailyRefreshContainer instance = new WorldDailyRefreshContainer();
    private DailyWorldRefreshBO bo = null;

    public static WorldDailyRefreshContainer getInstance() {
        return instance;
    }

    public WorldDailyRefreshContainer() {
        this.init();
    }

    public void init() {
        try {
            List<DailyWorldRefreshBO> bos = BM.getBM(DailyWorldRefreshBO.class).findAll();
            if (bos.size() == 0) {
                this.bo = new DailyWorldRefreshBO();
                this.bo.setServerStart(CommTime.getTodayZeroClockS());
                this.bo.insert();
            } else {
                this.bo = bos.get(0);
            }
        } catch (Exception var2) {
            CommLog.error("load daily world refresh db error:", (Throwable)var2);
        }

        this.reload();
    }

    @Override
    public synchronized void gm_reset() {
        BM.getBM(DailyWorldRefreshBO.class).delAll();
        this.init();
    }

    @Override
    public synchronized void reload() {
        CommLog.warn("WorldDailyRefreshMgr reload!!!");
        this.refreshList.clear();
        this.refreshMap.clear();

        try {
            for (RefDailyWorldRefresh data : RefDataMgr.getAll(RefDailyWorldRefresh.class).values()) {
                if (data.Index > this.bo.getIndexLastTimeSize()) {
                    CommLog.error("reload RefDailyWorldRefresh index:{} > {}", data.Index, this.bo.getIndexLastTimeSize());
                } else {
                    WorldDailyRefresh dailyRefresh = new WorldDailyRefresh(data, this);
                    dailyRefresh.fixTime();
                    this.refreshList.add(dailyRefresh);
                    this.refreshMap.put(dailyRefresh.ref.getEventTypes(), dailyRefresh);
                }
            }
        } catch (Exception var4) {
            CommLog.error("load daily world refresh db error:", (Throwable)var4);
        }
    }

    @Override
    public int getLastRefreshTime(int index) {
        return this.bo.getIndexLastTime(index);
    }

    @Override
    public void setLastRefreshTime(int index, int nextRefreshTime) {
        this.bo.saveIndexLastTime(index, nextRefreshTime);
    }
}
