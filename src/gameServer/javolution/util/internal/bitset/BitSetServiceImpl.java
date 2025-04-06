/*     */ package javolution.util.internal.bitset;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.util.Index;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.set.SetView;
/*     */ import javolution.util.service.BitSetService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BitSetServiceImpl
/*     */   extends SetView<Index>
/*     */   implements BitSetService, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private long[] bits;
/*     */   
/*     */   public BitSetServiceImpl() {
/*  33 */     super(null);
/*  34 */     this.bits = new long[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(Index index) {
/*  39 */     return !getAndSet(index.intValue(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void and(BitSetService that) {
/*  44 */     long[] thatBits = that.toLongArray();
/*  45 */     int n = MathLib.min(this.bits.length, thatBits.length); int i;
/*  46 */     for (i = 0; i < n; i++) {
/*  47 */       this.bits[i] = this.bits[i] & thatBits[i];
/*     */     }
/*  49 */     for (i = n; i < this.bits.length; i++) {
/*  50 */       this.bits[i] = 0L;
/*     */     }
/*  52 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public void andNot(BitSetService that) {
/*  57 */     long[] thatBits = that.toLongArray();
/*  58 */     int n = MathLib.min(this.bits.length, thatBits.length);
/*  59 */     for (int i = 0; i < n; i++) {
/*  60 */       this.bits[i] = this.bits[i] & (thatBits[i] ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*  62 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public int cardinality() {
/*  67 */     int sum = 0;
/*  68 */     for (int i = 0; i < this.bits.length; i++) {
/*  69 */       sum += MathLib.bitCount(this.bits[i]);
/*     */     }
/*  71 */     return sum;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  76 */     this.bits = new long[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int bitIndex) {
/*  81 */     int longIndex = bitIndex >> 6;
/*  82 */     if (longIndex >= this.bits.length)
/*     */       return; 
/*  84 */     this.bits[longIndex] = this.bits[longIndex] & (1L << bitIndex ^ 0xFFFFFFFFFFFFFFFFL);
/*  85 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(int fromIndex, int toIndex) {
/*  90 */     if (fromIndex < 0 || toIndex < fromIndex)
/*  91 */       throw new IndexOutOfBoundsException(); 
/*  92 */     int i = fromIndex >>> 6;
/*  93 */     if (i >= this.bits.length)
/*     */       return; 
/*  95 */     int j = toIndex >>> 6;
/*  96 */     if (i == j) {
/*  97 */       this.bits[i] = this.bits[i] & ((1L << fromIndex) - 1L | -1L << toIndex);
/*     */       return;
/*     */     } 
/* 100 */     this.bits[i] = this.bits[i] & (1L << fromIndex) - 1L;
/* 101 */     if (j < this.bits.length) {
/* 102 */       this.bits[j] = this.bits[j] & -1L << toIndex;
/*     */     }
/* 104 */     for (int k = i + 1; k < j && k < this.bits.length; k++) {
/* 105 */       this.bits[k] = 0L;
/*     */     }
/* 107 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super Index> comparator() {
/* 112 */     return Equalities.IDENTITY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object index) {
/* 117 */     return get(((Index)index).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void flip(int bitIndex) {
/* 122 */     int i = bitIndex >> 6;
/* 123 */     setLength(i + 1);
/* 124 */     this.bits[i] = this.bits[i] ^ 1L << bitIndex;
/* 125 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flip(int fromIndex, int toIndex) {
/* 130 */     if (fromIndex < 0 || toIndex < fromIndex)
/* 131 */       throw new IndexOutOfBoundsException(); 
/* 132 */     int i = fromIndex >>> 6;
/* 133 */     int j = toIndex >>> 6;
/* 134 */     setLength(j + 1);
/* 135 */     if (i == j) {
/* 136 */       this.bits[i] = this.bits[i] ^ -1L << fromIndex & (1L << toIndex) - 1L;
/*     */       return;
/*     */     } 
/* 139 */     this.bits[i] = this.bits[i] ^ -1L << fromIndex;
/* 140 */     this.bits[j] = this.bits[j] ^ (1L << toIndex) - 1L;
/* 141 */     for (int k = i + 1; k < j; k++) {
/* 142 */       this.bits[k] = this.bits[k] ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     }
/* 144 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean get(int bitIndex) {
/* 149 */     int i = bitIndex >> 6;
/* 150 */     return (i >= this.bits.length) ? false : (((this.bits[i] & 1L << bitIndex) != 0L));
/*     */   }
/*     */ 
/*     */   
/*     */   public BitSetServiceImpl get(int fromIndex, int toIndex) {
/* 155 */     if (fromIndex < 0 || fromIndex > toIndex)
/* 156 */       throw new IndexOutOfBoundsException(); 
/* 157 */     BitSetServiceImpl bitSet = new BitSetServiceImpl();
/* 158 */     int length = MathLib.min(this.bits.length, (toIndex >>> 6) + 1);
/* 159 */     bitSet.bits = new long[length];
/* 160 */     System.arraycopy(this.bits, 0, bitSet.bits, 0, length);
/* 161 */     bitSet.clear(0, fromIndex);
/* 162 */     bitSet.clear(toIndex, length << 6);
/* 163 */     return bitSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAndSet(int bitIndex, boolean value) {
/* 169 */     int i = bitIndex >> 6;
/* 170 */     if (i >= this.bits.length) {
/* 171 */       setLength(i + 1);
/*     */     }
/* 173 */     boolean previous = ((this.bits[i] & 1L << bitIndex) != 0L);
/* 174 */     if (value) {
/* 175 */       this.bits[i] = this.bits[i] | 1L << bitIndex;
/*     */     } else {
/* 177 */       this.bits[i] = this.bits[i] & (1L << bitIndex ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     } 
/* 179 */     trim();
/* 180 */     return previous;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersects(BitSetService that) {
/* 185 */     long[] thatBits = that.toLongArray();
/* 186 */     int i = MathLib.min(this.bits.length, thatBits.length);
/* 187 */     while (--i >= 0) {
/* 188 */       if ((this.bits[i] & thatBits[i]) != 0L) return true; 
/*     */     } 
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Index> iterator() {
/* 195 */     return new BitSetIteratorImpl(this, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/* 200 */     if (this.bits.length == 0) return 0; 
/* 201 */     return (this.bits.length << 6) - MathLib.numberOfLeadingZeros(this.bits[this.bits.length - 1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextClearBit(int fromIndex) {
/* 206 */     int offset = fromIndex >> 6;
/* 207 */     long mask = 1L << fromIndex;
/* 208 */     while (offset < this.bits.length) {
/* 209 */       long h = this.bits[offset];
/*     */       while (true) {
/* 211 */         if ((h & mask) == 0L) return fromIndex; 
/* 212 */         mask <<= 1L;
/* 213 */         fromIndex++;
/* 214 */         if (mask == 0L)
/* 215 */         { mask = 1L;
/* 216 */           offset++; } 
/*     */       } 
/* 218 */     }  return fromIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextSetBit(int fromIndex) {
/* 223 */     int offset = fromIndex >> 6;
/* 224 */     long mask = 1L << fromIndex;
/* 225 */     while (offset < this.bits.length) {
/* 226 */       long h = this.bits[offset];
/*     */       while (true) {
/* 228 */         if ((h & mask) != 0L)
/* 229 */           return fromIndex; 
/* 230 */         mask <<= 1L;
/* 231 */         fromIndex++;
/* 232 */         if (mask == 0L)
/* 233 */         { mask = 1L;
/* 234 */           offset++; } 
/*     */       } 
/* 236 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void or(BitSetService that) {
/* 241 */     long[] thatBits = (that instanceof BitSetServiceImpl) ? ((BitSetServiceImpl)that).bits : that.toLongArray();
/*     */     
/* 243 */     if (thatBits.length > this.bits.length) {
/* 244 */       setLength(thatBits.length);
/*     */     }
/* 246 */     for (int i = thatBits.length; --i >= 0;) {
/* 247 */       this.bits[i] = this.bits[i] | thatBits[i];
/*     */     }
/* 249 */     trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public int previousClearBit(int fromIndex) {
/* 254 */     int offset = fromIndex >> 6;
/* 255 */     long mask = 1L << fromIndex;
/* 256 */     while (offset >= 0) {
/* 257 */       long h = this.bits[offset];
/*     */       while (true) {
/* 259 */         if ((h & mask) == 0L)
/* 260 */           return fromIndex; 
/* 261 */         mask >>= 1L;
/* 262 */         fromIndex--;
/* 263 */         if (mask == 0L)
/* 264 */         { mask = Long.MIN_VALUE;
/* 265 */           offset--; } 
/*     */       } 
/* 267 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int previousSetBit(int fromIndex) {
/* 272 */     int offset = fromIndex >> 6;
/* 273 */     long mask = 1L << fromIndex;
/* 274 */     while (offset >= 0) {
/* 275 */       long h = this.bits[offset];
/*     */       while (true) {
/* 277 */         if ((h & mask) != 0L)
/* 278 */           return fromIndex; 
/* 279 */         mask >>= 1L;
/* 280 */         fromIndex--;
/* 281 */         if (mask == 0L)
/* 282 */         { mask = Long.MIN_VALUE;
/* 283 */           offset--; } 
/*     */       } 
/* 285 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object index) {
/* 290 */     return getAndSet(((Index)index).intValue(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int bitIndex) {
/* 295 */     int i = bitIndex >> 6;
/* 296 */     if (i >= this.bits.length) {
/* 297 */       setLength(i + 1);
/*     */     }
/* 299 */     this.bits[i] = this.bits[i] | 1L << bitIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int bitIndex, boolean value) {
/* 304 */     if (value) {
/* 305 */       set(bitIndex);
/*     */     } else {
/* 307 */       clear(bitIndex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int fromIndex, int toIndex) {
/* 313 */     int i = fromIndex >>> 6;
/* 314 */     int j = toIndex >>> 6;
/* 315 */     setLength(j + 1);
/* 316 */     if (i == j) {
/* 317 */       this.bits[i] = this.bits[i] | -1L << fromIndex & (1L << toIndex) - 1L;
/*     */       return;
/*     */     } 
/* 320 */     this.bits[i] = this.bits[i] | -1L << fromIndex;
/* 321 */     this.bits[j] = this.bits[j] | (1L << toIndex) - 1L;
/* 322 */     for (int k = i + 1; k < j; k++) {
/* 323 */       this.bits[k] = -1L;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int fromIndex, int toIndex, boolean value) {
/* 329 */     if (value) {
/* 330 */       set(fromIndex, toIndex);
/*     */     } else {
/* 332 */       clear(fromIndex, toIndex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 338 */     return cardinality();
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] toLongArray() {
/* 343 */     return this.bits;
/*     */   }
/*     */ 
/*     */   
/*     */   public void xor(BitSetService that) {
/* 348 */     long[] thatBits = (that instanceof BitSetServiceImpl) ? ((BitSetServiceImpl)that).bits : that.toLongArray();
/*     */     
/* 350 */     if (thatBits.length > this.bits.length) {
/* 351 */       setLength(thatBits.length);
/*     */     }
/* 353 */     for (int i = thatBits.length; --i >= 0;) {
/* 354 */       this.bits[i] = this.bits[i] ^ thatBits[i];
/*     */     }
/* 356 */     trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLength(int newLength) {
/* 363 */     long[] tmp = new long[newLength];
/* 364 */     if (newLength >= this.bits.length) {
/* 365 */       System.arraycopy(this.bits, 0, tmp, 0, this.bits.length);
/*     */     } else {
/* 367 */       System.arraycopy(this.bits, 0, tmp, 0, newLength);
/*     */     } 
/* 369 */     this.bits = tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 376 */     int n = this.bits.length;
/* 377 */     while (--n >= 0 && this.bits[n] == 0L);
/* 378 */     if (++n < this.bits.length) setLength(n); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/bitset/BitSetServiceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */