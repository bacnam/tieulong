package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import com.zhonglian.server.websocket.codecfactory.WebDecoder;
import com.zhonglian.server.websocket.codecfactory.WebEncoder;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public abstract class BaseAcceptor<Session extends BaseSession> {
    private String ip;
    private int port;
    protected NioSocketAcceptor acceptor;
    protected final BaseIoHandler<Session> handler;
    protected final ProtocolCodecFilter codecFilter;

    public BaseAcceptor(BaseIoHandler<Session> ioHandler) {
        this.handler = ioHandler;
        this.codecFilter = new ProtocolCodecFilter(new WebEncoder(), new WebDecoder());
    }

    public BaseAcceptor(BaseIoHandler<Session> ioHandler, ProtocolEncoder encoder, ProtocolDecoder decoder) {
        this.handler = ioHandler;
        this.codecFilter = new ProtocolCodecFilter(encoder, decoder);
    }

    public boolean startSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;

        try {
            int threadCount = Runtime.getRuntime().availableProcessors() * 2;
            this.acceptor = new NioSocketAcceptor(threadCount);
            this.config();
            DefaultIoFilterChainBuilder chain = this.acceptor.getFilterChain();
            chain.addLast("logger", new LoggingFilter());
            chain.addLast("codec", this.codecFilter);
            this.acceptor.setHandler(this.handler);
            this.acceptor.bind(new InetSocketAddress(ip, port));
            CommLog.info("Service {} listening on ip {}, port {}", this.getClass().getName(), ip, port);
            return true;
        } catch (Throwable var5) {
            CommLog.error("Service {} on ip {}, port {}, faield!!!\n{}", this.getClass().getName(), ip, port, var5);
            System.exit(-1);
            return false;
        }
    }

    public void close() {
        this.acceptor.dispose();
        CommLog.error("Service {} on ip {}, port {}, closed!", this.getClass().getName(), this.ip, this.port);
    }

    public boolean isOpened() {
        return this.acceptor.isActive() && !this.acceptor.isDisposing();
    }

    private void config() {
        SocketSessionConfig sessioncfg = this.acceptor.getSessionConfig();
        sessioncfg.setReceiveBufferSize(8192);
        sessioncfg.setSendBufferSize(32768);
        sessioncfg.setKeepAlive(true);
        sessioncfg.setTcpNoDelay(false);
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        this.acceptor.setBacklog(1024);
        this.acceptor.setReuseAddress(true);
    }

    public boolean startSocket(Integer port) {
        return this.startSocket("0.0.0.0", port);
    }
}
