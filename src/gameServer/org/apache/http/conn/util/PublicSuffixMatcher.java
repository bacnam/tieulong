/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
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
/*     */ @ThreadSafe
/*     */ public final class PublicSuffixMatcher
/*     */ {
/*     */   private final Map<String, String> rules;
/*     */   private final Map<String, String> exceptions;
/*     */   
/*     */   public PublicSuffixMatcher(Collection<String> rules, Collection<String> exceptions) {
/*  55 */     Args.notNull(rules, "Domain suffix rules");
/*  56 */     this.rules = new ConcurrentHashMap<String, String>(rules.size());
/*  57 */     for (String rule : rules) {
/*  58 */       this.rules.put(rule, rule);
/*     */     }
/*  60 */     if (exceptions != null) {
/*  61 */       this.exceptions = new ConcurrentHashMap<String, String>(exceptions.size());
/*  62 */       for (String exception : exceptions) {
/*  63 */         this.exceptions.put(exception, exception);
/*     */       }
/*     */     } else {
/*  66 */       this.exceptions = null;
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
/*     */   public String getDomainRoot(String domain) {
/*  78 */     if (domain == null) {
/*  79 */       return null;
/*     */     }
/*  81 */     if (domain.startsWith(".")) {
/*  82 */       return null;
/*     */     }
/*  84 */     String domainName = null;
/*  85 */     String segment = domain.toLowerCase(Locale.ROOT);
/*  86 */     while (segment != null) {
/*     */ 
/*     */       
/*  89 */       if (this.exceptions != null && this.exceptions.containsKey(IDN.toUnicode(segment))) {
/*  90 */         return segment;
/*     */       }
/*     */       
/*  93 */       if (this.rules.containsKey(IDN.toUnicode(segment))) {
/*     */         break;
/*     */       }
/*     */       
/*  97 */       int nextdot = segment.indexOf('.');
/*  98 */       String nextSegment = (nextdot != -1) ? segment.substring(nextdot + 1) : null;
/*     */       
/* 100 */       if (nextSegment != null && 
/* 101 */         this.rules.containsKey("*." + IDN.toUnicode(nextSegment))) {
/*     */         break;
/*     */       }
/*     */       
/* 105 */       if (nextdot != -1) {
/* 106 */         domainName = segment;
/*     */       }
/* 108 */       segment = nextSegment;
/*     */     } 
/* 110 */     return domainName;
/*     */   }
/*     */   
/*     */   public boolean matches(String domain) {
/* 114 */     if (domain == null) {
/* 115 */       return false;
/*     */     }
/* 117 */     String domainRoot = getDomainRoot(domain.startsWith(".") ? domain.substring(1) : domain);
/* 118 */     return (domainRoot == null);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/util/PublicSuffixMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */