package javolution.osgi.internal;

import javolution.context.ConcurrentContext;
import javolution.context.LocalContext;
import javolution.context.LogContext;
import javolution.context.SecurityContext;
import javolution.context.internal.ConcurrentContextImpl;
import javolution.context.internal.LocalContextImpl;
import javolution.context.internal.LogContextImpl;
import javolution.context.internal.SecurityContextImpl;
import javolution.io.Struct;
import javolution.lang.Configurable;
import javolution.lang.Initializer;
import javolution.lang.MathLib;
import javolution.text.Text;
import javolution.text.TextContext;
import javolution.text.TypeFormat;
import javolution.text.internal.TextContextImpl;
import javolution.util.FastBitSet;
import javolution.util.FastSortedMap;
import javolution.util.FastSortedSet;
import javolution.util.FastSortedTable;
import javolution.util.Index;
import javolution.util.function.Equalities;
import javolution.util.function.Reducers;
import javolution.xml.XMLContext;
import javolution.xml.internal.XMLContextImpl;
import javolution.xml.internal.stream.XMLInputFactoryImpl;
import javolution.xml.internal.stream.XMLOutputFactoryImpl;
import javolution.xml.internal.stream.XMLStreamReaderImpl;
import javolution.xml.internal.stream.XMLStreamWriterImpl;
import javolution.xml.stream.XMLInputFactory;
import javolution.xml.stream.XMLOutputFactory;
import org.osgi.service.log.LogService;

public class OSGiServices
{
static final ServiceTrackerImpl<ConcurrentContext> CONCURRENT_CONTEXT_TRACKER = new ServiceTrackerImpl(ConcurrentContext.class, ConcurrentContextImpl.class);

static final ServiceTrackerImpl<Configurable.Listener> CONFIGURABLE_LISTENER_TRACKER = new ServiceTrackerImpl<Configurable.Listener>(Configurable.Listener.class, (Class)ConfigurableListenerImpl.class);

static final ServiceTrackerImpl<LocalContext> LOCAL_CONTEXT_TRACKER = new ServiceTrackerImpl(LocalContext.class, LocalContextImpl.class);

static final ServiceTrackerImpl<LogContext> LOG_CONTEXT_TRACKER = new ServiceTrackerImpl(LogContext.class, LogContextImpl.class);

static final ServiceTrackerImpl<LogService> LOG_SERVICE_TRACKER = new ServiceTrackerImpl<LogService>(LogService.class, (Class)LogServiceImpl.class);

static final ServiceTrackerImpl<SecurityContext> SECURITY_CONTEXT_TRACKER = new ServiceTrackerImpl(SecurityContext.class, SecurityContextImpl.class);

static final ServiceTrackerImpl<TextContext> TEXT_CONTEXT_TRACKER = new ServiceTrackerImpl(TextContext.class, TextContextImpl.class);

static final ServiceTrackerImpl<XMLContext> XML_CONTEXT_TRACKER = new ServiceTrackerImpl(XMLContext.class, XMLContextImpl.class);

static final ServiceTrackerImpl<XMLInputFactory> XML_INPUT_FACTORY_TRACKER = new ServiceTrackerImpl(XMLInputFactory.class, XMLInputFactoryImpl.class);

static final ServiceTrackerImpl<XMLOutputFactory> XML_OUTPUT_FACTORY_TRACKER = new ServiceTrackerImpl(XMLOutputFactory.class, XMLOutputFactoryImpl.class);

public static ConcurrentContext getConcurrentContext() {
return (ConcurrentContext)CONCURRENT_CONTEXT_TRACKER.getServices()[0];
}

public static Object[] getConfigurableListeners() {
return CONFIGURABLE_LISTENER_TRACKER.getServices();
}

public static LocalContext getLocalContext() {
return (LocalContext)LOCAL_CONTEXT_TRACKER.getServices()[0];
}

public static LogContext getLogContext() {
return (LogContext)LOG_CONTEXT_TRACKER.getServices()[0];
}

public static Object[] getLogServices() {
return LOG_SERVICE_TRACKER.getServices();
}

public static SecurityContext getSecurityContext() {
return (SecurityContext)SECURITY_CONTEXT_TRACKER.getServices()[0];
}

public static TextContext getTextContext() {
return (TextContext)TEXT_CONTEXT_TRACKER.getServices()[0];
}

public static XMLContext getXMLContext() {
return (XMLContext)XML_CONTEXT_TRACKER.getServices()[0];
}

public static XMLInputFactory getXMLInputFactory() {
return (XMLInputFactory)XML_INPUT_FACTORY_TRACKER.getServices()[0];
}

public static XMLOutputFactory getXMLOutputFactory() {
return (XMLOutputFactory)XML_OUTPUT_FACTORY_TRACKER.getServices()[0];
}

public static boolean initializeRealtimeClasses() {
Initializer initializer = new Initializer(OSGiServices.class.getClassLoader());
initializer.loadClass(MathLib.class);
initializer.loadClass(Text.class);
initializer.loadClass(TypeFormat.class);
initializer.loadClass(Struct.class);
initializer.loadClass(FastBitSet.class);
initializer.loadClass(FastSortedMap.class);
initializer.loadClass(FastSortedSet.class);
initializer.loadClass(FastSortedTable.class);
initializer.loadClass(Index.class);
initializer.loadClass(Reducers.class);
initializer.loadClass(Equalities.class);
initializer.loadClass(XMLStreamReaderImpl.class);
initializer.loadClass(XMLStreamWriterImpl.class);
return initializer.initializeLoadedClasses();
}
}

