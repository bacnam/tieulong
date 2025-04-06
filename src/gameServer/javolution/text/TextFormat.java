/*     */ package javolution.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javolution.lang.Parallelizable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Parallelizable
/*     */ public abstract class TextFormat<T>
/*     */ {
/*     */   public abstract T parse(CharSequence paramCharSequence, Cursor paramCursor);
/*     */   
/*     */   public abstract Appendable format(T paramT, Appendable paramAppendable) throws IOException;
/*     */   
/*     */   public T parse(CharSequence csq) throws IllegalArgumentException {
/*  90 */     Cursor cursor = new Cursor();
/*  91 */     T obj = parse(csq, cursor);
/*  92 */     if (!cursor.atEnd(csq)) {
/*  93 */       throw new IllegalArgumentException("Extraneous character(s) \"" + cursor.tail(csq) + "\"");
/*     */     }
/*  95 */     return obj;
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
/*     */   public TextBuilder format(T obj, TextBuilder dest) {
/*     */     try {
/* 108 */       format(obj, dest);
/* 109 */       return dest;
/* 110 */     } catch (IOException e) {
/* 111 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(T obj) {
/* 122 */     return format(obj, new TextBuilder()).toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/TextFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */