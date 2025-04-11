package com.notnoop.apns.internal;

import com.notnoop.exceptions.NetworkIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

public class ApnsFeedbackConnection {
    private static final Logger logger = LoggerFactory.getLogger(ApnsFeedbackConnection.class);
    private static final int RETRIES = 3;
    private final SocketFactory factory;
    private final String host;
    private final int port;
    private final Proxy proxy;
    int DELAY_IN_MS;

    public ApnsFeedbackConnection(SocketFactory factory, String host, int port) {
        this(factory, host, port, null);
    }

    public ApnsFeedbackConnection(SocketFactory factory, String host, int port, Proxy proxy) {
        this.DELAY_IN_MS = 1000;
        this.factory = factory;
        this.host = host;
        this.port = port;
        this.proxy = proxy;
    }

    public Map<String, Date> getInactiveDevices() throws NetworkIOException {
        int attempts = 0;
        while (true) {
            try {
                attempts++;
                Map<String, Date> result = getInactiveDevicesImpl();

                attempts = 0;
                return result;
            } catch (Exception e) {
                logger.warn("Failed to retreive invalid devices", e);
                if (attempts >= 3) {
                    logger.error("Couldn't get feedback connection", e);
                    Utilities.wrapAndThrowAsRuntimeException(e);
                }
                Utilities.sleep(this.DELAY_IN_MS);
            }
        }
    }

    public Map<String, Date> getInactiveDevicesImpl() throws IOException {
        Socket proxySocket = null;
        Socket socket = null;
        try {
            if (this.proxy == null) {
                socket = this.factory.createSocket(this.host, this.port);
            } else if (this.proxy.type() == Proxy.Type.HTTP) {
                TlsTunnelBuilder tunnelBuilder = new TlsTunnelBuilder();
                socket = tunnelBuilder.build((SSLSocketFactory) this.factory, this.proxy, this.host, this.port);
            } else {
                proxySocket = new Socket(this.proxy);
                proxySocket.connect(new InetSocketAddress(this.host, this.port));
                socket = ((SSLSocketFactory) this.factory).createSocket(proxySocket, this.host, this.port, false);
            }

            InputStream stream = socket.getInputStream();
            return Utilities.parseFeedbackStream(stream);
        } finally {
            Utilities.close(socket);
            Utilities.close(proxySocket);
        }
    }
}

