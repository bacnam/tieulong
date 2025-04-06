/*     */ package jsc.descriptive;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DoubleTally
/*     */   implements Cloneable
/*     */ {
/*     */   private int N;
/*     */   private Vector freq;
/*     */   private Vector x;
/*     */   private double tolerance;
/*     */   
/*     */   public DoubleTally(double[] paramArrayOfdouble) {
/*  39 */     this(paramArrayOfdouble, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleTally(double[] paramArrayOfdouble, double paramDouble) {
/*  49 */     this.tolerance = paramDouble;
/*     */     
/*  51 */     this.N = paramArrayOfdouble.length;
/*     */     
/*  53 */     Arrays.sort(paramArrayOfdouble);
/*     */ 
/*     */     
/*  56 */     byte b2 = 1;
/*  57 */     double d1 = paramArrayOfdouble[0]; byte b1;
/*  58 */     for (b1 = 1; b1 < this.N; b1++) {
/*     */       
/*  60 */       if (Math.abs(paramArrayOfdouble[b1] - d1) > paramDouble) {
/*  61 */         b2++; d1 = paramArrayOfdouble[b1];
/*     */       } 
/*     */     } 
/*  64 */     this.x = new Vector(b2);
/*     */     
/*  66 */     this.freq = new Vector(b2);
/*     */     
/*  68 */     for (b1 = 0; b1 < b2; ) { this.freq.add(new Integer(0)); b1++; }
/*     */     
/*  70 */     double d2 = paramArrayOfdouble[0];
/*  71 */     this.x.add(new Double(d2));
/*     */     
/*  73 */     this.freq.add(0, new Integer(1));
/*  74 */     byte b3 = 0;
/*     */     
/*  76 */     for (b1 = 1; b1 < this.N; b1++) {
/*     */ 
/*     */       
/*  79 */       if (Math.abs(paramArrayOfdouble[b1] - d2) > paramDouble) {
/*     */         
/*  81 */         b3++;
/*  82 */         d2 = paramArrayOfdouble[b1];
/*     */         
/*  84 */         this.x.add(new Double(d2));
/*     */       } 
/*     */       
/*  87 */       int i = ((Integer)this.freq.get(b3)).intValue();
/*  88 */       this.freq.set(b3, new Integer(++i));
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
/*     */   public DoubleTally() {
/* 108 */     this(0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleTally(double paramDouble) {
/* 119 */     this(10, 0, paramDouble);
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
/*     */   public DoubleTally(int paramInt1, int paramInt2) {
/* 134 */     this(paramInt1, paramInt2, 0.0D);
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
/*     */   public DoubleTally(int paramInt1, int paramInt2, double paramDouble) {
/* 149 */     this.tolerance = paramDouble;
/* 150 */     this.N = 0;
/* 151 */     this.x = new Vector(paramInt1, paramInt2);
/* 152 */     this.freq = new Vector(paramInt1, paramInt2);
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
/*     */   public int addValue(double paramDouble) {
/* 168 */     this.N++;
/*     */     
/* 170 */     int i = this.x.size();
/* 171 */     if (i == 0) {
/*     */       
/* 173 */       addEnd(paramDouble);
/* 174 */       return 0;
/*     */     } 
/*     */     
/* 177 */     double d1 = ((Double)this.x.firstElement()).doubleValue();
/* 178 */     double d2 = ((Double)this.x.lastElement()).doubleValue();
/* 179 */     if (Math.abs(paramDouble - d1) <= this.tolerance) { addExisting(0); return 0; }
/* 180 */      if (paramDouble < d1) { addNew(0, paramDouble); return -1; }
/* 181 */      if (Math.abs(paramDouble - d2) <= this.tolerance) { addExisting(i - 1); return i - 1; }
/* 182 */      if (paramDouble > d2) { addEnd(paramDouble); return -1; }
/*     */     
/* 184 */     int j = 0, k = i - 1;
/* 185 */     while (k - j > 1) {
/*     */       
/* 187 */       int m = k + j >> 1;
/* 188 */       double d = ((Double)this.x.get(m)).doubleValue();
/* 189 */       if (Math.abs(paramDouble - d) <= this.tolerance) { addExisting(m); return m; }
/* 190 */        if (paramDouble < d) {
/* 191 */         k = m; continue;
/*     */       } 
/* 193 */       j = m;
/*     */     } 
/* 195 */     double d3 = ((Double)this.x.get(k)).doubleValue();
/* 196 */     if (Math.abs(paramDouble - d3) <= this.tolerance) { addExisting(k); return k; }
/* 197 */      addNew(k, paramDouble);
/* 198 */     return k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEnd(double paramDouble) {
/* 207 */     this.x.add(new Double(paramDouble));
/* 208 */     this.freq.add(new Integer(1));
/*     */   }
/*     */ 
/*     */   
/*     */   private void addExisting(int paramInt) {
/* 213 */     int i = ((Integer)this.freq.get(paramInt)).intValue();
/* 214 */     this.freq.set(paramInt, new Integer(++i));
/*     */   }
/*     */ 
/*     */   
/*     */   private void addNew(int paramInt, double paramDouble) {
/* 219 */     this.x.add(paramInt, new Double(paramDouble));
/* 220 */     this.freq.add(paramInt, new Integer(1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 229 */     Object object = null; try {
/* 230 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 232 */       System.out.println("DoubleTally can't clone");
/* 233 */     }  return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFrequency(int paramInt) {
/* 243 */     return ((Integer)this.freq.get(paramInt)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMax() {
/* 250 */     return ((Double)this.x.lastElement()).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxFreq() {
/* 259 */     int i = 0;
/*     */     
/* 261 */     for (byte b = 0; b < this.freq.size(); b++) {
/*     */       
/* 263 */       int j = ((Integer)this.freq.get(b)).intValue();
/* 264 */       if (j > i) i = j; 
/*     */     } 
/* 266 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMin() {
/* 274 */     return ((Double)this.x.firstElement()).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 281 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProportion(int paramInt) {
/* 291 */     return (this.N > 0) ? (((Integer)this.freq.get(paramInt)).intValue() / this.N) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue(int paramInt) {
/* 300 */     return ((Double)this.x.get(paramInt)).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValueCount() {
/* 307 */     return this.x.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(double paramDouble) {
/* 317 */     return this.x.indexOf(new Double(paramDouble));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 328 */       double[] arrayOfDouble = { 4.0D, 5.0D, 8.0D, 9.0D, 3.0D, 2.0D, 5.0D, 4.0D, 6.0D, 7.0D, 8.0D, 9.0D, 2.0D, 1.0D, 0.0D, -3.0D, -2.0D, 0.0D, -6.0D, -3.0D };
/*     */ 
/*     */       
/* 331 */       DoubleTally doubleTally = new DoubleTally(); byte b;
/* 332 */       for (b = 0; b < arrayOfDouble.length; ) { doubleTally.addValue(arrayOfDouble[b]); b++; }
/*     */       
/* 334 */       System.out.println("DoubleTally " + doubleTally.getN() + " values, " + "Min = " + doubleTally.getMin() + ", Max = " + doubleTally.getMax());
/*     */       
/* 336 */       for (b = 0; b < doubleTally.getValueCount(); b++) {
/* 337 */         System.out.println(doubleTally.getValue(b) + ", Freq = " + doubleTally.getFrequency(b) + ", p = " + doubleTally.getProportion(b));
/*     */       }
/* 339 */       for (b = 0; b < arrayOfDouble.length; b++)
/* 340 */         System.out.println("Index of " + arrayOfDouble[b] + " is " + doubleTally.indexOf(arrayOfDouble[b])); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/DoubleTally.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */