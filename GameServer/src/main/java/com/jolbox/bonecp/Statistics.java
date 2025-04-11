package com.jolbox.bonecp;

import java.util.concurrent.atomic.AtomicLong;

public class Statistics
        implements StatisticsMBean {
    private final AtomicLong cacheHits = new AtomicLong(0L);

    private final AtomicLong cacheMiss = new AtomicLong(0L);

    private final AtomicLong statementsCached = new AtomicLong(0L);

    private final AtomicLong connectionsRequested = new AtomicLong(0L);

    private final AtomicLong cumulativeConnectionWaitTime = new AtomicLong(0L);

    private final AtomicLong cumulativeStatementExecuteTime = new AtomicLong(0L);

    private final AtomicLong cumulativeStatementPrepareTime = new AtomicLong(0L);

    private final AtomicLong statementsExecuted = new AtomicLong(0L);

    private final AtomicLong statementsPrepared = new AtomicLong(0L);

    private BoneCP pool;

    public Statistics(BoneCP pool) {
        this.pool = pool;
    }

    public void resetStats() {
        this.cacheHits.set(0L);
        this.cacheMiss.set(0L);
        this.statementsCached.set(0L);
        this.connectionsRequested.set(0L);
        this.cumulativeConnectionWaitTime.set(0L);
        this.cumulativeStatementExecuteTime.set(0L);
        this.cumulativeStatementPrepareTime.set(0L);
        this.statementsExecuted.set(0L);
        this.statementsPrepared.set(0L);
    }

    public double getConnectionWaitTimeAvg() {
        return (this.connectionsRequested.get() == 0L) ? 0.0D : (this.cumulativeConnectionWaitTime.get() / 1.0D * this.connectionsRequested.get() / 1000000.0D);
    }

    public double getStatementExecuteTimeAvg() {
        return (this.statementsExecuted.get() == 0L) ? 0.0D : (this.cumulativeStatementExecuteTime.get() / 1.0D * this.statementsExecuted.get() / 1000000.0D);
    }

    public double getStatementPrepareTimeAvg() {
        return (this.statementsPrepared.get() == 0L) ? 0.0D : (this.statementsPrepared.get() / 1.0D * this.cumulativeStatementPrepareTime.get() / 1000000.0D);
    }

    public int getTotalLeased() {
        return this.pool.getTotalLeased();
    }

    public int getTotalFree() {
        return this.pool.getTotalFree();
    }

    public int getTotalCreatedConnections() {
        return this.pool.getTotalCreatedConnections();
    }

    public long getCacheHits() {
        return this.cacheHits.get();
    }

    public long getCacheMiss() {
        return this.cacheMiss.get();
    }

    public long getStatementsCached() {
        return this.statementsCached.get();
    }

    public long getConnectionsRequested() {
        return this.connectionsRequested.get();
    }

    public long getCumulativeConnectionWaitTime() {
        return this.cumulativeConnectionWaitTime.get() / 1000000L;
    }

    protected void addCumulativeConnectionWaitTime(long increment) {
        this.cumulativeConnectionWaitTime.addAndGet(increment);
    }

    protected void incrementStatementsExecuted() {
        this.statementsExecuted.incrementAndGet();
    }

    protected void incrementStatementsPrepared() {
        this.statementsPrepared.incrementAndGet();
    }

    protected void incrementStatementsCached() {
        this.statementsCached.incrementAndGet();
    }

    protected void incrementCacheMiss() {
        this.cacheMiss.incrementAndGet();
    }

    protected void incrementCacheHits() {
        this.cacheHits.incrementAndGet();
    }

    protected void incrementConnectionsRequested() {
        this.connectionsRequested.incrementAndGet();
    }

    public double getCacheHitRatio() {
        return (this.cacheHits.get() + this.cacheMiss.get() == 0L) ? 0.0D : (this.cacheHits.get() / (1.0D * this.cacheHits.get() + this.cacheMiss.get()));
    }

    public long getStatementsExecuted() {
        return this.statementsExecuted.get();
    }

    public long getCumulativeStatementExecutionTime() {
        return this.cumulativeStatementExecuteTime.get() / 1000000L;
    }

    protected void addStatementExecuteTime(long time) {
        this.cumulativeStatementExecuteTime.addAndGet(time);
    }

    protected void addStatementPrepareTime(long time) {
        this.cumulativeStatementPrepareTime.addAndGet(time);
    }

    public long getCumulativeStatementPrepareTime() {
        return this.cumulativeStatementPrepareTime.get() / 1000000L;
    }

    public long getStatementsPrepared() {
        return this.statementsPrepared.get();
    }
}

