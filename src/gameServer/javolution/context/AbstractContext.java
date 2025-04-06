/*     */ package javolution.context;
/*     */ 
/*     */ import javolution.lang.Parallelizable;
/*     */ import javolution.lang.Realtime;
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
/*     */ @Realtime
/*     */ @Parallelizable(comment = "Sequential configuration, parallel use")
/*     */ public abstract class AbstractContext
/*     */ {
/*  45 */   private static final ThreadLocal<AbstractContext> CURRENT = new ThreadLocal<AbstractContext>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractContext outer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AbstractContext current() {
/*  62 */     return CURRENT.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static <T extends AbstractContext> T current(Class<T> type) {
/*  70 */     AbstractContext ctx = CURRENT.get();
/*  71 */     while (ctx != null) {
/*  72 */       if (type.isInstance(ctx))
/*  73 */         return (T)ctx; 
/*  74 */       ctx = ctx.outer;
/*     */     } 
/*  76 */     return null;
/*     */   }
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
/*     */   public static <T extends AbstractContext> T enter(Class<T> custom) {
/* 102 */     SecurityContext.check(new SecurityContext.Permission(custom, "enter"));
/*     */     try {
/* 104 */       return (T)((AbstractContext)custom.newInstance()).enterInner();
/* 105 */     } catch (InstantiationException e) {
/* 106 */       throw new IllegalArgumentException("Cannot instantiate instance of " + custom, e);
/*     */     }
/* 108 */     catch (IllegalAccessException e) {
/* 109 */       throw new IllegalArgumentException("Cannot access " + custom, e);
/*     */     } 
/*     */   }
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
/*     */   public static void inherit(AbstractContext ctx) {
/* 132 */     CURRENT.set(ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractContext enterInner() {
/* 142 */     AbstractContext inner = inner();
/* 143 */     inner.outer = CURRENT.get();
/* 144 */     CURRENT.set(inner);
/* 145 */     return inner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exit() {
/* 156 */     if (this != CURRENT.get()) {
/* 157 */       throw new IllegalStateException("This context is not the current context");
/*     */     }
/* 159 */     CURRENT.set(this.outer);
/* 160 */     this.outer = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractContext getOuter() {
/* 168 */     return this.outer;
/*     */   }
/*     */   
/*     */   protected abstract AbstractContext inner();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/AbstractContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */