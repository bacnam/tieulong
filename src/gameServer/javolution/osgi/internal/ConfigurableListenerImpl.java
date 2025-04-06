/*    */ package javolution.osgi.internal;
/*    */ 
/*    */ import javolution.context.LogContext;
/*    */ import javolution.lang.Configurable;
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
/*    */ public final class ConfigurableListenerImpl
/*    */   implements Configurable.Listener
/*    */ {
/*    */   public <T> void configurableInitialized(Configurable<T> configurable, T value) {
/* 23 */     LogContext.debug(new Object[] { configurable.getName(), "=", value });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> void configurableReconfigured(Configurable<T> configurable, T oldValue, T newValue) {
/* 29 */     LogContext.debug(new Object[] { configurable.getName(), " reconfigured from ", oldValue, " to ", newValue });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/ConfigurableListenerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */