package business.global.refresh;

import BaseCommon.CommLog;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.mgr.daily.IDailyRefresh;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDailyWorldRefresh;
import core.database.game.bo.DailyWorldRefreshBO;
import java.util.List;

public class WorldDailyRefreshContainer
extends IDailyRefresh<WorldDailyRefresh>
{
private static WorldDailyRefreshContainer instance = new WorldDailyRefreshContainer();

public static WorldDailyRefreshContainer getInstance() {
return instance;
}

private DailyWorldRefreshBO bo = null;

public WorldDailyRefreshContainer() {
init();
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
} catch (Exception e) {
CommLog.error("load daily world refresh db error:", e);
} 

reload();
}

public synchronized void gm_reset() {
BM.getBM(DailyWorldRefreshBO.class).delAll();
init();
}

public synchronized void reload() {
CommLog.warn("WorldDailyRefreshMgr reload!!!");

this.refreshList.clear();
this.refreshMap.clear();

try {
for (RefDailyWorldRefresh data : RefDataMgr.getAll(RefDailyWorldRefresh.class).values()) {
if (data.Index > this.bo.getIndexLastTimeSize()) {
CommLog.error("reload RefDailyWorldRefresh index:{} > {}", Integer.valueOf(data.Index), Integer.valueOf(this.bo.getIndexLastTimeSize()));

continue;
} 
WorldDailyRefresh dailyRefresh = new WorldDailyRefresh((IDailyRefreshRef)data, this);
dailyRefresh.fixTime();
this.refreshList.add(dailyRefresh);
this.refreshMap.put(dailyRefresh.ref.getEventTypes(), dailyRefresh);
} 
} catch (Exception e) {
CommLog.error("load daily world refresh db error:", e);
} 
}

public int getLastRefreshTime(int index) {
return this.bo.getIndexLastTime(index);
}

public void setLastRefreshTime(int index, int nextRefreshTime) {
this.bo.saveIndexLastTime(index, nextRefreshTime);
}
}

