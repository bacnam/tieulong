/*    */ package jsc.event;
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
/*    */ public class SampleEvent
/*    */   extends StatisticEvent
/*    */ {
/*    */   protected double[] sample;
/*    */   
/*    */   public SampleEvent(Object paramObject, double[] paramArrayOfdouble, double paramDouble) {
/* 28 */     super(paramObject, paramDouble);
/* 29 */     this.sample = paramArrayOfdouble;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double[] getSample() {
/* 37 */     return this.sample;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/event/SampleEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */