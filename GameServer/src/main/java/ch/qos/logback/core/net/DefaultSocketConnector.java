package ch.qos.logback.core.net;

import ch.qos.logback.core.util.DelayStrategy;
import ch.qos.logback.core.util.FixedDelay;

import javax.net.SocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class DefaultSocketConnector
        implements SocketConnector {
    private final InetAddress address;
    private final int port;
    private final DelayStrategy delayStrategy;
    private SocketConnector.ExceptionHandler exceptionHandler;
    private SocketFactory socketFactory;

    public DefaultSocketConnector(InetAddress address, int port, long initialDelay, long retryDelay) {
        this(address, port, (DelayStrategy) new FixedDelay(initialDelay, retryDelay));
    }

    public DefaultSocketConnector(InetAddress address, int port, DelayStrategy delayStrategy) {
        this.address = address;
        this.port = port;
        this.delayStrategy = delayStrategy;
    }

    public Socket call() throws InterruptedException {
        useDefaultsForMissingFields();
        Socket socket = createSocket();
        while (socket == null && !Thread.currentThread().isInterrupted()) {
            Thread.sleep(this.delayStrategy.nextDelay());
            socket = createSocket();
        }
        return socket;
    }

    private Socket createSocket() {
        Socket newSocket = null;
        try {
            newSocket = this.socketFactory.createSocket(this.address, this.port);
        } catch (IOException ioex) {
            this.exceptionHandler.connectionFailed(this, ioex);
        }
        return newSocket;
    }

    private void useDefaultsForMissingFields() {
        if (this.exceptionHandler == null) {
            this.exceptionHandler = new ConsoleExceptionHandler();
        }
        if (this.socketFactory == null) {
            this.socketFactory = SocketFactory.getDefault();
        }
    }

    public void setExceptionHandler(SocketConnector.ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    private static class ConsoleExceptionHandler
            implements SocketConnector.ExceptionHandler {
        private ConsoleExceptionHandler() {
        }

        public void connectionFailed(SocketConnector connector, Exception ex) {
            System.out.println(ex);
        }
    }
}

