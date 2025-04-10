package org.apache.mina.transport.socket;

import org.apache.mina.core.session.AbstractIoSessionConfig;
import org.apache.mina.core.session.IoSessionConfig;

public abstract class AbstractSocketSessionConfig
extends AbstractIoSessionConfig
implements SocketSessionConfig
{
protected final void doSetAll(IoSessionConfig config) {
if (!(config instanceof SocketSessionConfig)) {
return;
}

if (config instanceof AbstractSocketSessionConfig) {

AbstractSocketSessionConfig cfg = (AbstractSocketSessionConfig)config;
if (cfg.isKeepAliveChanged()) {
setKeepAlive(cfg.isKeepAlive());
}
if (cfg.isOobInlineChanged()) {
setOobInline(cfg.isOobInline());
}
if (cfg.isReceiveBufferSizeChanged()) {
setReceiveBufferSize(cfg.getReceiveBufferSize());
}
if (cfg.isReuseAddressChanged()) {
setReuseAddress(cfg.isReuseAddress());
}
if (cfg.isSendBufferSizeChanged()) {
setSendBufferSize(cfg.getSendBufferSize());
}
if (cfg.isSoLingerChanged()) {
setSoLinger(cfg.getSoLinger());
}
if (cfg.isTcpNoDelayChanged()) {
setTcpNoDelay(cfg.isTcpNoDelay());
}
if (cfg.isTrafficClassChanged() && getTrafficClass() != cfg.getTrafficClass()) {
setTrafficClass(cfg.getTrafficClass());
}
} else {
SocketSessionConfig cfg = (SocketSessionConfig)config;
setKeepAlive(cfg.isKeepAlive());
setOobInline(cfg.isOobInline());
setReceiveBufferSize(cfg.getReceiveBufferSize());
setReuseAddress(cfg.isReuseAddress());
setSendBufferSize(cfg.getSendBufferSize());
setSoLinger(cfg.getSoLinger());
setTcpNoDelay(cfg.isTcpNoDelay());
if (getTrafficClass() != cfg.getTrafficClass()) {
setTrafficClass(cfg.getTrafficClass());
}
} 
}

protected boolean isKeepAliveChanged() {
return true;
}

protected boolean isOobInlineChanged() {
return true;
}

protected boolean isReceiveBufferSizeChanged() {
return true;
}

protected boolean isReuseAddressChanged() {
return true;
}

protected boolean isSendBufferSizeChanged() {
return true;
}

protected boolean isSoLingerChanged() {
return true;
}

protected boolean isTcpNoDelayChanged() {
return true;
}

protected boolean isTrafficClassChanged() {
return true;
}
}

