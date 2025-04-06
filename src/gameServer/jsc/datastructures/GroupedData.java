/*     */ package jsc.datastructures;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GroupedData
/*     */ {
/*     */   private int groupCount;
/*     */   private int n;
/*     */   private Vector groupLabels;
/*     */   private double minValue;
/*     */   private double maxValue;
/*     */   private double[][] groupedData;
/*     */   
/*     */   public GroupedData(double[] paramArrayOfdouble, String[] paramArrayOfString) {
/*  45 */     this.n = paramArrayOfdouble.length;
/*  46 */     if (this.n < 1)
/*  47 */       throw new IllegalArgumentException("No data."); 
/*  48 */     if (this.n != paramArrayOfString.length) {
/*  49 */       throw new IllegalArgumentException("Arrays not equal length.");
/*     */     }
/*     */     
/*  52 */     this.minValue = paramArrayOfdouble[0]; this.maxValue = paramArrayOfdouble[0];
/*  53 */     for (byte b1 = 1; b1 < paramArrayOfdouble.length; b1++) {
/*     */       
/*  55 */       if (paramArrayOfdouble[b1] > this.maxValue) this.maxValue = paramArrayOfdouble[b1]; 
/*  56 */       if (paramArrayOfdouble[b1] < this.minValue) this.minValue = paramArrayOfdouble[b1];
/*     */     
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     this.groupLabels = Sort.getLabels(paramArrayOfString);
/*     */     
/*  67 */     this.groupCount = this.groupLabels.size();
/*  68 */     this.groupedData = new double[this.groupCount][];
/*  69 */     Vector vector = new Vector(this.n);
/*     */ 
/*     */     
/*  72 */     for (byte b2 = 0; b2 < this.groupCount; b2++) {
/*     */       
/*  74 */       vector.clear();
/*  75 */       String str = this.groupLabels.get(b2);
/*  76 */       for (byte b3 = 0; b3 < this.n; b3++) {
/*  77 */         if (str.equals(paramArrayOfString[b3])) vector.add(new Double(paramArrayOfdouble[b3])); 
/*     */       } 
/*  79 */       int i = vector.size();
/*  80 */       this.groupedData[b2] = new double[i];
/*  81 */       for (byte b4 = 0; b4 < i; b4++) {
/*  82 */         this.groupedData[b2][b4] = ((Double)vector.get(b4)).doubleValue();
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
/*     */   public GroupedData(double[] paramArrayOfdouble, int[] paramArrayOfint) {
/*  97 */     this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint));
/*     */   }
/*     */   public Object clone() {
/* 100 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroupedData copy() {
/* 110 */     byte b2 = 0;
/*     */     
/* 112 */     String[] arrayOfString = new String[this.n];
/* 113 */     double[] arrayOfDouble = new double[this.n];
/*     */ 
/*     */     
/* 116 */     for (byte b1 = 0; b1 < this.groupCount; b1++) {
/*     */       
/* 118 */       String str = getLabel(b1);
/* 119 */       double[] arrayOfDouble1 = getData(b1);
/* 120 */       for (byte b = 0; b < getSize(b1); b++) {
/*     */         
/* 122 */         arrayOfDouble[b2] = arrayOfDouble1[b];
/* 123 */         arrayOfString[b2] = str;
/* 124 */         b2++;
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return new GroupedData(arrayOfDouble, arrayOfString);
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
/*     */   public double[] getData(String paramString) {
/* 140 */     int i = this.groupLabels.indexOf(paramString);
/* 141 */     if (i < 0) return null; 
/* 142 */     return this.groupedData[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getData() {
/* 153 */     double[] arrayOfDouble = new double[this.n];
/* 154 */     byte b1 = 0;
/* 155 */     for (byte b2 = 0; b2 < this.groupCount; b2++) {
/*     */       
/* 157 */       double[] arrayOfDouble1 = this.groupedData[b2];
/* 158 */       for (byte b = 0; b < arrayOfDouble1.length; b++) {
/* 159 */         arrayOfDouble[b1] = arrayOfDouble1[b]; b1++;
/*     */       } 
/* 161 */     }  return arrayOfDouble;
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
/*     */   public double[] getData(int paramInt) {
/* 173 */     return this.groupedData[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGroupCount() {
/* 180 */     return this.groupCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLabel(int paramInt) {
/* 191 */     return this.groupLabels.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getLabels() {
/* 198 */     return this.groupLabels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxValue() {
/* 206 */     return this.maxValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinValue() {
/* 213 */     return this.minValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxSize() {
/* 222 */     int i = (this.groupedData[0]).length;
/* 223 */     for (byte b = 1; b < this.groupCount; b++) {
/* 224 */       if ((this.groupedData[b]).length > i) i = (this.groupedData[b]).length; 
/* 225 */     }  return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinSize() {
/* 235 */     int i = (this.groupedData[0]).length;
/* 236 */     for (byte b = 1; b < this.groupCount; b++) {
/* 237 */       if ((this.groupedData[b]).length < i) i = (this.groupedData[b]).length; 
/* 238 */     }  return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 246 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize(int paramInt) {
/* 257 */     return (this.groupedData[paramInt]).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize(String paramString) {
/* 268 */     int i = this.groupLabels.indexOf(paramString);
/* 269 */     if (i < 0) return 0; 
/* 270 */     return (this.groupedData[i]).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getSizes() {
/* 281 */     int[] arrayOfInt = new int[this.groupCount];
/* 282 */     for (byte b = 0; b < this.groupCount; b++)
/* 283 */       arrayOfInt[b] = (this.groupedData[b]).length; 
/* 284 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLabel(String paramString) {
/* 293 */     return this.groupLabels.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(String paramString) {
/* 302 */     return this.groupLabels.indexOf(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 309 */     StringBuffer stringBuffer = new StringBuffer();
/* 310 */     stringBuffer.append("\nGrouped data");
/* 311 */     for (byte b = 0; b < getGroupCount(); b++) {
/*     */       
/* 313 */       String str = getLabel(b);
/* 314 */       stringBuffer.append("\n" + str);
/* 315 */       double[] arrayOfDouble = getData(b);
/* 316 */       for (byte b1 = 0; b1 < getSize(str); ) { stringBuffer.append(" " + arrayOfDouble[b1]); b1++; }
/*     */     
/* 318 */     }  return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 328 */       String[] arrayOfString = { "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Italy", "Britain", "Japan", "West Germany", "Britain", "Japan", "West Germany", "Britain", "Japan", "West Germany", "Britain", "West Germany", "Britain", "Britain", "Britain", "Britain", "Britain" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 337 */       double[] arrayOfDouble = { 21.0D, 9.0D, 18.0D, 41.0D, 33.0D, 13.0D, 35.0D, 41.0D, 12.0D, 6.0D, 8.0D, 48.0D, 28.0D, 3.0D, 17.0D, 34.0D, 41.0D, 5.0D, 22.0D, 39.0D, 10.0D, 20.0D, 24.0D, 4.0D, 37.0D, 29.0D, 11.0D, 30.0D, 19.0D, 27.0D, 38.0D, 23.0D };
/* 338 */       GroupedData groupedData1 = new GroupedData(arrayOfDouble, arrayOfString);
/* 339 */       System.out.println(groupedData1.getN() + " values, " + groupedData1.getGroupCount() + " groups" + " min size = " + groupedData1.getMinSize() + " max size = " + groupedData1.getMaxSize());
/*     */ 
/*     */       
/* 342 */       System.out.println(groupedData1.toString());
/* 343 */       GroupedData groupedData2 = groupedData1.copy();
/* 344 */       System.out.println(groupedData2.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/datastructures/GroupedData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */