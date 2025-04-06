/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import javax.swing.JFrame;
/*     */ import jsc.ci.ConfidenceBand;
/*     */ import jsc.distributions.Beta;
/*     */ import jsc.distributions.Distribution;
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.goodnessfit.SampleDistributionFunction;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.plot.PlotFunction;
/*     */ import jsc.swt.virtualgraphics.Polyline;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.tests.H1;
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
/*     */ public class CdfPlot
/*     */   extends FunctionPlot2D
/*     */   implements PlotFunction, Cloneable
/*     */ {
/*     */   private Distribution F;
/*     */   
/*     */   public CdfPlot(String paramString, AxisModel paramAxisModel) {
/*  44 */     super(paramAxisModel, (AxisModel)new LinearAxisModel("CDF", new Scale(0.0D, 1.0D, 11, false), "#.#"), paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CdfPlot(String paramString, AxisModel paramAxisModel1, AxisModel paramAxisModel2) {
/*  53 */     super(paramAxisModel1, paramAxisModel2, paramString);
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
/*     */   public PlotObject addCdf(Distribution paramDistribution, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
/*  72 */     this.F = paramDistribution;
/*  73 */     return addFunction(this, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke);
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
/*     */   public PlotObject addCdf(Distribution paramDistribution, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
/*  91 */     int i = 1 + paramInt2 - paramInt1;
/*  92 */     double[] arrayOfDouble1 = new double[i];
/*  93 */     double[] arrayOfDouble2 = new double[i];
/*  94 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  96 */       arrayOfDouble1[b] = (paramInt1 + b);
/*     */       
/*  98 */       arrayOfDouble2[b] = paramDistribution.cdf(arrayOfDouble1[b]);
/*     */     } 
/*     */     
/* 101 */     return addStepFunction(arrayOfDouble1, arrayOfDouble2, true, paramColor, paramStroke);
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
/*     */   public PlotObject[] addConfidenceBand(ConfidenceBand paramConfidenceBand, Color paramColor, Stroke paramStroke) {
/* 121 */     int i = paramConfidenceBand.getN();
/* 122 */     double[] arrayOfDouble1 = new double[i];
/* 123 */     double[] arrayOfDouble2 = new double[i];
/* 124 */     double[] arrayOfDouble3 = new double[i];
/* 125 */     PlotObject[] arrayOfPlotObject = new PlotObject[2];
/*     */     
/* 127 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 129 */       arrayOfDouble1[b] = paramConfidenceBand.getX(b);
/* 130 */       arrayOfDouble2[b] = paramConfidenceBand.getLowerLimit(b);
/* 131 */       arrayOfDouble3[b] = paramConfidenceBand.getUpperLimit(b);
/*     */     } 
/* 133 */     arrayOfPlotObject[0] = addStepFunction(arrayOfDouble1, arrayOfDouble2, false, paramColor, paramStroke);
/* 134 */     arrayOfPlotObject[1] = addStepFunction(arrayOfDouble1, arrayOfDouble3, false, paramColor, paramStroke);
/*     */ 
/*     */     
/* 137 */     return arrayOfPlotObject;
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
/*     */   public PlotObject addSampleDistributionFunction(SampleDistributionFunction paramSampleDistributionFunction, Color paramColor, Stroke paramStroke) {
/* 151 */     return addStepFunction(paramSampleDistributionFunction.getOrderedX(), paramSampleDistributionFunction.getOrderedS(), true, paramColor, paramStroke);
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
/*     */   public PlotObject addStepFunction(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, Color paramColor, Stroke paramStroke) {
/*     */     byte b;
/* 175 */     int i = paramArrayOfdouble1.length;
/* 176 */     int j = paramBoolean ? (2 * i + 1) : (2 * i - 1);
/*     */ 
/*     */     
/* 179 */     if (paramArrayOfdouble2.length != i) {
/* 180 */       throw new IllegalArgumentException("Arrays not equal length.");
/*     */     }
/* 182 */     VPoint[] arrayOfVPoint = new VPoint[j];
/* 183 */     if (paramBoolean) {
/*     */       
/* 185 */       arrayOfVPoint[0] = new VPoint(paramArrayOfdouble1[0], 0.0D);
/* 186 */       b = 0;
/*     */     } else {
/*     */       
/* 189 */       b = -1;
/*     */     } 
/* 191 */     for (byte b1 = 0; b1 < i - 1; b1++) {
/*     */       
/* 193 */       double d = paramArrayOfdouble2[b1];
/* 194 */       arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[b1], d);
/* 195 */       arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[b1 + 1], d);
/*     */     } 
/* 197 */     arrayOfVPoint[++b] = new VPoint(paramArrayOfdouble1[i - 1], paramArrayOfdouble2[i - 1]);
/* 198 */     if (paramBoolean) {
/* 199 */       arrayOfVPoint[++b] = new VPoint(this.horizontalAxis.getMax(), paramArrayOfdouble2[i - 1]);
/*     */     }
/*     */     
/* 202 */     PlotShape plotShape = new PlotShape((Shape)new Polyline(arrayOfVPoint), paramColor, paramStroke);
/* 203 */     addObject(plotShape);
/* 204 */     repaint();
/* 205 */     return plotShape;
/*     */   }
/*     */   public Object clone() {
/* 208 */     return super.clone();
/*     */   }
/*     */   public double getOrdinate(double paramDouble) {
/*     */     
/* 212 */     try { return this.F.cdf(paramDouble); }
/* 213 */     catch (IllegalArgumentException illegalArgumentException) { return Double.NaN; }
/*     */   
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
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 270 */       char c = 'ᎈ';
/* 271 */       Beta beta = new Beta(0.5D, 0.1D);
/* 272 */       double d1 = 0.0D, d2 = 1.0D;
/* 273 */       Scale scale = new Scale(0.0D, 1.0D, 11, false);
/* 274 */       double[] arrayOfDouble = new double[c];
/* 275 */       for (byte b = 0; b < c; ) { arrayOfDouble[b] = beta.random(); b++; }
/*     */       
/* 277 */       JFrame jFrame = new JFrame("CDF plot");
/*     */       
/* 279 */       LinearAxisModel linearAxisModel = new LinearAxisModel("x", scale, "##.#");
/* 280 */       CdfPlot cdfPlot = new CdfPlot("Cdf plot", (AxisModel)linearAxisModel);
/*     */       
/* 282 */       cdfPlot.setPreferredSize(new Dimension(500, 400));
/* 283 */       cdfPlot.addSampleDistributionFunction(new SampleDistributionFunction(arrayOfDouble), Color.black, new BasicStroke(1.0F));
/*     */ 
/*     */       
/* 286 */       cdfPlot.addCdf((Distribution)beta, d1, d2, c, Color.red, new DashStroke(1.0F));
/*     */ 
/*     */       
/* 289 */       cdfPlot.setZoomable(true);
/*     */       
/* 291 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble, (Distribution)beta, H1.NOT_EQUAL, false);
/* 292 */       double d3 = kolmogorovTest.xOfD();
/* 293 */       System.out.println("D = " + kolmogorovTest.getTestStatistic() + " x = " + d3 + " SP = " + kolmogorovTest.getSP());
/* 294 */       cdfPlot.addVerticalLine(d3, Color.blue, new DashStroke(1.0F));
/* 295 */       Container container = jFrame.getContentPane();
/* 296 */       container.setLayout(new BorderLayout());
/* 297 */       container.add(cdfPlot, "Center");
/*     */       
/* 299 */       jFrame.setVisible(true);
/* 300 */       jFrame.setDefaultCloseOperation(3);
/* 301 */       jFrame.pack();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/CdfPlot.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */