package org.apache.http.impl.nio;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

class SSLContextUtils {
    static SSLContext getDefault() {
        SSLContext sSLContext;
        try {
            try {
                sSLContext = SSLContext.getInstance("Default");
            } catch (NoSuchAlgorithmException ex) {
                sSLContext = SSLContext.getInstance("TLS");
            }
            sSLContext.init(null, null, null);
        } catch (Exception ex) {
            throw new IllegalStateException("Failure initializing default SSL context", ex);
        }
        return sSLContext;
    }
}

