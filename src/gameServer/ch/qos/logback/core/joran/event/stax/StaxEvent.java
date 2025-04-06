/*    */ package ch.qos.logback.core.joran.event.stax;
/*    */ 
/*    */ import javax.xml.stream.Location;
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
/*    */ public class StaxEvent
/*    */ {
/*    */   final String name;
/*    */   final Location location;
/*    */   
/*    */   StaxEvent(String name, Location location) {
/* 25 */     this.name = name;
/* 26 */     this.location = location;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 35 */     return this.location;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/joran/event/stax/StaxEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */