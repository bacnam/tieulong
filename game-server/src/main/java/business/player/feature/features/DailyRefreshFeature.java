package business.player.feature.features;

import BaseCommon.CommLog;
import business.player.Player;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.mgr.daily.IDailyRefresh;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyPlayerRefresh;
import core.database.game.bo.DailyPlayerRefreshBO;

public class DailyRefreshFeature extends IDailyRefresh<PlayerDailyRefresh> {
    private DailyPlayerRefreshBO bo = null;
    private Player player;

    public DailyRefreshFeature(Player player) {
        this.player = player;
        this.init();
    }

    public void init() {
        try {
            this.bo = BM.getBM(DailyPlayerRefreshBO.class).findOne("pid", this.player.getPid());
            if (this.bo == null) {
                this.bo = new DailyPlayerRefreshBO();
                this.bo.setPid(this.player.getPid());
                this.bo.insert();
            }
        } catch (Exception var2) {
            CommLog.error("load daily player refresh db error:", (Throwable)var2);
        }

        this.reload();
    }

    @Override
    public synchronized void gm_reset() {
        this.bo.del();
        this.init();
    }

    @Override
    public synchronized void reload() {
        this.refreshList.clear();
        this.refreshMap.clear();
        boolean needSave = false;

        try {
            for (RefDailyPlayerRefresh data : RefDataMgr.getAll(RefDailyPlayerRefresh.class).values()) {
                if (data.Index > this.bo.getIndexLastTimeSize()) {
                    CommLog.error("reload RefDailyPlayerRefresh index:{} > {}", data.Index, this.bo.getIndexLastTimeSize());
                } else {
                    PlayerDailyRefresh dailyRefresh = new PlayerDailyRefresh(data, this);
                    if (dailyRefresh.getLastRefreshTime() == 0) {
                        needSave = true;
                        this.bo.setIndexLastTime(data.Index, dailyRefresh.getInitLastSec());
                    } else {
                        dailyRefresh.fixTime();
                    }

                    this.refreshList.add(dailyRefresh);
                    this.refreshMap.put(dailyRefresh.ref.getEventTypes(), dailyRefresh);
                }
            }
        } catch (Exception var5) {
            CommLog.error("load daily player refresh db error:", (Throwable)var5);
        }

        if (needSave) {
            this.bo.saveAll();
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

    public Player getPlayer() {
        return this.player;
    }
}
