/*    */ package javolution.text;
/*    */ 
/*    */ import javolution.context.AbstractContext;
/*    */ import javolution.context.FormatContext;
/*    */ import javolution.osgi.internal.OSGiServices;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TextContext
/*    */   extends FormatContext
/*    */ {
/*    */   public static TextContext enter() {
/* 60 */     return (TextContext)currentTextContext().enterInner();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> TextFormat<T> getFormat(Class<? extends T> type) {
/* 70 */     return currentTextContext().searchFormat(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract <T> void setFormat(Class<? extends T> paramClass, TextFormat<T> paramTextFormat);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract <T> TextFormat<T> searchFormat(Class<? extends T> paramClass);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static TextContext currentTextContext() {
/* 88 */     TextContext ctx = (TextContext)AbstractContext.current(TextContext.class);
/* 89 */     if (ctx != null)
/* 90 */       return ctx; 
/* 91 */     return OSGiServices.getTextContext();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/TextContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */