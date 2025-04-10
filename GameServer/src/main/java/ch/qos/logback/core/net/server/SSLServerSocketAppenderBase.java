package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
import ch.qos.logback.core.net.ssl.SSLComponent;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import ch.qos.logback.core.spi.ContextAware;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;

public abstract class SSLServerSocketAppenderBase<E>
extends AbstractServerSocketAppender<E>
implements SSLComponent
{
private SSLConfiguration ssl;
private ServerSocketFactory socketFactory;

protected ServerSocketFactory getServerSocketFactory() {
return this.socketFactory;
}

public void start() {
try {
SSLContext sslContext = getSsl().createContext((ContextAware)this);
SSLParametersConfiguration parameters = getSsl().getParameters();
parameters.setContext(getContext());
this.socketFactory = (ServerSocketFactory)new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());

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

