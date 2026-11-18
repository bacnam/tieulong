package com.zhonglian.server.common.mgr.daily;

import BaseCommon.CommLog;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.utils.CommTime;

public abstract class BaseDailyRefreshEvent {
    final int ONEDAY = 86400;
    final int ONEWEEK = 604800;
    protected IDailyRefresh<?> dailyRefresh;
    public IDailyRefreshRef ref;

    public BaseDailyRefreshEvent(IDailyRefreshRef ref, IDailyRefresh<?> dailyRefresh) {
        this.ref = ref;
        this.dailyRefresh = dailyRefresh;
    }

    public int getStartReferSec(int curSec) {
        int firstTime = 0;
        int firstSec = this.ref.getFirstSec();
        switch (this.ref.getStartRefer()) {
            case NowWeek:
                firstTime = CommTime.getFirstDayOfWeekZeroClockS() + firstSec;
                break;
            case NowDay:
                firstTime = CommTime.getTodayZeroClockS() + firstSec;
                break;
            case NowHour:
                firstTime = CommTime.getTodayZeroClockS() + CommTime.getTodayHour() * 3600 + firstSec;
                break;
            case NowSec:
                firstTime = curSec + firstSec;
                break;
            case StartServerDay:
                firstTime = Config.getServerStartTime() + firstSec;
        }

        return firstTime;
    }

    public int getInitLastSec() {
        int ret = -1;
        int curSec = CommTime.nowSecond();
        int interval = this.ref.getInterval();
        int firstTime = this.getStartReferSec(curSec);
        if (firstTime > curSec) {
            ret = firstTime - interval;
        } else {
            int passTimes = (curSec - firstTime) / interval;
            ret = firstTime + passTimes * interval;
        }

        return ret;
    }

    public void fixTime() {
        int lastRefresh = this.getLastRefreshTime();
        if (lastRefresh == 0) {
            this.setLastRefreshTime(this.getInitLastSec());
        } else {
            boolean needFix = false;
            int interval = this.ref.getInterval();
            int newLastRefreshTime = 0;
            int thisRefreshTime = this.getStartReferSec(CommTime.nowSecond());
            if (interval == 86400) {
                newLastRefreshTime = CommTime.getZeroClockS(lastRefresh) + this.ref.getFirstSec();
                needFix = (thisRefreshTime - lastRefresh) % interval != 0;
            } else if (interval == 604800) {
                newLastRefreshTime = (int)(CommTime.getFirstDayOfWeekZeroClockMS(lastRefresh * 1000L) / 1000L) + this.ref.getFirstSec();
                needFix = (thisRefreshTime - lastRefresh) % interval != 0;
            }

            if (needFix) {
                this.setLastRefreshTime(newLastRefreshTime);
                String oldTime = CommTime.getTimeStringS(lastRefresh);
                String newTime = CommTime.getTimeStringS(newLastRefreshTime);
                CommLog.info("fixTime index:{}, oldLast:{}, newLast{}", this.ref.getIndex(), oldTime, newTime);
            }
        }
    }

    public void process(int curSec) {
        int lastRefreshTime = this.getLastRefreshTime();
        int trigTimes = (curSec - lastRefreshTime) / this.ref.getInterval();
        if (trigTimes > 0) {
            this.setLastRefreshTime(lastRefreshTime + trigTimes * this.ref.getInterval());
            this.onTriger(trigTimes);
        }
    }

    public void onTriger(int trigTimes) {
        IDailyRefreshRef.DailyRefreshEventType event = this.ref.getEventTypes();
        long u1 = CommTime.nowMS();

        try {
            this.doEvent(trigTimes);
        } catch (Exception var7) {
            CommLog.error(
                "{}:onTriger id:{} event:{} time:{} times:{}",
                this.getClass().getSimpleName(),
                this.ref.getIndex(),
                event == null ? "null" : event.toString(),
                CommTime.getNowTimeString(),
                trigTimes,
                var7
            );
        }

        long u2 = CommTime.nowMS();
        if (u2 - u1 > 50L) {
            CommLog.warn("daily refresh {} event:{} use {} ms", this.getClass().getSimpleName(), event, u2 - u1);
        }
    }

    public int getClosestRefreshTime() {
        int closestRefreshTime = this.getNextRefreshTime() - CommTime.nowSecond();
        return closestRefreshTime < 0 ? closestRefreshTime % this.ref.getInterval() : closestRefreshTime;
    }

    public void setLastRefreshTime(int nextRefreshTime) {
        this.dailyRefresh.setLastRefreshTime(this.ref.getIndex(), nextRefreshTime);
    }

    public int getLastRefreshTime() {
        return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex());
    }

    public int getNextRefreshTime() {
        return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex()) + this.ref.getInterval();
    }

    public abstract void doEvent(int var1);
}
