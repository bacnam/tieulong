package com.zhonglian.server.common.mgr.daily;

import com.zhonglian.server.common.utils.CommTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IDailyRefresh<Refresh extends BaseDailyRefreshEvent> {
    protected List<Refresh> refreshList = new ArrayList<>();
    protected Map<IDailyRefreshRef.DailyRefreshEventType, Refresh> refreshMap = new HashMap<>();

    public abstract void setLastRefreshTime(int var1, int var2);

    public abstract int getLastRefreshTime(int var1);

    public abstract void gm_reset();

    public abstract void reload();

    public synchronized void process(int curSec) {
        List<Refresh> toDeals = new ArrayList<>();

        for (Refresh event : this.refreshList) {
            boolean needDeal = curSec >= event.getNextRefreshTime();
            if (needDeal) {
                toDeals.add(event);
            }
        }

        if (toDeals.size() != 0) {
            Collections.sort(toDeals, (o1, o2) -> {
                int diff = o1.getNextRefreshTime() - o2.getNextRefreshTime();
                return diff != 0 ? diff : o1.ref.getIndex() - o2.ref.getIndex();
            });

            for (Refresh eventx : toDeals) {
                eventx.process(curSec);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName() + " Refresh:").append(System.lineSeparator());
        sb.append(String.format("%-10s%-30s%-10s%-10s%s", "Index", "lastTime", "interval", "nextTime", "Comment")).append(System.lineSeparator());

        for (BaseDailyRefreshEvent refresh : this.refreshList) {
            String lastTime = CommTime.getTimeStringS(refresh.getLastRefreshTime());
            String nextTime = CommTime.getTimeString(refresh.getLastRefreshTime() + refresh.ref.getInterval());
            sb.append(String.format("%-10s%-30s%-10s%-10s%s", refresh.ref.getIndex(), lastTime, refresh.ref.getInterval(), nextTime, refresh.ref.getComment()))
                .append(System.lineSeparator());
        }

        return sb.toString();
    }

    public Refresh getEvent(IDailyRefreshRef.DailyRefreshEventType event) {
        return this.refreshMap.get(event);
    }

    public int getNextFireTime(IDailyRefreshRef.DailyRefreshEventType event) {
        BaseDailyRefreshEvent refresh = this.getEvent(event);
        return refresh != null ? refresh.getClosestRefreshTime() : -1;
    }
}
