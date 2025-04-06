/*    */ package javolution.context.internal;
/*    */ 
/*    */ import javolution.context.AbstractContext;
/*    */ import javolution.context.LocalContext;
/*    */ import javolution.util.FastMap;
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
/*    */ public final class LocalContextImpl
/*    */   extends LocalContext
/*    */ {
/* 19 */   private FastMap<LocalContext.Parameter<?>, Object> localSettings = new FastMap();
/*    */   
/*    */   private LocalContextImpl parent;
/*    */   
/*    */   protected LocalContext inner() {
/* 24 */     LocalContextImpl ctx = new LocalContextImpl();
/* 25 */     ctx.parent = this;
/* 26 */     return ctx;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void supersede(LocalContext.Parameter<T> param, T localValue) {
/* 31 */     if (localValue == null) throw new NullPointerException(); 
/* 32 */     this.localSettings.put(param, localValue);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> T getValue(LocalContext.Parameter<T> param, T defaultValue) {
/* 38 */     Object value = this.localSettings.get(param);
/* 39 */     if (value != null) return (T)value; 
/* 40 */     if (this.parent != null) return this.parent.getValue(param, defaultValue); 
/* 41 */     return defaultValue;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/internal/LocalContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */