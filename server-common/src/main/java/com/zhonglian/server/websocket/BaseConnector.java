package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.websocket.codecfactory.WebDecoder;
import com.zhonglian.server.websocket.codecfactory.WebEncoder;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public abstract class BaseConnector<Session extends BaseSession> {
    protected String ip;
    protected int port;
    protected NioSocketConnector connector;
    protected BaseIoHandler<Session> handler;
    protected ProtocolCodecFilter codecFilter;
    protected Session _session;

    public BaseConnector(BaseIoHandler<Session> ioHandler) {
        this.handler = ioHandler;
        this.codecFilter = new ProtocolCodecFilter(new WebEncoder(), new WebDecoder());
    }

    public BaseConnector(BaseIoHandler<Session> ioHandler, ProtocolEncoder encoder, ProtocolDecoder decoder) {
        this.handler = ioHandler;
        this.codecFilter = new ProtocolCodecFilter(encoder, decoder);
    }

    public Session getSocketSession() {
        return this._session;
    }

    private boolean connect(SocketAddress sa, long timeout) {
        try {
            this.connector = new NioSocketConnector();
            this.config(this.connector);
            DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
            chain.addLast("logger", new LoggingFilter());
            chain.addLast("codec", this.codecFilter);
            this.connector.setHandler(this.handler);
            ConnectFuture f = this.connector.connect(sa);
            f.awaitUninterruptibly(timeout);
            if (f.isConnected()) {
                Long sessionId = (Long)f.getSession().getAttribute(BaseIoHandler.SESSION_ID);
                this._session = this.handler.getSession(sessionId);
                CommLog.info("Connector {} Connnect to {} Success!", this.getClass().getSimpleName(), sa.toString());
            } else {
                CommLog.error("Connector {} Connnect to {} Failed!", this.getClass().getSimpleName(), sa.toString());
                this.onConnectFailed();
            }

            return f.isConnected();
        } catch (Exception var7) {
            CommLog.error("Connector {} Connnect to {} Failed! \n{}", this.getClass().getName(), sa.toString(), var7);
            return false;
        }
    }

    public BaseIoHandler<Session> getSocketHandler() {
        return this.handler;
    }

    protected void config(NioSocketConnector connector) {
        SocketSessionConfig sessioncfg = connector.getSessionConfig();
        sessioncfg.setReceiveBufferSize(8192);
        sessioncfg.setSendBufferSize(65536);
        sessioncfg.setKeepAlive(true);
        sessioncfg.setTcpNoDelay(false);
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
    }

    public boolean isConnected() {
        return this._session != null && this._session.isConnected();
    }

    public void disconnect() {
        try {
            if (this.connector != null) {
                this.connector.dispose();
            }

            this._session = null;
        } catch (Throwable var2) {
            CommLog.error(null, var2);
        }
    }

    public synchronized boolean reconnect(String ip, int port) {
        if (this.isConnected()) {
            return true;
        } else {
            this.ip = ip;
            this.port = port;
            this.disconnect();
            return this.connect(new InetSocketAddress(ip, port), 25000L);
        }
    }

    protected void onConnectFailed() {
        SyncTaskManager.task(() -> this.reconnect(this.ip, this.port), 3000);
    }
}
