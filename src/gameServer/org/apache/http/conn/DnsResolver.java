package org.apache.http.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;

public interface DnsResolver {
  InetAddress[] resolve(String paramString) throws UnknownHostException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/DnsResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */