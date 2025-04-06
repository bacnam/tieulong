/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.virtualgraphics.Polyline;
/*     */ import jsc.swt.virtualgraphics.VLine;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.util.Scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProportionPlot2D
/*     */   extends AxesPlot
/*     */ {
/*     */   int numberOfTrials;
/*     */   double targetProportion;
/*     */   int trialCount;
/*     */   Polyline polyline;
/*     */   PlotObject curve;
/*  36 */   PlotObject line = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Color curveColour;
/*     */ 
/*     */ 
/*     */   
/*     */   Stroke lineStroke;
/*     */ 
/*     */ 
/*     */   
/*     */   Color lineColour;
/*     */ 
/*     */ 
/*     */   
/*     */   Stroke curveStroke;
/*     */ 
/*     */ 
/*     */   
/*     */   String xTitle;
/*     */ 
/*     */ 
/*     */   
/*     */   int xTicks;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProportionPlot2D(String paramString1, String paramString2, int paramInt1, String paramString3, int paramInt2, int paramInt3, Color paramColor1, Stroke paramStroke1, double paramDouble, Color paramColor2, Stroke paramStroke2) {
/*  67 */     super((AxisModel)new LinearAxisModel(paramString2, new Scale(0.0D, paramInt3, paramInt1, false), "##"), (AxisModel)new LinearAxisModel(paramString3, new Scale(0.0D, 1.0D, paramInt2, false), "#.##"), paramString1);
/*     */     
/*  69 */     this.curveColour = paramColor1;
/*  70 */     this.curveStroke = paramStroke1;
/*  71 */     this.lineColour = paramColor2;
/*  72 */     this.lineStroke = paramStroke2;
/*     */     
/*  74 */     this.numberOfTrials = paramInt3;
/*  75 */     this.xTitle = paramString2;
/*  76 */     this.xTicks = paramInt1;
/*  77 */     this.targetProportion = paramDouble;
/*     */     
/*  79 */     setAntialiasing(false);
/*  80 */     setPreferredSize(new Dimension(400, 300));
/*  81 */     setTargetProportion(paramDouble);
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
/*     */   public double addProportion(int paramInt) {
/* 102 */     if (this.trialCount >= this.numberOfTrials) return -1.0D;
/*     */     
/* 104 */     this.trialCount++;
/* 105 */     double d = paramInt / this.trialCount;
/* 106 */     if (d < 0.0D || d > 1.0D) {
/*     */       
/* 108 */       System.out.println("numberOfSuccesses = " + paramInt + " trialCount = " + this.trialCount + " proportion = " + d);
/* 109 */       throw new IllegalArgumentException("Invalid number of successes.");
/*     */     } 
/* 111 */     if (this.trialCount == 1) {
/*     */       
/* 113 */       VPoint vPoint = new VPoint(1.0D, d);
/* 114 */       this.polyline = new Polyline(vPoint, this.numberOfTrials + 1);
/*     */       
/* 116 */       addObject(new StandardMarker(vPoint, 4, 5, this.curveColour));
/*     */     }
/*     */     else {
/*     */       
/* 120 */       this.polyline.lineTo(new VPoint(this.trialCount, d));
/*     */ 
/*     */       
/* 123 */       removeObject(this.curve);
/* 124 */       this.curve = new PlotShape((Shape)this.polyline, this.curveColour, this.curveStroke);
/* 125 */       addObject(this.curve);
/*     */     } 
/* 127 */     repaint();
/* 128 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrialCount() {
/* 136 */     return this.trialCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 143 */     this.trialCount = 0;
/*     */ 
/*     */     
/* 146 */     removeAllObjects();
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
/*     */   public void rescale(int paramInt1, int paramInt2) {
/* 163 */     this.numberOfTrials = paramInt1;
/* 164 */     this.xTicks = paramInt2;
/* 165 */     rescaleHorizontal((AxisModel)new LinearAxisModel(this.xTitle, new Scale(0.0D, paramInt1, paramInt2, false), "##"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     removeAllObjects();
/* 172 */     setTargetProportion(this.targetProportion);
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
/*     */   public void setTargetProportion(double paramDouble) {
/* 185 */     if (paramDouble > 0.0D && paramDouble < 1.0D) {
/*     */       
/* 187 */       this.line = new PlotShape((Shape)new VLine(new VPoint(0.0D, paramDouble), new VPoint(this.numberOfTrials, paramDouble)), this.lineColour, this.lineStroke);
/*     */       
/* 189 */       addObject(this.line);
/*     */     }
/*     */     else {
/*     */       
/* 193 */       if (this.line != null) removeObject(this.line); 
/* 194 */       this.line = null;
/*     */     } 
/* 196 */     repaint();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/ProportionPlot2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */