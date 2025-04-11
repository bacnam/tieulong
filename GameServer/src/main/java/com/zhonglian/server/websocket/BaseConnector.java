package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.websocket.codecfactory.WebDecoder;
import com.zhonglian.server.websocket.codecfactory.WebEncoder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class BaseConnector<Session extends BaseSession> {
    protected String ip;
    protected int port;
    protected NioSocketConnector connector;
    protected BaseIoHandler<Session> handler;
    protected ProtocolCodecFilter codecFilter;
    protected Session _session;

    public BaseConnector(BaseIoHandler<Session> ioHandler) {
        this.handler = ioHandler;
        this.codecFilter = new ProtocolCodecFilter((ProtocolEncoder) new WebEncoder(), (ProtocolDecoder) new WebDecoder());
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
            config(this.connector);

            DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
            chain.addLast("logger", (IoFilter) new LoggingFilter());
            chain.addLast("codec", (IoFilter) this.codecFilter);

            this.connector.setHandler(this.handler);

            ConnectFuture f = this.connector.connect(sa);

            f.awaitUninterruptibly(timeout);

            if (f.isConnected()) {
                Long sessionId = (Long) f.getSession().getAttribute(BaseIoHandler.SESSION_ID);
                this._session = this.handler.getSession(sessionId.longValue());
                CommLog.info("Connector {} Connnect to {} Success!", getClass().getSimpleName(), sa.toString());
            } else {
                CommLog.error("Connector {} Connnect to {} Failed!", getClass().getSimpleName(), sa.toString());
                onConnectFailed();
            }
            return f.isConnected();
        } catch (Exception e) {
            CommLog.error("Connector {} Connnect to {} Failed! \n{}", new Object[]{getClass().getName(), sa.toString(), e});

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
        IoBuffer.setAllocator((IoBufferAllocator) new SimpleBufferAllocator());
    }

    public boolean isConnected() {
        return (this._session != null && this._session.isConnected());
    }

    public void disconnect() {
        try {
            if (this.connector != null) {
                this.connector.dispose();
            }
            this._session = null;
        } catch (Throwable ex) {
            CommLog.error(null, ex);
        }
    }

    public synchronized boolean reconnect(String ip, int port) {
        if (isConnected()) {
            return true;
        }
        this.ip = ip;
        this.port = port;

        disconnect();

        return connect(new InetSocketAddress(ip, port), 25000L);
    }

    protected void onConnectFailed() {
        SyncTaskManager.task(() -> reconnect(this.ip, this.port),

                3000);
    }
}

