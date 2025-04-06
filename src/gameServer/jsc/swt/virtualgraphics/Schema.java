/*    */ package jsc.swt.virtualgraphics;
/*    */ 
/*    */ import jsc.descriptive.OrderStatistics;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Schema
/*    */   extends Path
/*    */ {
/*    */   public Schema(OrderStatistics paramOrderStatistics, double paramDouble1, double paramDouble2) {
/* 32 */     super(9);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     moveTo(paramDouble1 - 0.25D * paramDouble2, paramOrderStatistics.getMinimum());
/* 38 */     lineTo(paramDouble1 + 0.25D * paramDouble2, paramOrderStatistics.getMinimum());
/* 39 */     moveTo(paramDouble1, paramOrderStatistics.getMinimum());
/* 40 */     lineTo(paramDouble1, paramOrderStatistics.getLowerQuartile());
/*    */ 
/*    */     
/* 43 */     moveTo(paramDouble1 - 0.25D * paramDouble2, paramOrderStatistics.getMaximum());
/* 44 */     lineTo(paramDouble1 + 0.25D * paramDouble2, paramOrderStatistics.getMaximum());
/* 45 */     moveTo(paramDouble1, paramOrderStatistics.getMaximum());
/* 46 */     lineTo(paramDouble1, paramOrderStatistics.getUpperQuartile());
/*    */ 
/*    */     
/* 49 */     moveTo(paramDouble1 - 0.5D * paramDouble2, paramOrderStatistics.getMedian());
/* 50 */     lineTo(paramDouble1 + 0.5D * paramDouble2, paramOrderStatistics.getMedian());
/*    */ 
/*    */     
/* 53 */     append(new VRectangle(paramDouble1 - 0.5D * paramDouble2, paramOrderStatistics.getLowerQuartile(), paramDouble2, paramOrderStatistics.getInterquartileRange()), false);
/*    */     
/* 55 */     this.path.closePath();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/virtualgraphics/Schema.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */