package org.apache.mina.filter.ssl;

import javax.net.ssl.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class BogusTrustManagerFactory
        extends TrustManagerFactory {
    private static final X509TrustManager X509 = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
    private static final TrustManager[] X509_MANAGERS = new TrustManager[]{X509};

    public BogusTrustManagerFactory() {
        super(new BogusTrustManagerFactorySpi(null), new Provider("MinaBogus", 1.0D, "") {
            private static final long serialVersionUID = -4024169055312053827L;
        }"MinaBogus");
    }

    private static class BogusTrustManagerFactorySpi extends TrustManagerFactorySpi {
        private BogusTrustManagerFactorySpi() {
        }

        protected TrustManager[] engineGetTrustManagers() {
            return BogusTrustManagerFactory.X509_MANAGERS;
        }

        protected void engineInit(KeyStore keystore) throws KeyStoreException {
        }

        protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        }
    }
}

