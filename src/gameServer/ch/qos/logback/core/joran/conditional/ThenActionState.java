/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.joran.event.InPlayListener;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ class ThenActionState
/*    */   implements InPlayListener
/*    */ {
/* 82 */   List<SaxEvent> eventList = new ArrayList<SaxEvent>();
/*    */   boolean isRegistered = false;
/*    */   
/*    */   public void inPlay(SaxEvent event) {
/* 86 */     this.eventList.add(event);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/joran/conditional/ThenActionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */