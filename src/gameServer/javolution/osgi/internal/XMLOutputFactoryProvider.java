/*    */ package javolution.osgi.internal;
/*    */ 
/*    */ import javolution.xml.internal.stream.XMLOutputFactoryImpl;
/*    */ import javolution.xml.stream.XMLOutputFactory;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.ServiceFactory;
/*    */ import org.osgi.framework.ServiceRegistration;
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
/*    */ public final class XMLOutputFactoryProvider
/*    */   implements ServiceFactory<XMLOutputFactory>
/*    */ {
/*    */   public XMLOutputFactory getService(Bundle bundle, ServiceRegistration<XMLOutputFactory> registration) {
/* 26 */     return (XMLOutputFactory)new XMLOutputFactoryImpl();
/*    */   }
/*    */   
/*    */   public void ungetService(Bundle bundle, ServiceRegistration<XMLOutputFactory> registration, XMLOutputFactory service) {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/XMLOutputFactoryProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */