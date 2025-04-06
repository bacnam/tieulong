/*     */ package jsc.swt.plot2d;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Stroke;
/*     */ import jsc.swt.plot.AxisModel;
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
/*     */ public class ScatterPlot2D
/*     */   extends FunctionPlot2D
/*     */   implements Cloneable
/*     */ {
/*     */   public ScatterPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, String paramString) {
/*  30 */     super(paramAxisModel1, paramAxisModel2, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScatterPlot2D(AxisModel paramAxisModel1, AxisModel paramAxisModel2, double paramDouble1, double paramDouble2, String paramString) {
/*  41 */     super(paramAxisModel1, paramAxisModel2, paramDouble1, paramDouble2, paramString);
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
/*     */   public void addMarkers(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt1, int paramInt2, Color paramColor, Stroke paramStroke) {
/*  58 */     int i = paramArrayOfdouble1.length;
/*  59 */     if (paramArrayOfdouble2.length != i) {
/*  60 */       throw new IllegalArgumentException("Arrays not equal length.");
/*     */     }
/*  62 */     StandardMarker[] arrayOfStandardMarker = new StandardMarker[i];
/*  63 */     for (byte b = 0; b < i; b++)
/*  64 */       arrayOfStandardMarker[b] = new StandardMarker(new VPoint(paramArrayOfdouble1[b], paramArrayOfdouble2[b]), paramInt1, paramInt2, paramColor, paramStroke); 
/*  65 */     addObjects((PlotObject[])arrayOfStandardMarker);
/*     */   }
/*     */   public Object clone() {
/*  68 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarkerColour(Color paramColor) {
/*  78 */     for (byte b = 0; b < this.objects.size(); b++) {
/*     */       
/*  80 */       if (this.objects.elementAt(b) instanceof Marker) {
/*     */         
/*  82 */         Marker marker = this.objects.elementAt(b);
/*  83 */         marker.setColour(paramColor);
/*     */       } 
/*     */     } 
/*  86 */     repaint();
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
/*     */   public void setMarkerColour(int paramInt, Color paramColor) {
/* 100 */     if (this.objects.elementAt(paramInt) instanceof Marker) {
/*     */       
/* 102 */       Marker marker = this.objects.elementAt(paramInt);
/* 103 */       marker.setColour(paramColor);
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
/*     */   public void setMarkerLocation(int paramInt, VPoint paramVPoint) {
/* 118 */     if (this.objects.elementAt(paramInt) instanceof Marker) {
/*     */       
/* 120 */       Marker marker = this.objects.elementAt(paramInt);
/* 121 */       marker.setLocation(paramVPoint);
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
/*     */   public void setMarkerSize(int paramInt) {
/* 134 */     for (byte b = 0; b < this.objects.size(); b++) {
/*     */       
/* 136 */       if (this.objects.elementAt(b) instanceof Marker) {
/*     */         
/* 138 */         Marker marker = this.objects.elementAt(b);
/* 139 */         marker.setSize(paramInt);
/*     */       } 
/*     */     } 
/* 142 */     repaint();
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
/*     */   public void setMarkerSize(int paramInt1, int paramInt2) {
/* 156 */     if (this.objects.elementAt(paramInt1) instanceof Marker) {
/*     */       
/* 158 */       Marker marker = this.objects.elementAt(paramInt1);
/* 159 */       marker.setSize(paramInt2);
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
/*     */   public void setMarkerStroke(Stroke paramStroke) {
/* 173 */     for (byte b = 0; b < this.objects.size(); b++) {
/*     */       
/* 175 */       if (this.objects.elementAt(b) instanceof Marker) {
/*     */         
/* 177 */         Marker marker = this.objects.elementAt(b);
/* 178 */         marker.setStroke(paramStroke);
/*     */       } 
/*     */     } 
/* 181 */     repaint();
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
/*     */   public void setMarkerStroke(int paramInt, Stroke paramStroke) {
/* 195 */     if (this.objects.elementAt(paramInt) instanceof Marker) {
/*     */       
/* 197 */       Marker marker = this.objects.elementAt(paramInt);
/* 198 */       marker.setStroke(paramStroke);
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
/*     */   public void setMarkerType(int paramInt) {
/* 213 */     for (byte b = 0; b < this.objects.size(); b++) {
/*     */       
/* 215 */       if (this.objects.elementAt(b) instanceof Marker) {
/*     */         
/* 217 */         Marker marker = this.objects.elementAt(b);
/* 218 */         marker.setType(paramInt);
/*     */       } 
/*     */     } 
/* 221 */     repaint();
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
/*     */   public void setMarkerType(int paramInt1, int paramInt2) {
/* 236 */     if (this.objects.elementAt(paramInt1) instanceof Marker) {
/*     */       
/* 238 */       Marker marker = this.objects.elementAt(paramInt1);
/* 239 */       marker.setType(paramInt2);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/ScatterPlot2D.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */