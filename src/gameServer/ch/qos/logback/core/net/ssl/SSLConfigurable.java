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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/net/ssl/SSLConfigurable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */