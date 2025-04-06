/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Point2D;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.virtualgraphics.VirtualTransform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Axis
/*     */   implements PlotObject
/*     */ {
/*  26 */   static final Font DEFAULT_AXIS_LABEL_FONT = new Font("SansSerif", 0, 14);
/*  27 */   static final Color DEFAULT_COLOUR = Color.black;
/*  28 */   static final Stroke DEFAULT_STROKE = new BasicStroke();
/*  29 */   static final Font DEFAULT_TICK_LABEL_FONT = new Font("Monospaced", 0, 12);
/*     */ 
/*     */ 
/*     */   
/*     */   protected AxisModel axisModel;
/*     */ 
/*     */   
/*     */   protected int tickCount;
/*     */ 
/*     */   
/*     */   protected boolean paintLabels = true;
/*     */ 
/*     */   
/*     */   protected PlotText axisLabel;
/*     */ 
/*     */   
/*     */   protected PlotShape axisLine;
/*     */ 
/*     */   
/*     */   protected PlotText[] tickLabels;
/*     */ 
/*     */   
/*     */   protected TickMark[] tickMarks;
/*     */ 
/*     */ 
/*     */   
/*     */   public Axis() {
/*  56 */     this((AxisModel)new LinearAxisModel());
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
/*     */   public Axis(AxisModel paramAxisModel) {
/*  77 */     this.tickCount = paramAxisModel.getTickCount();
/*     */     
/*  79 */     this.axisModel = (AxisModel)paramAxisModel.clone();
/*     */     
/*  81 */     this.tickLabels = new PlotText[this.tickCount];
/*  82 */     this.tickMarks = new TickMark[this.tickCount];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
/*  92 */     this.axisLine.draw(paramGraphics2D, paramVirtualTransform);
/*     */ 
/*     */     
/*  95 */     for (byte b = 0; b < this.tickCount; b++) {
/*     */       
/*  97 */       this.tickMarks[b].draw(paramGraphics2D, paramVirtualTransform);
/*     */       
/*  99 */       if (this.paintLabels) this.tickLabels[b].draw(paramGraphics2D, paramVirtualTransform);
/*     */     
/*     */     } 
/*     */     
/* 103 */     this.axisLabel.draw(paramGraphics2D, paramVirtualTransform);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getAxisColour() {
/* 111 */     return this.axisLine.colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotText getAxisLabel() {
/* 118 */     return this.axisLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getAxisLabelColour() {
/* 125 */     return this.axisLabel.colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font getAxisLabelFont() {
/* 132 */     return this.axisLabel.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotShape getAxisLine() {
/* 139 */     return this.axisLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stroke getAxisStroke() {
/* 146 */     return this.axisLine.stroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMin() {
/* 153 */     return this.axisModel.getMin();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMax() {
/* 160 */     return this.axisModel.getMax();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AxisModel getModel() {
/* 167 */     return this.axisModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTickCount() {
/* 176 */     return this.tickCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotText getTickLabel(int paramInt) {
/* 185 */     return this.tickLabels[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getTickLabelColour() {
/* 192 */     return (this.tickLabels[0]).colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font getTickLabelFont() {
/* 199 */     return (this.tickLabels[0]).font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TickMark getTickMark(int paramInt) {
/* 208 */     return this.tickMarks[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTickSize() {
/* 215 */     return (this.tickMarks[0]).size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stroke getTickStroke() {
/* 222 */     return (this.tickMarks[0]).stroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaintLabels(boolean paramBoolean) {
/* 229 */     this.paintLabels = paramBoolean;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/Axis.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */