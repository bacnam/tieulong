package org.apache.mina.transport.socket;

public class DefaultDatagramSessionConfig
        extends AbstractDatagramSessionConfig {
    private static boolean DEFAULT_BROADCAST = false;
    private static boolean DEFAULT_REUSE_ADDRESS = false;
    private static int DEFAULT_RECEIVE_BUFFER_SIZE = -1;

    private static int DEFAULT_SEND_BUFFER_SIZE = -1;

    private static int DEFAULT_TRAFFIC_CLASS = 0;

    private boolean broadcast = DEFAULT_BROADCAST;

    private boolean reuseAddress = DEFAULT_REUSE_ADDRESS;

    private int receiveBufferSize = DEFAULT_RECEIVE_BUFFER_SIZE;

    private int sendBufferSize = DEFAULT_SEND_BUFFER_SIZE;

    private int trafficClass = DEFAULT_TRAFFIC_CLASS;

    public boolean isBroadcast() {
        return this.broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
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

    protected boolean isBroadcastChanged() {
        return (this.broadcast != DEFAULT_BROADCAST);
    }

    protected boolean isReceiveBufferSizeChanged() {
        return (this.receiveBufferSize != DEFAULT_RECEIVE_BUFFER_SIZE);
    }

    protected boolean isReuseAddressChanged() {
        return (this.reuseAddress != DEFAULT_REUSE_ADDRESS);
    }

    protected boolean isSendBufferSizeChanged() {
        return (this.sendBufferSize != DEFAULT_SEND_BUFFER_SIZE);
    }

    protected boolean isTrafficClassChanged() {
        return (this.trafficClass != DEFAULT_TRAFFIC_CLASS);
    }
}

