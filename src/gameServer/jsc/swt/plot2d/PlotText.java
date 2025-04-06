/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import javax.swing.JFrame;
/*     */ import jsc.swt.virtualgraphics.VPoint;
/*     */ import jsc.swt.virtualgraphics.VRectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlotText
/*     */   implements PlotObject
/*     */ {
/*     */   public static final int CENTER = 0;
/*     */   public static final int ABOVE = 1;
/*     */   public static final int BELOW = 2;
/*     */   public static final int LEFT = 3;
/*     */   public static final int RIGHT = 4;
/*  55 */   static Color defaultColour = Color.black;
/*     */ 
/*     */   
/*  58 */   static Font defaultFont = new Font("SansSerif", 0, 12);
/*     */ 
/*     */   
/*  61 */   static int defaultGap = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   String string;
/*     */ 
/*     */ 
/*     */   
/*     */   VPoint anchorPoint;
/*     */ 
/*     */ 
/*     */   
/*     */   Font font;
/*     */ 
/*     */ 
/*     */   
/*     */   Color colour;
/*     */ 
/*     */ 
/*     */   
/*     */   int orientation;
/*     */ 
/*     */ 
/*     */   
/*     */   int gap;
/*     */ 
/*     */ 
/*     */   
/*     */   double angrad;
/*     */ 
/*     */ 
/*     */   
/*     */   public PlotText(String paramString, VPoint paramVPoint) {
/*  94 */     this(paramString, paramVPoint, 0, defaultGap, defaultColour, defaultFont, 0.0D);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt) {
/* 106 */     this(paramString, paramVPoint, paramInt, defaultGap, defaultColour, defaultFont, 0.0D);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2) {
/* 120 */     this(paramString, paramVPoint, paramInt1, paramInt2, defaultColour, defaultFont, 0.0D);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, double paramDouble) {
/* 135 */     this(paramString, paramVPoint, paramInt1, paramInt2, defaultColour, defaultFont, paramDouble);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor) {
/* 150 */     this(paramString, paramVPoint, paramInt1, paramInt2, paramColor, defaultFont, 0.0D);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Font paramFont) {
/* 166 */     this(paramString, paramVPoint, paramInt1, paramInt2, paramColor, paramFont, 0.0D);
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
/*     */   public PlotText(String paramString, VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Font paramFont, double paramDouble) {
/* 185 */     if (paramInt1 < 0 || paramInt1 > 4) {
/* 186 */       throw new IllegalArgumentException("Invalid orientation.");
/*     */     }
/* 188 */     this.string = paramString;
/* 189 */     this.anchorPoint = paramVPoint;
/* 190 */     this.orientation = paramInt1;
/* 191 */     this.angrad = -Math.toRadians(paramDouble);
/* 192 */     this.gap = paramInt2;
/* 193 */     this.colour = paramColor;
/* 194 */     this.font = paramFont;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Point2D paramPoint2D, VirtualTransform paramVirtualTransform) {
/* 200 */     return false;
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
/*     */   public void draw(Graphics2D paramGraphics2D, VirtualTransform paramVirtualTransform) {
/* 261 */     paramGraphics2D.setColor(this.colour);
/* 262 */     paramGraphics2D.setFont(this.font);
/*     */     
/* 264 */     Point point = new Point();
/* 265 */     paramVirtualTransform.transform((Point2D)this.anchorPoint, point);
/*     */     
/* 267 */     AffineTransform affineTransform1 = paramGraphics2D.getTransform();
/*     */ 
/*     */     
/* 270 */     AffineTransform affineTransform2 = AffineTransform.getTranslateInstance(point.x, point.y);
/* 271 */     if (this.angrad != 0.0D) affineTransform2.rotate(this.angrad);
/*     */ 
/*     */ 
/*     */     
/* 275 */     FontMetrics fontMetrics = paramGraphics2D.getFontMetrics();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     switch (this.orientation) {
/*     */       
/*     */       case 1:
/* 288 */         affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), -this.gap);
/*     */         break;
/*     */       case 4:
/* 291 */         affineTransform2.translate(this.gap, ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
/*     */         break;
/*     */       case 2:
/* 294 */         affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), (fontMetrics.getAscent() + this.gap));
/*     */         break;
/*     */       case 3:
/* 297 */         affineTransform2.translate((-fontMetrics.stringWidth(this.string) - this.gap), ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
/*     */         break;
/*     */       default:
/* 300 */         affineTransform2.translate((-fontMetrics.stringWidth(this.string) / 2), ((fontMetrics.getAscent() - fontMetrics.getDescent()) / 2));
/*     */         break;
/*     */     } 
/*     */     
/* 304 */     paramGraphics2D.transform(affineTransform2);
/*     */     
/* 306 */     paramGraphics2D.drawString(this.string, 0, 0);
/* 307 */     paramGraphics2D.setTransform(affineTransform1);
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
/*     */   public VPoint getAnchorPoint() {
/* 322 */     return this.anchorPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnchorPoint(VPoint paramVPoint) {
/* 329 */     this.anchorPoint = paramVPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAngle() {
/* 336 */     return -Math.toDegrees(this.angrad);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAngle(double paramDouble) {
/* 343 */     this.angrad = -Math.toRadians(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColour() {
/* 350 */     return this.colour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColour(Color paramColor) {
/* 357 */     this.colour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGap() {
/* 364 */     return this.gap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGap(int paramInt) {
/* 371 */     this.gap = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font getFont() {
/* 378 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFont(Font paramFont) {
/* 385 */     this.font = paramFont;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrientation() {
/* 392 */     return this.orientation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrientation(int paramInt) {
/* 403 */     if (paramInt < 0 || paramInt > 4)
/* 404 */       throw new IllegalArgumentException("Invalid orientation."); 
/* 405 */     this.orientation = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/* 413 */     return this.string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setString(String paramString) {
/* 420 */     this.string = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultColour(Color paramColor) {
/* 429 */     defaultColour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultFont(Font paramFont) {
/* 438 */     defaultFont = paramFont;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 448 */       JFrame jFrame = new JFrame("PlotText Test");
/*     */ 
/*     */       
/* 451 */       PlotPanel plotPanel = new PlotPanel(new Dimension(500, 400), new VRectangle(-50.0D, -50.0D, 100.0D, 100.0D));
/* 452 */       plotPanel.setZoomable(true);
/*     */       
/* 454 */       plotPanel.setFocusable(true);
/*     */ 
/*     */ 
/*     */       
/* 458 */       byte b1 = 5;
/* 459 */       VPoint vPoint = new VPoint(-20.0D, -10.0D);
/* 460 */       Font font = new Font("SansSerif", 0, 24);
/*     */ 
/*     */       
/* 463 */       plotPanel.addObject(new StandardMarker(vPoint, 0, 10, Color.red, new BasicStroke(1.0F)));
/* 464 */       plotPanel.addObject(new PlotText("North horizontal text", vPoint, 1, b1, Color.black, font));
/* 465 */       plotPanel.addObject(new PlotText("South horizontal text", vPoint, 2, b1, Color.black, font));
/* 466 */       plotPanel.addObject(new PlotText("East horizontal text", vPoint, 4, b1, Color.black, font));
/* 467 */       plotPanel.addObject(new PlotText("West horizontal text", vPoint, 3, b1, Color.black, font));
/*     */ 
/*     */ 
/*     */       
/* 471 */       vPoint = new VPoint(20.0D, 0.0D);
/* 472 */       plotPanel.addObject(new StandardMarker(vPoint, 6, 10, Color.red, new BasicStroke(1.0F)));
/* 473 */       plotPanel.addObject(new PlotText("North vertical text", vPoint, 1, b1, Color.black, font, 90.0D));
/* 474 */       plotPanel.addObject(new PlotText("South vertical text", vPoint, 2, b1, Color.black, font, 90.0D));
/* 475 */       plotPanel.addObject(new PlotText("East vertical text", vPoint, 4, b1, Color.black, font, 90.0D));
/* 476 */       plotPanel.addObject(new PlotText("West vertical text", vPoint, 3, b1, Color.black, font, 90.0D));
/*     */ 
/*     */       
/* 479 */       vPoint = new VPoint(-25.0D, 25.0D);
/* 480 */       plotPanel.addObject(new StandardMarker(vPoint, 4, 6, Color.red));
/* 481 */       for (byte b2 = 0; b2 < 'Ũ'; b2 += 30) {
/*     */         
/* 483 */         PlotText plotText = new PlotText("Rotated text " + b2, vPoint, 4, 7, b2);
/*     */         
/* 485 */         plotPanel.addObject(plotText);
/*     */       } 
/*     */       
/* 488 */       Container container = jFrame.getContentPane();
/* 489 */       container.setLayout(new BorderLayout());
/* 490 */       container.add(plotPanel, "Center");
/*     */       
/* 492 */       jFrame.setVisible(true);
/* 493 */       jFrame.setDefaultCloseOperation(3);
/* 494 */       jFrame.pack();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/PlotText.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */