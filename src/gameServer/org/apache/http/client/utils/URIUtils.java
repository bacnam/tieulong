/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Stack;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class URIUtils
/*     */ {
/*     */   @Deprecated
/*     */   public static URI createURI(String scheme, String host, int port, String path, String query, String fragment) throws URISyntaxException {
/*  86 */     StringBuilder buffer = new StringBuilder();
/*  87 */     if (host != null) {
/*  88 */       if (scheme != null) {
/*  89 */         buffer.append(scheme);
/*  90 */         buffer.append("://");
/*     */       } 
/*  92 */       buffer.append(host);
/*  93 */       if (port > 0) {
/*  94 */         buffer.append(':');
/*  95 */         buffer.append(port);
/*     */       } 
/*     */     } 
/*  98 */     if (path == null || !path.startsWith("/")) {
/*  99 */       buffer.append('/');
/*     */     }
/* 101 */     if (path != null) {
/* 102 */       buffer.append(path);
/*     */     }
/* 104 */     if (query != null) {
/* 105 */       buffer.append('?');
/* 106 */       buffer.append(query);
/*     */     } 
/* 108 */     if (fragment != null) {
/* 109 */       buffer.append('#');
/* 110 */       buffer.append(fragment);
/*     */     } 
/* 112 */     return new URI(buffer.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
/* 135 */     Args.notNull(uri, "URI");
/* 136 */     if (uri.isOpaque()) {
/* 137 */       return uri;
/*     */     }
/* 139 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 140 */     if (target != null) {
/* 141 */       uribuilder.setScheme(target.getSchemeName());
/* 142 */       uribuilder.setHost(target.getHostName());
/* 143 */       uribuilder.setPort(target.getPort());
/*     */     } else {
/* 145 */       uribuilder.setScheme(null);
/* 146 */       uribuilder.setHost(null);
/* 147 */       uribuilder.setPort(-1);
/*     */     } 
/* 149 */     if (dropFragment) {
/* 150 */       uribuilder.setFragment(null);
/*     */     }
/* 152 */     if (TextUtils.isEmpty(uribuilder.getPath())) {
/* 153 */       uribuilder.setPath("/");
/*     */     }
/* 155 */     return uribuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
/* 166 */     return rewriteURI(uri, target, false);
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
/*     */   public static URI rewriteURI(URI uri) throws URISyntaxException {
/* 181 */     Args.notNull(uri, "URI");
/* 182 */     if (uri.isOpaque()) {
/* 183 */       return uri;
/*     */     }
/* 185 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 186 */     if (uribuilder.getUserInfo() != null) {
/* 187 */       uribuilder.setUserInfo(null);
/*     */     }
/* 189 */     if (TextUtils.isEmpty(uribuilder.getPath())) {
/* 190 */       uribuilder.setPath("/");
/*     */     }
/* 192 */     if (uribuilder.getHost() != null) {
/* 193 */       uribuilder.setHost(uribuilder.getHost().toLowerCase(Locale.ROOT));
/*     */     }
/* 195 */     uribuilder.setFragment(null);
/* 196 */     return uribuilder.build();
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
/*     */   public static URI rewriteURIForRoute(URI uri, RouteInfo route) throws URISyntaxException {
/* 211 */     if (uri == null) {
/* 212 */       return null;
/*     */     }
/* 214 */     if (route.getProxyHost() != null && !route.isTunnelled()) {
/*     */       
/* 216 */       if (!uri.isAbsolute()) {
/* 217 */         HttpHost target = route.getTargetHost();
/* 218 */         return rewriteURI(uri, target, true);
/*     */       } 
/* 220 */       return rewriteURI(uri);
/*     */     } 
/*     */ 
/*     */     
/* 224 */     if (uri.isAbsolute()) {
/* 225 */       return rewriteURI(uri, null, true);
/*     */     }
/* 227 */     return rewriteURI(uri);
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
/*     */   public static URI resolve(URI baseURI, String reference) {
/* 241 */     return resolve(baseURI, URI.create(reference));
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
/*     */   public static URI resolve(URI baseURI, URI reference) {
/* 253 */     Args.notNull(baseURI, "Base URI");
/* 254 */     Args.notNull(reference, "Reference URI");
/* 255 */     URI ref = reference;
/* 256 */     String s = ref.toString();
/* 257 */     if (s.startsWith("?")) {
/* 258 */       return resolveReferenceStartingWithQueryString(baseURI, ref);
/*     */     }
/* 260 */     boolean emptyReference = s.isEmpty();
/* 261 */     if (emptyReference) {
/* 262 */       ref = URI.create("#");
/*     */     }
/* 264 */     URI resolved = baseURI.resolve(ref);
/* 265 */     if (emptyReference) {
/* 266 */       String resolvedString = resolved.toString();
/* 267 */       resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf('#')));
/*     */     } 
/*     */     
/* 270 */     return normalizeSyntax(resolved);
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
/*     */   private static URI resolveReferenceStartingWithQueryString(URI baseURI, URI reference) {
/* 282 */     String baseUri = baseURI.toString();
/* 283 */     baseUri = (baseUri.indexOf('?') > -1) ? baseUri.substring(0, baseUri.indexOf('?')) : baseUri;
/*     */     
/* 285 */     return URI.create(baseUri + reference.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URI normalizeSyntax(URI uri) {
/* 296 */     if (uri.isOpaque() || uri.getAuthority() == null)
/*     */     {
/* 298 */       return uri;
/*     */     }
/* 300 */     Args.check(uri.isAbsolute(), "Base URI must be absolute");
/* 301 */     String path = (uri.getPath() == null) ? "" : uri.getPath();
/* 302 */     String[] inputSegments = path.split("/");
/* 303 */     Stack<String> outputSegments = new Stack<String>();
/* 304 */     for (String inputSegment : inputSegments) {
/* 305 */       if (!inputSegment.isEmpty() && !".".equals(inputSegment))
/*     */       {
/*     */         
/* 308 */         if ("..".equals(inputSegment)) {
/* 309 */           if (!outputSegments.isEmpty()) {
/* 310 */             outputSegments.pop();
/*     */           }
/*     */         } else {
/* 313 */           outputSegments.push(inputSegment);
/*     */         }  } 
/*     */     } 
/* 316 */     StringBuilder outputBuffer = new StringBuilder();
/* 317 */     for (String outputSegment : outputSegments) {
/* 318 */       outputBuffer.append('/').append(outputSegment);
/*     */     }
/* 320 */     if (path.lastIndexOf('/') == path.length() - 1)
/*     */     {
/* 322 */       outputBuffer.append('/');
/*     */     }
/*     */     try {
/* 325 */       String scheme = uri.getScheme().toLowerCase(Locale.ROOT);
/* 326 */       String auth = uri.getAuthority().toLowerCase(Locale.ROOT);
/* 327 */       URI ref = new URI(scheme, auth, outputBuffer.toString(), null, null);
/*     */       
/* 329 */       if (uri.getQuery() == null && uri.getFragment() == null) {
/* 330 */         return ref;
/*     */       }
/* 332 */       StringBuilder normalized = new StringBuilder(ref.toASCIIString());
/*     */       
/* 334 */       if (uri.getQuery() != null)
/*     */       {
/* 336 */         normalized.append('?').append(uri.getRawQuery());
/*     */       }
/* 338 */       if (uri.getFragment() != null)
/*     */       {
/* 340 */         normalized.append('#').append(uri.getRawFragment());
/*     */       }
/* 342 */       return URI.create(normalized.toString());
/* 343 */     } catch (URISyntaxException e) {
/* 344 */       throw new IllegalArgumentException(e);
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
/*     */   public static HttpHost extractHost(URI uri) {
/* 358 */     if (uri == null) {
/* 359 */       return null;
/*     */     }
/* 361 */     HttpHost target = null;
/* 362 */     if (uri.isAbsolute()) {
/* 363 */       int port = uri.getPort();
/* 364 */       String host = uri.getHost();
/* 365 */       if (host == null) {
/*     */         
/* 367 */         host = uri.getAuthority();
/* 368 */         if (host != null) {
/*     */           
/* 370 */           int at = host.indexOf('@');
/* 371 */           if (at >= 0) {
/* 372 */             if (host.length() > at + 1) {
/* 373 */               host = host.substring(at + 1);
/*     */             } else {
/* 375 */               host = null;
/*     */             } 
/*     */           }
/*     */           
/* 379 */           if (host != null) {
/* 380 */             int colon = host.indexOf(':');
/* 381 */             if (colon >= 0) {
/* 382 */               int pos = colon + 1;
/* 383 */               int len = 0;
/* 384 */               for (int i = pos; i < host.length() && 
/* 385 */                 Character.isDigit(host.charAt(i)); i++) {
/* 386 */                 len++;
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 391 */               if (len > 0) {
/*     */                 try {
/* 393 */                   port = Integer.parseInt(host.substring(pos, pos + len));
/* 394 */                 } catch (NumberFormatException ex) {}
/*     */               }
/*     */               
/* 397 */               host = host.substring(0, colon);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 402 */       String scheme = uri.getScheme();
/* 403 */       if (!TextUtils.isBlank(host)) {
/*     */         try {
/* 405 */           target = new HttpHost(host, port, scheme);
/* 406 */         } catch (IllegalArgumentException ignore) {}
/*     */       }
/*     */     } 
/*     */     
/* 410 */     return target;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI resolve(URI originalURI, HttpHost target, List<URI> redirects) throws URISyntaxException {
/*     */     URIBuilder uribuilder;
/* 433 */     Args.notNull(originalURI, "Request URI");
/*     */     
/* 435 */     if (redirects == null || redirects.isEmpty()) {
/* 436 */       uribuilder = new URIBuilder(originalURI);
/*     */     } else {
/* 438 */       uribuilder = new URIBuilder(redirects.get(redirects.size() - 1));
/* 439 */       String frag = uribuilder.getFragment();
/*     */       
/* 441 */       for (int i = redirects.size() - 1; frag == null && i >= 0; i--) {
/* 442 */         frag = ((URI)redirects.get(i)).getFragment();
/*     */       }
/* 444 */       uribuilder.setFragment(frag);
/*     */     } 
/*     */     
/* 447 */     if (uribuilder.getFragment() == null) {
/* 448 */       uribuilder.setFragment(originalURI.getFragment());
/*     */     }
/*     */     
/* 451 */     if (target != null && !uribuilder.isAbsolute()) {
/* 452 */       uribuilder.setScheme(target.getSchemeName());
/* 453 */       uribuilder.setHost(target.getHostName());
/* 454 */       uribuilder.setPort(target.getPort());
/*     */     } 
/* 456 */     return uribuilder.build();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/utils/URIUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */