/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Collections;
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
/*     */ public abstract class BufferByteArray
/*     */   extends AbstractByteArray
/*     */ {
/*     */   protected IoBuffer bb;
/*     */   
/*     */   public BufferByteArray(IoBuffer bb) {
/*  51 */     this.bb = bb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<IoBuffer> getIoBuffers() {
/*  58 */     return Collections.singletonList(this.bb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoBuffer getSingleIoBuffer() {
/*  65 */     return this.bb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray slice(int index, int length) {
/*  74 */     int oldLimit = this.bb.limit();
/*  75 */     this.bb.position(index);
/*  76 */     this.bb.limit(index + length);
/*  77 */     IoBuffer slice = this.bb.slice();
/*  78 */     this.bb.limit(oldLimit);
/*  79 */     return new BufferByteArray(slice)
/*     */       {
/*     */         public void free() {}
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void free();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray.Cursor cursor() {
/*  97 */     return new CursorImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray.Cursor cursor(int index) {
/* 104 */     return new CursorImpl(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int first() {
/* 111 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int last() {
/* 118 */     return this.bb.limit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/* 125 */     return this.bb.order();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void order(ByteOrder order) {
/* 132 */     this.bb.order(order);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte get(int index) {
/* 139 */     return this.bb.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, byte b) {
/* 146 */     this.bb.put(index, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void get(int index, IoBuffer other) {
/* 153 */     this.bb.position(index);
/* 154 */     other.put(this.bb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, IoBuffer other) {
/* 161 */     this.bb.position(index);
/* 162 */     this.bb.put(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getShort(int index) {
/* 169 */     return this.bb.getShort(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putShort(int index, short s) {
/* 176 */     this.bb.putShort(index, s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInt(int index) {
/* 183 */     return this.bb.getInt(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putInt(int index, int i) {
/* 190 */     this.bb.putInt(index, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLong(int index) {
/* 197 */     return this.bb.getLong(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putLong(int index, long l) {
/* 204 */     this.bb.putLong(index, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloat(int index) {
/* 211 */     return this.bb.getFloat(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putFloat(int index, float f) {
/* 218 */     this.bb.putFloat(index, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDouble(int index) {
/* 225 */     return this.bb.getDouble(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putDouble(int index, double d) {
/* 232 */     this.bb.putDouble(index, d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getChar(int index) {
/* 239 */     return this.bb.getChar(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putChar(int index, char c) {
/* 246 */     this.bb.putChar(index, c);
/*     */   }
/*     */ 
/*     */   
/*     */   private class CursorImpl
/*     */     implements ByteArray.Cursor
/*     */   {
/*     */     private int index;
/*     */     
/*     */     public CursorImpl() {}
/*     */     
/*     */     public CursorImpl(int index) {
/* 258 */       setIndex(index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getRemaining() {
/* 265 */       return BufferByteArray.this.last() - this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasRemaining() {
/* 272 */       return (getRemaining() > 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 279 */       return this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setIndex(int index) {
/* 286 */       if (index < 0 || index > BufferByteArray.this.last()) {
/* 287 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 289 */       this.index = index;
/*     */     }
/*     */     
/*     */     public void skip(int length) {
/* 293 */       setIndex(this.index + length);
/*     */     }
/*     */     
/*     */     public ByteArray slice(int length) {
/* 297 */       ByteArray slice = BufferByteArray.this.slice(this.index, length);
/* 298 */       this.index += length;
/* 299 */       return slice;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteOrder order() {
/* 306 */       return BufferByteArray.this.order();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte get() {
/* 313 */       byte b = BufferByteArray.this.get(this.index);
/* 314 */       this.index++;
/* 315 */       return b;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(byte b) {
/* 322 */       BufferByteArray.this.put(this.index, b);
/* 323 */       this.index++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void get(IoBuffer bb) {
/* 330 */       int size = Math.min(getRemaining(), bb.remaining());
/* 331 */       BufferByteArray.this.get(this.index, bb);
/* 332 */       this.index += size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(IoBuffer bb) {
/* 339 */       int size = bb.remaining();
/* 340 */       BufferByteArray.this.put(this.index, bb);
/* 341 */       this.index += size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public short getShort() {
/* 348 */       short s = BufferByteArray.this.getShort(this.index);
/* 349 */       this.index += 2;
/* 350 */       return s;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putShort(short s) {
/* 357 */       BufferByteArray.this.putShort(this.index, s);
/* 358 */       this.index += 2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getInt() {
/* 365 */       int i = BufferByteArray.this.getInt(this.index);
/* 366 */       this.index += 4;
/* 367 */       return i;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putInt(int i) {
/* 374 */       BufferByteArray.this.putInt(this.index, i);
/* 375 */       this.index += 4;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getLong() {
/* 382 */       long l = BufferByteArray.this.getLong(this.index);
/* 383 */       this.index += 8;
/* 384 */       return l;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putLong(long l) {
/* 391 */       BufferByteArray.this.putLong(this.index, l);
/* 392 */       this.index += 8;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float getFloat() {
/* 399 */       float f = BufferByteArray.this.getFloat(this.index);
/* 400 */       this.index += 4;
/* 401 */       return f;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putFloat(float f) {
/* 408 */       BufferByteArray.this.putFloat(this.index, f);
/* 409 */       this.index += 4;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getDouble() {
/* 416 */       double d = BufferByteArray.this.getDouble(this.index);
/* 417 */       this.index += 8;
/* 418 */       return d;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putDouble(double d) {
/* 425 */       BufferByteArray.this.putDouble(this.index, d);
/* 426 */       this.index += 8;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public char getChar() {
/* 433 */       char c = BufferByteArray.this.getChar(this.index);
/* 434 */       this.index += 2;
/* 435 */       return c;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void putChar(char c) {
/* 442 */       BufferByteArray.this.putChar(this.index, c);
/* 443 */       this.index += 2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/BufferByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */