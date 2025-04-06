/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StandardMarker
/*     */   extends Marker
/*     */ {
/*     */   public static final int ASTERISK = 0;
/*     */   public static final int CIRCLE = 1;
/*     */   public static final int CROSS = 2;
/*     */   public static final int DIAMOND = 3;
/*     */   public static final int FILLED_CIRCLE = 4;
/*     */   public static final int FILLED_DIAMOND = 5;
/*     */   public static final int FILLED_SQUARE = 6;
/*     */   public static final int PIXEL = 7;
/*     */   public static final int PLUS = 8;
/*     */   public static final int SQUARE = 9;
/*     */   static final int TYPE_COUNT = 10;
/*  51 */   static final String[] names = new String[] { "Asterisk", "Circle", "Cross", "Diamond", "Filled circle", "Filled diamond", "Filled square", "Pixel", "Plus", "Square" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardMarker(VPoint paramVPoint, int paramInt) {
/*  67 */     this(paramVPoint, paramInt, Marker.defaultSize, Marker.defaultColour, Marker.defaultStroke);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2) {
/*  78 */     this(paramVPoint, paramInt1, paramInt2, Marker.defaultColour, Marker.defaultStroke);
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
/*     */   public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor) {
/*  90 */     this(paramVPoint, paramInt1, paramInt2, paramColor, Marker.defaultStroke);
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
/*     */   public StandardMarker(VPoint paramVPoint, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
/* 104 */     super(paramVPoint, paramInt2, paramColor, paramStroke);
/* 105 */     setType(paramInt1);
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
/*     */   public Shape getShape(Point2D.Float paramFloat) {
/*     */     GeneralPath generalPath;
/* 124 */     switch (this.type) {
/*     */       
/*     */       case 1:
/*     */       case 4:
/* 128 */         return new Ellipse2D.Float(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize, this.size, this.size);
/*     */       case 8:
/* 130 */         generalPath = new GeneralPath(1, 2);
/* 131 */         generalPath.moveTo(paramFloat.x, paramFloat.y - this.halfSize);
/* 132 */         generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
/* 133 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
/* 134 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
/* 135 */         return generalPath;
/*     */       case 0:
/* 137 */         generalPath = new GeneralPath(1, 4);
/* 138 */         generalPath.moveTo(paramFloat.x, paramFloat.y - this.halfSize);
/* 139 */         generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
/* 140 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
/* 141 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
/* 142 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize);
/* 143 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y + this.halfSize);
/* 144 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y + this.halfSize);
/* 145 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y - this.halfSize);
/* 146 */         return generalPath;
/*     */       case 2:
/* 148 */         generalPath = new GeneralPath(1, 2);
/* 149 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize);
/* 150 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y + this.halfSize);
/* 151 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y + this.halfSize);
/* 152 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y - this.halfSize);
/* 153 */         return generalPath;
/*     */       case 3:
/*     */       case 5:
/* 156 */         generalPath = new GeneralPath(1, 4);
/* 157 */         generalPath.moveTo(paramFloat.x - this.halfSize, paramFloat.y);
/* 158 */         generalPath.lineTo(paramFloat.x, paramFloat.y - this.halfSize);
/* 159 */         generalPath.lineTo(paramFloat.x + this.halfSize, paramFloat.y);
/* 160 */         generalPath.lineTo(paramFloat.x, paramFloat.y + this.halfSize);
/* 161 */         generalPath.closePath();
/* 162 */         return generalPath;
/*     */       case 6:
/*     */       case 9:
/* 165 */         return new Rectangle2D.Float(paramFloat.x - this.halfSize, paramFloat.y - this.halfSize, this.size, this.size);
/*     */       case 7:
/* 167 */         return new Rectangle2D.Float(paramFloat.x - 0.5F, paramFloat.y - 0.5F, 1.0F, 1.0F);
/*     */     } 
/* 169 */     throw new IllegalArgumentException("Invalid marker type.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 178 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int paramInt) {
/* 188 */     if (paramInt < 0 || paramInt >= 10)
/* 189 */       throw new IllegalArgumentException("Invalid marker type."); 
/* 190 */     this.type = paramInt;
/* 191 */     if (paramInt == 4 || paramInt == 6 || paramInt == 5) {
/* 192 */       this.filled = true;
/*     */     } else {
/* 194 */       this.filled = false;
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
/*     */   public static int getType(String paramString) {
/* 207 */     for (byte b = 0; b < 10; ) { if (paramString.equals(names[b])) return b;  b++; }
/* 208 */      return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTypeCount() {
/* 216 */     return 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTypeName(int paramInt) {
/* 227 */     if (paramInt < 0 || paramInt >= names.length) return null; 
/* 228 */     return names[paramInt];
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/StandardMarker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */