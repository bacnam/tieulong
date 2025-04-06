/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.nio.ByteOrder;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeByteArrayRelativeWriter
/*     */   extends CompositeByteArrayRelativeBase
/*     */   implements IoRelativeWriter
/*     */ {
/*     */   private final Expander expander;
/*     */   private final Flusher flusher;
/*     */   private final boolean autoFlush;
/*     */   
/*     */   public static interface Expander
/*     */   {
/*     */     void expand(CompositeByteArray param1CompositeByteArray, int param1Int);
/*     */   }
/*     */   
/*     */   public static class NopExpander
/*     */     implements Expander
/*     */   {
/*     */     public void expand(CompositeByteArray cba, int minSize) {}
/*     */   }
/*     */   
/*     */   public static class ChunkedExpander
/*     */     implements Expander
/*     */   {
/*     */     private final ByteArrayFactory baf;
/*     */     private final int newComponentSize;
/*     */     
/*     */     public ChunkedExpander(ByteArrayFactory baf, int newComponentSize) {
/*  71 */       this.baf = baf;
/*  72 */       this.newComponentSize = newComponentSize;
/*     */     }
/*     */     
/*     */     public void expand(CompositeByteArray cba, int minSize) {
/*  76 */       int remaining = minSize;
/*  77 */       while (remaining > 0) {
/*  78 */         ByteArray component = this.baf.create(this.newComponentSize);
/*  79 */         cba.addLast(component);
/*  80 */         remaining -= this.newComponentSize;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeByteArrayRelativeWriter(CompositeByteArray cba, Expander expander, Flusher flusher, boolean autoFlush) {
/* 125 */     super(cba);
/* 126 */     this.expander = expander;
/* 127 */     this.flusher = flusher;
/* 128 */     this.autoFlush = autoFlush;
/*     */   }
/*     */   
/*     */   private void prepareForAccess(int size) {
/* 132 */     int underflow = this.cursor.getIndex() + size - last();
/* 133 */     if (underflow > 0) {
/* 134 */       this.expander.expand(this.cba, underflow);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {
/* 142 */     flushTo(this.cursor.getIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushTo(int index) {
/* 149 */     ByteArray removed = this.cba.removeTo(index);
/* 150 */     this.flusher.flush(removed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skip(int length) {
/* 157 */     this.cursor.skip(length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cursorPassedFirstComponent() {
/* 162 */     if (this.autoFlush) {
/* 163 */       flushTo(this.cba.first() + this.cba.getFirst().length());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(byte b) {
/* 171 */     prepareForAccess(1);
/* 172 */     this.cursor.put(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(IoBuffer bb) {
/* 179 */     prepareForAccess(bb.remaining());
/* 180 */     this.cursor.put(bb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putShort(short s) {
/* 187 */     prepareForAccess(2);
/* 188 */     this.cursor.putShort(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putInt(int i) {
/* 195 */     prepareForAccess(4);
/* 196 */     this.cursor.putInt(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putLong(long l) {
/* 203 */     prepareForAccess(8);
/* 204 */     this.cursor.putLong(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putFloat(float f) {
/* 211 */     prepareForAccess(4);
/* 212 */     this.cursor.putFloat(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putDouble(double d) {
/* 219 */     prepareForAccess(8);
/* 220 */     this.cursor.putDouble(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putChar(char c) {
/* 227 */     prepareForAccess(2);
/* 228 */     this.cursor.putChar(c);
/*     */   }
/*     */   
/*     */   public static interface Flusher {
/*     */     void flush(ByteArray param1ByteArray);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/CompositeByteArrayRelativeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */