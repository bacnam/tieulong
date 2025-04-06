/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.transport.socket.AbstractDatagramSessionConfig;
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
/*     */ class NioDatagramSessionConfig
/*     */   extends AbstractDatagramSessionConfig
/*     */ {
/*     */   private final DatagramChannel channel;
/*     */   
/*     */   NioDatagramSessionConfig(DatagramChannel channel) {
/*  45 */     this.channel = channel;
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
/*     */   
/*     */   public int getReceiveBufferSize() {
/*     */     try {
/*  59 */       return this.channel.socket().getReceiveBufferSize();
/*  60 */     } catch (SocketException e) {
/*  61 */       throw new RuntimeIoException(e);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiveBufferSize(int receiveBufferSize) {
/*     */     try {
/*  79 */       this.channel.socket().setReceiveBufferSize(receiveBufferSize);
/*  80 */     } catch (SocketException e) {
/*  81 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBroadcast() {
/*     */     try {
/*  94 */       return this.channel.socket().getBroadcast();
/*  95 */     } catch (SocketException e) {
/*  96 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBroadcast(boolean broadcast) {
/*     */     try {
/* 102 */       this.channel.socket().setBroadcast(broadcast);
/* 103 */     } catch (SocketException e) {
/* 104 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSendBufferSize() {
/*     */     try {
/* 115 */       return this.channel.socket().getSendBufferSize();
/* 116 */     } catch (SocketException e) {
/* 117 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendBufferSize(int sendBufferSize) {
/*     */     try {
/* 128 */       this.channel.socket().setSendBufferSize(sendBufferSize);
/* 129 */     } catch (SocketException e) {
/* 130 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReuseAddress() {
/*     */     try {
/* 143 */       return this.channel.socket().getReuseAddress();
/* 144 */     } catch (SocketException e) {
/* 145 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReuseAddress(boolean reuseAddress) {
/*     */     try {
/* 156 */       this.channel.socket().setReuseAddress(reuseAddress);
/* 157 */     } catch (SocketException e) {
/* 158 */       throw new RuntimeIoException(e);
/*     */     } 
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
/*     */   
/*     */   public int getTrafficClass() {
/*     */     try {
/* 173 */       return this.channel.socket().getTrafficClass();
/* 174 */     } catch (SocketException e) {
/* 175 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrafficClass(int trafficClass) {
/*     */     try {
/* 186 */       this.channel.socket().setTrafficClass(trafficClass);
/* 187 */     } catch (SocketException e) {
/* 188 */       throw new RuntimeIoException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioDatagramSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */