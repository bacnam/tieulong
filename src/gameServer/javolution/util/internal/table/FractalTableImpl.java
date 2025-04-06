/*     */ package javolution.util.internal.table;
/*     */ 
/*     */ import javolution.lang.MathLib;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FractalTableImpl
/*     */ {
/*     */   static final int BASE_CAPACITY_MIN = 16;
/*     */   static final int SHIFT = 8;
/*     */   private static final int BASE_CAPACITY_MAX = 256;
/*     */   int offset;
/*     */   private Object[] data;
/*     */   private final int shift;
/*     */   
/*     */   public FractalTableImpl() {
/*  36 */     this.shift = 0;
/*  37 */     this.data = new Object[16];
/*     */   }
/*     */   
/*     */   public FractalTableImpl(int shift) {
/*  41 */     this.shift = shift;
/*  42 */     this.data = new Object[2];
/*     */   }
/*     */   
/*     */   public FractalTableImpl(int shift, Object[] data, int offset) {
/*  46 */     this.shift = shift;
/*  47 */     this.data = data;
/*  48 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  54 */     return this.data.length - 1 << this.shift;
/*     */   }
/*     */   
/*     */   public Object get(int index) {
/*  58 */     Object fractal = this.data[index + this.offset >> this.shift & this.data.length - 1];
/*  59 */     return (this.shift == 0) ? fractal : ((FractalTableImpl)fractal).get(index + this.offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object set(int index, Object element) {
/*  64 */     int i = index + this.offset >> this.shift & this.data.length - 1;
/*  65 */     if (this.shift != 0) return F(i).set(index + this.offset, element); 
/*  66 */     Object previous = this.data[i];
/*  67 */     this.data[i] = element;
/*  68 */     return previous;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shiftLeft(Object inserted, int last, int length) {
/*  74 */     int mask = (this.data.length << this.shift) - 1;
/*  75 */     int tail = last + this.offset & mask;
/*  76 */     int head = last + this.offset - length & mask;
/*  77 */     if (this.shift == 0) {
/*  78 */       int n = tail - head;
/*  79 */       if (head > tail) {
/*  80 */         System.arraycopy(this.data, head + 1, this.data, head, mask - head);
/*  81 */         this.data[mask] = this.data[0];
/*  82 */         n = tail;
/*     */       } 
/*  84 */       System.arraycopy(this.data, tail - n + 1, this.data, tail - n, n);
/*  85 */       this.data[tail] = inserted;
/*  86 */     } else if (head <= tail && head >> this.shift == tail >> this.shift) {
/*  87 */       F(head >> this.shift).shiftLeft(inserted, tail, length);
/*     */     } else {
/*  89 */       int low = head >> this.shift;
/*  90 */       int high = (low != this.data.length - 1) ? (low + 1) : 0;
/*  91 */       F(low).shiftLeft(F(high).get(0), -1, mask - head);
/*  92 */       while (high != tail >> this.shift) {
/*  93 */         low = high;
/*  94 */         high = (low != this.data.length - 1) ? (low + 1) : 0;
/*  95 */         (F(low)).offset++;
/*  96 */         F(low).set(-1, F(high).get(0));
/*     */       } 
/*  98 */       F(high).shiftLeft(inserted, tail, tail);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shiftRight(Object inserted, int first, int length) {
/* 105 */     int mask = (this.data.length << this.shift) - 1;
/* 106 */     int head = first + this.offset & mask;
/* 107 */     int tail = first + this.offset + length & mask;
/* 108 */     if (this.shift == 0) {
/* 109 */       int n = tail - head;
/* 110 */       if (head > tail) {
/* 111 */         System.arraycopy(this.data, 0, this.data, 1, tail);
/* 112 */         this.data[0] = this.data[mask];
/* 113 */         n = mask - head;
/*     */       } 
/* 115 */       System.arraycopy(this.data, head, this.data, head + 1, n);
/* 116 */       this.data[head] = inserted;
/* 117 */     } else if (head <= tail && head >> this.shift == tail >> this.shift) {
/* 118 */       F(head >> this.shift).shiftRight(inserted, head, length);
/*     */     } else {
/* 120 */       int high = tail >> this.shift;
/* 121 */       int low = (high != 0) ? (high - 1) : (this.data.length - 1);
/* 122 */       F(high).shiftRight(F(low).get(-1), 0, tail);
/* 123 */       while (low != head >> this.shift) {
/* 124 */         high = low;
/* 125 */         low = (high != 0) ? (high - 1) : (this.data.length - 1);
/* 126 */         (F(high)).offset--;
/* 127 */         F(high).set(0, F(low).get(-1));
/*     */       } 
/* 129 */       F(low).shiftRight(inserted, head, mask - head);
/*     */     } 
/*     */   }
/*     */   
/*     */   public FractalTableImpl upsize() {
/* 134 */     if (this.data.length >= 256) {
/* 135 */       FractalTableImpl fractalTableImpl = new FractalTableImpl(this.shift + 8);
/* 136 */       copyTo(fractalTableImpl.F(0));
/* 137 */       return fractalTableImpl;
/*     */     } 
/* 139 */     FractalTableImpl table = new FractalTableImpl(this.shift, new Object[this.data.length << 1], 0);
/*     */     
/* 141 */     copyTo(table);
/* 142 */     return table;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyTo(FractalTableImpl that) {
/* 148 */     int n = MathLib.min(this.data.length, that.data.length);
/* 149 */     this.offset &= (this.data.length << this.shift) - 1;
/* 150 */     int o = this.offset >> this.shift;
/* 151 */     if (o + n > this.data.length) {
/* 152 */       int w = o + n - this.data.length;
/* 153 */       n -= w;
/* 154 */       System.arraycopy(this.data, 0, that.data, n, w);
/*     */     } 
/* 156 */     System.arraycopy(this.data, o, that.data, 0, n);
/* 157 */     this.offset -= o << this.shift;
/*     */   }
/*     */   
/*     */   private FractalTableImpl allocate(int i) {
/* 161 */     FractalTableImpl fractal = new FractalTableImpl(this.shift - 8, new Object[256], 0);
/*     */     
/* 163 */     this.data[i] = fractal;
/* 164 */     return fractal;
/*     */   }
/*     */   
/*     */   private FractalTableImpl F(int i) {
/* 168 */     FractalTableImpl table = (FractalTableImpl)this.data[i];
/* 169 */     return (table != null) ? table : allocate(i);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/FractalTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */