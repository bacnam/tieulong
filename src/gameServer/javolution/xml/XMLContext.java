/*    */ package javolution.xml;
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
/*    */ public abstract class XMLContext
/*    */   extends FormatContext
/*    */ {
/*    */   public static XMLContext enter() {
/* 42 */     return (XMLContext)currentXMLContext().enterInner();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> XMLFormat<T> getFormat(Class<? extends T> type) {
/* 50 */     return currentXMLContext().searchFormat(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract <T> void setFormat(Class<? extends T> paramClass, XMLFormat<T> paramXMLFormat);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract <T> XMLFormat<T> searchFormat(Class<? extends T> paramClass);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static XMLContext currentXMLContext() {
/* 69 */     XMLContext ctx = (XMLContext)AbstractContext.current(XMLContext.class);
/* 70 */     if (ctx != null)
/* 71 */       return ctx; 
/* 72 */     return OSGiServices.getXMLContext();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */