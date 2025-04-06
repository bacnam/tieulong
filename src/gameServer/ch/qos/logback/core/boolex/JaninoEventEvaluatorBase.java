/*    */ package ch.qos.logback.core.boolex;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.codehaus.janino.ScriptEvaluator;
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
/*    */ public abstract class JaninoEventEvaluatorBase<E>
/*    */   extends EventEvaluatorBase<E>
/*    */ {
/* 30 */   static Class<?> EXPRESSION_TYPE = boolean.class;
/* 31 */   static Class<?>[] THROWN_EXCEPTIONS = new Class[1];
/*    */   public static final int ERROR_THRESHOLD = 4;
/*    */   
/*    */   static {
/* 35 */     THROWN_EXCEPTIONS[0] = EvaluationException.class;
/*    */   }
/*    */ 
/*    */   
/*    */   private String expression;
/*    */   ScriptEvaluator scriptEvaluator;
/* 41 */   private int errorCount = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   protected List<Matcher> matcherList = new ArrayList<Matcher>();
/*    */ 
/*    */   
/*    */   public void start() {
/*    */     try {
/* 56 */       assert this.context != null;
/* 57 */       this.scriptEvaluator = new ScriptEvaluator(getDecoratedExpression(), EXPRESSION_TYPE, getParameterNames(), getParameterTypes(), THROWN_EXCEPTIONS);
/*    */       
/* 59 */       super.start();
/* 60 */     } catch (Exception e) {
/* 61 */       addError("Could not start evaluator with expression [" + this.expression + "]", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean evaluate(E event) throws EvaluationException {
/* 67 */     if (!isStarted()) {
/* 68 */       throw new IllegalStateException("Evaluator [" + this.name + "] was called in stopped state");
/*    */     }
/*    */     
/*    */     try {
/* 72 */       Boolean result = (Boolean)this.scriptEvaluator.evaluate(getParameterValues(event));
/* 73 */       return result.booleanValue();
/* 74 */     } catch (Exception ex) {
/* 75 */       this.errorCount++;
/* 76 */       if (this.errorCount >= 4) {
/* 77 */         stop();
/*    */       }
/* 79 */       throw new EvaluationException("Evaluator [" + this.name + "] caused an exception", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getExpression() {
/* 85 */     return this.expression;
/*    */   }
/*    */   
/*    */   public void setExpression(String expression) {
/* 89 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public void addMatcher(Matcher matcher) {
/* 93 */     this.matcherList.add(matcher);
/*    */   }
/*    */   
/*    */   public List<Matcher> getMatcherList() {
/* 97 */     return this.matcherList;
/*    */   }
/*    */   
/*    */   protected abstract String getDecoratedExpression();
/*    */   
/*    */   protected abstract String[] getParameterNames();
/*    */   
/*    */   protected abstract Class<?>[] getParameterTypes();
/*    */   
/*    */   protected abstract Object[] getParameterValues(E paramE);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/boolex/JaninoEventEvaluatorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */