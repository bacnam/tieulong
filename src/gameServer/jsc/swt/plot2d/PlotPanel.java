/*      */ package jsc.swt.plot2d;
/*      */ 
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Paint;
/*      */ import java.awt.Point;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyAdapter;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionAdapter;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.SwingUtilities;
/*      */ import jsc.swt.control.RealField;
/*      */ import jsc.swt.dialogue.FieldDialogue;
/*      */ import jsc.swt.virtualgraphics.Polyline;
/*      */ import jsc.swt.virtualgraphics.VDimension;
/*      */ import jsc.swt.virtualgraphics.VPoint;
/*      */ import jsc.swt.virtualgraphics.VRectangle;
/*      */ import jsc.swt.virtualgraphics.VirtualTransform;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PlotPanel
/*      */   extends JPanel
/*      */   implements Cloneable
/*      */ {
/*   71 */   public static final Composite SEE_THRU = AlphaComposite.getInstance(3, 0.5F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String VT_ERROR_MESSAGE = "PlotPanel component must be displayed at this stage.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected VRectangle virtualSpace;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   89 */   protected VirtualTransform vt = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   94 */   private Composite objectComposite = AlphaComposite.SrcOver;
/*      */ 
/*      */   
/*   97 */   protected RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector componentBounds;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector objects;
/*      */ 
/*      */ 
/*      */   
/*      */   private Component parent;
/*      */ 
/*      */ 
/*      */   
/*  113 */   private Color focusColour = Color.red;
/*      */ 
/*      */   
/*      */   private VRectangle zoomBox;
/*      */   
/*      */   private Stack zoomBoxes;
/*      */   
/*  120 */   private Color zoomBoxColour = Color.black;
/*      */   private PlotShape zoomBoxObject;
/*      */   private VPoint zoomBoxStart;
/*  123 */   private Stroke zoomBoxStroke = new DashStroke(1.0F);
/*      */   
/*      */   private KeyListener zoomKeyListener;
/*      */   
/*      */   private MouseMotionListener zoomMotionListener;
/*      */   
/*      */   private MouseListener zoomMouseListener;
/*      */   
/*      */   public PlotPanel() {
/*  132 */     this(new Dimension(300, 300), new VRectangle(0.0D, 0.0D, 1.0D, 1.0D));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlotPanel(Dimension paramDimension) {
/*  140 */     this(paramDimension, new VRectangle(0.0D, 0.0D, 1.0D, 1.0D));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlotPanel(Dimension paramDimension, VRectangle paramVRectangle) {
/*  155 */     super((LayoutManager)null, true);
/*      */ 
/*      */     
/*  158 */     this.parent = this;
/*  159 */     setOpaque(true);
/*  160 */     setBackground(Color.white);
/*  161 */     setBorder(BorderFactory.createLineBorder(Color.black, 1));
/*  162 */     setPreferredSize(paramDimension);
/*  163 */     this.virtualSpace = paramVRectangle;
/*      */     
/*  165 */     this.componentBounds = new Vector();
/*  166 */     this.objects = new Vector();
/*      */     
/*  168 */     addFocusListener(new KeyboardFocusListener(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ensureComponentCapacity(int paramInt) {
/*  183 */     this.componentBounds.ensureCapacity(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addComponent(Component paramComponent, VRectangle paramVRectangle) {
/*  196 */     add(paramComponent); this.componentBounds.add(paramVRectangle);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addComponent(Component paramComponent, VPoint paramVPoint, VDimension paramVDimension) {
/*  210 */     addComponent(paramComponent, new VRectangle(paramVPoint, paramVDimension));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeComponent(int paramInt) {
/*  224 */     remove(paramInt);
/*  225 */     this.componentBounds.remove(paramInt);
/*  226 */     repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeLastComponent() {
/*  235 */     int i = getComponentCount() - 1;
/*  236 */     if (i >= 0) removeComponent(i);
/*      */   
/*      */   }
/*      */   
/*      */   public void removeAllComponents() {
/*  241 */     super.removeAll(); this.componentBounds.removeAllElements(); repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getComponentBounds() {
/*  248 */     return this.componentBounds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VRectangle getComponentBounds(int paramInt) {
/*  258 */     return this.componentBounds.elementAt(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComponentBounds(int paramInt, VRectangle paramVRectangle) {
/*  269 */     this.componentBounds.setElementAt(paramVRectangle, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComponentBounds(int paramInt, VPoint paramVPoint, VDimension paramVDimension) {
/*  280 */     setComponentBounds(paramInt, new VRectangle(paramVPoint, paramVDimension));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ensureObjectCapacity(int paramInt) {
/*  294 */     this.objects.ensureCapacity(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  301 */     return this.objects.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addObject(PlotObject paramPlotObject) {
/*  309 */     this.objects.add(paramPlotObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addObjects(PlotObject[] paramArrayOfPlotObject) {
/*  319 */     int i = paramArrayOfPlotObject.length;
/*  320 */     this.objects.ensureCapacity(this.objects.capacity() + i);
/*  321 */     for (byte b = 0; b < i; ) { this.objects.add(paramArrayOfPlotObject[b]); b++; }
/*  322 */      repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlotObject getObject(int paramInt) {
/*  335 */     return this.objects.get(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlotObject getObject(Point2D paramPoint2D) {
/*  347 */     if (this.objects.isEmpty()) return null;
/*      */     
/*  349 */     for (byte b = 0; b < this.objects.size(); b++) {
/*      */       
/*  351 */       PlotObject plotObject = this.objects.elementAt(b);
/*  352 */       if (plotObject.contains(paramPoint2D, this.vt)) return plotObject; 
/*      */     } 
/*  354 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getObjectCount() {
/*  362 */     return this.objects.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getObjectIndex(Point2D paramPoint2D) {
/*  374 */     if (this.objects.isEmpty()) return -1;
/*      */     
/*  376 */     for (byte b = 0; b < this.objects.size(); b++) {
/*      */       
/*  378 */       PlotObject plotObject = this.objects.elementAt(b);
/*  379 */       if (plotObject.contains(paramPoint2D, this.vt)) return b; 
/*      */     } 
/*  381 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getObjects() {
/*  389 */     return this.objects;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeObject(PlotObject paramPlotObject) {
/*  399 */     return this.objects.remove(paramPlotObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeObject(int paramInt) {
/*  414 */     this.objects.remove(paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeLastObject() {
/*  422 */     int i = this.objects.size() - 1;
/*  423 */     if (i >= 0) this.objects.remove(i);
/*      */   
/*      */   }
/*      */   
/*      */   public void removeAllObjects() {
/*  428 */     this.objects.removeAllElements(); repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObjectComposite(Composite paramComposite) {
/*  445 */     this.objectComposite = paramComposite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addRenderingHint(RenderingHints.Key paramKey, Object paramObject) {
/*  474 */     this.hints.add(new RenderingHints(paramKey, paramObject));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RenderingHints getRenderingHints() {
/*  481 */     return this.hints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object removeRenderingHint(Object paramObject) {
/*  493 */     return this.hints.remove(paramObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/*  508 */     return copyPlot();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void copyContents(PlotPanel paramPlotPanel) {
/*  522 */     paramPlotPanel.objects = (Vector)this.objects.clone();
/*  523 */     paramPlotPanel.componentBounds = (Vector)this.componentBounds.clone();
/*  524 */     for (byte b = 0; b < getComponentCount(); b++) {
/*  525 */       paramPlotPanel.add(getComponent(b));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PlotPanel copyPlot() {
/*  537 */     PlotPanel plotPanel = new PlotPanel(getSize(), this.virtualSpace);
/*      */ 
/*      */     
/*  540 */     plotPanel.setDoubleBuffered(isDoubleBuffered());
/*  541 */     plotPanel.setOpaque(isOpaque());
/*  542 */     plotPanel.setBackground(getBackground());
/*  543 */     plotPanel.setBorder(getBorder());
/*      */ 
/*      */     
/*  546 */     copyContents(plotPanel);
/*      */ 
/*      */     
/*  549 */     plotPanel.hints = (RenderingHints)this.hints.clone();
/*  550 */     plotPanel.objectComposite = this.objectComposite;
/*      */     
/*  552 */     plotPanel.focusColour = this.focusColour;
/*      */     
/*  554 */     plotPanel.zoomBoxColour = this.zoomBoxColour;
/*  555 */     plotPanel.zoomBoxStroke = this.zoomBoxStroke;
/*      */ 
/*      */     
/*  558 */     return plotPanel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedImage getBufferedImage() {
/*  572 */     BufferedImage bufferedImage = (BufferedImage)createImage(getWidth(), getHeight());
/*  573 */     if (bufferedImage == null) return null; 
/*  574 */     Graphics2D graphics2D = bufferedImage.createGraphics();
/*  575 */     paintComponent(graphics2D);
/*  576 */     return bufferedImage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getParHeight(double paramDouble) {
/*  601 */     int i = virtualToPixelWidth(paramDouble);
/*  602 */     VDimension vDimension = pixelToVirtual(new Dimension(0, i));
/*  603 */     return vDimension.height;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getParWidth(double paramDouble) {
/*  621 */     int i = virtualToPixelHeight(paramDouble);
/*  622 */     VDimension vDimension = pixelToVirtual(new Dimension(i, 0));
/*  623 */     return vDimension.width;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VRectangle getVirtualSpace() {
/*  634 */     return this.virtualSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVirtualSpace(VRectangle paramVRectangle) {
/*  646 */     this.virtualSpace = paramVRectangle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VirtualTransform getVirtualTransform() {
/*  654 */     return this.vt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void merge(PlotPanel paramPlotPanel) {
/*      */     byte b;
/*  668 */     for (b = 0; b < paramPlotPanel.objects.size(); b++)
/*  669 */       this.objects.add(paramPlotPanel.objects.elementAt(b)); 
/*  670 */     for (b = 0; b < paramPlotPanel.componentBounds.size(); b++)
/*  671 */       this.componentBounds.add(paramPlotPanel.componentBounds.elementAt(b)); 
/*  672 */     for (b = 0; b < paramPlotPanel.getComponentCount(); b++) {
/*  673 */       add(paramPlotPanel.getComponent(b));
/*      */     }
/*      */     
/*  676 */     Rectangle2D.Double double_ = (Rectangle2D.Double)this.virtualSpace.createUnion((Rectangle2D)paramPlotPanel.virtualSpace);
/*  677 */     this.virtualSpace = new VRectangle(double_);
/*      */     
/*  679 */     repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintBackground(Graphics paramGraphics) {
/*  704 */     super.paintComponent(paramGraphics);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paintComponent(Graphics paramGraphics) {
/*  721 */     super.paintComponent(paramGraphics);
/*  722 */     Graphics2D graphics2D = (Graphics2D)paramGraphics;
/*  723 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*      */     
/*  725 */     graphics2D.setRenderingHints(this.hints);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  730 */     Color color = paramGraphics.getColor();
/*  731 */     Paint paint = graphics2D.getPaint();
/*  732 */     Stroke stroke = graphics2D.getStroke();
/*  733 */     Font font = graphics2D.getFont();
/*  734 */     Composite composite = graphics2D.getComposite();
/*      */     
/*  736 */     if (!this.objects.isEmpty()) {
/*      */       
/*  738 */       graphics2D.setComposite(this.objectComposite);
/*      */       
/*  740 */       for (byte b = 0; b < this.objects.size(); b++) {
/*      */         
/*  742 */         PlotObject plotObject = this.objects.elementAt(b);
/*  743 */         plotObject.draw(graphics2D, this.vt);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  752 */     paramGraphics.setColor(color);
/*  753 */     graphics2D.setPaint(paint);
/*  754 */     graphics2D.setStroke(stroke);
/*  755 */     graphics2D.setFont(font);
/*  756 */     graphics2D.setComposite(composite);
/*      */ 
/*      */ 
/*      */     
/*  760 */     if (!this.componentBounds.isEmpty()) {
/*      */       
/*  762 */       int i = getComponentCount();
/*  763 */       if (i != this.componentBounds.size()) {
/*  764 */         throw new NoSuchElementException("Mismatch between numbers of components and virtual bounds. Do not use Container methods to add/remove components. Do not add the same component to the plot and to another container.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  769 */       for (byte b = 0; b < i; b++) {
/*      */         
/*  771 */         VRectangle vRectangle = this.componentBounds.elementAt(b);
/*  772 */         Component component = getComponent(b);
/*  773 */         Shape shape = this.vt.createTransformedShape((Shape)vRectangle);
/*  774 */         component.setBounds(shape.getBounds());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VDimension pixelToVirtual(Dimension paramDimension) {
/*  790 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*  791 */     return this.vt.pixelToVirtual(paramDimension);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public VPoint pixelToVirtual(Point paramPoint) {
/*  804 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*  805 */     return this.vt.pixelToVirtual(paramPoint);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeAll() {
/*  814 */     this.objects.removeAllElements();
/*  815 */     super.removeAll();
/*  816 */     this.componentBounds.removeAllElements();
/*  817 */     repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAntialiased() {
/*  828 */     return (this.hints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAntialiasing(boolean paramBoolean) {
/*  843 */     this.hints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTextAntialiased() {
/*  855 */     return (this.hints.get(RenderingHints.KEY_TEXT_ANTIALIASING) == RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextAntialiasing(boolean paramBoolean) {
/*  866 */     this.hints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, paramBoolean ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFocusColour(Color paramColor) {
/*  877 */     this.focusColour = paramColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZoomable(boolean paramBoolean) {
/*  899 */     if (paramBoolean) {
/*      */       
/*  901 */       this.zoomBoxes = new Stack();
/*  902 */       this.zoomMotionListener = new ZoomMotionListener(this);
/*  903 */       this.zoomMouseListener = new ZoomMouseListener(this);
/*  904 */       this.zoomKeyListener = new ZoomKeyListener(this);
/*  905 */       addMouseMotionListener(this.zoomMotionListener);
/*  906 */       addMouseListener(this.zoomMouseListener);
/*  907 */       addKeyListener(this.zoomKeyListener);
/*      */     }
/*      */     else {
/*      */       
/*  911 */       removeMouseMotionListener(this.zoomMotionListener);
/*  912 */       removeMouseListener(this.zoomMouseListener);
/*  913 */       removeKeyListener(this.zoomKeyListener);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZoomBoxColour(Color paramColor) {
/*  922 */     this.zoomBoxColour = paramColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZoomBoxStroke(Stroke paramStroke) {
/*  930 */     this.zoomBoxStroke = paramStroke;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Dimension virtualToPixel(VDimension paramVDimension) {
/*  946 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*  947 */     int i = (int)(paramVDimension.width * this.vt.getScaleX());
/*  948 */     int j = (int)(-paramVDimension.height * this.vt.getScaleY());
/*  949 */     return new Dimension(i, j);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Point virtualToPixel(VPoint paramVPoint) {
/*  965 */     Point point = new Point();
/*      */     
/*  967 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*  968 */     return (Point)this.vt.transform((Point2D)paramVPoint, point);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int virtualToPixelHeight(double paramDouble) {
/*  985 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/*  986 */     return (int)(-paramDouble * this.vt.getScaleY());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int virtualToPixelWidth(double paramDouble) {
/* 1003 */     this.vt = new VirtualTransform(this.virtualSpace, getSize());
/* 1004 */     return (int)(paramDouble * this.vt.getScaleX());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean write(File paramFile) throws IOException {
/* 1021 */     String str = paramFile.getName();
/* 1022 */     String[] arrayOfString = ImageIO.getWriterFormatNames();
/* 1023 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 1024 */       if (str.endsWith("." + arrayOfString[b])) return write(paramFile, arrayOfString[b]); 
/* 1025 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean write(File paramFile, String paramString) throws IOException {
/* 1042 */     return ImageIO.write(getBufferedImage(), paramString, paramFile);
/*      */   }
/*      */ 
/*      */   
/*      */   private void zoomIn() {
/* 1047 */     if (this.zoomBox.isEmpty())
/* 1048 */       return;  this.zoomBoxes.push(this.virtualSpace);
/*      */     
/* 1050 */     setVirtualSpace(this.zoomBox);
/* 1051 */     repaint();
/*      */   }
/*      */ 
/*      */   
/*      */   private void zoomOut() {
/* 1056 */     if (this.zoomBoxes.empty())
/*      */       return; 
/* 1058 */     setVirtualSpace(this.zoomBoxes.pop());
/* 1059 */     repaint();
/*      */   }
/*      */   class KeyboardFocusListener extends FocusAdapter { private final PlotPanel this$0;
/*      */     KeyboardFocusListener(PlotPanel this$0) {
/* 1063 */       this.this$0 = this$0;
/*      */     }
/*      */     public void focusGained(FocusEvent param1FocusEvent) {
/* 1066 */       this.this$0.setBorder(BorderFactory.createLineBorder(this.this$0.focusColour, 1));
/*      */     } public void focusLost(FocusEvent param1FocusEvent) {
/* 1068 */       this.this$0.setBorder(BorderFactory.createLineBorder(Color.black, 1));
/*      */     } }
/*      */   
/*      */   class ZoomKeyListener extends KeyAdapter { ZoomKeyListener(PlotPanel this$0) {
/* 1072 */       this.this$0 = this$0;
/*      */     }
/*      */     private final PlotPanel this$0;
/*      */     
/*      */     public void keyTyped(KeyEvent param1KeyEvent) {
/* 1077 */       char c = param1KeyEvent.getKeyChar();
/*      */       
/* 1079 */       if (c == 'z' || c == 'Z') {
/*      */         
/* 1081 */         FieldDialogue fieldDialogue = new FieldDialogue(this.this$0.parent, "Zooming rectangle", "Enter origin & dimensions of zooming box.");
/*      */         
/* 1083 */         RealField realField1 = new RealField(this.this$0.virtualSpace.x, 10);
/* 1084 */         RealField realField2 = new RealField(this.this$0.virtualSpace.y, 10);
/* 1085 */         RealField realField3 = new RealField(this.this$0.virtualSpace.width, 10);
/* 1086 */         RealField realField4 = new RealField(this.this$0.virtualSpace.height, 10);
/* 1087 */         fieldDialogue.addField("Lower-left corner x", (JTextField)realField1, 0);
/* 1088 */         fieldDialogue.addField("Lower-left corner y", (JTextField)realField2, 1);
/* 1089 */         fieldDialogue.addField("Width", (JTextField)realField3, 2);
/* 1090 */         fieldDialogue.addField("Height", (JTextField)realField4, 3);
/*      */ 
/*      */         
/* 1093 */         fieldDialogue.setLocationRelativeTo(null);
/*      */ 
/*      */ 
/*      */         
/* 1097 */         if (fieldDialogue.show() == null)
/* 1098 */           return;  double d1 = realField1.getValue();
/* 1099 */         double d2 = realField2.getValue();
/* 1100 */         double d3 = realField3.getValue();
/* 1101 */         double d4 = realField4.getValue();
/* 1102 */         this.this$0.zoomBox = new VRectangle(d1, d2, d3, d4);
/* 1103 */         this.this$0.zoomIn();
/* 1104 */         param1KeyEvent.consume();
/*      */       }
/* 1106 */       else if (c == 'o' || c == 'O') {
/*      */         
/* 1108 */         this.this$0.zoomOut();
/* 1109 */         param1KeyEvent.consume();
/*      */       } else {
/*      */         return;
/*      */       } 
/*      */     } }
/*      */   class ZoomMouseListener extends MouseAdapter { private final PlotPanel this$0;
/*      */     
/*      */     ZoomMouseListener(PlotPanel this$0) {
/* 1117 */       this.this$0 = this$0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent param1MouseEvent) {
/* 1124 */       if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown()) {
/* 1125 */         this.this$0.zoomOut(); return;
/* 1126 */       }  this.this$0.zoomBoxStart = this.this$0.pixelToVirtual(param1MouseEvent.getPoint());
/* 1127 */       this.this$0.removeObject(this.this$0.zoomBoxObject);
/* 1128 */       this.this$0.zoomBox = new VRectangle(this.this$0.zoomBoxStart, this.this$0.zoomBoxStart);
/* 1129 */       this.this$0.zoomBoxObject = new PlotShape((Shape)this.this$0.zoomBox, this.this$0.zoomBoxColour, this.this$0.zoomBoxStroke);
/* 1130 */       this.this$0.addObject(this.this$0.zoomBoxObject);
/* 1131 */       this.this$0.repaint();
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent param1MouseEvent) {
/* 1135 */       if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown())
/* 1136 */         return;  this.this$0.removeObject(this.this$0.zoomBoxObject);
/*      */       
/* 1138 */       this.this$0.zoomIn();
/*      */     } }
/*      */   
/*      */   class ZoomMotionListener extends MouseMotionAdapter { ZoomMotionListener(PlotPanel this$0) {
/* 1142 */       this.this$0 = this$0;
/*      */     }
/*      */     private final PlotPanel this$0;
/*      */     public void mouseDragged(MouseEvent param1MouseEvent) {
/* 1146 */       if (SwingUtilities.isRightMouseButton(param1MouseEvent) || param1MouseEvent.isShiftDown())
/* 1147 */         return;  VPoint vPoint = this.this$0.pixelToVirtual(param1MouseEvent.getPoint());
/* 1148 */       this.this$0.removeObject(this.this$0.zoomBoxObject);
/* 1149 */       this.this$0.zoomBox = new VRectangle(this.this$0.zoomBoxStart, vPoint);
/* 1150 */       this.this$0.zoomBoxObject = new PlotShape((Shape)this.this$0.zoomBox, this.this$0.zoomBoxColour, this.this$0.zoomBoxStroke);
/* 1151 */       this.this$0.addObject(this.this$0.zoomBoxObject);
/* 1152 */       this.this$0.repaint();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Test
/*      */   {
/*      */     public static void main(String[] param1ArrayOfString) {
/* 1169 */       JFrame jFrame = new JFrame("PlotPanel Test");
/*      */ 
/*      */       
/* 1172 */       PlotPanel plotPanel = new PlotPanel(new Dimension(500, 400), new VRectangle(-50.0D, -50.0D, 100.0D, 100.0D));
/* 1173 */       plotPanel.setZoomable(true);
/*      */       
/* 1175 */       plotPanel.setFocusable(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1181 */       char c = 'Ϩ';
/* 1182 */       double d = -50.0D;
/* 1183 */       VPoint[] arrayOfVPoint = new VPoint[c];
/* 1184 */       for (byte b1 = 0; b1 < c; b1++) {
/*      */         
/* 1186 */         arrayOfVPoint[b1] = new VPoint(d, 40.0D * Math.sin(d));
/* 1187 */         d += 100.0D / c;
/*      */       } 
/* 1189 */       Polyline polyline = new Polyline(arrayOfVPoint);
/* 1190 */       plotPanel.addObject(new PlotShape((Shape)polyline, Color.black, new BasicStroke(1.0F)));
/*      */       
/* 1192 */       plotPanel.addObject(new StandardMarker(new VPoint(0.0D, 0.0D), 4, 40, Color.blue));
/*      */       
/* 1194 */       byte b2 = 5;
/* 1195 */       VPoint vPoint = new VPoint(-20.0D, 0.0D);
/*      */ 
/*      */       
/* 1198 */       Font font = new Font("SansSerif", 0, 12);
/* 1199 */       plotPanel.addObject(new StandardMarker(vPoint, 0, 10, Color.red, new BasicStroke(1.0F)));
/* 1200 */       plotPanel.addObject(new PlotText("North horizontal text", vPoint, 1, b2, Color.black, font));
/* 1201 */       plotPanel.addObject(new PlotText("South horizontal text", vPoint, 2, b2, Color.black, font));
/* 1202 */       plotPanel.addObject(new PlotText("East horizontal text", vPoint, 4, b2, Color.black, font));
/* 1203 */       plotPanel.addObject(new PlotText("West horizontal text", vPoint, 3, b2, Color.black, font));
/*      */ 
/*      */ 
/*      */       
/* 1207 */       vPoint = new VPoint(20.0D, 0.0D);
/* 1208 */       plotPanel.addObject(new StandardMarker(vPoint, 6, 10, Color.red, new BasicStroke(1.0F)));
/* 1209 */       plotPanel.addObject(new PlotText("North vertical text", vPoint, 1, b2, Color.black, font, 90.0D));
/* 1210 */       plotPanel.addObject(new PlotText("South vertical text", vPoint, 2, b2, Color.black, font, 90.0D));
/* 1211 */       plotPanel.addObject(new PlotText("East vertical text", vPoint, 4, b2, Color.black, font, 90.0D));
/* 1212 */       plotPanel.addObject(new PlotText("West vertical text", vPoint, 3, b2, Color.black, font, 90.0D));
/*      */       
/* 1214 */       plotPanel.addComponent(new JButton("Button"), new VPoint(-40.0D, -40.0D), new VDimension(30.0D, 10.0D));
/*      */ 
/*      */       
/* 1217 */       vPoint = new VPoint(-25.0D, 25.0D);
/* 1218 */       plotPanel.addObject(new StandardMarker(vPoint, 4, 6, Color.red));
/* 1219 */       for (byte b3 = 0; b3 < 'Ũ'; b3 += 30) {
/*      */         
/* 1221 */         PlotText plotText = new PlotText("Rotated text " + b3, vPoint, 4, 7, b3);
/*      */         
/* 1223 */         plotPanel.addObject(plotText);
/*      */       } 
/*      */       
/* 1226 */       Container container = jFrame.getContentPane();
/* 1227 */       container.setLayout(new BorderLayout());
/* 1228 */       container.add(plotPanel, "Center");
/*      */       
/* 1230 */       jFrame.setVisible(true);
/* 1231 */       jFrame.setDefaultCloseOperation(3);
/* 1232 */       jFrame.pack();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/PlotPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */