package ch.qos.logback.classic.net;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

public class SimpleSSLSocketServer
        extends SimpleSocketServer {
    private final ServerSocketFactory socketFactory;

    public SimpleSSLSocketServer(LoggerContext lc, int port) throws NoSuchAlgorithmException {
        this(lc, port, SSLContext.getDefault());
    }

    public SimpleSSLSocketServer(LoggerContext lc, int port, SSLContext sslContext) {
        super(lc, port);
        if (sslContext == null) {
            throw new NullPointerException("SSL context required");
        }
        SSLParametersConfiguration parameters = new SSLParametersConfiguration();

        parameters.setContext((Context) lc);
        this.socketFactory = (ServerSocketFactory) new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
    }

    public static void main(String[] argv) throws Exception {
        doMain((Class) SimpleSSLSocketServer.class, argv);
    }

    protected ServerSocketFactory getServerSocketFactory() {
        return this.socketFactory;
    }
}

