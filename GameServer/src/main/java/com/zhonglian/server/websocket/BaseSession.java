package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.handler.MessageHeader;
import org.apache.mina.core.session.IoSession;

import java.net.SocketAddress;

public abstract class BaseSession {
    protected IoSession session;
    protected long sessionID;
    protected String _remoteIP = "";
    protected int _remotePort;
    private PkgCacheMgr pkgCacheMgr = new PkgCacheMgr();

    public BaseSession(IoSession session, long sessionID) {
        this.session = session;
        this.sessionID = sessionID;
        CommLog.info("create session id:{} from ip:{}", Long.valueOf(getSessionId()), remoteIP());
    }

    public void close() {
        CommLog.info("close session id:{} from ip:{}", Long.valueOf(getSessionId()), remoteIP());
        this.session.close(true);
    }

    public long getSessionId() {
        return this.sessionID;
    }

    public PkgCacheMgr getPkgCacheMgr() {
        return this.pkgCacheMgr;
    }

    public SocketAddress remoteAddress() {
        return this.session.getRemoteAddress();
    }

    public SocketAddress localAddress() {
        return this.session.getLocalAddress();
    }

    public boolean isConnected() {
        return (this.session.isConnected() && !this.session.isClosing());
    }

    public String remoteIP() {
        try {
            if (this._remoteIP == null || this._remoteIP.isEmpty()) {
                String remoteIp = remoteAddress().toString().replaceFirst("/", "");
                int indexOfFound = remoteIp.indexOf(':');
                remoteIp = remoteIp.substring(0, indexOfFound);
                this._remoteIP = remoteIp;
            }
            return this._remoteIP;
        } catch (Throwable e) {
            CommLog.error("{} - remoteIP:() session not connected!", CommTime.getStringMS());
            return "";
        }
    }

    public int remotePort() {
        try {
            if (this._remotePort == 0) {
                String remoteAddr = remoteAddress().toString().replaceFirst("/", "");
                int indexOfFound = remoteAddr.indexOf(':');
                if (indexOfFound >= 0) {
                    String strPort = remoteAddr.substring(indexOfFound + 1, remoteAddr.length());
                    this._remotePort = Integer.parseInt(strPort);
                }
            }
            return this._remotePort;
        } catch (Throwable e) {
            CommLog.error("remotePort:() session not connected!");
            return 0;
        }
    }

    public void onSent(String message) {
        CommLog.debug("session {} sent {} chars, webaccept:{}", new Object[]{Integer.valueOf(hashCode()), Integer.valueOf(message.length()), message});
    }

    public void onReceived(MessageHeader header, String message) {
        CommLog.debug("session {} received ({}), {} bytes", new Object[]{Integer.valueOf(hashCode()), header.event, Integer.valueOf(message.length())});
    }

    public abstract void onCreated();

    public abstract void onClosed();
}

