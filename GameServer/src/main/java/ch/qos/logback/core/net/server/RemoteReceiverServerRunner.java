package ch.qos.logback.core.net.server;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

class RemoteReceiverServerRunner
        extends ConcurrentServerRunner<RemoteReceiverClient> {
    private final int clientQueueSize;

    public RemoteReceiverServerRunner(ServerListener<RemoteReceiverClient> listener, Executor executor, int clientQueueSize) {
        super(listener, executor);
        this.clientQueueSize = clientQueueSize;
    }

    protected boolean configureClient(RemoteReceiverClient client) {
        client.setContext(getContext());
        client.setQueue(new ArrayBlockingQueue<Serializable>(this.clientQueueSize));
        return true;
    }
}

