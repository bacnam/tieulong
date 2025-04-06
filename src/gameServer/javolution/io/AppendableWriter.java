/*     */ package javolution.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javolution.text.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AppendableWriter
/*     */   extends Writer
/*     */ {
/*     */   private Appendable _output;
/*     */   private char[] _tmpBuffer;
/*     */   
/*     */   public AppendableWriter setOutput(Appendable output) {
/*  49 */     if (this._output != null)
/*  50 */       throw new IllegalStateException("Writer not closed or reset"); 
/*  51 */     this._output = output;
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char c) throws IOException {
/*  62 */     if (this._output == null)
/*  63 */       throw new IOException("Writer closed"); 
/*  64 */     this._output.append(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*  75 */     if (this._output == null)
/*  76 */       throw new IOException("Writer closed"); 
/*  77 */     this._output.append((char)c);
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  89 */     if (this._output == null)
/*  90 */       throw new IOException("Writer closed"); 
/*  91 */     this._tmpBuffer = cbuf;
/*  92 */     this._output.append(this._tmpBufferAsCharSequence, off, off + len);
/*  93 */     this._tmpBuffer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   private final CharSequence _tmpBufferAsCharSequence = new CharSequence() {
/*     */       public int length() {
/* 100 */         return AppendableWriter.this._tmpBuffer.length;
/*     */       }
/*     */       
/*     */       public char charAt(int index) {
/* 104 */         return AppendableWriter.this._tmpBuffer[index];
/*     */       }
/*     */       
/*     */       public CharSequence subSequence(int start, int end) {
/* 108 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str, int off, int len) throws IOException {
/* 121 */     if (this._output == null)
/* 122 */       throw new IOException("Writer closed"); 
/* 123 */     Object obj = str;
/* 124 */     if (obj instanceof CharSequence) {
/* 125 */       this._output.append((CharSequence)obj);
/*     */     } else {
/* 127 */       this._output.append((CharSequence)Text.valueOf(str));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(CharSequence csq) throws IOException {
/* 138 */     if (this._output == null)
/* 139 */       throw new IOException("Writer closed"); 
/* 140 */     this._output.append(csq);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 154 */     if (this._output != null) {
/* 155 */       reset();
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 160 */     this._output = null;
/* 161 */     this._tmpBuffer = null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/io/AppendableWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */