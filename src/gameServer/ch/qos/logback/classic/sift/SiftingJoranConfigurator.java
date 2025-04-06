/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.util.DefaultNestedComponentRules;
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.action.AppenderAction;
/*    */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*    */ import ch.qos.logback.core.joran.spi.ElementPath;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class SiftingJoranConfigurator
/*    */   extends SiftingJoranConfiguratorBase<ILoggingEvent>
/*    */ {
/*    */   SiftingJoranConfigurator(String key, String value, Map<String, String> parentPropertyMap) {
/* 35 */     super(key, value, parentPropertyMap);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ElementPath initialElementPath() {
/* 40 */     return new ElementPath("configuration");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addInstanceRules(RuleStore rs) {
/* 45 */     super.addInstanceRules(rs);
/* 46 */     rs.addRule(new ElementSelector("configuration/appender"), (Action)new AppenderAction());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
/* 53 */     DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void buildInterpreter() {
/* 58 */     super.buildInterpreter();
/* 59 */     Map<String, Object> omap = this.interpreter.getInterpretationContext().getObjectMap();
/* 60 */     omap.put("APPENDER_BAG", new HashMap<Object, Object>());
/* 61 */     omap.put("FILTER_CHAIN_BAG", new HashMap<Object, Object>());
/* 62 */     Map<String, String> propertiesMap = new HashMap<String, String>();
/* 63 */     propertiesMap.putAll(this.parentPropertyMap);
/* 64 */     propertiesMap.put(this.key, this.value);
/* 65 */     this.interpreter.setInterpretationContextPropertiesMap(propertiesMap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Appender<ILoggingEvent> getAppender() {
/* 70 */     Map<String, Object> omap = this.interpreter.getInterpretationContext().getObjectMap();
/* 71 */     HashMap appenderMap = (HashMap)omap.get("APPENDER_BAG");
/* 72 */     oneAndOnlyOneCheck(appenderMap);
/* 73 */     Collection<Appender<ILoggingEvent>> values = appenderMap.values();
/* 74 */     if (values.size() == 0) {
/* 75 */       return null;
/*    */     }
/* 77 */     return values.iterator().next();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/sift/SiftingJoranConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */