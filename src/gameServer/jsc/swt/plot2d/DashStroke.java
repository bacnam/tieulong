/*    */ package jsc.swt.plot2d;
/*    */ 
/*    */ import java.awt.BasicStroke;
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
/*    */ public class DashStroke
/*    */   extends BasicStroke
/*    */ {
/*    */   static final float DEFAULT_DASH_LENGTH = 5.0F;
/*    */   
/*    */   public DashStroke(float paramFloat1, float paramFloat2) {
/* 24 */     super(paramFloat1, 0, 2, 0.0F, new float[] { paramFloat2, 0.6F * paramFloat2 }, 0.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DashStroke(float paramFloat) {
/* 31 */     this(paramFloat, 5.0F);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot2d/DashStroke.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */