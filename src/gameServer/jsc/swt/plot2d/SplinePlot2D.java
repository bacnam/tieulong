/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Stroke;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import jsc.numerical.Function;
/*     */ import jsc.numerical.Spline;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.BeadSlider;
/*     */ import jsc.swt.plot.PlotFunction;
/*     */ import jsc.swt.virtualgraphics.VDimension;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.swt.virtualgraphics.VRectangle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SplinePlot2D
/*     */   extends FunctionPlot2D
/*     */   implements Function
/*     */ {
/*     */   int m;
/*     */   int beadSize;
/*     */   double a;
/*     */   double b;
/*     */   private Spline spline;
/*     */   boolean cutOff = false;
/*     */   double minorTickGap;
/*     */   double majorTickGap;
/*     */   private double[] x;
/*     */   private double[] y;
/*     */   SplineFunc splineFunc;
/*     */   boolean changed = false;
/*     */   int n;
/*     */   Color curveColour;
/*     */   Stroke stroke;
/*     */   Paint paint;
/*     */   boolean filled;
/*     */   PlotObject po;
/*     */   
/*     */   public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
/*  94 */     super(paramAxisModel1, paramAxisModel2, paramString);
/*  95 */     this.m = paramInt1;
/*  96 */     this.beadSize = paramInt2;
/*     */     
/*  98 */     if (paramDouble2 <= paramDouble1)
/*  99 */       throw new IllegalArgumentException("Invalid spline interval."); 
/* 100 */     this.a = paramDouble1; this.b = paramDouble2;
/* 101 */     double d1 = (this.b - this.a) / (paramInt1 - 1.0D);
/*     */     
/* 103 */     double d2 = paramAxisModel2.getMin();
/* 104 */     double d3 = paramAxisModel2.getMax();
/* 105 */     double d4 = paramAxisModel2.getLength();
/* 106 */     this.minorTickGap = d4 / 100.0D;
/* 107 */     this.majorTickGap = d4 / 10.0D;
/* 108 */     this.x = new double[paramInt1];
/* 109 */     this.y = new double[paramInt1];
/* 110 */     SliderListener sliderListener = new SliderListener(this);
/*     */     
/* 112 */     VDimension vDimension = new VDimension(d1, d4);
/* 113 */     for (byte b = 0; b < paramInt1; b++) {
/*     */       
/* 115 */       this.x[b] = this.a + b * d1;
/* 116 */       this.y[b] = paramArrayOfdouble[b];
/* 117 */       BeadSlider beadSlider = new BeadSlider(b, d2, d3, paramArrayOfdouble[b], this.minorTickGap, this.majorTickGap, paramColor1);
/* 118 */       beadSlider.addChangeListener(sliderListener);
/* 119 */       addComponent((Component)beadSlider, new VPoint(this.x[b] - 0.5D * d1, d2), vDimension);
/*     */     } 
/*     */ 
/*     */     
/* 123 */     this.spline = new Spline(paramInt1, this.x, paramArrayOfdouble);
/* 124 */     this.splineFunc = new SplineFunc(this);
/* 125 */     this.n = paramInt3;
/* 126 */     this.curveColour = paramColor2;
/* 127 */     this.stroke = paramStroke;
/* 128 */     this.paint = paramPaint;
/* 129 */     this.filled = paramBoolean;
/* 130 */     this.po = addFunction(this.splineFunc, this.a, this.b, paramInt3, paramColor2, paramStroke, paramPaint, paramBoolean);
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
/*     */   public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2, Stroke paramStroke) {
/* 157 */     this(paramString, paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramInt1, paramArrayOfdouble, paramInt2, paramColor1, paramInt3, paramColor2, paramStroke, PlotShape.defaultPaint, false);
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
/*     */   public SplinePlot2D(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, int paramInt1, double[] paramArrayOfdouble, int paramInt2, Color paramColor1, int paramInt3, Color paramColor2) {
/* 182 */     this(paramString, paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramInt1, paramArrayOfdouble, paramInt2, paramColor1, paramInt3, paramColor2, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChangeListener(ChangeListener paramChangeListener) {
/* 193 */     for (byte b = 0; b < this.m; b++) {
/*     */       
/* 195 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/* 196 */       beadSlider.addChangeListener(paramChangeListener);
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
/*     */   public double function(double paramDouble) {
/* 208 */     updateSpline();
/* 209 */     return this.spline.splint(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeadSlider getSlider(int paramInt) {
/* 220 */     return (BeadSlider)getComponent(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spline getSpline() {
/* 229 */     updateSpline();
/* 230 */     return this.spline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getX(int paramInt) {
/* 239 */     return this.spline.getX(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getY(int paramInt) {
/* 247 */     return this.spline.getY(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getY(double paramDouble) {
/* 255 */     return this.spline.splint(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChanged() {
/* 263 */     return this.changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 271 */     super.paintComponent(paramGraphics);
/* 272 */     VDimension vDimension = pixelToVirtual(new Dimension(this.beadSize, this.beadSize));
/*     */     
/* 274 */     double d1 = vDimension.width;
/* 275 */     double d2 = 0.5D * d1;
/*     */ 
/*     */ 
/*     */     
/* 279 */     for (byte b = 0; b < this.m; b++) {
/*     */ 
/*     */       
/* 282 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/* 283 */       setComponentBounds(b, new VRectangle(this.x[b] - d2, beadSlider.getMinimumValue(), d1, beadSlider.getMaximumValue() - beadSlider.getMinimumValue()));
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
/*     */   public void setBeads(AxisModel paramAxisModel, double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
/* 303 */     if (paramArrayOfdouble.length != this.m) {
/* 304 */       throw new IllegalArgumentException("Array has incorrect length.");
/*     */     }
/* 306 */     if (paramDouble2 <= paramDouble1)
/* 307 */       throw new IllegalArgumentException("Invalid spline interval."); 
/* 308 */     this.a = paramDouble1; this.b = paramDouble2;
/* 309 */     double d = (this.b - this.a) / (this.m - 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     for (byte b = 0; b < this.m; b++) {
/*     */       
/* 316 */       this.x[b] = this.a + b * d;
/* 317 */       this.y[b] = paramArrayOfdouble[b];
/* 318 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/* 319 */       beadSlider.setRealValue(paramArrayOfdouble[b]);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     rescaleHorizontal(paramAxisModel);
/* 329 */     this.spline = new Spline(this.m, this.x, paramArrayOfdouble);
/*     */     
/* 331 */     removeObject(this.po);
/* 332 */     this.po = addFunction(this.splineFunc, this.a, this.b, this.n, this.curveColour, this.stroke, this.paint, this.filled);
/* 333 */     this.changed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChanged(boolean paramBoolean) {
/* 341 */     this.changed = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCutOff(boolean paramBoolean) {
/* 351 */     this.cutOff = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocusColour(Color paramColor) {
/* 360 */     for (byte b = 0; b < this.m; b++) {
/*     */       
/* 362 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/* 363 */       beadSlider.setFocusColour(paramColor);
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
/*     */ 
/*     */   
/*     */   public void setSliderY(double paramDouble1, double paramDouble2, double[] paramArrayOfdouble) {
/* 385 */     double[] arrayOfDouble1 = new double[this.m];
/* 386 */     double[] arrayOfDouble2 = new double[this.m];
/* 387 */     for (byte b = 0; b < this.m; ) { arrayOfDouble1[b] = paramDouble1; arrayOfDouble2[b] = paramDouble2; b++; }
/* 388 */      setSliderY(arrayOfDouble1, arrayOfDouble2, paramArrayOfdouble);
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
/*     */   public void setSliderY(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double[] paramArrayOfdouble3) {
/* 412 */     if (paramArrayOfdouble1.length != this.m || paramArrayOfdouble2.length != this.m || paramArrayOfdouble3.length != this.m) {
/* 413 */       throw new IllegalArgumentException("Array has incorrect length.");
/*     */     }
/* 415 */     for (byte b = 0; b < this.m; b++) {
/*     */ 
/*     */       
/* 418 */       this.y[b] = paramArrayOfdouble3[b];
/* 419 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/*     */       
/* 421 */       if (paramArrayOfdouble1[b] >= paramArrayOfdouble2[b])
/* 422 */         throw new IllegalArgumentException("Invalid slider ordinates."); 
/* 423 */       beadSlider.rescale(paramArrayOfdouble1[b], paramArrayOfdouble2[b], paramArrayOfdouble3[b], this.minorTickGap, this.majorTickGap);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 430 */     this.spline = new Spline(this.m, this.x, paramArrayOfdouble3);
/*     */     
/* 432 */     removeObject(this.po);
/* 433 */     this.po = addFunction(this.splineFunc, this.a, this.b, this.n, this.curveColour, this.stroke, this.paint, this.filled);
/* 434 */     this.changed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateSpline() {
/* 441 */     for (byte b = 0; b < this.m; b++) {
/*     */       
/* 443 */       BeadSlider beadSlider = (BeadSlider)getComponent(b);
/* 444 */       this.y[b] = beadSlider.getRealValue();
/*     */     } 
/*     */ 
/*     */     
/* 448 */     this.spline = new Spline(this.m, this.x, this.y);
/*     */   }
/*     */   class SliderListener implements ChangeListener { SliderListener(SplinePlot2D this$0) {
/* 451 */       this.this$0 = this$0;
/*     */     }
/*     */     private final SplinePlot2D this$0;
/*     */     public void stateChanged(ChangeEvent param1ChangeEvent) {
/* 455 */       this.this$0.updateSpline();
/* 456 */       this.this$0.removeObject(this.this$0.po);
/* 457 */       this.this$0.po = this.this$0.addFunction(this.this$0.splineFunc, this.this$0.a, this.this$0.b, this.this$0.n, this.this$0.curveColour, this.this$0.stroke, this.this$0.paint, this.this$0.filled);
/* 458 */       this.this$0.changed = true;
/*     */     } }
/*     */   class SplineFunc implements PlotFunction { private final SplinePlot2D this$0;
/*     */     SplineFunc(SplinePlot2D this$0) {
/* 462 */       this.this$0 = this$0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public double getOrdinate(double param1Double) {
/* 468 */       if (this.this$0.cutOff) {
/* 469 */         return Math.max(this.this$0.verticalAxis.getMin(), this.this$0.spline.splint(param1Double));
/*     */       }
/* 471 */       return this.this$0.spline.splint(param1Double);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/SplinePlot2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */