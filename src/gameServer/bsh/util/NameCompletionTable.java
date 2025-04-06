/*     */ package bsh.util;
/*     */ 
/*     */ import bsh.NameSource;
/*     */ import bsh.StringUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameCompletionTable
/*     */   extends ArrayList
/*     */   implements NameCompletion
/*     */ {
/*     */   NameCompletionTable table;
/*     */   List sources;
/*     */   
/*     */   public void add(NameCompletionTable table) {
/*  64 */     if (this.table != null) {
/*  65 */       throw new RuntimeException("Unimplemented usage error");
/*     */     }
/*  67 */     this.table = table;
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
/*     */   public void add(NameSource source) {
/*  80 */     if (this.sources == null) {
/*  81 */       this.sources = new ArrayList();
/*     */     }
/*  83 */     this.sources.add(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getMatchingNames(String part, List<String> found) {
/*     */     int i;
/*  92 */     for (i = 0; i < size(); i++) {
/*  93 */       String name = (String)get(i);
/*  94 */       if (name.startsWith(part)) {
/*  95 */         found.add(name);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (this.table != null) {
/* 101 */       this.table.getMatchingNames(part, found);
/*     */     }
/*     */ 
/*     */     
/* 105 */     if (this.sources != null)
/* 106 */       for (i = 0; i < this.sources.size(); i++) {
/*     */         
/* 108 */         NameSource src = this.sources.get(i);
/* 109 */         String[] names = src.getAllNames();
/* 110 */         for (int j = 0; j < names.length; j++) {
/* 111 */           if (names[j].startsWith(part)) {
/* 112 */             found.add(names[j]);
/*     */           }
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public String[] completeName(String part) {
/* 119 */     List<String> found = new ArrayList();
/* 120 */     getMatchingNames(part, found);
/*     */     
/* 122 */     if (found.size() == 0) {
/* 123 */       return new String[0];
/*     */     }
/*     */     
/* 126 */     String maxCommon = found.get(0);
/* 127 */     for (int i = 1; i < found.size() && maxCommon.length() > 0; i++) {
/* 128 */       maxCommon = StringUtil.maxCommonPrefix(maxCommon, found.get(i));
/*     */ 
/*     */ 
/*     */       
/* 132 */       if (maxCommon.equals(part)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 137 */     if (maxCommon.length() > part.length()) {
/* 138 */       return new String[] { maxCommon };
/*     */     }
/* 140 */     return found.<String>toArray(new String[0]);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/NameCompletionTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */