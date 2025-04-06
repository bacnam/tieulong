/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.virtualgraphics.VLine;
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
/*     */ public class HorizontalAxis
/*     */   extends Axis
/*     */ {
/*     */   double ordinate;
/*     */   
/*     */   public HorizontalAxis() {
/*  32 */     this((AxisModel)new LinearAxisModel(), 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalAxis(AxisModel paramAxisModel, double paramDouble) {
/*  43 */     this(paramAxisModel, paramDouble, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT);
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
/*     */   public HorizontalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor, Stroke paramStroke, int paramInt, Font paramFont1, Font paramFont2) {
/*  61 */     this(paramAxisModel, paramDouble, paramColor, paramStroke, paramInt, paramStroke, paramColor, paramFont1, paramColor, paramFont2);
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
/*     */   public HorizontalAxis(AxisModel paramAxisModel, double paramDouble, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2) {
/*  83 */     super(paramAxisModel);
/*  84 */     int i = 2 + paramInt + paramFont2.getSize();
/*     */ 
/*     */ 
/*     */     
/*  88 */     this.ordinate = paramDouble;
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.axisLine = new PlotShape((Shape)new VLine((float)paramAxisModel.getTickValue(0), paramDouble, (float)paramAxisModel.getTickValue(this.tickCount - 1), paramDouble), paramColor1, paramStroke1);
/*     */ 
/*     */ 
/*     */     
/*  96 */     for (byte b = 0; b < this.tickCount; b++) {
/*     */       
/*  98 */       VPoint vPoint = new VPoint(paramAxisModel.getTickValue(b), paramDouble);
/*  99 */       this.tickMarks[b] = new VerticalTickMark(vPoint, paramInt, paramColor1, paramStroke2);
/* 100 */       this.tickLabels[b] = new PlotText(paramAxisModel.getTickLabel(b), vPoint, 2, paramInt, paramColor3, paramFont2);
/*     */     } 
/*     */     
/* 103 */     this.axisLabel = new PlotText(paramAxisModel.getLabel(), new VPoint(0.5D * (paramAxisModel.getTickValue(0) + paramAxisModel.getTickValue(this.tickCount - 1)), paramDouble), 2, i, paramColor2, paramFont1);
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
/*     */   public HorizontalAxis setModel(AxisModel paramAxisModel) {
/* 117 */     return new HorizontalAxis(paramAxisModel, this.ordinate, getAxisColour(), getAxisStroke(), getTickSize(), getTickStroke(), getAxisLabelColour(), getAxisLabelFont(), getTickLabelColour(), getTickLabelFont());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/HorizontalAxis.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */