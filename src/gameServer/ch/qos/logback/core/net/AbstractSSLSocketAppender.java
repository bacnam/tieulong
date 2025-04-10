package ch.qos.logback.core.net;

import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
import ch.qos.logback.core.net.ssl.SSLComponent;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import ch.qos.logback.core.spi.ContextAware;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

public abstract class AbstractSSLSocketAppender<E>
extends AbstractSocketAppender<E>
implements SSLComponent
{
private SSLConfiguration ssl;
private SocketFactory socketFactory;

protected SocketFactory getSocketFactory() {
return this.socketFactory;
}

public void start() {
try {
SSLContext sslContext = getSsl().createContext((ContextAware)this);
SSLParametersConfiguration parameters = getSsl().getParameters();
parameters.setContext(getContext());
this.socketFactory = (SocketFactory)new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());

super.start();
}
catch (Exception ex) {
addError(ex.getMessage(), ex);
} 
}

public SSLConfiguration getSsl() {
if (this.ssl == null) {
this.ssl = new SSLConfiguration();
}
return this.ssl;
}

public void setSsl(SSLConfiguration ssl) {
this.ssl = ssl;
}
}

