/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.net.ReceiverBase;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import ch.qos.logback.core.util.OptionHelper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReceiverAction
/*    */   extends Action
/*    */ {
/*    */   private ReceiverBase receiver;
/*    */   private boolean inError;
/*    */   
/*    */   public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
/* 39 */     String className = attributes.getValue("class");
/* 40 */     if (OptionHelper.isEmpty(className)) {
/* 41 */       addError("Missing class name for receiver. Near [" + name + "] line " + getLineNumber(ic));
/*    */       
/* 43 */       this.inError = true;
/*    */       
/*    */       return;
/*    */     } 
/*    */     try {
/* 48 */       addInfo("About to instantiate receiver of type [" + className + "]");
/*    */       
/* 50 */       this.receiver = (ReceiverBase)OptionHelper.instantiateByClassName(className, ReceiverBase.class, this.context);
/*    */       
/* 52 */       this.receiver.setContext(this.context);
/*    */       
/* 54 */       ic.pushObject(this.receiver);
/*    */     }
/* 56 */     catch (Exception ex) {
/* 57 */       this.inError = true;
/* 58 */       addError("Could not create a receiver of type [" + className + "].", ex);
/* 59 */       throw new ActionException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void end(InterpretationContext ic, String name) throws ActionException {
/* 67 */     if (this.inError)
/*    */       return; 
/* 69 */     ic.getContext().register((LifeCycle)this.receiver);
/* 70 */     this.receiver.start();
/*    */     
/* 72 */     Object o = ic.peekObject();
/* 73 */     if (o != this.receiver) {
/* 74 */       addWarn("The object at the of the stack is not the remote pushed earlier.");
/*    */     } else {
/*    */       
/* 77 */       ic.popObject();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/joran/action/ReceiverAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */