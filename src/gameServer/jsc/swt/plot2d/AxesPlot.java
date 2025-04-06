/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import javax.swing.JFrame;
/*     */ import jsc.swt.plot.AxisModel;
/*     */ import jsc.swt.plot.LinearAxisModel;
/*     */ import jsc.swt.virtualgraphics.VLine;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.swt.virtualgraphics.VRectangle;
/*     */ import jsc.swt.virtualgraphics.VirtualTransform;
/*     */ import jsc.util.Scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AxesPlot
/*     */   extends PlotPanel
/*     */   implements Cloneable
/*     */ {
/*     */   protected PlotText title;
/*     */   protected HorizontalAxis horizontalAxis;
/*     */   protected VerticalAxis verticalAxis;
/*  35 */   static final Font DEFAULT_TITLE_FONT = new Font("SansSerif", 0, 14);
/*  36 */   static final Color DEFAULT_TITLE_COLOUR = Color.black;
/*     */ 
/*     */ 
/*     */   
/*     */   private VRectangle plottingArea;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean paintXaxis = true;
/*     */ 
/*     */   
/*     */   private boolean paintYaxis = true;
/*     */ 
/*     */   
/*     */   private boolean clipping = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
/*  55 */     this(paramAxisModel1, paramAxisModel2, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
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
/*     */   public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
/*  70 */     this(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, Axis.DEFAULT_COLOUR, Axis.DEFAULT_STROKE, 5, Axis.DEFAULT_STROKE, Axis.DEFAULT_COLOUR, Axis.DEFAULT_AXIS_LABEL_FONT, Axis.DEFAULT_COLOUR, Axis.DEFAULT_TICK_LABEL_FONT, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
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
/*     */   public AxesPlot(HorizontalAxis paramHorizontalAxis, VerticalAxis paramVerticalAxis, String paramString) {
/*  84 */     this(paramHorizontalAxis, paramVerticalAxis, paramString, DEFAULT_TITLE_COLOUR, DEFAULT_TITLE_FONT);
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
/*     */   public AxesPlot(HorizontalAxis paramHorizontalAxis, VerticalAxis paramVerticalAxis, String paramString, Color paramColor, Font paramFont) {
/*  98 */     super(new Dimension(300, 300));
/*  99 */     this.horizontalAxis = paramHorizontalAxis;
/* 100 */     this.verticalAxis = paramVerticalAxis;
/* 101 */     setMargins();
/* 102 */     setTitle(paramString, paramColor, paramFont);
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
/*     */   public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2, String paramString, Color paramColor4, Font paramFont3) {
/* 126 */     this(paramAxisModel1, paramAxisModel2, paramAxisModel1.getMin(), paramAxisModel2.getMin(), paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2, paramString, paramColor4, paramFont3);
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
/*     */   public AxesPlot(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, Color paramColor1, Stroke paramStroke1, int paramInt, Stroke paramStroke2, Color paramColor2, Font paramFont1, Color paramColor3, Font paramFont2, String paramString, Color paramColor4, Font paramFont3) {
/* 151 */     this(new HorizontalAxis(paramAxisModel1, paramDouble2, paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2), new VerticalAxis(paramAxisModel2, paramDouble1, paramColor1, paramStroke1, paramInt, paramStroke2, paramColor2, paramFont1, paramColor3, paramFont2), paramString, paramColor4, paramFont3);
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
/*     */   public AxesPlot(String paramString) {
/* 166 */     this(new HorizontalAxis(), new VerticalAxis(), paramString);
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
/*     */   public PlotObject addVerticalLine(double paramDouble1, double paramDouble2, double paramDouble3, Color paramColor, Stroke paramStroke) {
/* 182 */     PlotShape plotShape = new PlotShape((Shape)new VLine(paramDouble1, paramDouble2, paramDouble1, paramDouble3), paramColor, paramStroke);
/* 183 */     addObject(plotShape);
/*     */     
/* 185 */     return plotShape;
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
/*     */   public PlotObject addVerticalLine(double paramDouble, Color paramColor, Stroke paramStroke) {
/* 200 */     return addVerticalLine(paramDouble, this.verticalAxis.getMin(), this.verticalAxis.getMax(), paramColor, paramStroke);
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
/*     */   public Object clone() {
/* 212 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AxesPlot copy() {
/* 222 */     AxesPlot axesPlot = new AxesPlot(this.horizontalAxis, this.verticalAxis, this.title.getString(), this.title.getColour(), this.title.getFont());
/*     */     
/* 224 */     copyContents(axesPlot);
/* 225 */     axesPlot.paintXaxis = this.paintXaxis;
/* 226 */     axesPlot.paintYaxis = this.paintYaxis;
/* 227 */     axesPlot.clipping = this.clipping;
/* 228 */     axesPlot.hints = (RenderingHints)this.hints.clone();
/* 229 */     return axesPlot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotText getTitle() {
/* 237 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String paramString, Color paramColor, Font paramFont) {
/* 246 */     this.title = new PlotText(paramString, new VPoint(this.virtualSpace.x + 0.5D * this.virtualSpace.width, this.virtualSpace.y + this.virtualSpace.height), 2, 3, paramColor, paramFont);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HorizontalAxis getHorizontalAxis() {
/* 256 */     return this.horizontalAxis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalAxis getVerticalAxis() {
/* 263 */     return this.verticalAxis;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean outsideAxes(VPoint paramVPoint) {
/* 328 */     if (paramVPoint.x < this.horizontalAxis.getMin() || paramVPoint.x > this.horizontalAxis.getMax() || paramVPoint.y < this.verticalAxis.getMin() || paramVPoint.y > this.verticalAxis.getMax())
/*     */     {
/* 330 */       return true;
/*     */     }
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 342 */     Graphics2D graphics2D = (Graphics2D)paramGraphics;
/*     */     
/* 344 */     if (this.clipping) {
/*     */       
/* 346 */       paintBackground(paramGraphics);
/* 347 */       this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*     */ 
/*     */ 
/*     */       
/* 351 */       graphics2D.setClip(this.vt.createTransformedShape((Shape)this.plottingArea));
/*     */     } 
/*     */ 
/*     */     
/* 355 */     super.paintComponent(paramGraphics);
/* 356 */     graphics2D.setClip(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     this.horizontalAxis.draw(graphics2D, this.vt);
/* 368 */     this.verticalAxis.draw(graphics2D, this.vt);
/* 369 */     this.title.draw(graphics2D, this.vt);
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
/*     */   public void rescaleHorizontal(AxisModel paramAxisModel) {
/* 389 */     this.horizontalAxis = this.horizontalAxis.setModel(paramAxisModel);
/* 390 */     setMargins();
/* 391 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rescaleVertical(AxisModel paramAxisModel) {
/* 401 */     this.verticalAxis = this.verticalAxis.setModel(paramAxisModel);
/* 402 */     setMargins();
/* 403 */     repaint();
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
/*     */   public void setClipping(boolean paramBoolean) {
/* 422 */     this.clipping = paramBoolean;
/*     */   }
/*     */   
/*     */   private void setMargins() {
/* 426 */     AxisModel axisModel1 = this.horizontalAxis.getModel();
/* 427 */     AxisModel axisModel2 = this.verticalAxis.getModel();
/*     */     
/* 429 */     this.plottingArea = new VRectangle(axisModel1.getMin(), axisModel2.getMin(), axisModel1.getLength(), axisModel2.getLength());
/*     */ 
/*     */     
/* 432 */     this.virtualSpace = new VRectangle(axisModel1.getMin() - 0.15D * axisModel1.getLength(), axisModel2.getMin() - 0.15D * axisModel2.getLength(), axisModel1.getLength() + 0.2D * axisModel1.getLength(), axisModel2.getLength() + 0.25D * axisModel2.getLength());
/*     */     
/* 434 */     this.virtualSpace = new VRectangle(axisModel1.getMin() - 0.15D * axisModel1.getLength(), axisModel2.getMin() - 0.15D * axisModel2.getLength(), axisModel1.getLength() + 0.2D * axisModel1.getLength(), axisModel2.getLength() + 0.25D * axisModel2.getLength());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaintXaxis(boolean paramBoolean) {
/* 443 */     this.paintXaxis = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaintYaxis(boolean paramBoolean) {
/* 450 */     this.paintYaxis = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 460 */       JFrame jFrame = new JFrame("AxesPlot Test");
/*     */ 
/*     */ 
/*     */       
/* 464 */       LinearAxisModel linearAxisModel1 = new LinearAxisModel("X axis label", new Scale(-10.0D, 10.0D, 11, true), "##.#");
/*     */       
/* 466 */       LinearAxisModel linearAxisModel2 = new LinearAxisModel("Y axis label", new Scale(-30.0D, 30.0D, 11, true), "##.#");
/*     */       
/* 468 */       AxesPlot axesPlot = new AxesPlot((AxisModel)linearAxisModel1, (AxisModel)linearAxisModel2, "Title of plot");
/* 469 */       axesPlot.setZoomable(true);
/*     */       
/* 471 */       axesPlot.setFocusable(true);
/*     */ 
/*     */ 
/*     */       
/* 475 */       Container container = jFrame.getContentPane();
/* 476 */       container.setLayout(new BorderLayout());
/* 477 */       container.add(axesPlot, "Center");
/*     */       
/* 479 */       jFrame.setVisible(true);
/* 480 */       jFrame.setDefaultCloseOperation(3);
/* 481 */       jFrame.pack();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/AxesPlot.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */