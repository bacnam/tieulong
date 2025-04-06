/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.file.FileRegion;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.service.DefaultTransportMetadata;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.filter.ssl.SslFilter;
/*     */ import org.apache.mina.transport.socket.AbstractSocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NioSocketSession
/*     */   extends NioSession
/*     */ {
/*  47 */   static final TransportMetadata METADATA = (TransportMetadata)new DefaultTransportMetadata("nio", "socket", false, true, InetSocketAddress.class, SocketSessionConfig.class, new Class[] { IoBuffer.class, FileRegion.class });
/*     */ 
/*     */   
/*     */   private Socket getSocket() {
/*  51 */     return ((SocketChannel)this.channel).socket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketSession(IoService service, IoProcessor<NioSession> processor, SocketChannel channel) {
/*  63 */     super(processor, service, channel);
/*  64 */     this.config = (IoSessionConfig)new SessionConfigImpl();
/*  65 */     this.config.setAll(service.getSessionConfig());
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/*  69 */     return METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketSessionConfig getConfig() {
/*  76 */     return (SocketSessionConfig)this.config;
/*     */   }
/*     */ 
/*     */   
/*     */   SocketChannel getChannel() {
/*  81 */     return (SocketChannel)this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/*  88 */     if (this.channel == null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     Socket socket = getSocket();
/*     */     
/*  94 */     if (socket == null) {
/*  95 */       return null;
/*     */     }
/*     */     
/*  98 */     return (InetSocketAddress)socket.getRemoteSocketAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getLocalAddress() {
/* 105 */     if (this.channel == null) {
/* 106 */       return null;
/*     */     }
/*     */     
/* 109 */     Socket socket = getSocket();
/*     */     
/* 111 */     if (socket == null) {
/* 112 */       return null;
/*     */     }
/*     */     
/* 115 */     return (InetSocketAddress)socket.getLocalSocketAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getServiceAddress() {
/* 120 */     return (InetSocketAddress)super.getServiceAddress();
/*     */   }
/*     */   
/*     */   private class SessionConfigImpl extends AbstractSocketSessionConfig {
/*     */     public boolean isKeepAlive() {
/*     */       try {
/* 126 */         return NioSocketSession.this.getSocket().getKeepAlive();
/* 127 */       } catch (SocketException e) {
/* 128 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     private SessionConfigImpl() {}
/*     */     public void setKeepAlive(boolean on) {
/*     */       try {
/* 134 */         NioSocketSession.this.getSocket().setKeepAlive(on);
/* 135 */       } catch (SocketException e) {
/* 136 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isOobInline() {
/*     */       try {
/* 142 */         return NioSocketSession.this.getSocket().getOOBInline();
/* 143 */       } catch (SocketException e) {
/* 144 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setOobInline(boolean on) {
/*     */       try {
/* 150 */         NioSocketSession.this.getSocket().setOOBInline(on);
/* 151 */       } catch (SocketException e) {
/* 152 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isReuseAddress() {
/*     */       try {
/* 158 */         return NioSocketSession.this.getSocket().getReuseAddress();
/* 159 */       } catch (SocketException e) {
/* 160 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setReuseAddress(boolean on) {
/*     */       try {
/* 166 */         NioSocketSession.this.getSocket().setReuseAddress(on);
/* 167 */       } catch (SocketException e) {
/* 168 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getSoLinger() {
/*     */       try {
/* 174 */         return NioSocketSession.this.getSocket().getSoLinger();
/* 175 */       } catch (SocketException e) {
/* 176 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setSoLinger(int linger) {
/*     */       try {
/* 182 */         if (linger < 0) {
/* 183 */           NioSocketSession.this.getSocket().setSoLinger(false, 0);
/*     */         } else {
/* 185 */           NioSocketSession.this.getSocket().setSoLinger(true, linger);
/*     */         } 
/* 187 */       } catch (SocketException e) {
/* 188 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isTcpNoDelay() {
/* 193 */       if (!NioSocketSession.this.isConnected()) {
/* 194 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 198 */         return NioSocketSession.this.getSocket().getTcpNoDelay();
/* 199 */       } catch (SocketException e) {
/* 200 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setTcpNoDelay(boolean on) {
/*     */       try {
/* 206 */         NioSocketSession.this.getSocket().setTcpNoDelay(on);
/* 207 */       } catch (SocketException e) {
/* 208 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTrafficClass() {
/*     */       try {
/* 217 */         return NioSocketSession.this.getSocket().getTrafficClass();
/* 218 */       } catch (SocketException e) {
/* 219 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTrafficClass(int tc) {
/*     */       try {
/* 228 */         NioSocketSession.this.getSocket().setTrafficClass(tc);
/* 229 */       } catch (SocketException e) {
/* 230 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getSendBufferSize() {
/*     */       try {
/* 236 */         return NioSocketSession.this.getSocket().getSendBufferSize();
/* 237 */       } catch (SocketException e) {
/* 238 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setSendBufferSize(int size) {
/*     */       try {
/* 244 */         NioSocketSession.this.getSocket().setSendBufferSize(size);
/* 245 */       } catch (SocketException e) {
/* 246 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getReceiveBufferSize() {
/*     */       try {
/* 252 */         return NioSocketSession.this.getSocket().getReceiveBufferSize();
/* 253 */       } catch (SocketException e) {
/* 254 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setReceiveBufferSize(int size) {
/*     */       try {
/* 260 */         NioSocketSession.this.getSocket().setReceiveBufferSize(size);
/* 261 */       } catch (SocketException e) {
/* 262 */         throw new RuntimeIoException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isSecured() {
/* 272 */     IoFilterChain chain = getFilterChain();
/*     */     
/* 274 */     IoFilter sslFilter = chain.get(SslFilter.class);
/*     */     
/* 276 */     if (sslFilter != null)
/*     */     {
/* 278 */       return ((SslFilter)sslFilter).isSslStarted((IoSession)this);
/*     */     }
/* 280 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioSocketSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */