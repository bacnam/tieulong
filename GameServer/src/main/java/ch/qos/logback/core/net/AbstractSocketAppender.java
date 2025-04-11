package ch.qos.logback.core.net;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.PreSerializationTransformer;
import ch.qos.logback.core.util.CloseUtil;
import ch.qos.logback.core.util.Duration;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSocketAppender<E>
        extends AppenderBase<E>
        implements SocketConnector.ExceptionHandler {
    public static final int DEFAULT_PORT = 4560;
    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    public static final int DEFAULT_QUEUE_SIZE = 128;
    private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
    private static final int DEFAULT_EVENT_DELAY_TIMEOUT = 100;
    private final ObjectWriterFactory objectWriterFactory;
    private final QueueFactory queueFactory;
    private String remoteHost;
    private int port = 4560;
    private InetAddress address;
    private Duration reconnectionDelay = new Duration(30000L);
    private int queueSize = 128;
    private int acceptConnectionTimeout = 5000;
    private Duration eventDelayLimit = new Duration(100L);

    private BlockingDeque<E> deque;

    private String peerId;

    private SocketConnector connector;

    private Future<?> task;

    private volatile Socket socket;

    protected AbstractSocketAppender() {
        this(new QueueFactory(), new ObjectWriterFactory());
    }

    AbstractSocketAppender(QueueFactory queueFactory, ObjectWriterFactory objectWriterFactory) {
        this.objectWriterFactory = objectWriterFactory;
        this.queueFactory = queueFactory;
    }

    public void start() {
        if (isStarted())
            return;
        int errorCount = 0;
        if (this.port <= 0) {
            errorCount++;
            addError("No port was configured for appender" + this.name + " For more information, please visit http:");
        }

        if (this.remoteHost == null) {
            errorCount++;
            addError("No remote host was configured for appender" + this.name + " For more information, please visit http:");
        }

        if (this.queueSize == 0) {
            addWarn("Queue size of zero is deprecated, use a size of one to indicate synchronous processing");
        }

        if (this.queueSize < 0) {
            errorCount++;
            addError("Queue size must be greater than zero");
        }

        if (errorCount == 0) {
            try {
                this.address = InetAddress.getByName(this.remoteHost);
            } catch (UnknownHostException ex) {
                addError("unknown host: " + this.remoteHost);
                errorCount++;
            }
        }

        if (errorCount == 0) {
            this.deque = this.queueFactory.newLinkedBlockingDeque(this.queueSize);
            this.peerId = "remote peer " + this.remoteHost + ":" + this.port + ": ";
            this.connector = createConnector(this.address, this.port, 0, this.reconnectionDelay.getMilliseconds());
            this.task = getContext().getExecutorService().submit(new Runnable() {
                public void run() {
                    AbstractSocketAppender.this.connectSocketAndDispatchEvents();
                }
            });
            super.start();
        }
    }

    public void stop() {
        if (!isStarted())
            return;
        CloseUtil.closeQuietly(this.socket);
        this.task.cancel(true);
        super.stop();
    }

    protected void append(E event) {
        if (event == null || !isStarted())
            return;
        try {
            boolean inserted = this.deque.offer(event, this.eventDelayLimit.getMilliseconds(), TimeUnit.MILLISECONDS);
            if (!inserted) {
                addInfo("Dropping event due to timeout limit of [" + this.eventDelayLimit + "] being exceeded");
            }
        } catch (InterruptedException e) {
            addError("Interrupted while appending event to SocketAppender", e);
        }
    }

    private void connectSocketAndDispatchEvents() {
        try {
            while (socketConnectionCouldBeEstablished()) {
                try {
                    ObjectWriter objectWriter = createObjectWriterForSocket();
                    addInfo(this.peerId + "connection established");
                    dispatchEvents(objectWriter);
                } catch (IOException ex) {
                    addInfo(this.peerId + "connection failed: " + ex);
                } finally {
                    CloseUtil.closeQuietly(this.socket);
                    this.socket = null;
                    addInfo(this.peerId + "connection closed");
                }
            }
        } catch (InterruptedException ex) {
        }

        addInfo("shutting down");
    }

    private boolean socketConnectionCouldBeEstablished() throws InterruptedException {
        return ((this.socket = this.connector.call()) != null);
    }

    private ObjectWriter createObjectWriterForSocket() throws IOException {
        this.socket.setSoTimeout(this.acceptConnectionTimeout);
        ObjectWriter objectWriter = this.objectWriterFactory.newAutoFlushingObjectWriter(this.socket.getOutputStream());
        this.socket.setSoTimeout(0);
        return objectWriter;
    }

    private SocketConnector createConnector(InetAddress address, int port, int initialDelay, long retryDelay) {
        SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);
        connector.setExceptionHandler(this);
        connector.setSocketFactory(getSocketFactory());
        return connector;
    }

    private void dispatchEvents(ObjectWriter objectWriter) throws InterruptedException, IOException {
        while (true) {
            E event = this.deque.takeFirst();
            postProcessEvent(event);
            Serializable serializableEvent = getPST().transform(event);
            try {
                objectWriter.write(serializableEvent);
            } catch (IOException e) {
                tryReAddingEventToFrontOfQueue(event);
                throw e;
            }
        }
    }

    private void tryReAddingEventToFrontOfQueue(E event) {
        boolean wasInserted = this.deque.offerFirst(event);
        if (!wasInserted) {
            addInfo("Dropping event due to socket connection error and maxed out deque capacity");
        }
    }

    public void connectionFailed(SocketConnector connector, Exception ex) {
        if (ex instanceof InterruptedException) {
            addInfo("connector interrupted");
        } else if (ex instanceof java.net.ConnectException) {
            addInfo(this.peerId + "connection refused");
        } else {
            addInfo(this.peerId + ex);
        }
    }

    protected SocketConnector newConnector(InetAddress address, int port, long initialDelay, long retryDelay) {
        return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
    }

    protected SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setRemoteHost(String host) {
        this.remoteHost = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Duration getReconnectionDelay() {
        return this.reconnectionDelay;
    }

    public void setReconnectionDelay(Duration delay) {
        this.reconnectionDelay = delay;
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public Duration getEventDelayLimit() {
        return this.eventDelayLimit;
    }

    public void setEventDelayLimit(Duration eventDelayLimit) {
        this.eventDelayLimit = eventDelayLimit;
    }

    void setAcceptConnectionTimeout(int acceptConnectionTimeout) {
        this.acceptConnectionTimeout = acceptConnectionTimeout;
    }

    protected abstract void postProcessEvent(E paramE);

    protected abstract PreSerializationTransformer<E> getPST();
}

