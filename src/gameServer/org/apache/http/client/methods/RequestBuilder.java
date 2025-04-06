/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.entity.UrlEncodedFormEntity;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import org.apache.http.client.utils.URLEncodedUtils;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.HeaderGroup;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ @NotThreadSafe
/*     */ public class RequestBuilder
/*     */ {
/*     */   private String method;
/*     */   private Charset charset;
/*     */   private ProtocolVersion version;
/*     */   private URI uri;
/*     */   private HeaderGroup headergroup;
/*     */   private HttpEntity entity;
/*     */   private List<NameValuePair> parameters;
/*     */   private RequestConfig config;
/*     */   
/*     */   RequestBuilder(String method) {
/*  84 */     this.charset = Consts.UTF_8;
/*  85 */     this.method = method;
/*     */   }
/*     */ 
/*     */   
/*     */   RequestBuilder(String method, URI uri) {
/*  90 */     this.method = method;
/*  91 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */   
/*     */   RequestBuilder(String method, String uri) {
/*  96 */     this.method = method;
/*  97 */     this.uri = (uri != null) ? URI.create(uri) : null;
/*     */   }
/*     */   
/*     */   RequestBuilder() {
/* 101 */     this(null);
/*     */   }
/*     */   
/*     */   public static RequestBuilder create(String method) {
/* 105 */     Args.notBlank(method, "HTTP method");
/* 106 */     return new RequestBuilder(method);
/*     */   }
/*     */   
/*     */   public static RequestBuilder get() {
/* 110 */     return new RequestBuilder("GET");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder get(URI uri) {
/* 117 */     return new RequestBuilder("GET", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder get(String uri) {
/* 124 */     return new RequestBuilder("GET", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder head() {
/* 128 */     return new RequestBuilder("HEAD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder head(URI uri) {
/* 135 */     return new RequestBuilder("HEAD", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder head(String uri) {
/* 142 */     return new RequestBuilder("HEAD", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch() {
/* 149 */     return new RequestBuilder("PATCH");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch(URI uri) {
/* 156 */     return new RequestBuilder("PATCH", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder patch(String uri) {
/* 163 */     return new RequestBuilder("PATCH", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder post() {
/* 167 */     return new RequestBuilder("POST");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder post(URI uri) {
/* 174 */     return new RequestBuilder("POST", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder post(String uri) {
/* 181 */     return new RequestBuilder("POST", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder put() {
/* 185 */     return new RequestBuilder("PUT");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder put(URI uri) {
/* 192 */     return new RequestBuilder("PUT", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder put(String uri) {
/* 199 */     return new RequestBuilder("PUT", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder delete() {
/* 203 */     return new RequestBuilder("DELETE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder delete(URI uri) {
/* 210 */     return new RequestBuilder("DELETE", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder delete(String uri) {
/* 217 */     return new RequestBuilder("DELETE", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder trace() {
/* 221 */     return new RequestBuilder("TRACE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder trace(URI uri) {
/* 228 */     return new RequestBuilder("TRACE", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder trace(String uri) {
/* 235 */     return new RequestBuilder("TRACE", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder options() {
/* 239 */     return new RequestBuilder("OPTIONS");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder options(URI uri) {
/* 246 */     return new RequestBuilder("OPTIONS", uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBuilder options(String uri) {
/* 253 */     return new RequestBuilder("OPTIONS", uri);
/*     */   }
/*     */   
/*     */   public static RequestBuilder copy(HttpRequest request) {
/* 257 */     Args.notNull(request, "HTTP request");
/* 258 */     return (new RequestBuilder()).doCopy(request);
/*     */   }
/*     */   private RequestBuilder doCopy(HttpRequest request) {
/*     */     URI originalUri;
/* 262 */     if (request == null) {
/* 263 */       return this;
/*     */     }
/* 265 */     this.method = request.getRequestLine().getMethod();
/* 266 */     this.version = request.getRequestLine().getProtocolVersion();
/*     */     
/* 268 */     if (this.headergroup == null) {
/* 269 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 271 */     this.headergroup.clear();
/* 272 */     this.headergroup.setHeaders(request.getAllHeaders());
/*     */     
/* 274 */     this.parameters = null;
/* 275 */     this.entity = null;
/*     */     
/* 277 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 278 */       HttpEntity originalEntity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 279 */       ContentType contentType = ContentType.get(originalEntity);
/* 280 */       if (contentType != null && contentType.getMimeType().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
/*     */         
/*     */         try {
/* 283 */           List<NameValuePair> formParams = URLEncodedUtils.parse(originalEntity);
/* 284 */           if (!formParams.isEmpty()) {
/* 285 */             this.parameters = formParams;
/*     */           }
/* 287 */         } catch (IOException ignore) {}
/*     */       } else {
/*     */         
/* 290 */         this.entity = originalEntity;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 295 */     if (request instanceof HttpUriRequest) {
/* 296 */       originalUri = ((HttpUriRequest)request).getURI();
/*     */     } else {
/* 298 */       originalUri = URI.create(request.getRequestLine().getUri());
/*     */     } 
/*     */     
/* 301 */     URIBuilder uriBuilder = new URIBuilder(originalUri);
/* 302 */     if (this.parameters == null) {
/* 303 */       List<NameValuePair> queryParams = uriBuilder.getQueryParams();
/* 304 */       if (!queryParams.isEmpty()) {
/* 305 */         this.parameters = queryParams;
/* 306 */         uriBuilder.clearParameters();
/*     */       } else {
/* 308 */         this.parameters = null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 312 */       this.uri = uriBuilder.build();
/* 313 */     } catch (URISyntaxException ex) {
/*     */       
/* 315 */       this.uri = originalUri;
/*     */     } 
/*     */     
/* 318 */     if (request instanceof Configurable) {
/* 319 */       this.config = ((Configurable)request).getConfig();
/*     */     } else {
/* 321 */       this.config = null;
/*     */     } 
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestBuilder setCharset(Charset charset) {
/* 330 */     this.charset = charset;
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 338 */     return this.charset;
/*     */   }
/*     */   
/*     */   public String getMethod() {
/* 342 */     return this.method;
/*     */   }
/*     */   
/*     */   public ProtocolVersion getVersion() {
/* 346 */     return this.version;
/*     */   }
/*     */   
/*     */   public RequestBuilder setVersion(ProtocolVersion version) {
/* 350 */     this.version = version;
/* 351 */     return this;
/*     */   }
/*     */   
/*     */   public URI getUri() {
/* 355 */     return this.uri;
/*     */   }
/*     */   
/*     */   public RequestBuilder setUri(URI uri) {
/* 359 */     this.uri = uri;
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setUri(String uri) {
/* 364 */     this.uri = (uri != null) ? URI.create(uri) : null;
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 369 */     return (this.headergroup != null) ? this.headergroup.getFirstHeader(name) : null;
/*     */   }
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 373 */     return (this.headergroup != null) ? this.headergroup.getLastHeader(name) : null;
/*     */   }
/*     */   
/*     */   public Header[] getHeaders(String name) {
/* 377 */     return (this.headergroup != null) ? this.headergroup.getHeaders(name) : null;
/*     */   }
/*     */   
/*     */   public RequestBuilder addHeader(Header header) {
/* 381 */     if (this.headergroup == null) {
/* 382 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 384 */     this.headergroup.addHeader(header);
/* 385 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder addHeader(String name, String value) {
/* 389 */     if (this.headergroup == null) {
/* 390 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 392 */     this.headergroup.addHeader((Header)new BasicHeader(name, value));
/* 393 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder removeHeader(Header header) {
/* 397 */     if (this.headergroup == null) {
/* 398 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 400 */     this.headergroup.removeHeader(header);
/* 401 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder removeHeaders(String name) {
/* 405 */     if (name == null || this.headergroup == null) {
/* 406 */       return this;
/*     */     }
/* 408 */     for (HeaderIterator i = this.headergroup.iterator(); i.hasNext(); ) {
/* 409 */       Header header = i.nextHeader();
/* 410 */       if (name.equalsIgnoreCase(header.getName())) {
/* 411 */         i.remove();
/*     */       }
/*     */     } 
/* 414 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setHeader(Header header) {
/* 418 */     if (this.headergroup == null) {
/* 419 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 421 */     this.headergroup.updateHeader(header);
/* 422 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder setHeader(String name, String value) {
/* 426 */     if (this.headergroup == null) {
/* 427 */       this.headergroup = new HeaderGroup();
/*     */     }
/* 429 */     this.headergroup.updateHeader((Header)new BasicHeader(name, value));
/* 430 */     return this;
/*     */   }
/*     */   
/*     */   public HttpEntity getEntity() {
/* 434 */     return this.entity;
/*     */   }
/*     */   
/*     */   public RequestBuilder setEntity(HttpEntity entity) {
/* 438 */     this.entity = entity;
/* 439 */     return this;
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 443 */     return (this.parameters != null) ? new ArrayList<NameValuePair>(this.parameters) : new ArrayList<NameValuePair>();
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestBuilder addParameter(NameValuePair nvp) {
/* 448 */     Args.notNull(nvp, "Name value pair");
/* 449 */     if (this.parameters == null) {
/* 450 */       this.parameters = new LinkedList<NameValuePair>();
/*     */     }
/* 452 */     this.parameters.add(nvp);
/* 453 */     return this;
/*     */   }
/*     */   
/*     */   public RequestBuilder addParameter(String name, String value) {
/* 457 */     return addParameter((NameValuePair)new BasicNameValuePair(name, value));
/*     */   }
/*     */   
/*     */   public RequestBuilder addParameters(NameValuePair... nvps) {
/* 461 */     for (NameValuePair nvp : nvps) {
/* 462 */       addParameter(nvp);
/*     */     }
/* 464 */     return this;
/*     */   }
/*     */   
/*     */   public RequestConfig getConfig() {
/* 468 */     return this.config;
/*     */   }
/*     */   
/*     */   public RequestBuilder setConfig(RequestConfig config) {
/* 472 */     this.config = config;
/* 473 */     return this;
/*     */   }
/*     */   public HttpUriRequest build() {
/*     */     HttpRequestBase result;
/*     */     UrlEncodedFormEntity urlEncodedFormEntity;
/* 478 */     URI uriNotNull = (this.uri != null) ? this.uri : URI.create("/");
/* 479 */     HttpEntity entityCopy = this.entity;
/* 480 */     if (this.parameters != null && !this.parameters.isEmpty()) {
/* 481 */       if (entityCopy == null && ("POST".equalsIgnoreCase(this.method) || "PUT".equalsIgnoreCase(this.method))) {
/*     */         
/* 483 */         urlEncodedFormEntity = new UrlEncodedFormEntity(this.parameters, HTTP.DEF_CONTENT_CHARSET);
/*     */       } else {
/*     */         try {
/* 486 */           uriNotNull = (new URIBuilder(uriNotNull)).setCharset(this.charset).addParameters(this.parameters).build();
/*     */ 
/*     */         
/*     */         }
/* 490 */         catch (URISyntaxException ex) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 495 */     if (urlEncodedFormEntity == null) {
/* 496 */       result = new InternalRequest(this.method);
/*     */     } else {
/* 498 */       InternalEntityEclosingRequest request = new InternalEntityEclosingRequest(this.method);
/* 499 */       request.setEntity((HttpEntity)urlEncodedFormEntity);
/* 500 */       result = request;
/*     */     } 
/* 502 */     result.setProtocolVersion(this.version);
/* 503 */     result.setURI(uriNotNull);
/* 504 */     if (this.headergroup != null) {
/* 505 */       result.setHeaders(this.headergroup.getAllHeaders());
/*     */     }
/* 507 */     result.setConfig(this.config);
/* 508 */     return result;
/*     */   }
/*     */   
/*     */   static class InternalRequest
/*     */     extends HttpRequestBase
/*     */   {
/*     */     private final String method;
/*     */     
/*     */     InternalRequest(String method) {
/* 517 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 522 */       return this.method;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalEntityEclosingRequest
/*     */     extends HttpEntityEnclosingRequestBase
/*     */   {
/*     */     private final String method;
/*     */     
/*     */     InternalEntityEclosingRequest(String method) {
/* 533 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 538 */       return this.method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/methods/RequestBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */