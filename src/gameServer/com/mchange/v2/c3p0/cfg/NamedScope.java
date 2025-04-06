/*     */ package com.mchange.v2.c3p0.cfg;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NamedScope
/*     */ {
/*     */   HashMap props;
/*     */   HashMap userNamesToOverrides;
/*     */   HashMap extensions;
/*     */   
/*     */   NamedScope() {
/*  50 */     this.props = new HashMap<Object, Object>();
/*  51 */     this.userNamesToOverrides = new HashMap<Object, Object>();
/*  52 */     this.extensions = new HashMap<Object, Object>();
/*     */   }
/*     */ 
/*     */   
/*     */   NamedScope(HashMap props, HashMap userNamesToOverrides, HashMap extensions) {
/*  57 */     this.props = props;
/*  58 */     this.userNamesToOverrides = userNamesToOverrides;
/*  59 */     this.extensions = extensions;
/*     */   }
/*     */ 
/*     */   
/*     */   NamedScope mergedOver(NamedScope underScope) {
/*  64 */     HashMap mergedProps = (HashMap)underScope.props.clone();
/*  65 */     mergedProps.putAll(this.props);
/*     */     
/*  67 */     HashMap mergedUserNamesToOverrides = mergeUserNamesToOverrides(this.userNamesToOverrides, underScope.userNamesToOverrides);
/*     */     
/*  69 */     HashMap mergedExtensions = mergeExtensions(this.extensions, underScope.extensions);
/*     */     
/*  71 */     return new NamedScope(mergedProps, mergedUserNamesToOverrides, mergedExtensions);
/*     */   }
/*     */ 
/*     */   
/*     */   static HashMap mergeExtensions(HashMap over, HashMap under) {
/*  76 */     HashMap out = (HashMap)under.clone();
/*  77 */     out.putAll(over);
/*  78 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   static HashMap mergeUserNamesToOverrides(HashMap over, HashMap under) {
/*  83 */     HashMap<String, Object> out = (HashMap)under.clone();
/*     */     
/*  85 */     HashSet<?> underUserNames = new HashSet(under.keySet());
/*  86 */     HashSet overUserNames = new HashSet(over.keySet());
/*     */     
/*  88 */     HashSet newUserNames = (HashSet)overUserNames.clone();
/*  89 */     newUserNames.removeAll(underUserNames);
/*     */     
/*  91 */     for (Iterator<String> ii = newUserNames.iterator(); ii.hasNext(); ) {
/*     */       
/*  93 */       String name = ii.next();
/*  94 */       out.put(name, ((HashMap)over.get(name)).clone());
/*     */     } 
/*     */     
/*  97 */     HashSet mergeUserNames = (HashSet)overUserNames.clone();
/*  98 */     mergeUserNames.retainAll(underUserNames);
/*     */     
/* 100 */     for (Iterator<String> iterator1 = mergeUserNames.iterator(); iterator1.hasNext(); ) {
/*     */       
/* 102 */       String name = iterator1.next();
/* 103 */       ((HashMap)out.get(name)).putAll((HashMap)over.get(name));
/*     */     } 
/*     */     
/* 106 */     return out;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/cfg/NamedScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */