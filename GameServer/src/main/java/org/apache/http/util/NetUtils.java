package org.apache.http.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class NetUtils
{
public static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
Args.notNull(buffer, "Buffer");
Args.notNull(socketAddress, "Socket address");
if (socketAddress instanceof InetSocketAddress) {
InetSocketAddress socketaddr = (InetSocketAddress)socketAddress;
InetAddress inetaddr = socketaddr.getAddress();
buffer.append((inetaddr != null) ? inetaddr.getHostAddress() : inetaddr).append(':').append(socketaddr.getPort());
} else {

buffer.append(socketAddress);
} 
}
}

