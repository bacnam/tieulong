package com.zhonglian.server.common.mgr.daily;

import BaseCommon.CommLog;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.utils.CommTime;

public abstract class BaseDailyRefreshEvent {
    final int ONEDAY = 86400;
    final int ONEWEEK = 604800;
    public IDailyRefreshRef ref;
    protected IDailyRefresh<?> dailyRefresh;

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
            case null:
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
                break;
        }

        return firstTime;
    }

    public int getInitLastSec() {
        int ret = -1;

        int curSec = CommTime.nowSecond();
        int interval = this.ref.getInterval();
        int firstTime = getStartReferSec(curSec);

        if (firstTime > curSec) {

            ret = firstTime - interval;
        } else {

            int passTimes = (curSec - firstTime) / interval;
            ret = firstTime + passTimes * interval;
        }

        return ret;
    }

    public void fixTime() {
        int lastRefresh = getLastRefreshTime();

        if (lastRefresh == 0) {
            setLastRefreshTime(getInitLastSec());

            return;
        }

        boolean needFix = false;
        int interval = this.ref.getInterval();
        int newLastRefreshTime = 0;

        int thisRefreshTime = getStartReferSec(CommTime.nowSecond());

        if (interval == 86400) {
            newLastRefreshTime = CommTime.getZeroClockS(lastRefresh) + this.ref.getFirstSec();
            needFix = ((thisRefreshTime - lastRefresh) % interval != 0);
        } else if (interval == 604800) {
            newLastRefreshTime = (int) (CommTime.getFirstDayOfWeekZeroClockMS(lastRefresh * 1000L) / 1000L) + this.ref.getFirstSec();
            needFix = ((thisRefreshTime - lastRefresh) % interval != 0);
        }

        if (!needFix) {
            return;
        }
        setLastRefreshTime(newLastRefreshTime);
        String oldTime = CommTime.getTimeStringS(lastRefresh);
        String newTime = CommTime.getTimeStringS(newLastRefreshTime);
        CommLog.info("fixTime index:{}, oldLast:{}, newLast{}", new Object[]{Integer.valueOf(this.ref.getIndex()), oldTime, newTime});
    }

    public void process(int curSec) {
        int lastRefreshTime = getLastRefreshTime();
        int trigTimes = (curSec - lastRefreshTime) / this.ref.getInterval();

        if (trigTimes > 0) {

            setLastRefreshTime(lastRefreshTime + trigTimes * this.ref.getInterval());
            onTriger(trigTimes);
        }
    }

    public void onTriger(int trigTimes) {
        IDailyRefreshRef.DailyRefreshEventType event = this.ref.getEventTypes();

        long u1 = CommTime.nowMS();
        try {
            doEvent(trigTimes);
        } catch (Exception e) {
            CommLog.error("{}:onTriger id:{} event:{} time:{} times:{}", new Object[]{getClass().getSimpleName(), Integer.valueOf(this.ref.getIndex()),
                    (event == null) ? "null" : event.toString(), CommTime.getNowTimeString(), Integer.valueOf(trigTimes), e});
        }

        long u2 = CommTime.nowMS();
        if (u2 - u1 > 50L) {
            CommLog.warn("daily refresh {} event:{} use {} ms", new Object[]{getClass().getSimpleName(), event, Long.valueOf(u2 - u1)});
        }
    }

    public int getClosestRefreshTime() {
        int closestRefreshTime = getNextRefreshTime() - CommTime.nowSecond();
        closestRefreshTime = (closestRefreshTime < 0) ? (closestRefreshTime % this.ref.getInterval()) : closestRefreshTime;
        return closestRefreshTime;
    }

    public int getLastRefreshTime() {
        return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex());
    }

    public void setLastRefreshTime(int nextRefreshTime) {
        this.dailyRefresh.setLastRefreshTime(this.ref.getIndex(), nextRefreshTime);
    }

    public int getNextRefreshTime() {
        return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex()) + this.ref.getInterval();
    }

    public abstract void doEvent(int paramInt);
}

