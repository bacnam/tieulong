package com.zhonglian.server.common.mgr.daily;

import java.util.List;

public interface IDailyRefreshRef
{
int getIndex();

String getComment();

StartRefer getStartRefer();

int getFirstSec();

int getInterval();

DailyRefreshEventType getEventTypes();

List<Integer> getEventValue();

public enum DailyRefreshEventType
{
None,
Day_h0,
Day_h4,
Day_h8,
Day_h9,
Day_h10,
Day_h11,
Day_h12,
Day_h13,
Day_h15,
Day_h16,
Day_h18,
Day_h19,
Day_h20,
Day_h21,
Day_h22,
Day_h14,
Week_d1h4,
Week_d1h0,
Every_m5,
Every_m15,
Every_m30,
Every_h1,
Every_h2,
Every_h3,
Every_h4,
Every_h6,
Every_h8,
Every_h12;
}

public enum StartRefer
{
NowWeek,
NowDay,
NowHour,
NowSec,
StartServerDay;
}
}

