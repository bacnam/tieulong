/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ByteChannel;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.file.FileRegion;
/*     */ import org.apache.mina.core.polling.AbstractPollingIoProcessor;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.SessionState;
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
/*     */ public final class NioProcessor
/*     */   extends AbstractPollingIoProcessor<NioSession>
/*     */ {
/*     */   private Selector selector;
/*  49 */   private SelectorProvider selectorProvider = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioProcessor(Executor executor) {
/*  58 */     super(executor);
/*     */ 
/*     */     
/*     */     try {
/*  62 */       this.selector = Selector.open();
/*  63 */     } catch (IOException e) {
/*  64 */       throw new RuntimeIoException("Failed to open a selector.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioProcessor(Executor executor, SelectorProvider selectorProvider) {
/*  75 */     super(executor);
/*     */ 
/*     */     
/*     */     try {
/*  79 */       if (selectorProvider == null) {
/*  80 */         this.selector = Selector.open();
/*     */       } else {
/*  82 */         this.selector = selectorProvider.openSelector();
/*     */       }
/*     */     
/*  85 */     } catch (IOException e) {
/*  86 */       throw new RuntimeIoException("Failed to open a selector.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDispose() throws Exception {
/*  92 */     this.selector.close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int select(long timeout) throws Exception {
/*  97 */     return this.selector.select(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int select() throws Exception {
/* 102 */     return this.selector.select();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSelectorEmpty() {
/* 107 */     return this.selector.keys().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void wakeup() {
/* 112 */     this.wakeupCalled.getAndSet(true);
/* 113 */     this.selector.wakeup();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Iterator<NioSession> allSessions() {
/* 118 */     return new IoSessionIterator<NioSession>(this.selector.keys());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<NioSession> selectedSessions() {
/* 124 */     return new IoSessionIterator<NioSession>(this.selector.selectedKeys());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init(NioSession session) throws Exception {
/* 129 */     SelectableChannel ch = (SelectableChannel)session.getChannel();
/* 130 */     ch.configureBlocking(false);
/* 131 */     session.setSelectionKey(ch.register(this.selector, 1, session));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void destroy(NioSession session) throws Exception {
/* 136 */     ByteChannel ch = session.getChannel();
/* 137 */     SelectionKey key = session.getSelectionKey();
/* 138 */     if (key != null) {
/* 139 */       key.cancel();
/*     */     }
/* 141 */     ch.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerNewSelector() throws IOException {
/* 151 */     synchronized (this.selector) {
/* 152 */       Set<SelectionKey> keys = this.selector.keys();
/*     */ 
/*     */       
/* 155 */       Selector newSelector = null;
/*     */       
/* 157 */       if (this.selectorProvider == null) {
/* 158 */         newSelector = Selector.open();
/*     */       } else {
/* 160 */         newSelector = this.selectorProvider.openSelector();
/*     */       } 
/*     */ 
/*     */       
/* 164 */       for (SelectionKey key : keys) {
/* 165 */         SelectableChannel ch = key.channel();
/*     */ 
/*     */         
/* 168 */         NioSession session = (NioSession)key.attachment();
/* 169 */         SelectionKey newKey = ch.register(newSelector, key.interestOps(), session);
/* 170 */         session.setSelectionKey(newKey);
/*     */       } 
/*     */ 
/*     */       
/* 174 */       this.selector.close();
/* 175 */       this.selector = newSelector;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBrokenConnection() throws IOException {
/* 185 */     boolean brokenSession = false;
/*     */     
/* 187 */     synchronized (this.selector) {
/*     */       
/* 189 */       Set<SelectionKey> keys = this.selector.keys();
/*     */ 
/*     */ 
/*     */       
/* 193 */       for (SelectionKey key : keys) {
/* 194 */         SelectableChannel channel = key.channel();
/*     */         
/* 196 */         if ((channel instanceof DatagramChannel && !((DatagramChannel)channel).isConnected()) || (channel instanceof SocketChannel && !((SocketChannel)channel).isConnected())) {
/*     */ 
/*     */ 
/*     */           
/* 200 */           key.cancel();
/*     */ 
/*     */           
/* 203 */           brokenSession = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     return brokenSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SessionState getState(NioSession session) {
/* 216 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 218 */     if (key == null)
/*     */     {
/* 220 */       return SessionState.OPENING;
/*     */     }
/*     */     
/* 223 */     if (key.isValid())
/*     */     {
/* 225 */       return SessionState.OPENED;
/*     */     }
/*     */     
/* 228 */     return SessionState.CLOSING;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReadable(NioSession session) {
/* 234 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 236 */     return (key != null && key.isValid() && key.isReadable());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isWritable(NioSession session) {
/* 241 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 243 */     return (key != null && key.isValid() && key.isWritable());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInterestedInRead(NioSession session) {
/* 248 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 250 */     return (key != null && key.isValid() && (key.interestOps() & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInterestedInWrite(NioSession session) {
/* 255 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 257 */     return (key != null && key.isValid() && (key.interestOps() & 0x4) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInterestedInRead(NioSession session, boolean isInterested) throws Exception {
/* 265 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 267 */     if (key == null || !key.isValid()) {
/*     */       return;
/*     */     }
/*     */     
/* 271 */     int oldInterestOps = key.interestOps();
/* 272 */     int newInterestOps = oldInterestOps;
/*     */     
/* 274 */     if (isInterested) {
/* 275 */       newInterestOps |= 0x1;
/*     */     } else {
/* 277 */       newInterestOps &= 0xFFFFFFFE;
/*     */     } 
/*     */     
/* 280 */     if (oldInterestOps != newInterestOps) {
/* 281 */       key.interestOps(newInterestOps);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInterestedInWrite(NioSession session, boolean isInterested) throws Exception {
/* 290 */     SelectionKey key = session.getSelectionKey();
/*     */     
/* 292 */     if (key == null || !key.isValid()) {
/*     */       return;
/*     */     }
/*     */     
/* 296 */     int newInterestOps = key.interestOps();
/*     */     
/* 298 */     if (isInterested) {
/* 299 */       newInterestOps |= 0x4;
/*     */     } else {
/* 301 */       newInterestOps &= 0xFFFFFFFB;
/*     */     } 
/*     */     
/* 304 */     key.interestOps(newInterestOps);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int read(NioSession session, IoBuffer buf) throws Exception {
/* 309 */     ByteChannel channel = session.getChannel();
/*     */     
/* 311 */     return channel.read(buf.buf());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int write(NioSession session, IoBuffer buf, int length) throws Exception {
/* 316 */     if (buf.remaining() <= length) {
/* 317 */       return session.getChannel().write(buf.buf());
/*     */     }
/*     */     
/* 320 */     int oldLimit = buf.limit();
/* 321 */     buf.limit(buf.position() + length);
/*     */     try {
/* 323 */       return session.getChannel().write(buf.buf());
/*     */     } finally {
/* 325 */       buf.limit(oldLimit);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int transferFile(NioSession session, FileRegion region, int length) throws Exception {
/*     */     try {
/* 332 */       return (int)region.getFileChannel().transferTo(region.getPosition(), length, session.getChannel());
/* 333 */     } catch (IOException e) {
/*     */ 
/*     */       
/* 336 */       String message = e.getMessage();
/* 337 */       if (message != null && message.contains("temporarily unavailable")) {
/* 338 */         return 0;
/*     */       }
/*     */       
/* 341 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class IoSessionIterator<NioSession>
/*     */     implements Iterator<NioSession>
/*     */   {
/*     */     private final Iterator<SelectionKey> iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private IoSessionIterator(Set<SelectionKey> keys) {
/* 359 */       this.iterator = keys.iterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 366 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NioSession next() {
/* 373 */       SelectionKey key = this.iterator.next();
/* 374 */       NioSession nioSession = (NioSession)key.attachment();
/* 375 */       return nioSession;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 382 */       this.iterator.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */