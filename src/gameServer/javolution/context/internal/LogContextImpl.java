/*    */ package javolution.context.internal;
/*    */ 
/*    */ import javolution.context.AbstractContext;
/*    */ import javolution.context.LogContext;
/*    */ import javolution.osgi.internal.OSGiServices;
/*    */ import javolution.text.TextBuilder;
/*    */ import org.osgi.service.log.LogService;
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
/*    */ public final class LogContextImpl
/*    */   extends LogContext
/*    */ {
/* 22 */   private static final Object[] NONE = new Object[0];
/* 23 */   private static final int[] TO_OSGI_LEVEL = new int[] { 4, 3, 2, 1 };
/*    */   
/*    */   private LogContext.Level level;
/*    */   
/* 27 */   private Object[] prefix = NONE;
/* 28 */   private Object[] suffix = NONE;
/*    */ 
/*    */   
/*    */   public void prefix(Object... pfx) {
/* 32 */     Object[] tmp = new Object[this.prefix.length + pfx.length];
/* 33 */     System.arraycopy(pfx, 0, tmp, 0, pfx.length);
/* 34 */     System.arraycopy(this.prefix, 0, tmp, pfx.length, this.prefix.length);
/* 35 */     this.prefix = tmp;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLevel(LogContext.Level level) {
/* 40 */     this.level = level;
/*    */   }
/*    */ 
/*    */   
/*    */   public void suffix(Object... sfx) {
/* 45 */     Object[] tmp = new Object[this.suffix.length + sfx.length];
/* 46 */     System.arraycopy(this.suffix, 0, tmp, 0, this.suffix.length);
/* 47 */     System.arraycopy(sfx, 0, tmp, this.suffix.length, sfx.length);
/* 48 */     this.suffix = tmp;
/*    */   }
/*    */ 
/*    */   
/*    */   protected LogContext inner() {
/* 53 */     LogContextImpl ctx = new LogContextImpl();
/* 54 */     ctx.prefix = this.prefix;
/* 55 */     ctx.suffix = this.suffix;
/* 56 */     ctx.level = this.level;
/* 57 */     return ctx;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void log(LogContext.Level level, Object... message) {
/* 62 */     if (level.compareTo((Enum)currentLevel()) < 0)
/*    */       return; 
/* 64 */     TextBuilder tmp = new TextBuilder();
/* 65 */     Throwable exception = null;
/* 66 */     for (Object pfx : this.prefix) {
/* 67 */       tmp.append(pfx);
/*    */     }
/* 69 */     for (Object obj : message) {
/* 70 */       if (exception == null && obj instanceof Throwable) {
/* 71 */         exception = (Throwable)obj;
/*    */       } else {
/* 73 */         tmp.append(obj);
/*    */       } 
/*    */     } 
/* 76 */     for (Object sfx : this.suffix) {
/* 77 */       tmp.append(sfx);
/*    */     }
/* 79 */     int osgiLevel = TO_OSGI_LEVEL[level.ordinal()];
/* 80 */     String msg = tmp.toString();
/* 81 */     Object[] logServices = OSGiServices.getLogServices();
/* 82 */     for (Object logService : logServices)
/* 83 */       ((LogService)logService).log(osgiLevel, msg, exception); 
/*    */   }
/*    */   
/*    */   private LogContext.Level currentLevel() {
/* 87 */     if (LEVEL == null) return LogContext.Level.DEBUG; 
/* 88 */     if (this.level == null) return (LogContext.Level)LEVEL.get(); 
/* 89 */     return this.level;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/internal/LogContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */