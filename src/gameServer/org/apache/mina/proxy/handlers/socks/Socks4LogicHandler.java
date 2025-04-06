/*     */ package org.apache.mina.proxy.handlers.socks;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.ByteUtilities;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class Socks4LogicHandler
/*     */   extends AbstractSocksLogicHandler
/*     */ {
/*  39 */   private static final Logger logger = LoggerFactory.getLogger(Socks4LogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socks4LogicHandler(ProxyIoSession proxyIoSession) {
/*  45 */     super(proxyIoSession);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandshake(IoFilter.NextFilter nextFilter) {
/*  54 */     logger.debug(" doHandshake()");
/*     */ 
/*     */     
/*  57 */     writeRequest(nextFilter, this.request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRequest(IoFilter.NextFilter nextFilter, SocksProxyRequest request) {
/*     */     try {
/*  69 */       boolean isV4ARequest = Arrays.equals(request.getIpAddress(), SocksProxyConstants.FAKE_IP);
/*  70 */       byte[] userID = request.getUserName().getBytes("ASCII");
/*  71 */       byte[] host = isV4ARequest ? request.getHost().getBytes("ASCII") : null;
/*     */       
/*  73 */       int len = 9 + userID.length;
/*     */       
/*  75 */       if (isV4ARequest) {
/*  76 */         len += host.length + 1;
/*     */       }
/*     */       
/*  79 */       IoBuffer buf = IoBuffer.allocate(len);
/*     */       
/*  81 */       buf.put(request.getProtocolVersion());
/*  82 */       buf.put(request.getCommandCode());
/*  83 */       buf.put(request.getPort());
/*  84 */       buf.put(request.getIpAddress());
/*  85 */       buf.put(userID);
/*  86 */       buf.put((byte)0);
/*     */       
/*  88 */       if (isV4ARequest) {
/*  89 */         buf.put(host);
/*  90 */         buf.put((byte)0);
/*     */       } 
/*     */       
/*  93 */       if (isV4ARequest) {
/*  94 */         logger.debug("  sending SOCKS4a request");
/*     */       } else {
/*  96 */         logger.debug("  sending SOCKS4 request");
/*     */       } 
/*     */       
/*  99 */       buf.flip();
/* 100 */       writeData(nextFilter, buf);
/* 101 */     } catch (Exception ex) {
/* 102 */       closeSession("Unable to send Socks request: ", ex);
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
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoBuffer buf) {
/*     */     try {
/* 115 */       if (buf.remaining() >= 8) {
/* 116 */         handleResponse(buf);
/*     */       }
/* 118 */     } catch (Exception ex) {
/* 119 */       closeSession("Proxy handshake failed: ", ex);
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
/*     */   protected void handleResponse(IoBuffer buf) throws Exception {
/* 133 */     byte first = buf.get(0);
/*     */     
/* 135 */     if (first != 0) {
/* 136 */       throw new Exception("Socks response seems to be malformed");
/*     */     }
/*     */     
/* 139 */     byte status = buf.get(1);
/*     */ 
/*     */     
/* 142 */     buf.position(buf.position() + 8);
/*     */     
/* 144 */     if (status == 90) {
/* 145 */       setHandshakeComplete();
/*     */     } else {
/* 147 */       throw new Exception("Proxy handshake failed - Code: 0x" + ByteUtilities.asHex(new byte[] { status }) + " (" + SocksProxyConstants.getReplyCodeAsString(status) + ")");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/socks/Socks4LogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */