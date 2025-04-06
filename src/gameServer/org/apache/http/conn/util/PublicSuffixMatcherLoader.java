/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Consts;
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
/*     */ @ThreadSafe
/*     */ public final class PublicSuffixMatcherLoader
/*     */ {
/*     */   private static volatile PublicSuffixMatcher DEFAULT_INSTANCE;
/*     */   
/*     */   private static PublicSuffixMatcher load(InputStream in) throws IOException {
/*  52 */     PublicSuffixList list = (new PublicSuffixListParser()).parse(new InputStreamReader(in, Consts.UTF_8));
/*     */     
/*  54 */     return new PublicSuffixMatcher(list.getRules(), list.getExceptions());
/*     */   }
/*     */   
/*     */   public static PublicSuffixMatcher load(URL url) throws IOException {
/*  58 */     Args.notNull(url, "URL");
/*  59 */     InputStream in = url.openStream();
/*     */     try {
/*  61 */       return load(in);
/*     */     } finally {
/*  63 */       in.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static PublicSuffixMatcher load(File file) throws IOException {
/*  68 */     Args.notNull(file, "File");
/*  69 */     InputStream in = new FileInputStream(file);
/*     */     try {
/*  71 */       return load(in);
/*     */     } finally {
/*  73 */       in.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicSuffixMatcher getDefault() {
/*  80 */     if (DEFAULT_INSTANCE == null) {
/*  81 */       synchronized (PublicSuffixMatcherLoader.class) {
/*  82 */         if (DEFAULT_INSTANCE == null) {
/*  83 */           URL url = PublicSuffixMatcherLoader.class.getResource("/mozilla/public-suffix-list.txt");
/*     */           
/*  85 */           if (url != null) {
/*     */             try {
/*  87 */               DEFAULT_INSTANCE = load(url);
/*  88 */             } catch (IOException ex) {
/*     */               
/*  90 */               Log log = LogFactory.getLog(PublicSuffixMatcherLoader.class);
/*  91 */               if (log.isWarnEnabled()) {
/*  92 */                 log.warn("Failure loading public suffix list from default resource", ex);
/*     */               }
/*     */             } 
/*     */           } else {
/*  96 */             DEFAULT_INSTANCE = new PublicSuffixMatcher(Arrays.asList(new String[] { "com" }, ), null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 101 */     return DEFAULT_INSTANCE;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/util/PublicSuffixMatcherLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */