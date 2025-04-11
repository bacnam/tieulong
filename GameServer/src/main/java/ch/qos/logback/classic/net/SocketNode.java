package ch.qos.logback.classic.net;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketNode
        implements Runnable {
    Socket socket;
    LoggerContext context;
    ObjectInputStream ois;
    SocketAddress remoteSocketAddress;
    Logger logger;
    boolean closed = false;
    SimpleSocketServer socketServer;

    public SocketNode(SimpleSocketServer socketServer, Socket socket, LoggerContext context) {
        this.socketServer = socketServer;
        this.socket = socket;
        this.remoteSocketAddress = socket.getRemoteSocketAddress();
        this.context = context;
        this.logger = context.getLogger(SocketNode.class);
    }

    public void run() {
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
        } catch (Exception e) {
            this.logger.error("Could not open ObjectInputStream to " + this.socket, e);
            this.closed = true;
        }

        while (!this.closed) {

            ILoggingEvent event = (ILoggingEvent) this.ois.readObject();

            Logger remoteLogger = this.context.getLogger(event.getLoggerName());

            if (remoteLogger.isEnabledFor(event.getLevel())) {
                remoteLogger.callAppenders(event);
            }
        }

        this.socketServer.socketNodeClosing(this);
        close();
    }

    void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.ois != null) {
            try {
                this.ois.close();
            } catch (IOException e) {
                this.logger.warn("Could not close connection.", e);
            } finally {
                this.ois = null;
            }
        }
    }

    public String toString() {
        return getClass().getName() + this.remoteSocketAddress.toString();
    }
}

