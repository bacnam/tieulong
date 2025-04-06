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
/*     */ public abstract class LocalContext
/*     */   extends AbstractContext
/*     */ {
/*     */   public static abstract class Parameter<T>
/*     */     extends Configurable<T>
/*     */   {
/*  55 */     public static final SecurityContext.Permission<Parameter<?>> SUPERSEDE_PERMISSION = new SecurityContext.Permission<Parameter<?>>((Class)Parameter.class, "supersede");
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
/*  67 */     private final SecurityContext.Permission<Parameter<T>> supersedePermission = new SecurityContext.Permission<Parameter<T>>((Class)Parameter.class, "supersede", this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SecurityContext.Permission<Parameter<T>> getSupersedePermission() {
/*  76 */       return this.supersedePermission;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/*  84 */       LocalContext ctx = AbstractContext.<LocalContext>current(LocalContext.class);
/*  85 */       return (ctx != null) ? ctx.<T>getValue(this, (T)super.get()) : (T)super.get();
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
/*     */   public static LocalContext enter() {
/*  98 */     LocalContext ctx = current(LocalContext.class);
/*  99 */     if (ctx == null) {
/* 100 */       ctx = OSGiServices.getLocalContext();
/*     */     }
/* 102 */     return (LocalContext)ctx.enterInner();
/*     */   }
/*     */   
/*     */   public abstract <T> void supersede(Parameter<T> paramParameter, T paramT);
/*     */   
/*     */   protected abstract <T> T getValue(Parameter<T> paramParameter, T paramT);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/LocalContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */