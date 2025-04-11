package org.apache.http.nio.conn.ssl;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.nio.conn.scheme.LayeringStrategy;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLIOSession;
import org.apache.http.nio.reactor.ssl.SSLMode;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Deprecated
public class SSLLayeringStrategy
        implements LayeringStrategy {
    public static final String TLS = "TLS";
    public static final String SSL = "SSL";
    public static final String SSLV2 = "SSLv2";
    private final SSLContext sslContext;
    private final X509HostnameVerifier hostnameVerifier;

    public SSLLayeringStrategy(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(createSSLContext(algorithm, keystore, keystorePassword, truststore, random, null), hostnameVerifier);
    }

    public SSLLayeringStrategy(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(createSSLContext(algorithm, keystore, keystorePassword, truststore, random, trustStrategy), hostnameVerifier);
    }

    public SSLLayeringStrategy(KeyStore keystore, String keystorePassword, KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this("TLS", keystore, keystorePassword, truststore, null, null, (X509HostnameVerifier) new BrowserCompatHostnameVerifier());
    }

    public SSLLayeringStrategy(KeyStore keystore, String keystorePassword) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this("TLS", keystore, keystorePassword, null, null, null, (X509HostnameVerifier) new BrowserCompatHostnameVerifier());
    }

    public SSLLayeringStrategy(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this("TLS", null, null, truststore, null, null, (X509HostnameVerifier) new BrowserCompatHostnameVerifier());
    }

    public SSLLayeringStrategy(TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this("TLS", null, null, null, null, trustStrategy, hostnameVerifier);
    }

    public SSLLayeringStrategy(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this("TLS", null, null, null, null, trustStrategy, (X509HostnameVerifier) new BrowserCompatHostnameVerifier());
    }

    public SSLLayeringStrategy(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
        this.sslContext = sslContext;
        this.hostnameVerifier = hostnameVerifier;
    }

    public SSLLayeringStrategy(SSLContext sslContext) {
        this(sslContext, (X509HostnameVerifier) new BrowserCompatHostnameVerifier());
    }

    public static SSLLayeringStrategy getDefaultStrategy() {
        return new SSLLayeringStrategy(SSLContexts.createDefault());
    }

    public static SSLLayeringStrategy getSystemDefaultStrategy() {
        return new SSLLayeringStrategy(SSLContexts.createSystemDefault());
    }

    private static SSLContext createSSLContext(String algorithm, KeyStore keystore, String keystorePassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        String algo = (algorithm != null) ? algorithm : "TLS";
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        kmfactory.init(keystore, (keystorePassword != null) ? keystorePassword.toCharArray() : null);
        KeyManager[] keymanagers = kmfactory.getKeyManagers();
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmfactory.init(truststore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        if (trustmanagers != null && trustStrategy != null) {
            for (int i = 0; i < trustmanagers.length; i++) {
                TrustManager tm = trustmanagers[i];
                if (tm instanceof X509TrustManager) {
                    trustmanagers[i] = new TrustManagerDecorator((X509TrustManager) tm, trustStrategy);
                }
            }
        }

        SSLContext sslcontext = SSLContext.getInstance(algo);
        sslcontext.init(keymanagers, trustmanagers, random);
        return sslcontext;
    }

    public boolean isSecure() {
        return true;
    }

    public SSLIOSession layer(IOSession iosession) {
        SSLIOSession ssliosession = new SSLIOSession(iosession, SSLMode.CLIENT, this.sslContext, new SSLSetupHandler() {

            public void initalize(SSLEngine sslengine) throws SSLException {
                SSLLayeringStrategy.this.initializeEngine(sslengine);
            }

            public void verify(IOSession iosession, SSLSession sslsession) throws SSLException {
                SSLLayeringStrategy.this.verifySession(iosession, sslsession);
            }
        });

        iosession.setAttribute("http.session.ssl", ssliosession);
        return ssliosession;
    }

    protected void initializeEngine(SSLEngine engine) {
    }

    protected void verifySession(IOSession iosession, SSLSession sslsession) throws SSLException {
        InetSocketAddress address = (InetSocketAddress) iosession.getRemoteAddress();

        Certificate[] certs = sslsession.getPeerCertificates();
        X509Certificate x509 = (X509Certificate) certs[0];
        this.hostnameVerifier.verify(address.getHostName(), x509);
    }
}

