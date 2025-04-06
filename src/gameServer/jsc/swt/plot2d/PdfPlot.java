/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import jsc.descriptive.FrequencyTable;
/*     */ import jsc.descriptive.Tally;
/*     */ import jsc.distributions.Distribution;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.PlotFunction;
/*     */ import jsc.swt.virtualgraphics.LineBars;
/*     */ import jsc.swt.virtualgraphics.RectangularBars;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.swt.virtualgraphics.VPolygon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPlot
/*     */   extends FunctionPlot2D
/*     */   implements PlotFunction, Cloneable
/*     */ {
/*     */   Distribution D;
/*     */   
/*     */   public PdfPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
/*  43 */     super(paramAxisModel1, paramAxisModel2, paramString);
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
/*     */   public PlotShape addBars(FrequencyTable paramFrequencyTable, Paint paramPaint) {
/*  57 */     return addBars(paramFrequencyTable, PlotShape.defaultColour, paramPaint, PlotShape.defaultStroke, true);
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
/*     */   public PlotShape addBars(FrequencyTable paramFrequencyTable, Color paramColor, Stroke paramStroke) {
/*  72 */     return addBars(paramFrequencyTable, paramColor, paramColor, paramStroke, false);
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
/*     */   public PlotShape addBars(FrequencyTable paramFrequencyTable, Color paramColor, Paint paramPaint, Stroke paramStroke, boolean paramBoolean) {
/*  91 */     int i = paramFrequencyTable.getNumberOfBins();
/*  92 */     int j = 2 + i + i;
/*  93 */     VPoint[] arrayOfVPoint = new VPoint[j];
/*  94 */     byte b1 = 0;
/*  95 */     double d = 0.0D;
/*  96 */     arrayOfVPoint[0] = new VPoint(paramFrequencyTable.getBoundary(0), 0.0D);
/*     */     byte b2;
/*  98 */     for (b2 = 0; b2 < i; b2++) {
/*     */       
/* 100 */       d = paramFrequencyTable.getNormalizedFrequency(b2);
/* 101 */       arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), d);
/* 102 */       arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2 + 1), d);
/*     */     } 
/* 104 */     arrayOfVPoint[++b1] = new VPoint(paramFrequencyTable.getBoundary(b2), 0.0D);
/* 105 */     PlotShape plotShape = new PlotShape((Shape)new VPolygon(arrayOfVPoint), paramColor, paramStroke, paramPaint, paramBoolean);
/*     */     
/* 107 */     addObject(plotShape);
/* 108 */     repaint();
/* 109 */     return plotShape;
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
/*     */   public PlotShape addBars(Tally paramTally, double paramDouble1, Color paramColor, Paint paramPaint, boolean paramBoolean, double paramDouble2) {
/*     */     PlotShape plotShape;
/* 143 */     int i = paramTally.getNumberOfBins();
/* 144 */     VPoint[] arrayOfVPoint = new VPoint[i];
/*     */     
/* 146 */     for (byte b = 0; b < i; b++) {
/* 147 */       arrayOfVPoint[b] = new VPoint(paramTally.getBinValue(b) + paramDouble2, paramTally.getProportion(b));
/*     */     }
/* 149 */     if (paramBoolean) {
/* 150 */       plotShape = new PlotShape((Shape)new LineBars(arrayOfVPoint), paramColor, new BasicStroke((float)paramDouble1, 0, 1));
/*     */     } else {
/* 152 */       plotShape = new PlotShape((Shape)new RectangularBars(arrayOfVPoint, paramDouble1), paramPaint);
/*     */     } 
/* 154 */     addObject(plotShape);
/* 155 */     repaint();
/* 156 */     return plotShape;
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
/*     */   public PlotShape addPdf(Distribution paramDistribution, int paramInt, Color paramColor) {
/* 171 */     return addPdf(paramDistribution, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addPdf(Distribution paramDistribution, int paramInt, Color paramColor, Stroke paramStroke) {
/* 185 */     return addPdf(paramDistribution, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, boolean paramBoolean) {
/* 207 */     return addPdf(paramDistribution, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, paramBoolean);
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
/*     */   public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
/* 225 */     return addPdf(paramDistribution, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addPdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
/* 250 */     this.D = paramDistribution;
/* 251 */     return addFunction(this, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, paramPaint, paramBoolean);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape addPmf(Distribution paramDistribution, int paramInt1, int paramInt2, double paramDouble, Color paramColor, Paint paramPaint, boolean paramBoolean) {
/* 303 */     return addPmf(paramDistribution, 1 + paramInt2 - paramInt1, paramInt1, 1.0D, paramDouble, paramColor, paramPaint, paramBoolean, 0.0D);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape addPmf(Distribution paramDistribution, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Paint paramPaint, boolean paramBoolean) {
/* 347 */     return addPmf(paramDistribution, paramInt, paramDouble1, paramDouble2, paramDouble3, paramColor, paramPaint, paramBoolean, 0.0D);
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
/*     */   public PlotShape addPmf(Distribution paramDistribution, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Paint paramPaint, boolean paramBoolean, double paramDouble4) {
/*     */     PlotShape plotShape;
/* 385 */     VPoint[] arrayOfVPoint = new VPoint[paramInt];
/*     */ 
/*     */     
/* 388 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 390 */       double d = paramDouble1 + b * paramDouble2;
/*     */       
/* 392 */       arrayOfVPoint[b] = new VPoint(d + paramDouble4, paramDistribution.pdf(d));
/*     */     } 
/* 394 */     if (paramBoolean) {
/* 395 */       plotShape = new PlotShape((Shape)new LineBars(arrayOfVPoint), paramColor, new BasicStroke((float)paramDouble3, 0, 1));
/*     */     } else {
/* 397 */       plotShape = new PlotShape((Shape)new RectangularBars(arrayOfVPoint, paramDouble3), paramPaint);
/*     */     } 
/* 399 */     addObject(plotShape);
/* 400 */     repaint();
/* 401 */     return plotShape;
/*     */   }
/*     */   
/*     */   public double getOrdinate(double paramDouble) {
/*     */     
/* 406 */     try { return this.D.pdf(paramDouble); }
/* 407 */     catch (IllegalArgumentException illegalArgumentException) { return Double.NaN; }
/*     */   
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/PdfPlot.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */