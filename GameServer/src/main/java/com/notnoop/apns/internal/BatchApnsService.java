package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

import java.util.Queue;
import java.util.concurrent.*;

public class BatchApnsService
        extends AbstractApnsService {
    private int batchWaitTimeInSec = 5;

    private int maxBatchWaitTimeInSec = 10;

    private long firstMessageArrivedTime;

    private ApnsConnection prototype;

    private Queue<ApnsNotification> batch = new ConcurrentLinkedQueue<ApnsNotification>();

    private ScheduledExecutorService scheduleService;

    private ScheduledFuture<?> taskFuture;
    private Runnable batchRunner = new SendMessagessBatch();

    public BatchApnsService(ApnsConnection prototype, ApnsFeedbackConnection feedback, int batchWaitTimeInSec, int maxBachWaitTimeInSec, ThreadFactory tf) {
        super(feedback);
        this.prototype = prototype;
        this.batchWaitTimeInSec = batchWaitTimeInSec;
        this.maxBatchWaitTimeInSec = maxBachWaitTimeInSec;
        this.scheduleService = new ScheduledThreadPoolExecutor(1, tf);
    }

    public void start() {
    }

    public void stop() {
        Utilities.close(this.prototype);
        if (this.taskFuture != null) {
            this.taskFuture.cancel(true);
        }
        this.scheduleService.shutdownNow();
    }

    public void testConnection() throws NetworkIOException {
        this.prototype.testConnection();
    }

    public void push(ApnsNotification message) throws NetworkIOException {
        if (this.batch.isEmpty()) {
            this.firstMessageArrivedTime = System.nanoTime();
        }

        long sincFirstMessageSec = (System.nanoTime() - this.firstMessageArrivedTime) / 1000L / 1000L / 1000L;

        if (this.taskFuture != null && sincFirstMessageSec < this.maxBatchWaitTimeInSec) {
            this.taskFuture.cancel(false);
        }

        this.batch.add(message);

        if (this.taskFuture == null || this.taskFuture.isDone())
            this.taskFuture = this.scheduleService.schedule(this.batchRunner, this.batchWaitTimeInSec, TimeUnit.SECONDS);
    }

    class SendMessagessBatch
            implements Runnable {
        public void run() {
            ApnsConnection newConnection = BatchApnsService.this.prototype.copy();
            try {
                ApnsNotification msg = null;
                while ((msg = BatchApnsService.this.batch.poll()) != null) {
                    try {
                        newConnection.sendMessage(msg);
                    } catch (NetworkIOException e) {
                    }
                }

            } finally {

                Utilities.close(newConnection);
            }
        }
    }
}

