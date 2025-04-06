/*     */ package jsc.datastructures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PairedData
/*     */ {
/*     */   private int n;
/*     */   private double[] x;
/*     */   private double[] y;
/*     */   
/*     */   public PairedData(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  45 */     this.n = paramArrayOfdouble1.length;
/*  46 */     if (this.n != paramArrayOfdouble2.length)
/*  47 */       throw new IllegalArgumentException("Arrays not equal length."); 
/*  48 */     if (this.n < 1)
/*  49 */       throw new IllegalArgumentException("No data."); 
/*  50 */     this.x = new double[this.n];
/*  51 */     this.y = new double[this.n];
/*  52 */     System.arraycopy(paramArrayOfdouble1, 0, this.x, 0, this.n);
/*  53 */     System.arraycopy(paramArrayOfdouble2, 0, this.y, 0, this.n);
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
/*     */   public PairedData(double[][] paramArrayOfdouble) {
/*  79 */     this.n = paramArrayOfdouble.length;
/*  80 */     if (this.n < 1)
/*  81 */       throw new IllegalArgumentException("No data."); 
/*  82 */     this.x = new double[this.n];
/*  83 */     this.y = new double[this.n];
/*  84 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/*  86 */       this.x[b] = paramArrayOfdouble[b][0];
/*  87 */       this.y[b] = paramArrayOfdouble[b][1];
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
/*     */   public double[] differences() {
/* 101 */     double[] arrayOfDouble = new double[this.n];
/* 102 */     for (byte b = 0; b < this.n; ) { arrayOfDouble[b] = this.x[b] - this.y[b]; b++; }
/* 103 */      return arrayOfDouble;
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
/*     */   public int getN() {
/* 125 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getX() {
/* 132 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getY() {
/* 139 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     StringBuffer stringBuffer = new StringBuffer();
/* 150 */     stringBuffer.append("\nPaired data");
/*     */     
/* 152 */     stringBuffer.append("\nX\tY");
/* 153 */     for (byte b = 0; b < getN(); b++) {
/* 154 */       stringBuffer.append("\n" + this.x[b] + "\t" + this.y[b]);
/*     */     }
/* 156 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 166 */       double[] arrayOfDouble1 = { 17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D };
/* 167 */       double[] arrayOfDouble2 = { 13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D };
/* 168 */       PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
/* 169 */       double[] arrayOfDouble3 = pairedData.getX();
/* 170 */       double[] arrayOfDouble4 = pairedData.getY();
/* 171 */       double[] arrayOfDouble5 = pairedData.differences();
/*     */       
/* 173 */       for (byte b = 0; b < pairedData.getN(); b++) {
/* 174 */         System.out.println(arrayOfDouble3[b] + " " + arrayOfDouble4[b] + " " + arrayOfDouble5[b]);
/*     */       }
/* 176 */       double[][] arrayOfDouble = { { 17.4D, 13.6D }, { 15.7D, 10.1D }, { 12.9D, 10.3D }, { 9.8D, 9.2D }, { 13.4D, 11.1D }, { 18.7D, 20.4D }, { 13.9D, 10.4D }, { 11.0D, 11.4D }, { 5.4D, 4.9D }, { 10.4D, 8.9D }, { 16.4D, 11.2D }, { 5.6D, 4.8D } };
/*     */       
/* 178 */       pairedData = new PairedData(arrayOfDouble);
/* 179 */       System.out.println(pairedData.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/datastructures/PairedData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */