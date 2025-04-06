/*     */ package javolution.util;
/*     */ 
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.util.internal.bitset.BitSetServiceImpl;
/*     */ import javolution.util.service.BitSetService;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.SetService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastBitSet
/*     */   extends FastSet<Index>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final BitSetService service;
/*     */   
/*     */   public FastBitSet() {
/*  40 */     this.service = (BitSetService)new BitSetServiceImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FastBitSet(BitSetService impl) {
/*  47 */     this.service = impl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastBitSet unmodifiable() {
/*  56 */     throw new UnsupportedOperationException("NOT DONE YET");
/*     */   }
/*     */ 
/*     */   
/*     */   public FastBitSet shared() {
/*  61 */     throw new UnsupportedOperationException("NOT DONE YET");
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void and(FastBitSet that) {
/*  77 */     this.service.and(that.service);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void andNot(FastBitSet that) {
/*  90 */     this.service.andNot(that.service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int cardinality() {
/* 100 */     return this.service.cardinality();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 108 */     this.service.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(int bitIndex) {
/* 119 */     this.service.clear(bitIndex);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void clear(int fromIndex, int toIndex) {
/* 133 */     this.service.clear(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flip(int bitIndex) {
/* 143 */     this.service.flip(bitIndex);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void flip(int fromIndex, int toIndex) {
/* 156 */     this.service.flip(fromIndex, toIndex);
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
/*     */   public boolean get(int bitIndex) {
/* 168 */     return this.service.get(bitIndex);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public FastBitSet get(int fromIndex, int toIndex) {
/* 182 */     return new FastBitSet(this.service.get(fromIndex, toIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public boolean intersects(FastBitSet that) {
/* 194 */     return this.service.intersects(that.service);
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
/*     */   public int length() {
/* 207 */     return this.service.length();
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
/*     */   public int nextClearBit(int fromIndex) {
/* 219 */     return this.service.nextClearBit(fromIndex);
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
/*     */   public int nextSetBit(int fromIndex) {
/* 235 */     return this.service.nextSetBit(fromIndex);
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
/*     */   public int previousClearBit(int fromIndex) {
/* 247 */     return this.service.previousClearBit(fromIndex);
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
/*     */   public int previousSetBit(int fromIndex) {
/* 263 */     return this.service.previousSetBit(fromIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void or(FastBitSet that) {
/* 275 */     this.service.or(that.service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int bitIndex) {
/* 286 */     this.service.set(bitIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int bitIndex, boolean value) {
/* 297 */     this.service.set(bitIndex, value);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void set(int fromIndex, int toIndex) {
/* 311 */     if (fromIndex < 0 || toIndex < fromIndex) throw new IndexOutOfBoundsException(); 
/* 312 */     this.service.set(fromIndex, toIndex);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void set(int fromIndex, int toIndex, boolean value) {
/* 326 */     this.service.set(fromIndex, toIndex, value);
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
/*     */   @Realtime(limit = Realtime.Limit.LINEAR)
/*     */   public void xor(FastBitSet that) {
/* 339 */     this.service.xor(that.service);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastBitSet addAll(Index... elements) {
/* 348 */     return (FastBitSet)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public FastBitSet addAll(FastCollection<? extends Index> elements) {
/* 353 */     return (FastBitSet)super.addAll(elements);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BitSetService service() {
/* 358 */     return this.service;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/FastBitSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */