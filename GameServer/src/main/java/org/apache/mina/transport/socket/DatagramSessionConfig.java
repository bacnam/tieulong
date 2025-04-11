package org.apache.mina.transport.socket;

import org.apache.mina.core.session.IoSessionConfig;

public interface DatagramSessionConfig extends IoSessionConfig {
    boolean isBroadcast();

    void setBroadcast(boolean paramBoolean);

    boolean isReuseAddress();

    void setReuseAddress(boolean paramBoolean);

    int getReceiveBufferSize();

    void setReceiveBufferSize(int paramInt);

    int getSendBufferSize();

    void setSendBufferSize(int paramInt);

    int getTrafficClass();

    void setTrafficClass(int paramInt);

    boolean isCloseOnPortUnreachable();

    void setCloseOnPortUnreachable(boolean paramBoolean);
}

