/*    */ package jsc.event;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class StatisticEvent
/*    */   extends EventObject
/*    */ {
/*    */   protected double statistic;
/*    */   
/*    */   public StatisticEvent(Object paramObject, double paramDouble) {
/* 29 */     super(paramObject);
/* 30 */     setStatistic(paramDouble);
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
/*    */   public double getStatistic() {
/* 47 */     return this.statistic;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStatistic(double paramDouble) {
/* 54 */     this.statistic = paramDouble;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/event/StatisticEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */