/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.PublicSuffixList;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
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
/*     */ @Immutable
/*     */ public class PublicSuffixDomainFilter
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   private final CommonCookieAttributeHandler handler;
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   
/*     */   public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
/*  58 */     this.handler = (CommonCookieAttributeHandler)Args.notNull(handler, "Cookie handler");
/*  59 */     this.publicSuffixMatcher = (PublicSuffixMatcher)Args.notNull(publicSuffixMatcher, "Public suffix matcher");
/*     */   }
/*     */ 
/*     */   
/*     */   public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixList suffixList) {
/*  64 */     Args.notNull(handler, "Cookie handler");
/*  65 */     Args.notNull(suffixList, "Public suffix list");
/*  66 */     this.handler = handler;
/*  67 */     this.publicSuffixMatcher = new PublicSuffixMatcher(suffixList.getRules(), suffixList.getExceptions());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/*  75 */     String domain = cookie.getDomain();
/*  76 */     if (!domain.equalsIgnoreCase("localhost") && this.publicSuffixMatcher.matches(domain)) {
/*  77 */       return false;
/*     */     }
/*  79 */     return this.handler.match(cookie, origin);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  85 */     this.handler.parse(cookie, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  90 */     this.handler.validate(cookie, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/*  95 */     return this.handler.getAttributeName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static CommonCookieAttributeHandler decorate(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
/* 100 */     Args.notNull(handler, "Cookie attribute handler");
/* 101 */     return (publicSuffixMatcher != null) ? new PublicSuffixDomainFilter(handler, publicSuffixMatcher) : handler;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/cookie/PublicSuffixDomainFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */