package org.apache.mina.transport.socket;

import org.apache.mina.core.session.AbstractIoSessionConfig;
import org.apache.mina.core.session.IoSessionConfig;

public abstract class AbstractDatagramSessionConfig
extends AbstractIoSessionConfig
implements DatagramSessionConfig
{
private static final boolean DEFAULT_CLOSE_ON_PORT_UNREACHABLE = true;
private boolean closeOnPortUnreachable = true;

protected void doSetAll(IoSessionConfig config) {
if (!(config instanceof DatagramSessionConfig)) {
return;
}

if (config instanceof AbstractDatagramSessionConfig) {

AbstractDatagramSessionConfig cfg = (AbstractDatagramSessionConfig)config;
if (cfg.isBroadcastChanged()) {
setBroadcast(cfg.isBroadcast());
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
if (cfg.isTrafficClassChanged() && getTrafficClass() != cfg.getTrafficClass()) {
setTrafficClass(cfg.getTrafficClass());
}
} else {
DatagramSessionConfig cfg = (DatagramSessionConfig)config;
setBroadcast(cfg.isBroadcast());
setReceiveBufferSize(cfg.getReceiveBufferSize());
setReuseAddress(cfg.isReuseAddress());
setSendBufferSize(cfg.getSendBufferSize());
if (getTrafficClass() != cfg.getTrafficClass()) {
setTrafficClass(cfg.getTrafficClass());
}
} 
}

protected boolean isBroadcastChanged() {
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

protected boolean isTrafficClassChanged() {
return true;
}

public boolean isCloseOnPortUnreachable() {
return this.closeOnPortUnreachable;
}

public void setCloseOnPortUnreachable(boolean closeOnPortUnreachable) {
this.closeOnPortUnreachable = closeOnPortUnreachable;
}
}

