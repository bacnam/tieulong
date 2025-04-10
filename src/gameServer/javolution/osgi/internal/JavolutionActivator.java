package javolution.osgi.internal;

import javolution.xml.stream.XMLInputFactory;
import javolution.xml.stream.XMLOutputFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class JavolutionActivator
implements BundleActivator
{
private ServiceRegistration<XMLInputFactory> xmlInputFactoryRegistration;
private ServiceRegistration<XMLOutputFactory> xmlOutputFactoryRegistration;

public void start(BundleContext bc) throws Exception {
OSGiServices.CONCURRENT_CONTEXT_TRACKER.activate(bc);
OSGiServices.CONFIGURABLE_LISTENER_TRACKER.activate(bc);
OSGiServices.LOCAL_CONTEXT_TRACKER.activate(bc);
OSGiServices.LOG_CONTEXT_TRACKER.activate(bc);
OSGiServices.LOG_SERVICE_TRACKER.activate(bc);
OSGiServices.SECURITY_CONTEXT_TRACKER.activate(bc);
OSGiServices.TEXT_CONTEXT_TRACKER.activate(bc);
OSGiServices.XML_CONTEXT_TRACKER.activate(bc);
OSGiServices.XML_INPUT_FACTORY_TRACKER.activate(bc);
OSGiServices.XML_OUTPUT_FACTORY_TRACKER.activate(bc);

this.xmlInputFactoryRegistration = bc.registerService(XMLInputFactory.class.getName(), new XMLInputFactoryProvider(), null);

this.xmlOutputFactoryRegistration = bc.registerService(XMLOutputFactory.class.getName(), new XMLOutputFactoryProvider(), null);

OSGiServices.initializeRealtimeClasses();
}

public void stop(BundleContext bc) throws Exception {
OSGiServices.CONCURRENT_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.CONFIGURABLE_LISTENER_TRACKER.deactivate(bc);
OSGiServices.LOCAL_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.LOG_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.LOG_SERVICE_TRACKER.deactivate(bc);
OSGiServices.SECURITY_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.TEXT_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.XML_CONTEXT_TRACKER.deactivate(bc);
OSGiServices.XML_INPUT_FACTORY_TRACKER.deactivate(bc);
OSGiServices.XML_OUTPUT_FACTORY_TRACKER.deactivate(bc);

this.xmlInputFactoryRegistration.unregister();
this.xmlOutputFactoryRegistration.unregister();
}
}

