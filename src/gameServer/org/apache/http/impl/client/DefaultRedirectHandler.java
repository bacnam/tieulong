/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CircularRedirectException;
/*     */ import org.apache.http.client.RedirectHandler;
/*     */ import org.apache.http.client.utils.URIUtils;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class DefaultRedirectHandler
/*     */   implements RedirectHandler
/*     */ {
/*  65 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
/*     */     HttpRequest request;
/*     */     String method;
/*  76 */     Args.notNull(response, "HTTP response");
/*     */     
/*  78 */     int statusCode = response.getStatusLine().getStatusCode();
/*  79 */     switch (statusCode) {
/*     */       case 301:
/*     */       case 302:
/*     */       case 307:
/*  83 */         request = (HttpRequest)context.getAttribute("http.request");
/*     */         
/*  85 */         method = request.getRequestLine().getMethod();
/*  86 */         return (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD"));
/*     */       
/*     */       case 303:
/*  89 */         return true;
/*     */     } 
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
/*     */     URI uri;
/*  98 */     Args.notNull(response, "HTTP response");
/*     */     
/* 100 */     Header locationHeader = response.getFirstHeader("location");
/* 101 */     if (locationHeader == null)
/*     */     {
/* 103 */       throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
/*     */     }
/*     */ 
/*     */     
/* 107 */     String location = locationHeader.getValue();
/* 108 */     if (this.log.isDebugEnabled()) {
/* 109 */       this.log.debug("Redirect requested to location '" + location + "'");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 114 */       uri = new URI(location);
/* 115 */     } catch (URISyntaxException ex) {
/* 116 */       throw new ProtocolException("Invalid redirect URI: " + location, ex);
/*     */     } 
/*     */     
/* 119 */     HttpParams params = response.getParams();
/*     */ 
/*     */     
/* 122 */     if (!uri.isAbsolute()) {
/* 123 */       if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
/* 124 */         throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
/*     */       }
/*     */ 
/*     */       
/* 128 */       HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*     */       
/* 130 */       Asserts.notNull(target, "Target host");
/*     */       
/* 132 */       HttpRequest request = (HttpRequest)context.getAttribute("http.request");
/*     */ 
/*     */       
/*     */       try {
/* 136 */         URI requestURI = new URI(request.getRequestLine().getUri());
/* 137 */         URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
/* 138 */         uri = URIUtils.resolve(absoluteRequestURI, uri);
/* 139 */       } catch (URISyntaxException ex) {
/* 140 */         throw new ProtocolException(ex.getMessage(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     if (params.isParameterFalse("http.protocol.allow-circular-redirects")) {
/*     */       URI uRI;
/* 146 */       RedirectLocations redirectLocations = (RedirectLocations)context.getAttribute("http.protocol.redirect-locations");
/*     */ 
/*     */       
/* 149 */       if (redirectLocations == null) {
/* 150 */         redirectLocations = new RedirectLocations();
/* 151 */         context.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */       } 
/*     */ 
/*     */       
/* 155 */       if (uri.getFragment() != null) {
/*     */         try {
/* 157 */           HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */ 
/*     */ 
/*     */           
/* 161 */           uRI = URIUtils.rewriteURI(uri, target, true);
/* 162 */         } catch (URISyntaxException ex) {
/* 163 */           throw new ProtocolException(ex.getMessage(), ex);
/*     */         } 
/*     */       } else {
/* 166 */         uRI = uri;
/*     */       } 
/*     */       
/* 169 */       if (redirectLocations.contains(uRI)) {
/* 170 */         throw new CircularRedirectException("Circular redirect to '" + uRI + "'");
/*     */       }
/*     */       
/* 173 */       redirectLocations.add(uRI);
/*     */     } 
/*     */ 
/*     */     
/* 177 */     return uri;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/DefaultRedirectHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */