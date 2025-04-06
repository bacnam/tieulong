/*     */ package ch.qos.logback.core.joran;
/*     */ 
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.joran.action.AppenderAction;
/*     */ import ch.qos.logback.core.joran.action.AppenderRefAction;
/*     */ import ch.qos.logback.core.joran.action.ContextPropertyAction;
/*     */ import ch.qos.logback.core.joran.action.ConversionRuleAction;
/*     */ import ch.qos.logback.core.joran.action.DefinePropertyAction;
/*     */ import ch.qos.logback.core.joran.action.ImplicitAction;
/*     */ import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
/*     */ import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
/*     */ import ch.qos.logback.core.joran.action.NewRuleAction;
/*     */ import ch.qos.logback.core.joran.action.ParamAction;
/*     */ import ch.qos.logback.core.joran.action.PropertyAction;
/*     */ import ch.qos.logback.core.joran.action.ShutdownHookAction;
/*     */ import ch.qos.logback.core.joran.action.StatusListenerAction;
/*     */ import ch.qos.logback.core.joran.action.TimestampAction;
/*     */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.Interpreter;
/*     */ import ch.qos.logback.core.joran.spi.RuleStore;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JoranConfiguratorBase
/*     */   extends GenericConfigurator
/*     */ {
/*     */   public List getErrorList() {
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addInstanceRules(RuleStore rs) {
/*  62 */     rs.addRule(new ElementSelector("configuration/variable"), (Action)new PropertyAction());
/*  63 */     rs.addRule(new ElementSelector("configuration/property"), (Action)new PropertyAction());
/*     */     
/*  65 */     rs.addRule(new ElementSelector("configuration/substitutionProperty"), (Action)new PropertyAction());
/*     */ 
/*     */     
/*  68 */     rs.addRule(new ElementSelector("configuration/timestamp"), (Action)new TimestampAction());
/*  69 */     rs.addRule(new ElementSelector("configuration/shutdownHook"), (Action)new ShutdownHookAction());
/*  70 */     rs.addRule(new ElementSelector("configuration/define"), (Action)new DefinePropertyAction());
/*     */ 
/*     */ 
/*     */     
/*  74 */     rs.addRule(new ElementSelector("configuration/contextProperty"), (Action)new ContextPropertyAction());
/*     */ 
/*     */     
/*  77 */     rs.addRule(new ElementSelector("configuration/conversionRule"), (Action)new ConversionRuleAction());
/*     */ 
/*     */     
/*  80 */     rs.addRule(new ElementSelector("configuration/statusListener"), (Action)new StatusListenerAction());
/*     */ 
/*     */     
/*  83 */     rs.addRule(new ElementSelector("configuration/appender"), (Action)new AppenderAction());
/*  84 */     rs.addRule(new ElementSelector("configuration/appender/appender-ref"), (Action)new AppenderRefAction());
/*     */     
/*  86 */     rs.addRule(new ElementSelector("configuration/newRule"), (Action)new NewRuleAction());
/*  87 */     rs.addRule(new ElementSelector("*/param"), (Action)new ParamAction());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addImplicitRules(Interpreter interpreter) {
/*  93 */     NestedComplexPropertyIA nestedComplexPropertyIA = new NestedComplexPropertyIA();
/*  94 */     nestedComplexPropertyIA.setContext(this.context);
/*  95 */     interpreter.addImplicitAction((ImplicitAction)nestedComplexPropertyIA);
/*     */     
/*  97 */     NestedBasicPropertyIA nestedBasicIA = new NestedBasicPropertyIA();
/*  98 */     nestedBasicIA.setContext(this.context);
/*  99 */     interpreter.addImplicitAction((ImplicitAction)nestedBasicIA);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void buildInterpreter() {
/* 104 */     super.buildInterpreter();
/* 105 */     Map<String, Object> omap = this.interpreter.getInterpretationContext().getObjectMap();
/*     */     
/* 107 */     omap.put("APPENDER_BAG", new HashMap<Object, Object>());
/* 108 */     omap.put("FILTER_CHAIN_BAG", new HashMap<Object, Object>());
/*     */   }
/*     */   
/*     */   public InterpretationContext getInterpretationContext() {
/* 112 */     return this.interpreter.getInterpretationContext();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/joran/JoranConfiguratorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */