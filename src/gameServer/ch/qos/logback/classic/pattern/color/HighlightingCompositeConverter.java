/*    */ package ch.qos.logback.classic.pattern.color;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;
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
/*    */ public class HighlightingCompositeConverter
/*    */   extends ForegroundCompositeConverterBase<ILoggingEvent>
/*    */ {
/*    */   protected String getForegroundColorCode(ILoggingEvent event) {
/* 30 */     Level level = event.getLevel();
/* 31 */     switch (level.toInt()) { case 40000:
/* 32 */         return "1;31";
/* 33 */       case 30000: return "31";
/* 34 */       case 20000: return "34"; }
/* 35 */      return "39";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/pattern/color/HighlightingCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */