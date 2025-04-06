/*     */ package org.apache.mina.proxy.handlers.socks;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetSocketAddress;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.ByteUtilities;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
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
/*     */ 
/*     */ 
/*     */ public class Socks5LogicHandler
/*     */   extends AbstractSocksLogicHandler
/*     */ {
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(Socks5LogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final String SELECTED_AUTH_METHOD = Socks5LogicHandler.class.getName() + ".SelectedAuthMethod";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final String HANDSHAKE_STEP = Socks5LogicHandler.class.getName() + ".HandshakeStep";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static final String GSS_CONTEXT = Socks5LogicHandler.class.getName() + ".GSSContext";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final String GSS_TOKEN = Socks5LogicHandler.class.getName() + ".GSSToken";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socks5LogicHandler(ProxyIoSession proxyIoSession) {
/*  73 */     super(proxyIoSession);
/*  74 */     getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void doHandshake(IoFilter.NextFilter nextFilter) {
/*  83 */     LOGGER.debug(" doHandshake()");
/*     */ 
/*     */     
/*  86 */     writeRequest(nextFilter, this.request, ((Integer)getSession().getAttribute(HANDSHAKE_STEP)).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoBuffer encodeInitialGreetingPacket(SocksProxyRequest request) {
/*  96 */     byte nbMethods = (byte)SocksProxyConstants.SUPPORTED_AUTH_METHODS.length;
/*  97 */     IoBuffer buf = IoBuffer.allocate(2 + nbMethods);
/*     */     
/*  99 */     buf.put(request.getProtocolVersion());
/* 100 */     buf.put(nbMethods);
/* 101 */     buf.put(SocksProxyConstants.SUPPORTED_AUTH_METHODS);
/*     */     
/* 103 */     return buf;
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
/*     */   private IoBuffer encodeProxyRequestPacket(SocksProxyRequest request) throws UnsupportedEncodingException {
/* 115 */     int len = 6;
/* 116 */     InetSocketAddress adr = request.getEndpointAddress();
/* 117 */     byte addressType = 0;
/* 118 */     byte[] host = null;
/*     */     
/* 120 */     if (adr != null && !adr.isUnresolved()) {
/* 121 */       if (adr.getAddress() instanceof java.net.Inet6Address) {
/* 122 */         len += 16;
/* 123 */         addressType = 4;
/* 124 */       } else if (adr.getAddress() instanceof java.net.Inet4Address) {
/* 125 */         len += 4;
/* 126 */         addressType = 1;
/*     */       } 
/*     */     } else {
/* 129 */       host = (request.getHost() != null) ? request.getHost().getBytes("ASCII") : null;
/*     */       
/* 131 */       if (host != null) {
/* 132 */         len += 1 + host.length;
/* 133 */         addressType = 3;
/*     */       } else {
/* 135 */         throw new IllegalArgumentException("SocksProxyRequest object has no suitable endpoint information");
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     IoBuffer buf = IoBuffer.allocate(len);
/*     */     
/* 141 */     buf.put(request.getProtocolVersion());
/* 142 */     buf.put(request.getCommandCode());
/* 143 */     buf.put((byte)0);
/* 144 */     buf.put(addressType);
/*     */     
/* 146 */     if (host == null) {
/* 147 */       buf.put(request.getIpAddress());
/*     */     } else {
/* 149 */       buf.put((byte)host.length);
/* 150 */       buf.put(host);
/*     */     } 
/*     */     
/* 153 */     buf.put(request.getPort());
/*     */     
/* 155 */     return buf;
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
/*     */   private IoBuffer encodeAuthenticationPacket(SocksProxyRequest request) throws UnsupportedEncodingException, GSSException {
/*     */     byte[] user, pwd;
/*     */     IoBuffer buf;
/* 170 */     byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();
/*     */     
/* 172 */     switch (method) {
/*     */ 
/*     */       
/*     */       case 0:
/* 176 */         getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(2));
/*     */         break;
/*     */       
/*     */       case 1:
/* 180 */         return encodeGSSAPIAuthenticationPacket(request);
/*     */ 
/*     */       
/*     */       case 2:
/* 184 */         user = request.getUserName().getBytes("ASCII");
/* 185 */         pwd = request.getPassword().getBytes("ASCII");
/* 186 */         buf = IoBuffer.allocate(3 + user.length + pwd.length);
/*     */         
/* 188 */         buf.put((byte)1);
/* 189 */         buf.put((byte)user.length);
/* 190 */         buf.put(user);
/* 191 */         buf.put((byte)pwd.length);
/* 192 */         buf.put(pwd);
/*     */         
/* 194 */         return buf;
/*     */     } 
/*     */     
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoBuffer encodeGSSAPIAuthenticationPacket(SocksProxyRequest request) throws GSSException {
/* 208 */     GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
/* 209 */     if (ctx == null) {
/*     */       
/* 211 */       GSSManager manager = GSSManager.getInstance();
/* 212 */       GSSName serverName = manager.createName(request.getServiceKerberosName(), (Oid)null);
/* 213 */       Oid krb5OID = new Oid("1.2.840.113554.1.2.2");
/*     */       
/* 215 */       if (LOGGER.isDebugEnabled()) {
/* 216 */         LOGGER.debug("Available mechs:");
/* 217 */         for (Oid o : manager.getMechs()) {
/* 218 */           if (o.equals(krb5OID)) {
/* 219 */             LOGGER.debug("Found Kerberos V OID available");
/*     */           }
/* 221 */           LOGGER.debug("{} with oid = {}", manager.getNamesForMech(o), o);
/*     */         } 
/*     */       } 
/*     */       
/* 225 */       ctx = manager.createContext(serverName, krb5OID, null, 0);
/*     */       
/* 227 */       ctx.requestMutualAuth(true);
/* 228 */       ctx.requestConf(false);
/* 229 */       ctx.requestInteg(false);
/*     */       
/* 231 */       getSession().setAttribute(GSS_CONTEXT, ctx);
/*     */     } 
/*     */     
/* 234 */     byte[] token = (byte[])getSession().getAttribute(GSS_TOKEN);
/* 235 */     if (token != null) {
/* 236 */       LOGGER.debug("  Received Token[{}] = {}", Integer.valueOf(token.length), ByteUtilities.asHex(token));
/*     */     }
/* 238 */     IoBuffer buf = null;
/*     */     
/* 240 */     if (!ctx.isEstablished()) {
/*     */       
/* 242 */       if (token == null) {
/* 243 */         token = new byte[32];
/*     */       }
/*     */       
/* 246 */       token = ctx.initSecContext(token, 0, token.length);
/*     */ 
/*     */ 
/*     */       
/* 250 */       if (token != null) {
/* 251 */         LOGGER.debug("  Sending Token[{}] = {}", Integer.valueOf(token.length), ByteUtilities.asHex(token));
/*     */         
/* 253 */         getSession().setAttribute(GSS_TOKEN, token);
/* 254 */         buf = IoBuffer.allocate(4 + token.length);
/* 255 */         buf.put(new byte[] { 1, 1 });
/*     */ 
/*     */         
/* 258 */         buf.put(ByteUtilities.intToNetworkByteOrder(token.length, 2));
/* 259 */         buf.put(token);
/*     */       } 
/*     */     } 
/*     */     
/* 263 */     return buf;
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
/*     */   private void writeRequest(IoFilter.NextFilter nextFilter, SocksProxyRequest request, int step) {
/*     */     try {
/* 276 */       IoBuffer buf = null;
/*     */       
/* 278 */       if (step == 0) {
/* 279 */         buf = encodeInitialGreetingPacket(request);
/* 280 */       } else if (step == 1) {
/*     */         
/* 282 */         buf = encodeAuthenticationPacket(request);
/*     */         
/* 284 */         if (buf == null) {
/* 285 */           step = 2;
/*     */         }
/*     */       } 
/*     */       
/* 289 */       if (step == 2) {
/* 290 */         buf = encodeProxyRequestPacket(request);
/*     */       }
/*     */       
/* 293 */       buf.flip();
/* 294 */       writeData(nextFilter, buf);
/*     */     }
/* 296 */     catch (Exception ex) {
/* 297 */       closeSession("Unable to send Socks request: ", ex);
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
/*     */   public synchronized void messageReceived(IoFilter.NextFilter nextFilter, IoBuffer buf) {
/*     */     try {
/* 310 */       int step = ((Integer)getSession().getAttribute(HANDSHAKE_STEP)).intValue();
/*     */       
/* 312 */       if (step == 0 && buf.get(0) != 5) {
/* 313 */         throw new IllegalStateException("Wrong socks version running on server");
/*     */       }
/*     */       
/* 316 */       if ((step == 0 || step == 1) && buf.remaining() >= 2) {
/*     */         
/* 318 */         handleResponse(nextFilter, buf, step);
/* 319 */       } else if (step == 2 && buf.remaining() >= 5) {
/* 320 */         handleResponse(nextFilter, buf, step);
/*     */       } 
/* 322 */     } catch (Exception ex) {
/* 323 */       closeSession("Proxy handshake failed: ", ex);
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
/*     */   protected void handleResponse(IoFilter.NextFilter nextFilter, IoBuffer buf, int step) throws Exception {
/* 335 */     int len = 2;
/* 336 */     if (step == 0) {
/*     */       
/* 338 */       byte method = buf.get(1);
/*     */       
/* 340 */       if (method == -1) {
/* 341 */         throw new IllegalStateException("No acceptable authentication method to use with the socks proxy server");
/*     */       }
/*     */ 
/*     */       
/* 345 */       getSession().setAttribute(SELECTED_AUTH_METHOD, Byte.valueOf(method));
/*     */     }
/* 347 */     else if (step == 1) {
/*     */       
/* 349 */       byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();
/*     */       
/* 351 */       if (method == 1) {
/* 352 */         int oldPos = buf.position();
/*     */         
/* 354 */         if (buf.get(0) != 1) {
/* 355 */           throw new IllegalStateException("Authentication failed");
/*     */         }
/* 357 */         if (buf.get(1) == 255) {
/* 358 */           throw new IllegalStateException("Authentication failed: GSS API Security Context Failure");
/*     */         }
/*     */         
/* 361 */         if (buf.remaining() >= 2) {
/* 362 */           byte[] size = new byte[2];
/* 363 */           buf.get(size);
/* 364 */           int s = ByteUtilities.makeIntFromByte2(size);
/* 365 */           if (buf.remaining() >= s) {
/* 366 */             byte[] token = new byte[s];
/* 367 */             buf.get(token);
/* 368 */             getSession().setAttribute(GSS_TOKEN, token);
/* 369 */             len = 0;
/*     */           } else {
/*     */             return;
/*     */           } 
/*     */         } else {
/* 374 */           buf.position(oldPos);
/*     */           return;
/*     */         } 
/* 377 */       } else if (buf.get(1) != 0) {
/* 378 */         throw new IllegalStateException("Authentication failed");
/*     */       }
/*     */     
/* 381 */     } else if (step == 2) {
/*     */       
/* 383 */       byte addressType = buf.get(3);
/* 384 */       len = 6;
/* 385 */       if (addressType == 4) {
/* 386 */         len += 16;
/* 387 */       } else if (addressType == 1) {
/* 388 */         len += 4;
/* 389 */       } else if (addressType == 3) {
/* 390 */         len += 1 + buf.get(4);
/*     */       } else {
/* 392 */         throw new IllegalStateException("Unknwon address type");
/*     */       } 
/*     */       
/* 395 */       if (buf.remaining() >= len) {
/*     */         
/* 397 */         byte status = buf.get(1);
/* 398 */         LOGGER.debug("  response status: {}", SocksProxyConstants.getReplyCodeAsString(status));
/*     */         
/* 400 */         if (status == 0) {
/* 401 */           buf.position(buf.position() + len);
/* 402 */           setHandshakeComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 406 */         throw new Exception("Proxy handshake failed - Code: 0x" + ByteUtilities.asHex(new byte[] { status }));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 412 */     if (len > 0) {
/* 413 */       buf.position(buf.position() + len);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 418 */     boolean isAuthenticating = false;
/* 419 */     if (step == 1) {
/* 420 */       byte method = ((Byte)getSession().getAttribute(SELECTED_AUTH_METHOD)).byteValue();
/* 421 */       if (method == 1) {
/* 422 */         GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
/* 423 */         if (ctx == null || !ctx.isEstablished()) {
/* 424 */           isAuthenticating = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 429 */     if (!isAuthenticating) {
/* 430 */       getSession().setAttribute(HANDSHAKE_STEP, Integer.valueOf(++step));
/*     */     }
/*     */     
/* 433 */     doHandshake(nextFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeSession(String message) {
/* 444 */     GSSContext ctx = (GSSContext)getSession().getAttribute(GSS_CONTEXT);
/* 445 */     if (ctx != null) {
/*     */       try {
/* 447 */         ctx.dispose();
/* 448 */       } catch (GSSException e) {
/* 449 */         e.printStackTrace();
/* 450 */         closeSession(message, e);
/*     */         return;
/*     */       } 
/*     */     }
/* 454 */     super.closeSession(message);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/socks/Socks5LogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */