/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.text.Text;
/*     */ import javolution.text.TextBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharSequenceReader
/*     */   extends Reader
/*     */ {
/*     */   private CharSequence _input;
/*     */   private int _index;
/*     */   
/*     */   public CharSequenceReader setInput(CharSequence charSequence) {
/*  54 */     if (this._input != null)
/*  55 */       throw new IllegalStateException("Reader not closed or reset"); 
/*  56 */     this._input = charSequence;
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/*  68 */     if (this._input == null)
/*  69 */       throw new IOException("Reader closed"); 
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  77 */     if (this._input != null) {
/*  78 */       reset();
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
/*     */   public int read() throws IOException {
/*  92 */     if (this._input == null)
/*  93 */       throw new IOException("Reader closed"); 
/*  94 */     return (this._index < this._input.length()) ? this._input.charAt(this._index++) : -1;
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
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 109 */     if (this._input == null)
/* 110 */       throw new IOException("Reader closed"); 
/* 111 */     int inputLength = this._input.length();
/* 112 */     if (this._index >= inputLength)
/* 113 */       return -1; 
/* 114 */     int count = MathLib.min(inputLength - this._index, len);
/* 115 */     Object csq = this._input;
/* 116 */     if (csq instanceof String) {
/* 117 */       String str = (String)csq;
/* 118 */       str.getChars(this._index, this._index + count, cbuf, off);
/* 119 */     } else if (csq instanceof Text) {
/* 120 */       Text txt = (Text)csq;
/* 121 */       txt.getChars(this._index, this._index + count, cbuf, off);
/* 122 */     } else if (csq instanceof TextBuilder) {
/* 123 */       TextBuilder tb = (TextBuilder)csq;
/* 124 */       tb.getChars(this._index, this._index + count, cbuf, off);
/* 125 */     } else if (csq instanceof CharArray) {
/* 126 */       CharArray ca = (CharArray)csq;
/* 127 */       System.arraycopy(ca.array(), this._index + ca.offset(), cbuf, off, count);
/*     */     } else {
/* 129 */       for (int i = off, n = off + count, j = this._index; i < n;) {
/* 130 */         cbuf[i++] = this._input.charAt(j++);
/*     */       }
/*     */     } 
/* 133 */     this._index += count;
/* 134 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(Appendable dest) throws IOException {
/* 145 */     if (this._input == null)
/* 146 */       throw new IOException("Reader closed"); 
/* 147 */     dest.append(this._input);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 152 */     this._index = 0;
/* 153 */     this._input = null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/CharSequenceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */