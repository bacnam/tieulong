package ch.qos.logback.core.net.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.net.ssl.TrustManagerFactory;

public class TrustManagerFactoryFactoryBean
{
private String algorithm;
private String provider;

public TrustManagerFactory createTrustManagerFactory() throws NoSuchProviderException, NoSuchAlgorithmException {
return (getProvider() != null) ? TrustManagerFactory.getInstance(getAlgorithm(), getProvider()) : TrustManagerFactory.getInstance(getAlgorithm());
}

public String getAlgorithm() {
if (this.algorithm == null) {
return TrustManagerFactory.getDefaultAlgorithm();
}
return this.algorithm;
}

public void setAlgorithm(String algorithm) {
this.algorithm = algorithm;
}

public String getProvider() {
return this.provider;
}

public void setProvider(String provider) {
this.provider = provider;
}
}

