/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import jsc.descriptive.FrequencyTable;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.swt.virtualgraphics.VPolygon;
/*     */ import jsc.util.Maths;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Histogram2D
/*     */   extends AxesPlot
/*     */ {
/*     */   public static final int FREQUENCY = 0;
/*     */   public static final int RELATIVE = 1;
/*     */   public static final int NORMALIZED = 2;
/*     */   private static final double ONE_THIRD = 0.3333333333333333D;
/*  36 */   int type = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Histogram2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2) {
/*  46 */     super(paramAxisModel1, paramAxisModel2, paramString); setAntialiasing(false);
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
/*     */   public PlotObject addBars(FrequencyTable paramFrequencyTable, Paint paramPaint) {
/*  63 */     return addBars(paramFrequencyTable, PlotShape.defaultColour, paramPaint, PlotShape.defaultStroke, true);
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
/*     */   public PlotObject addBars(FrequencyTable paramFrequencyTable, Color paramColor, Stroke paramStroke) {
/*  80 */     return addBars(paramFrequencyTable, paramColor, paramColor, paramStroke, false);
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
/*     */   public PlotObject addBars(FrequencyTable paramFrequencyTable, Color paramColor, Paint paramPaint, Stroke paramStroke, boolean paramBoolean) {
/* 106 */     int i = paramFrequencyTable.getNumberOfBins();
/* 107 */     int j = 2 + i + i;
/* 108 */     VPoint[] arrayOfVPoint = new VPoint[j];
/* 109 */     byte b1 = 0;
/* 110 */     double d = 0.0D;
/* 111 */     arrayOfVPoint[0] = new VPoint(paramFrequencyTable.getBoundary(0), 0.0D);
/*     */     byte b2;
/* 113 */     for (b2 = 0; b2 < i; b2++) {
/*     */       
/* 115 */       switch (this.type) {
/*     */         case 0:
/* 117 */           d = paramFrequencyTable.getFrequency(b2); break;
/* 118 */         case 1: d = paramFrequencyTable.getProportion(b2); break;
/* 119 */         case 2: d = paramFrequencyTable.getNormalizedFrequency(b2); break;
/*     */       } 
/* 121 */       arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), d);
/* 122 */       arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2 + 1), d);
/*     */     } 
/* 124 */     arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), 0.0D);
/* 125 */     PlotShape plotShape = new PlotShape((Shape)new VPolygon(arrayOfVPoint), paramColor, paramStroke, paramPaint, paramBoolean);
/*     */     
/* 127 */     addObject(plotShape);
/* 128 */     repaint();
/* 129 */     return plotShape;
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
/*     */   public static int doaneBins(int paramInt) {
/* 147 */     if (paramInt < 1)
/* 148 */       throw new IllegalArgumentException("Too few observations."); 
/* 149 */     return (int)Math.round(3.0D * Maths.log10(paramInt) * Maths.log2(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 157 */     return this.type;
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
/*     */   public static int scottBins(int paramInt) {
/* 173 */     if (paramInt < 1)
/* 174 */       throw new IllegalArgumentException("Too few observations."); 
/* 175 */     return (int)Math.round(Math.pow(paramInt, 0.3333333333333333D) / 0.6D);
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
/*     */   public static double scottBinWidth(int paramInt, double paramDouble) {
/* 195 */     if (paramInt < 1 || paramDouble <= 0.0D)
/* 196 */       throw new IllegalArgumentException("Cannot calculate class interval."); 
/* 197 */     return 3.49D * paramDouble * Math.pow(paramInt, -0.3333333333333333D);
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
/*     */   public static double scottBinWidth(int paramInt, double paramDouble1, double paramDouble2) {
/* 217 */     if (paramInt < 1 || paramDouble1 >= paramDouble2)
/* 218 */       throw new IllegalArgumentException("Cannot calculate class interval."); 
/* 219 */     return 0.6D * (paramDouble2 - paramDouble1) * Math.pow(paramInt, -0.3333333333333333D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int paramInt) {
/* 230 */     if (paramInt == 0 || paramInt == 1 || paramInt == 2) {
/* 231 */       this.type = paramInt;
/*     */     } else {
/* 233 */       throw new IllegalArgumentException("Invalid histogram type.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/Histogram2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */