package com.zhonglian.server.common.mgr.daily;

import com.zhonglian.server.common.utils.CommTime;

import java.util.*;

public abstract class IDailyRefresh<Refresh extends BaseDailyRefreshEvent> {
    protected List<Refresh> refreshList = new ArrayList<>();
    protected Map<IDailyRefreshRef.DailyRefreshEventType, Refresh> refreshMap = new HashMap<>();

    public synchronized void process(int curSec) {
        List<Refresh> toDeals = new ArrayList<>();
        for (BaseDailyRefreshEvent baseDailyRefreshEvent : this.refreshList) {
            boolean needDeal = (curSec >= baseDailyRefreshEvent.getNextRefreshTime());
            if (needDeal) {
                toDeals.add((Refresh) baseDailyRefreshEvent);
            }
        }
        if (toDeals.size() == 0) {
            return;
        }

        Collections.sort(toDeals, (o1, o2) -> {
            Refresh event1 = o1;

            Refresh event2 = o2;

            int diff = event1.getNextRefreshTime() - event2.getNextRefreshTime();

            return (diff != 0) ? diff : (((BaseDailyRefreshEvent) event1).ref.getIndex() - ((BaseDailyRefreshEvent) event2).ref.getIndex());
        });

        for (BaseDailyRefreshEvent baseDailyRefreshEvent : toDeals) {
            baseDailyRefreshEvent.process(curSec);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(getClass().getSimpleName()) + " Refresh:").append(System.lineSeparator());
        sb.append(String.format("%-10s%-30s%-10s%-10s%s", new Object[]{"Index", "lastTime", "interval", "nextTime", "Comment"})).append(System.lineSeparator());
        for (BaseDailyRefreshEvent refresh : this.refreshList) {
            String lastTime = CommTime.getTimeStringS(refresh.getLastRefreshTime());
            String nextTime = CommTime.getTimeString((refresh.getLastRefreshTime() + refresh.ref.getInterval()));

            sb.append(String.format("%-10s%-30s%-10s%-10s%s", new Object[]{Integer.valueOf(refresh.ref.getIndex()), lastTime, Integer.valueOf(refresh.ref.getInterval()), nextTime, refresh.ref.getComment()
            })).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public Refresh getEvent(IDailyRefreshRef.DailyRefreshEventType event) {
        return this.refreshMap.get(event);
    }

    public int getNextFireTime(IDailyRefreshRef.DailyRefreshEventType event) {
        BaseDailyRefreshEvent refresh = (BaseDailyRefreshEvent) getEvent(event);
        if (refresh != null) {
            return refresh.getClosestRefreshTime();
        }
        return -1;
    }

    public abstract void setLastRefreshTime(int paramInt1, int paramInt2);

    public abstract int getLastRefreshTime(int paramInt);

    public abstract void gm_reset();

    public abstract void reload();
}

