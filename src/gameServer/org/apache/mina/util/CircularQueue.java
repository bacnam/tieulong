/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CircularQueue<E>
/*     */   extends AbstractList<E>
/*     */   implements Queue<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3993421269224511264L;
/*     */   private static final int DEFAULT_CAPACITY = 4;
/*     */   private final int initialCapacity;
/*     */   private volatile Object[] items;
/*     */   private int mask;
/*  50 */   private int first = 0;
/*     */   
/*  52 */   private int last = 0;
/*     */ 
/*     */   
/*     */   private boolean full;
/*     */ 
/*     */   
/*     */   private int shrinkThreshold;
/*     */ 
/*     */   
/*     */   public CircularQueue() {
/*  62 */     this(4);
/*     */   }
/*     */   
/*     */   public CircularQueue(int initialCapacity) {
/*  66 */     int actualCapacity = normalizeCapacity(initialCapacity);
/*  67 */     this.items = new Object[actualCapacity];
/*  68 */     this.mask = actualCapacity - 1;
/*  69 */     this.initialCapacity = actualCapacity;
/*  70 */     this.shrinkThreshold = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int normalizeCapacity(int initialCapacity) {
/*  77 */     int actualCapacity = 1;
/*     */     
/*  79 */     while (actualCapacity < initialCapacity) {
/*  80 */       actualCapacity <<= 1;
/*  81 */       if (actualCapacity < 0) {
/*  82 */         actualCapacity = 1073741824;
/*     */         break;
/*     */       } 
/*     */     } 
/*  86 */     return actualCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  93 */     return this.items.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  98 */     if (!isEmpty()) {
/*  99 */       Arrays.fill(this.items, (Object)null);
/* 100 */       this.first = 0;
/* 101 */       this.last = 0;
/* 102 */       this.full = false;
/* 103 */       shrinkIfNeeded();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public E poll() {
/* 109 */     if (isEmpty()) {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     Object ret = this.items[this.first];
/* 114 */     this.items[this.first] = null;
/* 115 */     decreaseSize();
/*     */     
/* 117 */     if (this.first == this.last) {
/* 118 */       this.first = this.last = 0;
/*     */     }
/*     */     
/* 121 */     shrinkIfNeeded();
/* 122 */     return (E)ret;
/*     */   }
/*     */   
/*     */   public boolean offer(E item) {
/* 126 */     if (item == null) {
/* 127 */       throw new IllegalArgumentException("item");
/*     */     }
/*     */     
/* 130 */     expandIfNeeded();
/* 131 */     this.items[this.last] = item;
/* 132 */     increaseSize();
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 138 */     if (isEmpty()) {
/* 139 */       return null;
/*     */     }
/*     */     
/* 142 */     return (E)this.items[this.first];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int idx) {
/* 148 */     checkIndex(idx);
/* 149 */     return (E)this.items[getRealIndex(idx)];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 154 */     return (this.first == this.last && !this.full);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 159 */     if (this.full) {
/* 160 */       return capacity();
/*     */     }
/*     */     
/* 163 */     if (this.last >= this.first) {
/* 164 */       return this.last - this.first;
/*     */     }
/*     */     
/* 167 */     return this.last - this.first + capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return "first=" + this.first + ", last=" + this.last + ", size=" + size() + ", mask = " + this.mask;
/*     */   }
/*     */   
/*     */   private void checkIndex(int idx) {
/* 176 */     if (idx < 0 || idx >= size()) {
/* 177 */       throw new IndexOutOfBoundsException(String.valueOf(idx));
/*     */     }
/*     */   }
/*     */   
/*     */   private int getRealIndex(int idx) {
/* 182 */     return this.first + idx & this.mask;
/*     */   }
/*     */   
/*     */   private void increaseSize() {
/* 186 */     this.last = this.last + 1 & this.mask;
/* 187 */     this.full = (this.first == this.last);
/*     */   }
/*     */   
/*     */   private void decreaseSize() {
/* 191 */     this.first = this.first + 1 & this.mask;
/* 192 */     this.full = false;
/*     */   }
/*     */   
/*     */   private void expandIfNeeded() {
/* 196 */     if (this.full) {
/*     */       
/* 198 */       int oldLen = this.items.length;
/* 199 */       int newLen = oldLen << 1;
/* 200 */       Object[] tmp = new Object[newLen];
/*     */       
/* 202 */       if (this.first < this.last) {
/* 203 */         System.arraycopy(this.items, this.first, tmp, 0, this.last - this.first);
/*     */       } else {
/* 205 */         System.arraycopy(this.items, this.first, tmp, 0, oldLen - this.first);
/* 206 */         System.arraycopy(this.items, 0, tmp, oldLen - this.first, this.last);
/*     */       } 
/*     */       
/* 209 */       this.first = 0;
/* 210 */       this.last = oldLen;
/* 211 */       this.items = tmp;
/* 212 */       this.mask = tmp.length - 1;
/* 213 */       if (newLen >>> 3 > this.initialCapacity) {
/* 214 */         this.shrinkThreshold = newLen >>> 3;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shrinkIfNeeded() {
/* 220 */     int size = size();
/* 221 */     if (size <= this.shrinkThreshold) {
/*     */       
/* 223 */       int oldLen = this.items.length;
/* 224 */       int newLen = normalizeCapacity(size);
/* 225 */       if (size == newLen) {
/* 226 */         newLen <<= 1;
/*     */       }
/*     */       
/* 229 */       if (newLen >= oldLen) {
/*     */         return;
/*     */       }
/*     */       
/* 233 */       if (newLen < this.initialCapacity) {
/* 234 */         if (oldLen == this.initialCapacity) {
/*     */           return;
/*     */         }
/*     */         
/* 238 */         newLen = this.initialCapacity;
/*     */       } 
/*     */       
/* 241 */       Object[] tmp = new Object[newLen];
/*     */ 
/*     */       
/* 244 */       if (size > 0) {
/* 245 */         if (this.first < this.last) {
/* 246 */           System.arraycopy(this.items, this.first, tmp, 0, this.last - this.first);
/*     */         } else {
/* 248 */           System.arraycopy(this.items, this.first, tmp, 0, oldLen - this.first);
/* 249 */           System.arraycopy(this.items, 0, tmp, oldLen - this.first, this.last);
/*     */         } 
/*     */       }
/*     */       
/* 253 */       this.first = 0;
/* 254 */       this.last = size;
/* 255 */       this.items = tmp;
/* 256 */       this.mask = tmp.length - 1;
/* 257 */       this.shrinkThreshold = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E o) {
/* 263 */     return offer(o);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E set(int idx, E o) {
/* 269 */     checkIndex(idx);
/*     */     
/* 271 */     int realIdx = getRealIndex(idx);
/* 272 */     Object old = this.items[realIdx];
/* 273 */     this.items[realIdx] = o;
/* 274 */     return (E)old;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int idx, E o) {
/* 279 */     if (idx == size()) {
/* 280 */       offer(o);
/*     */       
/*     */       return;
/*     */     } 
/* 284 */     checkIndex(idx);
/* 285 */     expandIfNeeded();
/*     */     
/* 287 */     int realIdx = getRealIndex(idx);
/*     */ 
/*     */     
/* 290 */     if (this.first < this.last) {
/* 291 */       System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.last - realIdx);
/*     */     }
/* 293 */     else if (realIdx >= this.first) {
/* 294 */       System.arraycopy(this.items, 0, this.items, 1, this.last);
/* 295 */       this.items[0] = this.items[this.items.length - 1];
/* 296 */       System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.items.length - realIdx - 1);
/*     */     } else {
/* 298 */       System.arraycopy(this.items, realIdx, this.items, realIdx + 1, this.last - realIdx);
/*     */     } 
/*     */ 
/*     */     
/* 302 */     this.items[realIdx] = o;
/* 303 */     increaseSize();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E remove(int idx) {
/* 309 */     if (idx == 0) {
/* 310 */       return poll();
/*     */     }
/*     */     
/* 313 */     checkIndex(idx);
/*     */     
/* 315 */     int realIdx = getRealIndex(idx);
/* 316 */     Object removed = this.items[realIdx];
/*     */ 
/*     */     
/* 319 */     if (this.first < this.last) {
/* 320 */       System.arraycopy(this.items, this.first, this.items, this.first + 1, realIdx - this.first);
/*     */     }
/* 322 */     else if (realIdx >= this.first) {
/* 323 */       System.arraycopy(this.items, this.first, this.items, this.first + 1, realIdx - this.first);
/*     */     } else {
/* 325 */       System.arraycopy(this.items, 0, this.items, 1, realIdx);
/* 326 */       this.items[0] = this.items[this.items.length - 1];
/* 327 */       System.arraycopy(this.items, this.first, this.items, this.first + 1, this.items.length - this.first - 1);
/*     */     } 
/*     */ 
/*     */     
/* 331 */     this.items[this.first] = null;
/* 332 */     decreaseSize();
/*     */     
/* 334 */     shrinkIfNeeded();
/* 335 */     return (E)removed;
/*     */   }
/*     */   
/*     */   public E remove() {
/* 339 */     if (isEmpty()) {
/* 340 */       throw new NoSuchElementException();
/*     */     }
/* 342 */     return poll();
/*     */   }
/*     */   
/*     */   public E element() {
/* 346 */     if (isEmpty()) {
/* 347 */       throw new NoSuchElementException();
/*     */     }
/* 349 */     return peek();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/CircularQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */