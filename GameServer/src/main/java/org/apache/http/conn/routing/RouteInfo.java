package org.apache.http.conn.routing;

import org.apache.http.HttpHost;

import java.net.InetAddress;

public interface RouteInfo {
    HttpHost getTargetHost();

    InetAddress getLocalAddress();

    int getHopCount();

    HttpHost getHopTarget(int paramInt);

    HttpHost getProxyHost();

    TunnelType getTunnelType();

    boolean isTunnelled();

    LayerType getLayerType();

    boolean isLayered();

    boolean isSecure();

    public enum TunnelType {
        PLAIN, TUNNELLED;
    }

    public enum LayerType {
        PLAIN, LAYERED;
    }
}

