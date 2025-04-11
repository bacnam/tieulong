package com.jolbox.bonecp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

public class StatementReleaseHelperThread
        implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(StatementReleaseHelperThread.class);
    private BlockingQueue<StatementHandle> queue;
    private BoneCP pool;

    public StatementReleaseHelperThread(BlockingQueue<StatementHandle> queue, BoneCP pool) {
        this.queue = queue;
        this.pool = pool;
    }

    public void run() {
        boolean interrupted = false;
        while (!interrupted) {
            try {
                StatementHandle statement = this.queue.take();

                statement.closeStatement();
            } catch (InterruptedException e) {
                if (this.pool.poolShuttingDown) {
                    StatementHandle statement;

                    while ((statement = this.queue.poll()) != null) {
                        try {
                            statement.closeStatement();
                        } catch (SQLException e1) {
                        }
                    }
                }

                interrupted = true;
            } catch (Exception e) {
                logger.error("Could not close statement.", e);
            }
        }
    }
}

