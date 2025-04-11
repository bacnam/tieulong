package com.jolbox.bonecp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionTesterThread
        implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ConnectionTesterThread.class);
    private long idleConnectionTestPeriodInMs;
    private long idleMaxAgeInMs;
    private ConnectionPartition partition;
    private ScheduledExecutorService scheduler;
    private BoneCP pool;
    private boolean lifoMode;

    protected ConnectionTesterThread(ConnectionPartition connectionPartition, ScheduledExecutorService scheduler, BoneCP pool, long idleMaxAgeInMs, long idleConnectionTestPeriodInMs, boolean lifoMode) {
        this.partition = connectionPartition;
        this.scheduler = scheduler;
        this.idleMaxAgeInMs = idleMaxAgeInMs;
        this.idleConnectionTestPeriodInMs = idleConnectionTestPeriodInMs;
        this.pool = pool;
        this.lifoMode = lifoMode;
    }

    public void run() {
        ConnectionHandle connection = null;

        try {
            long nextCheckInMs = this.idleConnectionTestPeriodInMs;
            if (this.idleMaxAgeInMs > 0L) {
                if (this.idleConnectionTestPeriodInMs == 0L) {
                    nextCheckInMs = this.idleMaxAgeInMs;
                } else {
                    nextCheckInMs = Math.min(nextCheckInMs, this.idleMaxAgeInMs);
                }
            }

            int partitionSize = this.partition.getAvailableConnections();
            long currentTimeInMs = System.currentTimeMillis();

            int i = 0;
            while (true) {
                if (i < partitionSize) {

                    connection = (ConnectionHandle) this.partition.getFreeConnections().poll();
                    if (connection != null) {
                        connection.setOriginatingPartition(this.partition);

                        if (connection.isPossiblyBroken() || (this.idleMaxAgeInMs > 0L && this.partition.getAvailableConnections() >= this.partition.getMinConnections() && System.currentTimeMillis() - connection.getConnectionLastUsedInMs() > this.idleMaxAgeInMs)) {
                            closeConnection(connection);

                        } else if (this.idleConnectionTestPeriodInMs > 0L && currentTimeInMs - connection.getConnectionLastUsedInMs() > this.idleConnectionTestPeriodInMs && currentTimeInMs - connection.getConnectionLastResetInMs() >= this.idleConnectionTestPeriodInMs) {
                            if (!this.pool.isConnectionHandleAlive(connection)) {
                                closeConnection(connection);
                            } else {
                                long tmp = this.idleConnectionTestPeriodInMs;
                                if (this.idleMaxAgeInMs > 0L) {
                                    tmp = Math.min(tmp, this.idleMaxAgeInMs);
                                }

                                if (tmp < nextCheckInMs)
                                    nextCheckInMs = tmp;
                            }
                        } else {
                            long tmp = this.idleConnectionTestPeriodInMs - currentTimeInMs - connection.getConnectionLastResetInMs();
                            long tmp2 = this.idleMaxAgeInMs - currentTimeInMs - connection.getConnectionLastUsedInMs();
                            if (this.idleMaxAgeInMs > 0L) tmp = Math.min(tmp, tmp2);
                            if (tmp < nextCheckInMs) nextCheckInMs = tmp;
                        }

                    }
                } else {
                    break;
                }

                i++;
            }

            this.scheduler.schedule(this, nextCheckInMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            if (this.scheduler.isShutdown()) {
                logger.debug("Shutting down connection tester thread.");
            } else {
                logger.error("Connection tester thread interrupted", e);
            }
        }
    }

    private void closeConnection(ConnectionHandle connection) {
        if (connection != null)
            try {
                connection.internalClose();
            } catch (SQLException e) {
                logger.error("Destroy connection exception", e);
            } finally {
                this.pool.postDestroyConnection(connection);
            }
    }
}

