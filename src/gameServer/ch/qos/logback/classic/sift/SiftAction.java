/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.event.InPlayListener;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.sift.AppenderFactory;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class SiftAction
/*    */   extends Action
/*    */   implements InPlayListener
/*    */ {
/*    */   List<SaxEvent> seList;
/*    */   
/*    */   public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
/* 34 */     this.seList = new ArrayList<SaxEvent>();
/* 35 */     ic.addInPlayListener(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void end(InterpretationContext ic, String name) throws ActionException {
/* 40 */     ic.removeInPlayListener(this);
/* 41 */     Object o = ic.peekObject();
/* 42 */     if (o instanceof SiftingAppender) {
/* 43 */       SiftingAppender sa = (SiftingAppender)o;
/* 44 */       Map<String, String> propertyMap = ic.getCopyOfPropertyMap();
/* 45 */       AppenderFactoryUsingJoran appenderFactory = new AppenderFactoryUsingJoran(this.seList, sa.getDiscriminatorKey(), propertyMap);
/*    */       
/* 47 */       sa.setAppenderFactory((AppenderFactory)appenderFactory);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void inPlay(SaxEvent event) {
/* 52 */     this.seList.add(event);
/*    */   }
/*    */   
/*    */   public List<SaxEvent> getSeList() {
/* 56 */     return this.seList;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/sift/SiftAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */