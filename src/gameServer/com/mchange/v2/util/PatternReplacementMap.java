/*     */ package com.mchange.v2.util;
/*     */ 
/*     */ import com.mchange.v1.util.WrapperIterator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternReplacementMap
/*     */ {
/*  44 */   List mappings = new LinkedList();
/*     */   
/*     */   public synchronized void addMapping(Pattern paramPattern, String paramString) {
/*  47 */     this.mappings.add(new Mapping(paramPattern, paramString));
/*     */   } public synchronized void removeMapping(Pattern paramPattern) {
/*     */     byte b;
/*     */     int i;
/*  51 */     for (b = 0, i = this.mappings.size(); b < i; b++) {
/*  52 */       if (((Mapping)this.mappings.get(b)).getPattern().equals(paramPattern))
/*  53 */         this.mappings.remove(b); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized Iterator patterns() {
/*  58 */     return (Iterator)new WrapperIterator(this.mappings.iterator(), true)
/*     */       {
/*     */         protected Object transformObject(Object param1Object)
/*     */         {
/*  62 */           PatternReplacementMap.Mapping mapping = (PatternReplacementMap.Mapping)param1Object;
/*  63 */           return mapping.getPattern();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public synchronized int size() {
/*  69 */     return this.mappings.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String attemptReplace(String paramString) {
/*  76 */     String str = null;
/*  77 */     for (Mapping mapping : this.mappings) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       Matcher matcher = mapping.getPattern().matcher(paramString);
/*  84 */       if (matcher.matches()) {
/*     */         
/*  86 */         str = matcher.replaceAll(mapping.getReplacement());
/*     */         break;
/*     */       } 
/*     */     } 
/*  90 */     return str;
/*     */   }
/*     */   
/*     */   private static final class Mapping
/*     */   {
/*     */     Pattern pattern;
/*     */     String replacement;
/*     */     
/*     */     public Pattern getPattern() {
/*  99 */       return this.pattern;
/*     */     }
/*     */     public String getReplacement() {
/* 102 */       return this.replacement;
/*     */     }
/*     */     
/*     */     public Mapping(Pattern param1Pattern, String param1String) {
/* 106 */       this.pattern = param1Pattern;
/* 107 */       this.replacement = param1String;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/PatternReplacementMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */