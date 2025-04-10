package org.apache.mina.transport.socket;

import org.apache.mina.core.service.IoService;

public class DefaultSocketSessionConfig
extends AbstractSocketSessionConfig
{
private static boolean DEFAULT_REUSE_ADDRESS = false;
private static int DEFAULT_TRAFFIC_CLASS = 0;

private static boolean DEFAULT_KEEP_ALIVE = false;

private static boolean DEFAULT_OOB_INLINE = false;

private static int DEFAULT_SO_LINGER = -1;

private static boolean DEFAULT_TCP_NO_DELAY = false;

protected IoService parent;

private boolean defaultReuseAddress;

private boolean reuseAddress;

private int receiveBufferSize = -1;

private int sendBufferSize = -1;

private int trafficClass = DEFAULT_TRAFFIC_CLASS;

private boolean keepAlive = DEFAULT_KEEP_ALIVE;

private boolean oobInline = DEFAULT_OOB_INLINE;

private int soLinger = DEFAULT_SO_LINGER;

private boolean tcpNoDelay = DEFAULT_TCP_NO_DELAY;

public void init(IoService parent) {
this.parent = parent;

if (parent instanceof SocketAcceptor) {
this.defaultReuseAddress = true;
} else {
this.defaultReuseAddress = DEFAULT_REUSE_ADDRESS;
} 

this.reuseAddress = this.defaultReuseAddress;
}

public boolean isReuseAddress() {
return this.reuseAddress;
}

public void setReuseAddress(boolean reuseAddress) {
this.reuseAddress = reuseAddress;
}

public int getReceiveBufferSize() {
return this.receiveBufferSize;
}

public void setReceiveBufferSize(int receiveBufferSize) {
this.receiveBufferSize = receiveBufferSize;
}

public int getSendBufferSize() {
return this.sendBufferSize;
}

public void setSendBufferSize(int sendBufferSize) {
this.sendBufferSize = sendBufferSize;
}

public int getTrafficClass() {
return this.trafficClass;
}

public void setTrafficClass(int trafficClass) {
this.trafficClass = trafficClass;
}

public boolean isKeepAlive() {
return this.keepAlive;
}

public void setKeepAlive(boolean keepAlive) {
this.keepAlive = keepAlive;
}

public boolean isOobInline() {
return this.oobInline;
}

public void setOobInline(boolean oobInline) {
this.oobInline = oobInline;
}

public int getSoLinger() {
return this.soLinger;
}

public void setSoLinger(int soLinger) {
this.soLinger = soLinger;
}

public boolean isTcpNoDelay() {
return this.tcpNoDelay;
}

public void setTcpNoDelay(boolean tcpNoDelay) {
this.tcpNoDelay = tcpNoDelay;
}

protected boolean isKeepAliveChanged() {
return (this.keepAlive != DEFAULT_KEEP_ALIVE);
}

protected boolean isOobInlineChanged() {
return (this.oobInline != DEFAULT_OOB_INLINE);
}

protected boolean isReceiveBufferSizeChanged() {
return (this.receiveBufferSize != -1);
}

protected boolean isReuseAddressChanged() {
return (this.reuseAddress != this.defaultReuseAddress);
}

protected boolean isSendBufferSizeChanged() {
return (this.sendBufferSize != -1);
}

protected boolean isSoLingerChanged() {
return (this.soLinger != DEFAULT_SO_LINGER);
}

protected boolean isTcpNoDelayChanged() {
return (this.tcpNoDelay != DEFAULT_TCP_NO_DELAY);
}

protected boolean isTrafficClassChanged() {
return (this.trafficClass != DEFAULT_TRAFFIC_CLASS);
}
}

