/*    */ package javolution.osgi.internal;
/*    */ 
/*    */ import javolution.xml.internal.stream.XMLInputFactoryImpl;
/*    */ import javolution.xml.stream.XMLInputFactory;
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
/*    */ public final class XMLInputFactoryProvider
/*    */   implements ServiceFactory<XMLInputFactory>
/*    */ {
/*    */   public XMLInputFactory getService(Bundle bundle, ServiceRegistration<XMLInputFactory> registration) {
/* 26 */     return (XMLInputFactory)new XMLInputFactoryImpl();
/*    */   }
/*    */   
/*    */   public void ungetService(Bundle bundle, ServiceRegistration<XMLInputFactory> registration, XMLInputFactory service) {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/XMLInputFactoryProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */