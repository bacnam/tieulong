/*     */ package javolution.osgi.internal;
/*     */ 
/*     */ import javolution.context.ConcurrentContext;
/*     */ import javolution.context.LocalContext;
/*     */ import javolution.context.LogContext;
/*     */ import javolution.context.SecurityContext;
/*     */ import javolution.context.internal.ConcurrentContextImpl;
/*     */ import javolution.context.internal.LocalContextImpl;
/*     */ import javolution.context.internal.LogContextImpl;
/*     */ import javolution.context.internal.SecurityContextImpl;
/*     */ import javolution.io.Struct;
/*     */ import javolution.lang.Configurable;
/*     */ import javolution.lang.Initializer;
/*     */ import javolution.lang.MathLib;
/*     */ import javolution.text.Text;
/*     */ import javolution.text.TextContext;
/*     */ import javolution.text.TypeFormat;
/*     */ import javolution.text.internal.TextContextImpl;
/*     */ import javolution.util.FastBitSet;
/*     */ import javolution.util.FastSortedMap;
/*     */ import javolution.util.FastSortedSet;
/*     */ import javolution.util.FastSortedTable;
/*     */ import javolution.util.Index;
/*     */ import javolution.util.function.Equalities;
/*     */ import javolution.util.function.Reducers;
/*     */ import javolution.xml.XMLContext;
/*     */ import javolution.xml.internal.XMLContextImpl;
/*     */ import javolution.xml.internal.stream.XMLInputFactoryImpl;
/*     */ import javolution.xml.internal.stream.XMLOutputFactoryImpl;
/*     */ import javolution.xml.internal.stream.XMLStreamReaderImpl;
/*     */ import javolution.xml.internal.stream.XMLStreamWriterImpl;
/*     */ import javolution.xml.stream.XMLInputFactory;
/*     */ import javolution.xml.stream.XMLOutputFactory;
/*     */ import org.osgi.service.log.LogService;
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
/*     */ public class OSGiServices
/*     */ {
/*  52 */   static final ServiceTrackerImpl<ConcurrentContext> CONCURRENT_CONTEXT_TRACKER = new ServiceTrackerImpl(ConcurrentContext.class, ConcurrentContextImpl.class);
/*     */   
/*  54 */   static final ServiceTrackerImpl<Configurable.Listener> CONFIGURABLE_LISTENER_TRACKER = new ServiceTrackerImpl<Configurable.Listener>(Configurable.Listener.class, (Class)ConfigurableListenerImpl.class);
/*     */   
/*  56 */   static final ServiceTrackerImpl<LocalContext> LOCAL_CONTEXT_TRACKER = new ServiceTrackerImpl(LocalContext.class, LocalContextImpl.class);
/*     */   
/*  58 */   static final ServiceTrackerImpl<LogContext> LOG_CONTEXT_TRACKER = new ServiceTrackerImpl(LogContext.class, LogContextImpl.class);
/*     */   
/*  60 */   static final ServiceTrackerImpl<LogService> LOG_SERVICE_TRACKER = new ServiceTrackerImpl<LogService>(LogService.class, (Class)LogServiceImpl.class);
/*     */   
/*  62 */   static final ServiceTrackerImpl<SecurityContext> SECURITY_CONTEXT_TRACKER = new ServiceTrackerImpl(SecurityContext.class, SecurityContextImpl.class);
/*     */   
/*  64 */   static final ServiceTrackerImpl<TextContext> TEXT_CONTEXT_TRACKER = new ServiceTrackerImpl(TextContext.class, TextContextImpl.class);
/*     */   
/*  66 */   static final ServiceTrackerImpl<XMLContext> XML_CONTEXT_TRACKER = new ServiceTrackerImpl(XMLContext.class, XMLContextImpl.class);
/*     */   
/*  68 */   static final ServiceTrackerImpl<XMLInputFactory> XML_INPUT_FACTORY_TRACKER = new ServiceTrackerImpl(XMLInputFactory.class, XMLInputFactoryImpl.class);
/*     */   
/*  70 */   static final ServiceTrackerImpl<XMLOutputFactory> XML_OUTPUT_FACTORY_TRACKER = new ServiceTrackerImpl(XMLOutputFactory.class, XMLOutputFactoryImpl.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConcurrentContext getConcurrentContext() {
/*  75 */     return (ConcurrentContext)CONCURRENT_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] getConfigurableListeners() {
/*  80 */     return CONFIGURABLE_LISTENER_TRACKER.getServices();
/*     */   }
/*     */ 
/*     */   
/*     */   public static LocalContext getLocalContext() {
/*  85 */     return (LocalContext)LOCAL_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static LogContext getLogContext() {
/*  90 */     return (LogContext)LOG_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] getLogServices() {
/*  95 */     return LOG_SERVICE_TRACKER.getServices();
/*     */   }
/*     */ 
/*     */   
/*     */   public static SecurityContext getSecurityContext() {
/* 100 */     return (SecurityContext)SECURITY_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextContext getTextContext() {
/* 105 */     return (TextContext)TEXT_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static XMLContext getXMLContext() {
/* 110 */     return (XMLContext)XML_CONTEXT_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static XMLInputFactory getXMLInputFactory() {
/* 115 */     return (XMLInputFactory)XML_INPUT_FACTORY_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static XMLOutputFactory getXMLOutputFactory() {
/* 120 */     return (XMLOutputFactory)XML_OUTPUT_FACTORY_TRACKER.getServices()[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean initializeRealtimeClasses() {
/* 125 */     Initializer initializer = new Initializer(OSGiServices.class.getClassLoader());
/* 126 */     initializer.loadClass(MathLib.class);
/* 127 */     initializer.loadClass(Text.class);
/* 128 */     initializer.loadClass(TypeFormat.class);
/* 129 */     initializer.loadClass(Struct.class);
/* 130 */     initializer.loadClass(FastBitSet.class);
/* 131 */     initializer.loadClass(FastSortedMap.class);
/* 132 */     initializer.loadClass(FastSortedSet.class);
/* 133 */     initializer.loadClass(FastSortedTable.class);
/* 134 */     initializer.loadClass(Index.class);
/* 135 */     initializer.loadClass(Reducers.class);
/* 136 */     initializer.loadClass(Equalities.class);
/* 137 */     initializer.loadClass(XMLStreamReaderImpl.class);
/* 138 */     initializer.loadClass(XMLStreamWriterImpl.class);
/* 139 */     return initializer.initializeLoadedClasses();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/OSGiServices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */