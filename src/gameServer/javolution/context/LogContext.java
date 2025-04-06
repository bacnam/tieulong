/*     */ package javolution.context;
/*     */ 
/*     */ import javolution.lang.Configurable;
/*     */ import javolution.osgi.internal.OSGiServices;
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
/*     */ 
/*     */ public abstract class LogContext
/*     */   extends AbstractContext
/*     */ {
/*     */   public enum Level
/*     */   {
/*  63 */     DEBUG, INFO, WARNING, ERROR, FATAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final Configurable<Level> LEVEL = new Configurable<Level>()
/*     */     {
/*     */       protected LogContext.Level getDefault() {
/*  76 */         return LogContext.Level.DEBUG;
/*     */       }
/*     */       
/*     */       public LogContext.Level parse(String str) {
/*  80 */         return LogContext.Level.valueOf(str);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void debug(Object... message) {
/*  88 */     currentLogContext().log(Level.DEBUG, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LogContext enter() {
/*  95 */     return (LogContext)currentLogContext().enterInner();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void error(Object... message) {
/* 103 */     currentLogContext().log(Level.ERROR, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void info(Object... message) {
/* 110 */     currentLogContext().log(Level.INFO, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void warning(Object... message) {
/* 117 */     currentLogContext().log(Level.WARNING, message);
/*     */   }
/*     */   
/*     */   private static LogContext currentLogContext() {
/* 121 */     LogContext ctx = current(LogContext.class);
/* 122 */     if (ctx != null)
/* 123 */       return ctx; 
/* 124 */     return OSGiServices.getLogContext();
/*     */   }
/*     */   
/*     */   public abstract void prefix(Object... paramVarArgs);
/*     */   
/*     */   public abstract void setLevel(Level paramLevel);
/*     */   
/*     */   public abstract void suffix(Object... paramVarArgs);
/*     */   
/*     */   protected abstract void log(Level paramLevel, Object... paramVarArgs);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/LogContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */