/*     */ package com.zhonglian.server.websocket;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.websocket.codecfactory.WebDecoder;
/*     */ import com.zhonglian.server.websocket.codecfactory.WebEncoder;
/*     */ import java.net.InetSocketAddress;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.buffer.IoBufferAllocator;
/*     */ import org.apache.mina.core.buffer.SimpleBufferAllocator;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.filter.codec.ProtocolCodecFilter;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoder;
/*     */ import org.apache.mina.filter.logging.LoggingFilter;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseAcceptor<Session extends BaseSession>
/*     */ {
/*     */   private String ip;
/*     */   private int port;
/*     */   protected NioSocketAcceptor acceptor;
/*     */   protected final BaseIoHandler<Session> handler;
/*     */   protected final ProtocolCodecFilter codecFilter;
/*     */   
/*     */   public BaseAcceptor(BaseIoHandler<Session> ioHandler) {
/*  41 */     this.handler = ioHandler;
/*  42 */     this.codecFilter = new ProtocolCodecFilter((ProtocolEncoder)new WebEncoder(), (ProtocolDecoder)new WebDecoder());
/*     */   }
/*     */   
/*     */   public BaseAcceptor(BaseIoHandler<Session> ioHandler, ProtocolEncoder encoder, ProtocolDecoder decoder) {
/*  46 */     this.handler = ioHandler;
/*  47 */     this.codecFilter = new ProtocolCodecFilter(encoder, decoder);
/*     */   }
/*     */   
/*     */   public boolean startSocket(String ip, int port) {
/*  51 */     this.ip = ip;
/*  52 */     this.port = port;
/*     */     try {
/*  54 */       int threadCount = Runtime.getRuntime().availableProcessors() * 2;
/*  55 */       this.acceptor = new NioSocketAcceptor(threadCount);
/*  56 */       config();
/*  57 */       DefaultIoFilterChainBuilder chain = this.acceptor.getFilterChain();
/*  58 */       chain.addLast("logger", (IoFilter)new LoggingFilter());
/*  59 */       chain.addLast("codec", (IoFilter)this.codecFilter);
/*  60 */       this.acceptor.setHandler(this.handler);
/*  61 */       this.acceptor.bind(new InetSocketAddress(ip, port));
/*  62 */       CommLog.info("Service {} listening on ip {}, port {}", new Object[] { getClass().getName(), ip, Integer.valueOf(port) });
/*  63 */       return true;
/*  64 */     } catch (Throwable ex) {
/*  65 */       CommLog.error("Service {} on ip {}, port {}, faield!!!\n{}", new Object[] { getClass().getName(), ip, Integer.valueOf(port), ex });
/*  66 */       System.exit(-1);
/*  67 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/*  72 */     this.acceptor.dispose();
/*  73 */     CommLog.error("Service {} on ip {}, port {}, closed!", new Object[] { getClass().getName(), this.ip, Integer.valueOf(this.port) });
/*     */   }
/*     */   
/*     */   public boolean isOpened() {
/*  77 */     return (this.acceptor.isActive() && !this.acceptor.isDisposing());
/*     */   }
/*     */   
/*     */   private void config() {
/*  81 */     SocketSessionConfig sessioncfg = this.acceptor.getSessionConfig();
/*  82 */     sessioncfg.setReceiveBufferSize(8192);
/*  83 */     sessioncfg.setSendBufferSize(32768);
/*  84 */     sessioncfg.setKeepAlive(true);
/*  85 */     sessioncfg.setTcpNoDelay(false);
/*     */     
/*  87 */     IoBuffer.setUseDirectBuffer(false);
/*  88 */     IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
/*     */     
/*  90 */     this.acceptor.setBacklog(1024);
/*  91 */     this.acceptor.setReuseAddress(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startSocket(Integer port) {
/* 101 */     return startSocket("0.0.0.0", port.intValue());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/BaseAcceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */