/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import javax.swing.JFrame;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.plot.PiAxisModel;
/*     */ import jsc.swt.plot.PlotFunction;
/*     */ import jsc.swt.virtualgraphics.Path;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionPlot2D
/*     */   extends AxesPlot
/*     */ {
/*     */   public FunctionPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
/*  42 */     super(paramAxisModel1, paramAxisModel2, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FunctionPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
/*  53 */     super(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramString);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, int paramInt, Color paramColor) {
/*  69 */     return addFunction(paramPlotFunction, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, PlotShape.defaultStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, int paramInt, Color paramColor, Stroke paramStroke) {
/*  85 */     return addFunction(paramPlotFunction, this.horizontalAxis.getMin(), this.horizontalAxis.getMax(), paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor) {
/* 103 */     return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, false);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke) {
/* 121 */     return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, paramStroke, PlotShape.defaultPaint, false);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, boolean paramBoolean) {
/* 141 */     return addFunction(paramPlotFunction, paramDouble1, paramDouble2, paramInt, paramColor, PlotShape.defaultStroke, paramColor, paramBoolean);
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
/*     */   public PlotShape addFunction(PlotFunction paramPlotFunction, double paramDouble1, double paramDouble2, int paramInt, Color paramColor, Stroke paramStroke, Paint paramPaint, boolean paramBoolean) {
/*     */     PlotShape plotShape;
/* 165 */     double d = (paramDouble2 - paramDouble1) / (paramInt - 1.0D);
/* 166 */     Path path = new Path(paramInt);
/* 167 */     boolean bool = true;
/* 168 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 170 */       double d1 = paramDouble1 + b * d;
/* 171 */       double d2 = paramPlotFunction.getOrdinate(d1);
/* 172 */       if (Double.isNaN(d2)) {
/* 173 */         bool = true;
/*     */       
/*     */       }
/* 176 */       else if (bool) {
/*     */         
/* 178 */         path.moveTo(d1, d2);
/* 179 */         bool = false;
/*     */       } else {
/*     */         
/* 182 */         path.lineTo(d1, d2);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 187 */     if (paramBoolean) {
/*     */       
/* 189 */       path.lineTo(paramDouble2, 0.0D);
/* 190 */       path.lineTo(paramDouble1, 0.0D);
/* 191 */       path.closePath();
/* 192 */       plotShape = new PlotShape((Shape)path, paramPaint);
/*     */     } else {
/*     */       
/* 195 */       plotShape = new PlotShape((Shape)path, paramColor, paramStroke);
/*     */     } 
/* 197 */     addObject(plotShape);
/* 198 */     repaint();
/* 199 */     return plotShape;
/*     */   }
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     static class Cos
/*     */       implements PlotFunction
/*     */     {
/*     */       public double getOrdinate(double param2Double) {
/* 209 */         return Math.cos(param2Double);
/*     */       } }
/*     */     static class Sin implements PlotFunction { public double getOrdinate(double param2Double) {
/* 212 */         return Math.sin(param2Double);
/*     */       } }
/*     */     
/*     */     public static void main(String[] param1ArrayOfString) {
/* 216 */       byte b = 100;
/*     */       
/* 218 */       JFrame jFrame = new JFrame("FunctionPlot2D Test");
/*     */ 
/*     */ 
/*     */       
/* 222 */       PiAxisModel piAxisModel = new PiAxisModel("Angle", -5, 5, 2);
/* 223 */       LinearAxisModel linearAxisModel = new LinearAxisModel("", new Scale(-1.0D, 1.0D, 3, false), "##.#");
/* 224 */       FunctionPlot2D functionPlot2D = new FunctionPlot2D((AxisModel)piAxisModel, (AxisModel)linearAxisModel, 0.0D, 0.0D, "Plot of sine and cosine functions");
/* 225 */       functionPlot2D.setZoomable(true);
/*     */       
/* 227 */       functionPlot2D.setFocusable(true);
/*     */       
/* 229 */       functionPlot2D.addFunction(new Sin(), b, Color.blue);
/* 230 */       functionPlot2D.addFunction(new Cos(), b, Color.red);
/*     */       
/* 232 */       Container container = jFrame.getContentPane();
/* 233 */       container.setLayout(new BorderLayout());
/* 234 */       container.add(functionPlot2D, "Center");
/*     */       
/* 236 */       jFrame.setVisible(true);
/* 237 */       jFrame.setDefaultCloseOperation(3);
/* 238 */       jFrame.pack();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/FunctionPlot2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */