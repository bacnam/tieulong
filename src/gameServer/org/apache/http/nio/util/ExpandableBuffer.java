/*     */ package org.apache.http.nio.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ExpandableBuffer
/*     */   implements BufferInfo, BufferInfo
/*     */ {
/*     */   public static final int INPUT_MODE = 0;
/*     */   public static final int OUTPUT_MODE = 1;
/*     */   private final ByteBufferAllocator allocator;
/*     */   private int mode;
/*  55 */   protected ByteBuffer buffer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExpandableBuffer(int buffersize, ByteBufferAllocator allocator) {
/*  65 */     Args.notNull(allocator, "ByteBuffer allocator");
/*  66 */     this.allocator = allocator;
/*  67 */     this.buffer = allocator.allocate(buffersize);
/*  68 */     this.mode = 0;
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
/*     */   protected int getMode() {
/*  81 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOutputMode() {
/*  88 */     if (this.mode != 1) {
/*  89 */       this.buffer.flip();
/*  90 */       this.mode = 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInputMode() {
/*  98 */     if (this.mode != 0) {
/*  99 */       if (this.buffer.hasRemaining()) {
/* 100 */         this.buffer.compact();
/*     */       } else {
/* 102 */         this.buffer.clear();
/*     */       } 
/* 104 */       this.mode = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void expandCapacity(int capacity) {
/* 109 */     ByteBuffer oldbuffer = this.buffer;
/* 110 */     this.buffer = this.allocator.allocate(capacity);
/* 111 */     oldbuffer.flip();
/* 112 */     this.buffer.put(oldbuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void expand() {
/* 119 */     int newcapacity = this.buffer.capacity() + 1 << 1;
/* 120 */     if (newcapacity < 0) {
/* 121 */       newcapacity = Integer.MAX_VALUE;
/*     */     }
/* 123 */     expandCapacity(newcapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void ensureCapacity(int requiredCapacity) {
/* 130 */     if (requiredCapacity > this.buffer.capacity()) {
/* 131 */       expandCapacity(requiredCapacity);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 142 */     return this.buffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/* 152 */     setOutputMode();
/* 153 */     return this.buffer.hasRemaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 163 */     setOutputMode();
/* 164 */     return this.buffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 174 */     setInputMode();
/* 175 */     return this.buffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 182 */     this.buffer.clear();
/* 183 */     this.mode = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 188 */     StringBuilder sb = new StringBuilder();
/* 189 */     sb.append("[mode=");
/* 190 */     if (getMode() == 0) {
/* 191 */       sb.append("in");
/*     */     } else {
/* 193 */       sb.append("out");
/*     */     } 
/* 195 */     sb.append(" pos=");
/* 196 */     sb.append(this.buffer.position());
/* 197 */     sb.append(" lim=");
/* 198 */     sb.append(this.buffer.limit());
/* 199 */     sb.append(" cap=");
/* 200 */     sb.append(this.buffer.capacity());
/* 201 */     sb.append("]");
/* 202 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/ExpandableBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */