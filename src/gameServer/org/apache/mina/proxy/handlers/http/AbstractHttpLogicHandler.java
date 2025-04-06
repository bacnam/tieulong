/*     */ package org.apache.mina.proxy.handlers.http;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.proxy.AbstractProxyLogicHandler;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.IoBufferDecoder;
/*     */ import org.apache.mina.proxy.utils.StringUtilities;
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
/*     */ public abstract class AbstractHttpLogicHandler
/*     */   extends AbstractProxyLogicHandler
/*     */ {
/*  49 */   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHttpLogicHandler.class);
/*     */   
/*  51 */   private static final String DECODER = AbstractHttpLogicHandler.class.getName() + ".Decoder";
/*     */   
/*  53 */   private static final byte[] HTTP_DELIMITER = new byte[] { 13, 10, 13, 10 };
/*     */   
/*  55 */   private static final byte[] CRLF_DELIMITER = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private IoBuffer responseData = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private HttpProxyResponse parsedResponse = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private int contentLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasChunkedData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean waitingChunkedData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean waitingFooters;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int entityBodyStartPosition;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int entityBodyLimitPosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractHttpLogicHandler(ProxyIoSession proxyIoSession) {
/* 107 */     super(proxyIoSession);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void messageReceived(IoFilter.NextFilter nextFilter, IoBuffer buf) throws ProxyAuthException {
/* 118 */     LOGGER.debug(" messageReceived()");
/*     */     
/* 120 */     IoBufferDecoder decoder = (IoBufferDecoder)getSession().getAttribute(DECODER);
/* 121 */     if (decoder == null) {
/* 122 */       decoder = new IoBufferDecoder(HTTP_DELIMITER);
/* 123 */       getSession().setAttribute(DECODER, decoder);
/*     */     } 
/*     */     
/*     */     try {
/* 127 */       if (this.parsedResponse == null) {
/*     */         
/* 129 */         this.responseData = decoder.decodeFully(buf);
/* 130 */         if (this.responseData == null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 135 */         String responseHeader = this.responseData.getString(getProxyIoSession().getCharset().newDecoder());
/* 136 */         this.entityBodyStartPosition = this.responseData.position();
/*     */         
/* 138 */         LOGGER.debug("  response header received:\n{}", responseHeader.replace("\r", "\\r").replace("\n", "\\n\n"));
/*     */ 
/*     */ 
/*     */         
/* 142 */         this.parsedResponse = decodeResponse(responseHeader);
/*     */ 
/*     */         
/* 145 */         if (this.parsedResponse.getStatusCode() == 200 || (this.parsedResponse.getStatusCode() >= 300 && this.parsedResponse.getStatusCode() <= 307)) {
/*     */           
/* 147 */           buf.position(0);
/* 148 */           setHandshakeComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 152 */         String contentLengthHeader = StringUtilities.getSingleValuedHeader(this.parsedResponse.getHeaders(), "Content-Length");
/*     */ 
/*     */         
/* 155 */         if (contentLengthHeader == null) {
/* 156 */           this.contentLength = 0;
/*     */         } else {
/* 158 */           this.contentLength = Integer.parseInt(contentLengthHeader.trim());
/* 159 */           decoder.setContentLength(this.contentLength, true);
/*     */         } 
/*     */       } 
/*     */       
/* 163 */       if (!this.hasChunkedData) {
/* 164 */         if (this.contentLength > 0) {
/* 165 */           IoBuffer tmp = decoder.decodeFully(buf);
/* 166 */           if (tmp == null) {
/*     */             return;
/*     */           }
/* 169 */           this.responseData.setAutoExpand(true);
/* 170 */           this.responseData.put(tmp);
/* 171 */           this.contentLength = 0;
/*     */         } 
/*     */         
/* 174 */         if ("chunked".equalsIgnoreCase(StringUtilities.getSingleValuedHeader(this.parsedResponse.getHeaders(), "Transfer-Encoding"))) {
/*     */ 
/*     */           
/* 177 */           LOGGER.debug("Retrieving additional http response chunks");
/* 178 */           this.hasChunkedData = true;
/* 179 */           this.waitingChunkedData = true;
/*     */         } 
/*     */       } 
/*     */       
/* 183 */       if (this.hasChunkedData) {
/*     */         
/* 185 */         while (this.waitingChunkedData) {
/* 186 */           if (this.contentLength == 0) {
/* 187 */             decoder.setDelimiter(CRLF_DELIMITER, false);
/* 188 */             IoBuffer ioBuffer = decoder.decodeFully(buf);
/* 189 */             if (ioBuffer == null) {
/*     */               return;
/*     */             }
/*     */             
/* 193 */             String chunkSize = ioBuffer.getString(getProxyIoSession().getCharset().newDecoder());
/* 194 */             int pos = chunkSize.indexOf(';');
/* 195 */             if (pos >= 0) {
/* 196 */               chunkSize = chunkSize.substring(0, pos);
/*     */             } else {
/* 198 */               chunkSize = chunkSize.substring(0, chunkSize.length() - 2);
/*     */             } 
/* 200 */             this.contentLength = Integer.decode("0x" + chunkSize).intValue();
/* 201 */             if (this.contentLength > 0) {
/* 202 */               this.contentLength += 2;
/* 203 */               decoder.setContentLength(this.contentLength, true);
/*     */             } 
/*     */           } 
/*     */           
/* 207 */           if (this.contentLength == 0) {
/* 208 */             this.waitingChunkedData = false;
/* 209 */             this.waitingFooters = true;
/* 210 */             this.entityBodyLimitPosition = this.responseData.position();
/*     */             
/*     */             break;
/*     */           } 
/* 214 */           IoBuffer tmp = decoder.decodeFully(buf);
/* 215 */           if (tmp == null) {
/*     */             return;
/*     */           }
/* 218 */           this.contentLength = 0;
/* 219 */           this.responseData.put(tmp);
/* 220 */           buf.position(buf.position());
/*     */         } 
/*     */ 
/*     */         
/* 224 */         while (this.waitingFooters) {
/* 225 */           decoder.setDelimiter(CRLF_DELIMITER, false);
/* 226 */           IoBuffer tmp = decoder.decodeFully(buf);
/* 227 */           if (tmp == null) {
/*     */             return;
/*     */           }
/*     */           
/* 231 */           if (tmp.remaining() == 2) {
/* 232 */             this.waitingFooters = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 237 */           String footer = tmp.getString(getProxyIoSession().getCharset().newDecoder());
/* 238 */           String[] f = footer.split(":\\s?", 2);
/* 239 */           StringUtilities.addValueToHeader(this.parsedResponse.getHeaders(), f[0], f[1], false);
/* 240 */           this.responseData.put(tmp);
/* 241 */           this.responseData.put(CRLF_DELIMITER);
/*     */         } 
/*     */       } 
/*     */       
/* 245 */       this.responseData.flip();
/*     */       
/* 247 */       LOGGER.debug("  end of response received:\n{}", this.responseData.getString(getProxyIoSession().getCharset().newDecoder()));
/*     */ 
/*     */ 
/*     */       
/* 251 */       this.responseData.position(this.entityBodyStartPosition);
/* 252 */       this.responseData.limit(this.entityBodyLimitPosition);
/* 253 */       this.parsedResponse.setBody(this.responseData.getString(getProxyIoSession().getCharset().newDecoder()));
/*     */ 
/*     */       
/* 256 */       this.responseData.free();
/* 257 */       this.responseData = null;
/*     */       
/* 259 */       handleResponse(this.parsedResponse);
/*     */       
/* 261 */       this.parsedResponse = null;
/* 262 */       this.hasChunkedData = false;
/* 263 */       this.contentLength = -1;
/* 264 */       decoder.setDelimiter(HTTP_DELIMITER, true);
/*     */       
/* 266 */       if (!isHandshakeComplete()) {
/* 267 */         doHandshake(nextFilter);
/*     */       }
/* 269 */     } catch (Exception ex) {
/* 270 */       if (ex instanceof ProxyAuthException) {
/* 271 */         throw (ProxyAuthException)ex;
/*     */       }
/*     */       
/* 274 */       throw new ProxyAuthException("Handshake failed", ex);
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
/*     */   public void writeRequest(IoFilter.NextFilter nextFilter, HttpProxyRequest request) {
/* 293 */     ProxyIoSession proxyIoSession = getProxyIoSession();
/*     */     
/* 295 */     if (proxyIoSession.isReconnectionNeeded()) {
/* 296 */       reconnect(nextFilter, request);
/*     */     } else {
/* 298 */       writeRequest0(nextFilter, request);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeRequest0(IoFilter.NextFilter nextFilter, HttpProxyRequest request) {
/*     */     try {
/* 310 */       String data = request.toHttpString();
/* 311 */       IoBuffer buf = IoBuffer.wrap(data.getBytes(getProxyIoSession().getCharsetName()));
/*     */       
/* 313 */       LOGGER.debug("   write:\n{}", data.replace("\r", "\\r").replace("\n", "\\n\n"));
/*     */       
/* 315 */       writeData(nextFilter, buf);
/*     */     }
/* 317 */     catch (UnsupportedEncodingException ex) {
/* 318 */       closeSession("Unable to send HTTP request: ", ex);
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
/*     */   private void reconnect(final IoFilter.NextFilter nextFilter, final HttpProxyRequest request) {
/* 330 */     LOGGER.debug("Reconnecting to proxy ...");
/*     */     
/* 332 */     final ProxyIoSession proxyIoSession = getProxyIoSession();
/*     */ 
/*     */     
/* 335 */     proxyIoSession.getConnector().connect(new IoSessionInitializer<ConnectFuture>() {
/*     */           public void initializeSession(IoSession session, ConnectFuture future) {
/* 337 */             AbstractHttpLogicHandler.LOGGER.debug("Initializing new session: {}", session);
/* 338 */             session.setAttribute(ProxyIoSession.PROXY_SESSION, proxyIoSession);
/* 339 */             proxyIoSession.setSession(session);
/* 340 */             AbstractHttpLogicHandler.LOGGER.debug("  setting up proxyIoSession: {}", proxyIoSession);
/* 341 */             future.addListener(new IoFutureListener<ConnectFuture>()
/*     */                 {
/*     */                   public void operationComplete(ConnectFuture future)
/*     */                   {
/* 345 */                     proxyIoSession.setReconnectionNeeded(false);
/* 346 */                     AbstractHttpLogicHandler.this.writeRequest0(nextFilter, request);
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpProxyResponse decodeResponse(String response) throws Exception {
/* 359 */     LOGGER.debug("  parseResponse()");
/*     */ 
/*     */     
/* 362 */     String[] responseLines = response.split("\r\n");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     String[] statusLine = responseLines[0].trim().split(" ", 2);
/*     */     
/* 369 */     if (statusLine.length < 2) {
/* 370 */       throw new Exception("Invalid response status line (" + statusLine + "). Response: " + response);
/*     */     }
/*     */ 
/*     */     
/* 374 */     if (!statusLine[1].matches("^\\d\\d\\d")) {
/* 375 */       throw new Exception("Invalid response code (" + statusLine[1] + "). Response: " + response);
/*     */     }
/*     */     
/* 378 */     Map<String, List<String>> headers = new HashMap<String, List<String>>();
/*     */     
/* 380 */     for (int i = 1; i < responseLines.length; i++) {
/* 381 */       String[] args = responseLines[i].split(":\\s?", 2);
/* 382 */       StringUtilities.addValueToHeader(headers, args[0], args[1], false);
/*     */     } 
/*     */     
/* 385 */     return new HttpProxyResponse(statusLine[0], statusLine[1], headers);
/*     */   }
/*     */   
/*     */   public abstract void handleResponse(HttpProxyResponse paramHttpProxyResponse) throws ProxyAuthException;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/AbstractHttpLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */