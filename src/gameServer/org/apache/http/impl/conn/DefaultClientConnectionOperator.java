/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.HttpInetSocketAddress;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.conn.scheme.SchemeSocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ @ThreadSafe
/*     */ public class DefaultClientConnectionOperator
/*     */   implements ClientConnectionOperator
/*     */ {
/*  92 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DnsResolver dnsResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes) {
/* 108 */     Args.notNull(schemes, "Scheme registry");
/* 109 */     this.schemeRegistry = schemes;
/* 110 */     this.dnsResolver = new SystemDefaultDnsResolver();
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
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes, DnsResolver dnsResolver) {
/* 123 */     Args.notNull(schemes, "Scheme registry");
/*     */     
/* 125 */     Args.notNull(dnsResolver, "DNS resolver");
/*     */     
/* 127 */     this.schemeRegistry = schemes;
/* 128 */     this.dnsResolver = dnsResolver;
/*     */   }
/*     */   
/*     */   public OperatedClientConnection createConnection() {
/* 132 */     return new DefaultClientConnection();
/*     */   }
/*     */   
/*     */   private SchemeRegistry getSchemeRegistry(HttpContext context) {
/* 136 */     SchemeRegistry reg = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */     
/* 138 */     if (reg == null) {
/* 139 */       reg = this.schemeRegistry;
/*     */     }
/* 141 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void openConnection(OperatedClientConnection conn, HttpHost target, InetAddress local, HttpContext context, HttpParams params) throws IOException {
/* 150 */     Args.notNull(conn, "Connection");
/* 151 */     Args.notNull(target, "Target host");
/* 152 */     Args.notNull(params, "HTTP parameters");
/* 153 */     Asserts.check(!conn.isOpen(), "Connection must not be open");
/*     */     
/* 155 */     SchemeRegistry registry = getSchemeRegistry(context);
/* 156 */     Scheme schm = registry.getScheme(target.getSchemeName());
/* 157 */     SchemeSocketFactory sf = schm.getSchemeSocketFactory();
/*     */     
/* 159 */     InetAddress[] addresses = resolveHostname(target.getHostName());
/* 160 */     int port = schm.resolvePort(target.getPort());
/* 161 */     for (int i = 0; i < addresses.length; i++) {
/* 162 */       InetAddress address = addresses[i];
/* 163 */       boolean last = (i == addresses.length - 1);
/*     */       
/* 165 */       Socket sock = sf.createSocket(params);
/* 166 */       conn.opening(sock, target);
/*     */       
/* 168 */       HttpInetSocketAddress httpInetSocketAddress = new HttpInetSocketAddress(target, address, port);
/* 169 */       InetSocketAddress localAddress = null;
/* 170 */       if (local != null) {
/* 171 */         localAddress = new InetSocketAddress(local, 0);
/*     */       }
/* 173 */       if (this.log.isDebugEnabled()) {
/* 174 */         this.log.debug("Connecting to " + httpInetSocketAddress);
/*     */       }
/*     */       try {
/* 177 */         Socket connsock = sf.connectSocket(sock, (InetSocketAddress)httpInetSocketAddress, localAddress, params);
/* 178 */         if (sock != connsock) {
/* 179 */           sock = connsock;
/* 180 */           conn.opening(sock, target);
/*     */         } 
/* 182 */         prepareSocket(sock, context, params);
/* 183 */         conn.openCompleted(sf.isSecure(sock), params);
/*     */         return;
/* 185 */       } catch (ConnectException ex) {
/* 186 */         if (last) {
/* 187 */           throw ex;
/*     */         }
/* 189 */       } catch (ConnectTimeoutException ex) {
/* 190 */         if (last) {
/* 191 */           throw ex;
/*     */         }
/*     */       } 
/* 194 */       if (this.log.isDebugEnabled()) {
/* 195 */         this.log.debug("Connect to " + httpInetSocketAddress + " timed out. " + "Connection will be retried using another IP address");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
/* 206 */     Args.notNull(conn, "Connection");
/* 207 */     Args.notNull(target, "Target host");
/* 208 */     Args.notNull(params, "Parameters");
/* 209 */     Asserts.check(conn.isOpen(), "Connection must be open");
/*     */     
/* 211 */     SchemeRegistry registry = getSchemeRegistry(context);
/* 212 */     Scheme schm = registry.getScheme(target.getSchemeName());
/* 213 */     Asserts.check(schm.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
/*     */     
/* 215 */     SchemeLayeredSocketFactory lsf = (SchemeLayeredSocketFactory)schm.getSchemeSocketFactory();
/* 216 */     Socket sock = lsf.createLayeredSocket(conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()), params);
/*     */     
/* 218 */     prepareSocket(sock, context, params);
/* 219 */     conn.update(sock, target, lsf.isSecure(sock), params);
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
/*     */   protected void prepareSocket(Socket sock, HttpContext context, HttpParams params) throws IOException {
/* 235 */     sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 236 */     sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/*     */     
/* 238 */     int linger = HttpConnectionParams.getLinger(params);
/* 239 */     if (linger >= 0) {
/* 240 */       sock.setSoLinger((linger > 0), linger);
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
/*     */ 
/*     */   
/*     */   protected InetAddress[] resolveHostname(String host) throws UnknownHostException {
/* 259 */     return this.dnsResolver.resolve(host);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/DefaultClientConnectionOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */