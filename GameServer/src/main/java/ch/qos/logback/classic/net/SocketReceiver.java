package ch.qos.logback.classic.net;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.DefaultSocketConnector;
import ch.qos.logback.core.net.SocketConnector;
import ch.qos.logback.core.util.CloseUtil;

import javax.net.SocketFactory;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class SocketReceiver
        extends ReceiverBase
        implements Runnable, SocketConnector.ExceptionHandler {
    private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
    private String remoteHost;
    private InetAddress address;
    private int port;
    private int reconnectionDelay;
    private int acceptConnectionTimeout = 5000;

    private String receiverId;

    private volatile Socket socket;

    private Future<Socket> connectorTask;

    protected boolean shouldStart() {
        int errorCount = 0;
        if (this.port == 0) {
            errorCount++;
            addError("No port was configured for receiver. For more information, please visit http:");
        }

        if (this.remoteHost == null) {
            errorCount++;
            addError("No host name or address was configured for receiver. For more information, please visit http:");
        }

        if (this.reconnectionDelay == 0) {
            this.reconnectionDelay = 30000;
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
            this.receiverId = "receiver " + this.remoteHost + ":" + this.port + ": ";
        }

        return (errorCount == 0);
    }

    protected void onStop() {
        if (this.socket != null) {
            CloseUtil.closeQuietly(this.socket);
        }
    }

    protected Runnable getRunnableTask() {
        return this;
    }

    public void run() {
        try {
            LoggerContext lc = (LoggerContext) getContext();
            while (!Thread.currentThread().isInterrupted()) {
                SocketConnector connector = createConnector(this.address, this.port, 0, this.reconnectionDelay);

                this.connectorTask = activateConnector(connector);
                if (this.connectorTask == null)
                    break;
                this.socket = waitForConnectorToReturnASocket();
                if (this.socket == null)
                    break;
                dispatchEvents(lc);
            }
        } catch (InterruptedException ex) {
        }

        addInfo("shutting down");
    }

    private SocketConnector createConnector(InetAddress address, int port, int initialDelay, int retryDelay) {
        SocketConnector connector = newConnector(address, port, initialDelay, retryDelay);

        connector.setExceptionHandler(this);
        connector.setSocketFactory(getSocketFactory());
        return connector;
    }

    private Future<Socket> activateConnector(SocketConnector connector) {
        try {
            return getContext().getExecutorService().submit((Callable<Socket>) connector);
        } catch (RejectedExecutionException ex) {
            return null;
        }
    }

    private Socket waitForConnectorToReturnASocket() throws InterruptedException {
        try {
            Socket s = this.connectorTask.get();
            this.connectorTask = null;
            return s;
        } catch (ExecutionException e) {
            return null;
        }
    }

    private void dispatchEvents(LoggerContext lc) {
        try {
            this.socket.setSoTimeout(this.acceptConnectionTimeout);
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setSoTimeout(0);
            addInfo(this.receiverId + "connection established");
            while (true) {
                ILoggingEvent event = (ILoggingEvent) ois.readObject();
                Logger remoteLogger = lc.getLogger(event.getLoggerName());
                if (remoteLogger.isEnabledFor(event.getLevel())) {
                    remoteLogger.callAppenders(event);
                }
            }
        } catch (EOFException ex) {
            addInfo(this.receiverId + "end-of-stream detected");
        } catch (IOException ex) {
            addInfo(this.receiverId + "connection failed: " + ex);
        } catch (ClassNotFoundException ex) {
            addInfo(this.receiverId + "unknown event class: " + ex);
        } finally {
            CloseUtil.closeQuietly(this.socket);
            this.socket = null;
            addInfo(this.receiverId + "connection closed");
        }
    }

    public void connectionFailed(SocketConnector connector, Exception ex) {
        if (ex instanceof InterruptedException) {
            addInfo("connector interrupted");
        } else if (ex instanceof java.net.ConnectException) {
            addInfo(this.receiverId + "connection refused");
        } else {
            addInfo(this.receiverId + ex);
        }
    }

    protected SocketConnector newConnector(InetAddress address, int port, int initialDelay, int retryDelay) {
        return (SocketConnector) new DefaultSocketConnector(address, port, initialDelay, retryDelay);
    }

    protected SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setReconnectionDelay(int reconnectionDelay) {
        this.reconnectionDelay = reconnectionDelay;
    }

    public void setAcceptConnectionTimeout(int acceptConnectionTimeout) {
        this.acceptConnectionTimeout = acceptConnectionTimeout;
    }
}

