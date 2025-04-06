/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class AbstractPooledConnAdapter
/*     */   extends AbstractClientConnAdapter
/*     */ {
/*     */   protected volatile AbstractPoolEntry poolEntry;
/*     */   
/*     */   protected AbstractPooledConnAdapter(ClientConnectionManager manager, AbstractPoolEntry entry) {
/*  66 */     super(manager, entry.connection);
/*  67 */     this.poolEntry = entry;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  71 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected AbstractPoolEntry getPoolEntry() {
/*  83 */     return this.poolEntry;
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
/*     */   protected void assertValid(AbstractPoolEntry entry) {
/*  95 */     if (isReleased() || entry == null) {
/*  96 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void assertAttached() {
/* 105 */     if (this.poolEntry == null) {
/* 106 */       throw new ConnectionShutdownException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void detach() {
/* 116 */     this.poolEntry = null;
/* 117 */     super.detach();
/*     */   }
/*     */   
/*     */   public HttpRoute getRoute() {
/* 121 */     AbstractPoolEntry entry = getPoolEntry();
/* 122 */     assertValid(entry);
/* 123 */     return (entry.tracker == null) ? null : entry.tracker.toRoute();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
/* 129 */     AbstractPoolEntry entry = getPoolEntry();
/* 130 */     assertValid(entry);
/* 131 */     entry.open(route, context, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tunnelTarget(boolean secure, HttpParams params) throws IOException {
/* 136 */     AbstractPoolEntry entry = getPoolEntry();
/* 137 */     assertValid(entry);
/* 138 */     entry.tunnelTarget(secure, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) throws IOException {
/* 143 */     AbstractPoolEntry entry = getPoolEntry();
/* 144 */     assertValid(entry);
/* 145 */     entry.tunnelProxy(next, secure, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void layerProtocol(HttpContext context, HttpParams params) throws IOException {
/* 150 */     AbstractPoolEntry entry = getPoolEntry();
/* 151 */     assertValid(entry);
/* 152 */     entry.layerProtocol(context, params);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 156 */     AbstractPoolEntry entry = getPoolEntry();
/* 157 */     if (entry != null) {
/* 158 */       entry.shutdownEntry();
/*     */     }
/*     */     
/* 161 */     OperatedClientConnection conn = getWrappedConnection();
/* 162 */     if (conn != null) {
/* 163 */       conn.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 168 */     AbstractPoolEntry entry = getPoolEntry();
/* 169 */     if (entry != null) {
/* 170 */       entry.shutdownEntry();
/*     */     }
/*     */     
/* 173 */     OperatedClientConnection conn = getWrappedConnection();
/* 174 */     if (conn != null) {
/* 175 */       conn.shutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 180 */     AbstractPoolEntry entry = getPoolEntry();
/* 181 */     assertValid(entry);
/* 182 */     return entry.getState();
/*     */   }
/*     */   
/*     */   public void setState(Object state) {
/* 186 */     AbstractPoolEntry entry = getPoolEntry();
/* 187 */     assertValid(entry);
/* 188 */     entry.setState(state);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/AbstractPooledConnAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */