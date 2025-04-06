/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Point;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.NoninvertibleTransformException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VirtualTransform
/*    */   extends AffineTransform
/*    */ {
/*    */   public VirtualTransform(VRectangle paramVRectangle, Dimension paramDimension) {
/* 32 */     scale(paramDimension.width / paramVRectangle.width, -paramDimension.height / paramVRectangle.height);
/* 33 */     translate(-paramVRectangle.x, -paramVRectangle.y - paramVRectangle.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VDimension pixelToVirtual(Dimension paramDimension) {
/* 56 */     double d1 = getScaleX();
/* 57 */     double d2 = getScaleY();
/* 58 */     if (d1 == 0.0D || d2 == 0.0D)
/* 59 */       throw new IllegalArgumentException("Zero scale. Probably \"device\" has no size because it has not been painted yet."); 
/* 60 */     double d3 = paramDimension.width / d1;
/* 61 */     double d4 = paramDimension.height / -d2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     return new VDimension(d3, d4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VPoint pixelToVirtual(Point paramPoint) {
/* 80 */     VPoint vPoint = new VPoint(0.0D, 0.0D);
/*    */     try {
/* 82 */       return (VPoint)inverseTransform(paramPoint, vPoint);
/*    */     }
/*    */     catch (NoninvertibleTransformException noninvertibleTransformException) {
/*    */       
/* 86 */       System.out.println(noninvertibleTransformException.getMessage());
/* 87 */       return vPoint;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VirtualTransform.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */