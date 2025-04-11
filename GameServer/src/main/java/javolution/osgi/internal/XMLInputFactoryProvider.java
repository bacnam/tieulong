package javolution.osgi.internal;

import javolution.xml.internal.stream.XMLInputFactoryImpl;
import javolution.xml.stream.XMLInputFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

public final class XMLInputFactoryProvider
        implements ServiceFactory<XMLInputFactory> {
    public XMLInputFactory getService(Bundle bundle, ServiceRegistration<XMLInputFactory> registration) {
        return (XMLInputFactory) new XMLInputFactoryImpl();
    }

    public void ungetService(Bundle bundle, ServiceRegistration<XMLInputFactory> registration, XMLInputFactory service) {
    }
}

