package ch.qos.logback.core.net.ssl;

public interface SSLConfigurable {
  String[] getDefaultProtocols();

  String[] getSupportedProtocols();

  void setEnabledProtocols(String[] paramArrayOfString);

  String[] getDefaultCipherSuites();

  String[] getSupportedCipherSuites();

  void setEnabledCipherSuites(String[] paramArrayOfString);

  void setNeedClientAuth(boolean paramBoolean);

  void setWantClientAuth(boolean paramBoolean);
}

