/*    */ package org.apache.mina.transport.socket.nio;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
/*    */ import java.nio.channels.ByteChannel;
/*    */ import java.nio.channels.DatagramChannel;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.service.DefaultTransportMetadata;
/*    */ import org.apache.mina.core.service.IoProcessor;
/*    */ import org.apache.mina.core.service.IoService;
/*    */ import org.apache.mina.core.service.TransportMetadata;
/*    */ import org.apache.mina.core.session.IoSessionConfig;
/*    */ import org.apache.mina.transport.socket.DatagramSessionConfig;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NioDatagramSession
/*    */   extends NioSession
/*    */ {
/* 40 */   static final TransportMetadata METADATA = (TransportMetadata)new DefaultTransportMetadata("nio", "datagram", true, false, InetSocketAddress.class, DatagramSessionConfig.class, new Class[] { IoBuffer.class });
/*    */ 
/*    */ 
/*    */   
/*    */   private final InetSocketAddress localAddress;
/*    */ 
/*    */   
/*    */   private final InetSocketAddress remoteAddress;
/*    */ 
/*    */ 
/*    */   
/*    */   NioDatagramSession(IoService service, DatagramChannel channel, IoProcessor<NioSession> processor, SocketAddress remoteAddress) {
/* 52 */     super(processor, service, channel);
/* 53 */     this.config = (IoSessionConfig)new NioDatagramSessionConfig(channel);
/* 54 */     this.config.setAll(service.getSessionConfig());
/* 55 */     this.remoteAddress = (InetSocketAddress)remoteAddress;
/* 56 */     this.localAddress = (InetSocketAddress)channel.socket().getLocalSocketAddress();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   NioDatagramSession(IoService service, DatagramChannel channel, IoProcessor<NioSession> processor) {
/* 63 */     this(service, channel, processor, channel.socket().getRemoteSocketAddress());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DatagramSessionConfig getConfig() {
/* 70 */     return (DatagramSessionConfig)this.config;
/*    */   }
/*    */ 
/*    */   
/*    */   DatagramChannel getChannel() {
/* 75 */     return (DatagramChannel)this.channel;
/*    */   }
/*    */   
/*    */   public TransportMetadata getTransportMetadata() {
/* 79 */     return METADATA;
/*    */   }
/*    */   
/*    */   public InetSocketAddress getRemoteAddress() {
/* 83 */     return this.remoteAddress;
/*    */   }
/*    */   
/*    */   public InetSocketAddress getLocalAddress() {
/* 87 */     return this.localAddress;
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress getServiceAddress() {
/* 92 */     return (InetSocketAddress)super.getServiceAddress();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioDatagramSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */