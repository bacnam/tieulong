/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import org.apache.http.HttpRequestInterceptor;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.client.protocol.RequestAddCookies;
/*    */ import org.apache.http.client.protocol.RequestAuthCache;
/*    */ import org.apache.http.client.protocol.RequestClientConnControl;
/*    */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*    */ import org.apache.http.client.protocol.RequestProxyAuthentication;
/*    */ import org.apache.http.client.protocol.RequestTargetAuthentication;
/*    */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*    */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*    */ import org.apache.http.nio.conn.ClientAsyncConnectionManager;
/*    */ import org.apache.http.nio.reactor.IOReactorException;
/*    */ import org.apache.http.params.HttpConnectionParams;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.params.HttpProtocolParams;
/*    */ import org.apache.http.params.SyncBasicHttpParams;
/*    */ import org.apache.http.protocol.BasicHttpProcessor;
/*    */ import org.apache.http.protocol.HTTP;
/*    */ import org.apache.http.protocol.RequestContent;
/*    */ import org.apache.http.protocol.RequestExpectContinue;
/*    */ import org.apache.http.protocol.RequestTargetHost;
/*    */ import org.apache.http.protocol.RequestUserAgent;
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
/*    */ @Deprecated
/*    */ public class DefaultHttpAsyncClient
/*    */   extends AbstractHttpAsyncClient
/*    */ {
/*    */   public DefaultHttpAsyncClient(ClientAsyncConnectionManager connmgr) {
/* 55 */     super(connmgr);
/*    */   }
/*    */   
/*    */   public DefaultHttpAsyncClient(IOReactorConfig config) throws IOReactorException {
/* 59 */     super(config);
/*    */   }
/*    */   
/*    */   public DefaultHttpAsyncClient() throws IOReactorException {
/* 63 */     super(new IOReactorConfig());
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpParams createHttpParams() {
/* 68 */     SyncBasicHttpParams syncBasicHttpParams = new SyncBasicHttpParams();
/* 69 */     setDefaultHttpParams((HttpParams)syncBasicHttpParams);
/* 70 */     return (HttpParams)syncBasicHttpParams;
/*    */   }
/*    */   
/*    */   public static void setDefaultHttpParams(HttpParams params) {
/* 74 */     HttpProtocolParams.setVersion(params, (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 75 */     HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
/* 76 */     HttpConnectionParams.setTcpNoDelay(params, true);
/* 77 */     HttpConnectionParams.setSocketBufferSize(params, 8192);
/* 78 */     HttpProtocolParams.setUserAgent(params, HttpAsyncClientBuilder.DEFAULT_USER_AGENT);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BasicHttpProcessor createHttpProcessor() {
/* 83 */     BasicHttpProcessor httpproc = new BasicHttpProcessor();
/* 84 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestDefaultHeaders());
/*    */     
/* 86 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestContent());
/* 87 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetHost());
/*    */     
/* 89 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestClientConnControl());
/* 90 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestUserAgent());
/* 91 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestExpectContinue());
/*    */     
/* 93 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAddCookies());
/* 94 */     httpproc.addInterceptor((HttpResponseInterceptor)new ResponseProcessCookies());
/*    */     
/* 96 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAuthCache());
/* 97 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetAuthentication());
/* 98 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestProxyAuthentication());
/* 99 */     return httpproc;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/DefaultHttpAsyncClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */