package org.apache.http.conn.ssl;

import org.apache.http.annotation.Immutable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@Immutable
public class NoopHostnameVerifier
        implements HostnameVerifier {
    public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }

    public final String toString() {
        return "NO_OP";
    }
}

