package com.jolbox.bonecp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionMaxAgeThread
        implements Runnable {
    protected static Logger logger = LoggerFactory.getLogger(ConnectionTesterThread.class);
    private long maxAgeInMs;
    private ConnectionPartition partition;
    private ScheduledExecutorService scheduler;
    private BoneCP pool;
    private boolean lifoMode;

    protected ConnectionMaxAgeThread(ConnectionPartition connectionPartition, ScheduledExecutorService scheduler, BoneCP pool, long maxAgeInMs, boolean lifoMode) {
        this.partition = connectionPartition;
        this.scheduler = scheduler;
        this.maxAgeInMs = maxAgeInMs;
        this.pool = pool;
        this.lifoMode = lifoMode;
    }

    public void run() {
        ConnectionHandle connection = null;

        long nextCheckInMs = this.maxAgeInMs;

        int partitionSize = this.partition.getAvailableConnections();
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < partitionSize; i++) {
            try {
                connection = (ConnectionHandle) this.partition.getFreeConnections().poll();

                if (connection != null) {
                    connection.setOriginatingPartition(this.partition);

                    long tmp = this.maxAgeInMs - currentTime - connection.getConnectionCreationTimeInMs();

                    if (tmp < nextCheckInMs) {
                        nextCheckInMs = tmp;
                    }

                    if (connection.isExpired(currentTime)) {

                        closeConnection(connection);

                    } else {

                        if (this.lifoMode) {

                            if (!((LIFOQueue<ConnectionHandle>) connection.getOriginatingPartition().getFreeConnections()).offerLast(connection)) {
                                connection.internalClose();
                            }
                        } else {
                            this.pool.putConnectionBackInPartition(connection);
                        }

                        Thread.sleep(20L);
                    }
                }
            } catch (Exception e) {
                if (this.scheduler.isShutdown()) {
                    logger.debug("Shutting down connection max age thread.");
                    break;
                }
                logger.error("Connection max age thread exception.", e);
            }
        }

        if (!this.scheduler.isShutdown()) {
            this.scheduler.schedule(this, nextCheckInMs, TimeUnit.MILLISECONDS);
        }
    }

    protected void closeConnection(ConnectionHandle connection) {
        if (connection != null)
            try {
                connection.internalClose();
            } catch (Throwable t) {
                logger.error("Destroy connection exception", t);
            } finally {
                this.pool.postDestroyConnection(connection);
            }
    }
}

