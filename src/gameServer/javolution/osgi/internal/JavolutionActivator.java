/*    */ package javolution.osgi.internal;
/*    */ 
/*    */ import javolution.xml.stream.XMLInputFactory;
/*    */ import javolution.xml.stream.XMLOutputFactory;
/*    */ import org.osgi.framework.BundleActivator;
/*    */ import org.osgi.framework.BundleContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavolutionActivator
/*    */   implements BundleActivator
/*    */ {
/*    */   private ServiceRegistration<XMLInputFactory> xmlInputFactoryRegistration;
/*    */   private ServiceRegistration<XMLOutputFactory> xmlOutputFactoryRegistration;
/*    */   
/*    */   public void start(BundleContext bc) throws Exception {
/* 34 */     OSGiServices.CONCURRENT_CONTEXT_TRACKER.activate(bc);
/* 35 */     OSGiServices.CONFIGURABLE_LISTENER_TRACKER.activate(bc);
/* 36 */     OSGiServices.LOCAL_CONTEXT_TRACKER.activate(bc);
/* 37 */     OSGiServices.LOG_CONTEXT_TRACKER.activate(bc);
/* 38 */     OSGiServices.LOG_SERVICE_TRACKER.activate(bc);
/* 39 */     OSGiServices.SECURITY_CONTEXT_TRACKER.activate(bc);
/* 40 */     OSGiServices.TEXT_CONTEXT_TRACKER.activate(bc);
/* 41 */     OSGiServices.XML_CONTEXT_TRACKER.activate(bc);
/* 42 */     OSGiServices.XML_INPUT_FACTORY_TRACKER.activate(bc);
/* 43 */     OSGiServices.XML_OUTPUT_FACTORY_TRACKER.activate(bc);
/*    */ 
/*    */     
/* 46 */     this.xmlInputFactoryRegistration = bc.registerService(XMLInputFactory.class.getName(), new XMLInputFactoryProvider(), null);
/*    */ 
/*    */     
/* 49 */     this.xmlOutputFactoryRegistration = bc.registerService(XMLOutputFactory.class.getName(), new XMLOutputFactoryProvider(), null);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     OSGiServices.initializeRealtimeClasses();
/*    */   }
/*    */   
/*    */   public void stop(BundleContext bc) throws Exception {
/* 58 */     OSGiServices.CONCURRENT_CONTEXT_TRACKER.deactivate(bc);
/* 59 */     OSGiServices.CONFIGURABLE_LISTENER_TRACKER.deactivate(bc);
/* 60 */     OSGiServices.LOCAL_CONTEXT_TRACKER.deactivate(bc);
/* 61 */     OSGiServices.LOG_CONTEXT_TRACKER.deactivate(bc);
/* 62 */     OSGiServices.LOG_SERVICE_TRACKER.deactivate(bc);
/* 63 */     OSGiServices.SECURITY_CONTEXT_TRACKER.deactivate(bc);
/* 64 */     OSGiServices.TEXT_CONTEXT_TRACKER.deactivate(bc);
/* 65 */     OSGiServices.XML_CONTEXT_TRACKER.deactivate(bc);
/* 66 */     OSGiServices.XML_INPUT_FACTORY_TRACKER.deactivate(bc);
/* 67 */     OSGiServices.XML_OUTPUT_FACTORY_TRACKER.deactivate(bc);
/*    */     
/* 69 */     this.xmlInputFactoryRegistration.unregister();
/* 70 */     this.xmlOutputFactoryRegistration.unregister();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/JavolutionActivator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */