/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.util.function.Function;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EntitiesImpl
/*     */ {
/*  29 */   private int _maxLength = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> _entitiesMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharArray _tmp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLength() {
/*  45 */     return this._maxLength;
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
/*     */   public int replaceEntity(char[] buffer, int start, int length) throws XMLStreamException {
/*  63 */     if (buffer[start + 1] == '#') {
/*  64 */       char c = buffer[start + 2];
/*  65 */       int base = (c == 'x') ? 16 : 10;
/*  66 */       int j = (c == 'x') ? 3 : 2;
/*  67 */       int charValue = 0;
/*  68 */       for (; j < length - 1; j++) {
/*  69 */         c = buffer[start + j];
/*  70 */         charValue *= base;
/*  71 */         charValue += (c <= '9') ? (c - 48) : ((c <= 'Z') ? (c - 65) : (c - 97));
/*     */       } 
/*     */       
/*  74 */       buffer[start] = (char)charValue;
/*  75 */       return 1;
/*     */     } 
/*     */     
/*  78 */     if (buffer[start + 1] == 'l' && buffer[start + 2] == 't' && buffer[start + 3] == ';') {
/*     */       
/*  80 */       buffer[start] = '<';
/*  81 */       return 1;
/*     */     } 
/*     */     
/*  84 */     if (buffer[start + 1] == 'g' && buffer[start + 2] == 't' && buffer[start + 3] == ';') {
/*     */       
/*  86 */       buffer[start] = '>';
/*  87 */       return 1;
/*     */     } 
/*     */     
/*  90 */     if (buffer[start + 1] == 'a' && buffer[start + 2] == 'p' && buffer[start + 3] == 'o' && buffer[start + 4] == 's' && buffer[start + 5] == ';') {
/*     */ 
/*     */       
/*  93 */       buffer[start] = '\'';
/*  94 */       return 1;
/*     */     } 
/*     */     
/*  97 */     if (buffer[start + 1] == 'q' && buffer[start + 2] == 'u' && buffer[start + 3] == 'o' && buffer[start + 4] == 't' && buffer[start + 5] == ';') {
/*     */ 
/*     */       
/* 100 */       buffer[start] = '"';
/* 101 */       return 1;
/*     */     } 
/*     */     
/* 104 */     if (buffer[start + 1] == 'a' && buffer[start + 2] == 'm' && buffer[start + 3] == 'p' && buffer[start + 4] == ';') {
/*     */       
/* 106 */       buffer[start] = '&';
/* 107 */       return 1;
/*     */     } 
/*     */ 
/*     */     
/* 111 */     this._tmp.setArray(buffer, start + 1, length - 2);
/* 112 */     CharSequence replacementText = (this._entitiesMapping != null) ? this._entitiesMapping.get(this._tmp) : null;
/*     */     
/* 114 */     if (replacementText == null)
/* 115 */       throw new XMLStreamException("Entity " + this._tmp + " not recognized"); 
/* 116 */     int replacementTextLength = replacementText.length();
/* 117 */     for (int i = 0; i < replacementTextLength; i++) {
/* 118 */       buffer[start + i] = replacementText.charAt(i);
/*     */     }
/* 120 */     return replacementTextLength;
/*     */   }
/*     */   EntitiesImpl() {
/* 123 */     this._tmp = new CharArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntitiesMapping(Map<String, String> entityToReplacementText) {
/* 130 */     FastTable<String> values = new FastTable();
/* 131 */     values.addAll(entityToReplacementText.values());
/* 132 */     this._maxLength = ((Integer)values.mapped(new Function<CharSequence, Integer>()
/*     */         {
/*     */           public Integer apply(CharSequence csq)
/*     */           {
/* 136 */             return Integer.valueOf(csq.length());
/*     */           }
/*     */         }).max()).intValue();
/* 139 */     this._entitiesMapping = entityToReplacementText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getEntitiesMapping() {
/* 146 */     return this._entitiesMapping;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 151 */     this._maxLength = 1;
/* 152 */     this._entitiesMapping = null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/EntitiesImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */