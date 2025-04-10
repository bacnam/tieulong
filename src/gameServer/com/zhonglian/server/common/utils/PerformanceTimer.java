package com.zhonglian.server.common.utils;

public class PerformanceTimer
{
private long _begin = System.nanoTime();

public void reset() {
this._begin = System.nanoTime();
}

public long get() {
return (System.nanoTime() - this._begin) / 1000000L;
}

public long getNano() {
return System.nanoTime() - this._begin;
}
}

