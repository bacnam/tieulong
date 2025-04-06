/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import jsc.descriptive.CategoricalTally;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.virtualgraphics.LineBars;
/*     */ import jsc.swt.virtualgraphics.RectangularBars;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VerticalBarChart
/*     */   extends LabelledXaxisPlot
/*     */ {
/*     */   public static final int FREQUENCY = 0;
/*     */   public static final int PROPORTION = 1;
/*     */   public static final int PERCENTAGE = 2;
/*  34 */   int type = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalBarChart(String paramString1, String paramString2, String[] paramArrayOfString, Color paramColor, Font paramFont, AxisModel paramAxisModel) {
/*  55 */     super(paramString1, paramString2, paramArrayOfString, paramColor, paramFont, paramAxisModel);
/*  56 */     setAntialiasing(false);
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
/*     */   public PlotObject addBars(CategoricalTally paramCategoricalTally, float paramFloat, Color paramColor, Paint paramPaint, boolean paramBoolean) {
/*     */     PlotShape plotShape;
/*  86 */     int i = paramCategoricalTally.getNumberOfBins();
/*  87 */     if (i != this.labelCount)
/*  88 */       throw new IllegalArgumentException("Number of frequencies not equal to number of labels."); 
/*  89 */     VPoint[] arrayOfVPoint = new VPoint[i];
/*     */     
/*  91 */     double d = 0.0D;
/*  92 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  94 */       switch (this.type) {
/*     */         case 0:
/*  96 */           d = paramCategoricalTally.getFrequency(b); break;
/*  97 */         case 1: d = paramCategoricalTally.getProportion(b); break;
/*  98 */         case 2: d = paramCategoricalTally.getPercentage(b); break;
/*     */       } 
/* 100 */       arrayOfVPoint[b] = new VPoint((b + 1), d);
/*     */     } 
/* 102 */     if (paramBoolean) {
/* 103 */       plotShape = new PlotShape((Shape)new LineBars(arrayOfVPoint), paramColor, new BasicStroke(paramFloat, 0, 1));
/*     */     } else {
/* 105 */       plotShape = new PlotShape((Shape)new RectangularBars(arrayOfVPoint, paramFloat), paramPaint);
/*     */     } 
/* 107 */     addObject(plotShape);
/* 108 */     repaint();
/* 109 */     return plotShape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 117 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int paramInt) {
/* 127 */     if (paramInt == 0 || paramInt == 1 || paramInt == 2) {
/* 128 */       this.type = paramInt;
/*     */     } else {
/* 130 */       throw new IllegalArgumentException("Invalid bar chart type.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/VerticalBarChart.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */