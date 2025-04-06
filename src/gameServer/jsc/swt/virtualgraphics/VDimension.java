/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import java.awt.geom.Dimension2D;
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
/*    */ public class VDimension
/*    */   extends Dimension2D
/*    */ {
/*    */   public double width;
/*    */   public double height;
/*    */   
/*    */   public VDimension(double paramDouble1, double paramDouble2) {
/* 28 */     this.width = paramDouble1; this.height = paramDouble2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getHeight() {
/* 35 */     return this.height;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getWidth() {
/* 42 */     return this.width;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VDimension multiply(double paramDouble) {
/* 51 */     return new VDimension(paramDouble * this.width, paramDouble * this.height);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSize(double paramDouble1, double paramDouble2) {
/* 60 */     this.width = paramDouble1; this.height = paramDouble2;
/*    */   }
/*    */   public String toString() {
/* 63 */     return " width = " + this.width + ", height = " + this.height;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/VDimension.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */