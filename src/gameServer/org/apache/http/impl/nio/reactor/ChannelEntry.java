/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import java.nio.channels.SocketChannel;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Immutable
/*     */ public class ChannelEntry
/*     */ {
/*     */   private final SocketChannel channel;
/*     */   private final SessionRequestImpl sessionRequest;
/*     */   
/*     */   public ChannelEntry(SocketChannel channel, SessionRequestImpl sessionRequest) {
/*  57 */     Args.notNull(channel, "Socket channel");
/*  58 */     this.channel = channel;
/*  59 */     this.sessionRequest = sessionRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelEntry(SocketChannel channel) {
/*  68 */     this(channel, null);
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
/*     */   public SessionRequestImpl getSessionRequest() {
/*  80 */     return this.sessionRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getAttachment() {
/*  90 */     if (this.sessionRequest != null) {
/*  91 */       return this.sessionRequest.getAttachment();
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketChannel getChannel() {
/* 103 */     return this.channel;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/ChannelEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */