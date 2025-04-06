/*     */ package jsc.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scale
/*     */ {
/*     */   private int n;
/*     */   private boolean extend;
/*     */   private double step;
/*     */   private double valmin;
/*     */   private double valmax;
/*     */   
/*     */   public Scale() {
/*  26 */     this(0.0D, 1.0D, 2, false, false);
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
/*     */   public Scale(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean) {
/*  38 */     this(paramDouble1, paramDouble2, paramInt, paramBoolean, false);
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
/*     */   public Scale(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
/*  52 */     this.extend = paramBoolean1;
/*  53 */     if (paramBoolean1) {
/*  54 */       scale(paramInt, paramDouble1, paramDouble2, paramBoolean2);
/*     */     } else {
/*     */       
/*  57 */       if (paramDouble2 <= paramDouble1 || paramInt <= 1)
/*  58 */         throw new IllegalArgumentException("Invalid scale values."); 
/*  59 */       this.n = paramInt;
/*  60 */       this.valmin = paramDouble1; this.valmax = paramDouble2;
/*  61 */       this.step = (paramDouble2 - paramDouble1) / (paramInt - 1.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFirstTickValue() {
/*  70 */     return this.valmin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLastTickValue() {
/*  77 */     return this.valmax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLength() {
/*  84 */     return this.extend ? (this.valmax - this.valmin + this.step) : (this.valmax - this.valmin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMax() {
/*  93 */     return this.extend ? (this.valmax + 0.5D * this.step) : this.valmax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMin() {
/* 102 */     return this.extend ? (this.valmin - 0.5D * this.step) : this.valmin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfTicks() {
/* 110 */     return this.n;
/*     */   }
/*     */   public double getStep() {
/* 113 */     return this.step;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTickValue(int paramInt) {
/* 122 */     return this.valmin + paramInt * this.step;
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
/*     */   public void scale(int paramInt, double paramDouble1, double paramDouble2, boolean paramBoolean) {
/* 163 */     double[] arrayOfDouble1 = { 1.0D, 1.2D, 1.6D, 2.0D, 2.5D, 3.0D, 4.0D, 5.0D, 6.0D, 8.0D, 10.0D };
/* 164 */     int j = arrayOfDouble1.length;
/* 165 */     double[] arrayOfDouble2 = { 1.0D, 2.0D, 2.5D, 5.0D, 10.0D };
/* 166 */     int k = arrayOfDouble2.length;
/*     */     
/* 168 */     if (paramDouble2 < paramDouble1 || paramInt <= 1) {
/* 169 */       throw new IllegalArgumentException("Invalid axis values.");
/*     */     }
/* 171 */     this.n = paramInt;
/*     */     
/* 173 */     double d2 = (paramInt - 1);
/* 174 */     double d4 = Math.abs(paramDouble2);
/* 175 */     if (d4 == 0.0D) d4 = 1.0D; 
/* 176 */     if ((paramDouble2 - paramDouble1) / d4 <= 1.0E-8D)
/*     */     {
/*     */       
/* 179 */       if (paramDouble2 < 0.0D) {
/* 180 */         paramDouble2 = 0.0D;
/* 181 */       } else if (paramDouble2 == 0.0D) {
/* 182 */         paramDouble2 = 1.0D;
/*     */       } else {
/* 184 */         paramDouble1 = 0.0D;
/*     */       }  } 
/* 186 */     this.step = (paramDouble2 - paramDouble1) / d2;
/* 187 */     double d3 = this.step;
/*     */ 
/*     */     
/* 190 */     for (; d3 < 1.0D; d3 *= 10.0D);
/* 191 */     for (; d3 >= 10.0D; d3 /= 10.0D);
/*     */ 
/*     */     
/* 194 */     d4 = d3 - 1.0E-4D;
/* 195 */     if (paramBoolean) {
/*     */       byte b;
/* 197 */       for (b = 0; b < k && d4 > arrayOfDouble2[b]; b++);
/* 198 */       this.step *= arrayOfDouble2[b] / d3;
/*     */     } else {
/*     */       byte b;
/*     */       
/* 202 */       for (b = 0; b < j && d4 > arrayOfDouble1[b]; b++);
/* 203 */       this.step *= arrayOfDouble1[b] / d3;
/*     */     } 
/*     */     
/* 206 */     double d1 = this.step * d2;
/*     */ 
/*     */     
/* 209 */     d4 = 0.5D * (1.0D + (paramDouble1 + paramDouble2 - d1) / this.step);
/* 210 */     int i = (int)(d4 - 1.0E-4D);
/* 211 */     if (d4 < 0.0D) i--; 
/* 212 */     this.valmin = this.step * i;
/*     */ 
/*     */     
/* 215 */     if (paramDouble1 >= 0.0D && d1 >= paramDouble2) this.valmin = 0.0D; 
/* 216 */     this.valmax = this.valmin + d1;
/*     */ 
/*     */     
/* 219 */     if (paramDouble2 > 0.0D || d1 < -paramDouble1)
/* 220 */       return;  this.valmax = 0.0D;
/* 221 */     this.valmin = -d1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 227 */     return "Scale: " + this.n + " tick marks at " + this.step + " intervals. Min = " + getMin() + ", max = " + getMax() + ". First tick mark at " + this.valmin + ". Last tick mark at " + this.valmax;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Scale.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */