package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.CloseUtil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

class RemoteAppenderStreamClient
        implements RemoteAppenderClient {
    private final String id;
    private final Socket socket;
    private final InputStream inputStream;
    private LoggerContext lc;
    private Logger logger;

    public RemoteAppenderStreamClient(String id, Socket socket) {
        this.id = id;
        this.socket = socket;
        this.inputStream = null;
    }

    public RemoteAppenderStreamClient(String id, InputStream inputStream) {
        this.id = id;
        this.socket = null;
        this.inputStream = inputStream;
    }

    public void setLoggerContext(LoggerContext lc) {
        this.lc = lc;
        this.logger = lc.getLogger(getClass().getPackage().getName());
    }

    public void close() {
        if (this.socket == null)
            return;
        CloseUtil.closeQuietly(this.socket);
    }

    public void run() {
        this.logger.info(this + ": connected");
        ObjectInputStream ois = null;
        try {
            ois = createObjectInputStream();

            while (true) {
                ILoggingEvent event = (ILoggingEvent) ois.readObject();

                Logger remoteLogger = this.lc.getLogger(event.getLoggerName());

                if (remoteLogger.isEnabledFor(event.getLevel())) {
                    remoteLogger.callAppenders(event);
                }
            }

        } catch (EOFException ex) {

        } catch (IOException ex) {
            this.logger.info(this + ": " + ex);
        } catch (ClassNotFoundException ex) {
            this.logger.error(this + ": unknown event class");
        } catch (RuntimeException ex) {
            this.logger.error(this + ": " + ex);
        } finally {

            if (ois != null) {
                CloseUtil.closeQuietly(ois);
            }
            close();
            this.logger.info(this + ": connection closed");
        }
    }

    private ObjectInputStream createObjectInputStream() throws IOException {
        if (this.inputStream != null) {
            return new ObjectInputStream(this.inputStream);
        }
        return new ObjectInputStream(this.socket.getInputStream());
    }

    public String toString() {
        return "client " + this.id;
    }
}

