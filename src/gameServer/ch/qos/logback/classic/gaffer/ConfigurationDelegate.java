/*     */ package ch.qos.logback.classic.gaffer;
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.jmx.JMXConfigurator;
/*     */ import ch.qos.logback.classic.jmx.MBeanUtil;
/*     */ import ch.qos.logback.classic.net.ReceiverBase;
/*     */ import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;
/*     */ import ch.qos.logback.classic.turbo.TurboFilter;
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.util.CachingDateFormatter;
/*     */ import ch.qos.logback.core.util.Duration;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import groovy.lang.Reference;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.codehaus.groovy.reflection.ClassInfo;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSiteArray;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ConfigurationDelegate extends ContextAwareBase implements GroovyObject {
/*     */   private List<Appender> appenderList;
/*     */   
/*     */   public ConfigurationDelegate() {
/*  44 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); List<Appender> list = ScriptBytecodeAdapter.createList(new Object[0]); MetaClass metaClass = $getStaticMetaClass();
/*     */   }
/*     */   public Object getDeclaredOrigin() {
/*  47 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void scan(String scanPeriodStr) {
/*  54 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); ReconfigureOnChangeFilter rocf = (ReconfigureOnChangeFilter)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(ReconfigureOnChangeFilter.class), ReconfigureOnChangeFilter.class);
/*  55 */     arrayOfCallSite[1].call(rocf, arrayOfCallSite[2].callGroovyObjectGetProperty(this));
/*  56 */     if (DefaultTypeTransformation.booleanUnbox(scanPeriodStr)) {
/*     */       
/*  58 */       try { Duration duration = (Duration)ScriptBytecodeAdapter.castToType(arrayOfCallSite[3].call(Duration.class, scanPeriodStr), Duration.class);
/*  59 */         arrayOfCallSite[4].call(rocf, arrayOfCallSite[5].call(duration));
/*  60 */         arrayOfCallSite[6].callCurrent(this, arrayOfCallSite[7].call("Setting ReconfigureOnChangeFilter scanning period to ", duration)); } catch (NumberFormatException nfe)
/*     */       
/*     */       { 
/*  63 */         arrayOfCallSite[8].callCurrent(this, arrayOfCallSite[9].call(arrayOfCallSite[10].call("Error while converting [", arrayOfCallSite[11].callGroovyObjectGetProperty(this)), "] to long"), nfe); }
/*     */       finally {}
/*     */     }
/*  66 */     arrayOfCallSite[12].call(rocf);
/*  67 */     arrayOfCallSite[13].callCurrent(this, "Adding ReconfigureOnChangeFilter as a turbo filter");
/*  68 */     arrayOfCallSite[14].call(arrayOfCallSite[15].callGroovyObjectGetProperty(this), rocf);
/*     */   }
/*     */   
/*     */   public void statusListener(Class listenerClass) {
/*  72 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); StatusListener statusListener = (StatusListener)ScriptBytecodeAdapter.castToType(arrayOfCallSite[16].call(listenerClass), StatusListener.class);
/*  73 */     arrayOfCallSite[17].call(arrayOfCallSite[18].callGetProperty(arrayOfCallSite[19].callGroovyObjectGetProperty(this)), statusListener);
/*  74 */     if (statusListener instanceof ContextAware) {
/*  75 */       arrayOfCallSite[20].call(ScriptBytecodeAdapter.castToType(statusListener, ContextAware.class), arrayOfCallSite[21].callGroovyObjectGetProperty(this));
/*     */     }
/*  77 */     if (statusListener instanceof LifeCycle) {
/*  78 */       arrayOfCallSite[22].call(ScriptBytecodeAdapter.castToType(statusListener, LifeCycle.class));
/*     */     }
/*  80 */     arrayOfCallSite[23].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[24].callGetProperty(listenerClass) }, new String[] { "Added status listener of type [", "]" }));
/*     */   }
/*     */   
/*     */   public void conversionRule(String conversionWord, Class converterClass) {
/*  84 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); String converterClassName = ShortTypeHandling.castToString(arrayOfCallSite[25].call(converterClass));
/*     */     
/*  86 */     Map ruleRegistry = (Map)ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].call(arrayOfCallSite[27].callGroovyObjectGetProperty(this), arrayOfCallSite[28].callGetProperty(CoreConstants.class)), Map.class);
/*  87 */     if (ScriptBytecodeAdapter.compareEqual(ruleRegistry, null)) {
/*  88 */       Object object = arrayOfCallSite[29].callConstructor(HashMap.class); ruleRegistry = (Map)ScriptBytecodeAdapter.castToType(object, Map.class);
/*  89 */       arrayOfCallSite[30].call(arrayOfCallSite[31].callGroovyObjectGetProperty(this), arrayOfCallSite[32].callGetProperty(CoreConstants.class), ruleRegistry);
/*     */     } 
/*     */     
/*  92 */     arrayOfCallSite[33].callCurrent(this, arrayOfCallSite[34].call(arrayOfCallSite[35].call(arrayOfCallSite[36].call(arrayOfCallSite[37].call("registering conversion word ", conversionWord), " with class ["), converterClassName), "]"));
/*  93 */     arrayOfCallSite[38].call(ruleRegistry, conversionWord, converterClassName);
/*     */   }
/*     */   
/*  96 */   public void root(Level level) { CallSite[] arrayOfCallSite = $getCallSiteArray(); root(level, ScriptBytecodeAdapter.createList(new Object[0])); null; } public void root(Level level, List appenderNames) {
/*  97 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (ScriptBytecodeAdapter.compareEqual(level, null)) {
/*  98 */       arrayOfCallSite[39].callCurrent(this, "Root logger cannot be set to level null");
/*     */     } else {
/* 100 */       arrayOfCallSite[40].callCurrent(this, arrayOfCallSite[41].callGetProperty(Logger.class), level, appenderNames);
/*     */     } 
/*     */   }
/*     */   
/* 104 */   public void logger(String name, Level level) { CallSite[] arrayOfCallSite = $getCallSiteArray(); logger(name, level, ScriptBytecodeAdapter.createList(new Object[0]), null); null; }
/* 105 */   public void logger(String name, Level level, List appenderNames, Boolean additivity) { CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(name)) {
/* 106 */       Logger logger = (Logger)ScriptBytecodeAdapter.castToType(arrayOfCallSite[42].call(ScriptBytecodeAdapter.castToType(arrayOfCallSite[43].callGroovyObjectGetProperty(this), LoggerContext.class), name), Logger.class);
/* 107 */       arrayOfCallSite[44].callCurrent(this, arrayOfCallSite[45].call(new GStringImpl(new Object[] { name }, new String[] { "Setting level of logger [", "] to " }), level));
/* 108 */       Level level1 = level; ScriptBytecodeAdapter.setProperty(level1, null, logger, "level"); Reference aName;
/*     */       Iterator iterator;
/* 110 */       for (aName = new Reference(null), iterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[46].call(appenderNames), Iterator.class); iterator.hasNext(); ) { aName.set(iterator.next());
/* 111 */         Appender appender = (Appender)ScriptBytecodeAdapter.castToType(arrayOfCallSite[47].call(this.appenderList, new _logger_closure1(this, this, aName)), Appender.class);
/* 112 */         if (ScriptBytecodeAdapter.compareNotEqual(appender, null)) {
/* 113 */           arrayOfCallSite[48].callCurrent(this, arrayOfCallSite[49].call(new GStringImpl(new Object[] { aName.get() }, new String[] { "Attaching appender named [", "] to " }), logger));
/* 114 */           arrayOfCallSite[50].call(logger, appender); continue;
/*     */         } 
/* 116 */         arrayOfCallSite[51].callCurrent(this, new GStringImpl(new Object[] { aName.get() }, new String[] { "Failed to find appender named [", "]" })); }
/*     */ 
/*     */ 
/*     */       
/* 120 */       if (ScriptBytecodeAdapter.compareNotEqual(additivity, null)) {
/* 121 */         Boolean bool = additivity; ScriptBytecodeAdapter.setProperty(bool, null, logger, "additive");
/*     */       } 
/*     */     } else {
/* 124 */       arrayOfCallSite[52].callCurrent(this, "No name attribute for logger");
/*     */     }  }
/*     */ 
/*     */   
/*     */   public void appender(String name, Class clazz, Closure closure) {
/* 129 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); arrayOfCallSite[53].callCurrent(this, arrayOfCallSite[54].call(arrayOfCallSite[55].call("About to instantiate appender of type [", arrayOfCallSite[56].callGetProperty(clazz)), "]"));
/* 130 */     Appender appender = (Appender)ScriptBytecodeAdapter.castToType(arrayOfCallSite[57].call(clazz), Appender.class);
/* 131 */     arrayOfCallSite[58].callCurrent(this, arrayOfCallSite[59].call(arrayOfCallSite[60].call("Naming appender as [", name), "]"));
/* 132 */     String str = name; ScriptBytecodeAdapter.setProperty(str, null, appender, "name");
/* 133 */     Object object = arrayOfCallSite[61].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setProperty(object, null, appender, "context");
/* 134 */     arrayOfCallSite[62].call(this.appenderList, appender);
/* 135 */     if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 136 */       AppenderDelegate ad = (AppenderDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[63].callConstructor(AppenderDelegate.class, appender, this.appenderList), AppenderDelegate.class);
/* 137 */       arrayOfCallSite[64].callCurrent(this, ad, appender);
/* 138 */       Object object1 = arrayOfCallSite[65].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, ad, "context");
/* 139 */       AppenderDelegate appenderDelegate1 = ad; ScriptBytecodeAdapter.setGroovyObjectProperty(appenderDelegate1, ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
/* 140 */       Object object2 = arrayOfCallSite[66].callGetProperty(Closure.class); ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
/* 141 */       arrayOfCallSite[67].call(closure);
/*     */     } 
/*     */     
/* 144 */     try { arrayOfCallSite[68].call(appender); } catch (RuntimeException e)
/*     */     
/* 146 */     { arrayOfCallSite[69].callCurrent(this, arrayOfCallSite[70].call(arrayOfCallSite[71].call("Failed to start apppender named [", name), "]"), e); }
/*     */     finally {}
/*     */   }
/*     */   class _logger_closure1 extends Closure implements GeneratedClosure {
/*     */     public _logger_closure1(Object _outerInstance, Object _thisObject, Reference aName) { super(_outerInstance, _thisObject); Reference reference = aName; this.aName = reference; }
/* 151 */     public Object doCall(Object it) { CallSite[] arrayOfCallSite = $getCallSiteArray(); return Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[0].callGetProperty(it), this.aName.get())); } public Object getaName() { CallSite[] arrayOfCallSite = $getCallSiteArray(); return this.aName.get(); } } public void receiver(String name, Class aClass, Closure closure) { CallSite[] arrayOfCallSite = $getCallSiteArray(); arrayOfCallSite[72].callCurrent(this, arrayOfCallSite[73].call(arrayOfCallSite[74].call("About to instantiate receiver of type [", arrayOfCallSite[75].callGetProperty(arrayOfCallSite[76].callGroovyObjectGetProperty(this))), "]"));
/* 152 */     ReceiverBase receiver = (ReceiverBase)ScriptBytecodeAdapter.castToType(arrayOfCallSite[77].call(aClass), ReceiverBase.class);
/* 153 */     Object object = arrayOfCallSite[78].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setProperty(object, null, receiver, "context");
/* 154 */     if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 155 */       ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[79].callConstructor(ComponentDelegate.class, receiver), ComponentDelegate.class);
/* 156 */       Object object1 = arrayOfCallSite[80].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, componentDelegate, "context");
/* 157 */       ComponentDelegate componentDelegate1 = componentDelegate; ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
/* 158 */       Object object2 = arrayOfCallSite[81].callGetProperty(Closure.class); ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
/* 159 */       arrayOfCallSite[82].call(closure);
/*     */     } 
/*     */     
/* 162 */     try { arrayOfCallSite[83].call(receiver); } catch (RuntimeException e)
/*     */     
/* 164 */     { arrayOfCallSite[84].callCurrent(this, arrayOfCallSite[85].call(arrayOfCallSite[86].call("Failed to start receiver of type [", arrayOfCallSite[87].call(aClass)), "]"), e); }
/*     */     finally {} }
/*     */ 
/*     */   
/*     */   private void copyContributions(AppenderDelegate appenderDelegate, Appender appender) {
/* 169 */     Reference reference1 = new Reference(appenderDelegate), reference2 = new Reference(appender); CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((Appender)reference2.get() instanceof ConfigurationContributor)
/* 170 */     { ConfigurationContributor cc = (ConfigurationContributor)ScriptBytecodeAdapter.castToType(reference2.get(), ConfigurationContributor.class);
/* 171 */       arrayOfCallSite[88].call(arrayOfCallSite[89].call(cc), new _copyContributions_closure2(this, this, reference1, reference2)); } 
/* 172 */   } class _copyContributions_closure2 extends Closure implements GeneratedClosure { public _copyContributions_closure2(Object _outerInstance, Object _thisObject, Reference appenderDelegate, Reference appender) { super(_outerInstance, _thisObject); Reference reference1 = appenderDelegate; this.appenderDelegate = reference1; Reference reference2 = appender; this.appender = reference2; } public Object doCall(Object oldName, Object newName) { CallSite[] arrayOfCallSite = $getCallSiteArray(); Closure closure = ScriptBytecodeAdapter.getMethodPointer(this.appender.get(), ShortTypeHandling.castToString(new GStringImpl(new Object[] { oldName }, new String[] { "", "" }))); ScriptBytecodeAdapter.setProperty(closure, null, arrayOfCallSite[0].callGroovyObjectGetProperty(this.appenderDelegate.get()), ShortTypeHandling.castToString(new GStringImpl(new Object[] { newName }, new String[] { "", "" }))); return closure; } public Object call(Object oldName, Object newName) { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return arrayOfCallSite[1].callCurrent((GroovyObject)this, oldName, newName); }
/*     */     public AppenderDelegate getAppenderDelegate() { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (AppenderDelegate)ScriptBytecodeAdapter.castToType(this.appenderDelegate.get(), AppenderDelegate.class); }
/*     */     public Appender getAppender() { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (Appender)ScriptBytecodeAdapter.castToType(this.appender.get(), Appender.class); } }
/* 178 */   public void turboFilter(Class clazz, Closure closure) { CallSite[] arrayOfCallSite = $getCallSiteArray(); arrayOfCallSite[90].callCurrent(this, arrayOfCallSite[91].call(arrayOfCallSite[92].call("About to instantiate turboFilter of type [", arrayOfCallSite[93].callGetProperty(clazz)), "]"));
/* 179 */     TurboFilter turboFilter = (TurboFilter)ScriptBytecodeAdapter.castToType(arrayOfCallSite[94].call(clazz), TurboFilter.class);
/* 180 */     Object object = arrayOfCallSite[95].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setProperty(object, null, turboFilter, "context");
/*     */     
/* 182 */     if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 183 */       ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[96].callConstructor(ComponentDelegate.class, turboFilter), ComponentDelegate.class);
/* 184 */       Object object1 = arrayOfCallSite[97].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, componentDelegate, "context");
/* 185 */       ComponentDelegate componentDelegate1 = componentDelegate; ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ConfigurationDelegate.class, (GroovyObject)closure, "delegate");
/* 186 */       Object object2 = arrayOfCallSite[98].callGetProperty(Closure.class); ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject)closure, "resolveStrategy");
/* 187 */       arrayOfCallSite[99].call(closure);
/*     */     } 
/* 189 */     arrayOfCallSite[100].call(turboFilter);
/* 190 */     arrayOfCallSite[101].callCurrent(this, "Adding aforementioned turbo filter to context");
/* 191 */     arrayOfCallSite[102].call(arrayOfCallSite[103].callGroovyObjectGetProperty(this), turboFilter); }
/*     */ 
/*     */   
/*     */   public String timestamp(String datePattern, long timeReference) {
/* 195 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); long now = DefaultTypeTransformation.longUnbox(Integer.valueOf(-1));
/*     */     
/* 197 */     if (ScriptBytecodeAdapter.compareEqual(Long.valueOf(timeReference), Integer.valueOf(-1))) {
/* 198 */       arrayOfCallSite[104].callCurrent(this, "Using current interpretation time, i.e. now, as time reference.");
/* 199 */       Object object = arrayOfCallSite[105].call(System.class); now = DefaultTypeTransformation.longUnbox(object);
/*     */     } else {
/* 201 */       long l = timeReference;
/* 202 */       arrayOfCallSite[106].callCurrent(this, arrayOfCallSite[107].call(arrayOfCallSite[108].call("Using ", Long.valueOf(now)), " as time reference."));
/*     */     } 
/* 204 */     CachingDateFormatter sdf = (CachingDateFormatter)ScriptBytecodeAdapter.castToType(arrayOfCallSite[109].callConstructor(CachingDateFormatter.class, datePattern), CachingDateFormatter.class);
/* 205 */     return ShortTypeHandling.castToString(arrayOfCallSite[110].call(sdf, Long.valueOf(now)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void jmxConfigurator(String name) {
/* 216 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); Object objectName = null;
/* 217 */     Object contextName = arrayOfCallSite[111].callGetProperty(arrayOfCallSite[112].callGroovyObjectGetProperty(this));
/* 218 */     if (ScriptBytecodeAdapter.compareNotEqual(name, null)) {
/*     */ 
/*     */       
/* 221 */       try { Object object = arrayOfCallSite[113].callConstructor(ObjectName.class, name); } catch (MalformedObjectNameException e)
/*     */       
/* 223 */       { String str = name; }
/*     */       finally {}
/*     */     }
/* 226 */     if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 227 */       Object objectNameAsStr = arrayOfCallSite[114].call(MBeanUtil.class, contextName, JMXConfigurator.class);
/* 228 */       Object object1 = arrayOfCallSite[115].call(MBeanUtil.class, arrayOfCallSite[116].callGroovyObjectGetProperty(this), this, objectNameAsStr);
/* 229 */       if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 230 */         arrayOfCallSite[117].callCurrent(this, new GStringImpl(new Object[] { objectNameAsStr }, new String[] { "Failed to construct ObjectName for [", "]" }));
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 235 */     Object platformMBeanServer = arrayOfCallSite[118].callGetProperty(ManagementFactory.class);
/* 236 */     if (!DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[119].call(MBeanUtil.class, platformMBeanServer, objectName))) {
/* 237 */       JMXConfigurator jmxConfigurator = (JMXConfigurator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[120].callConstructor(JMXConfigurator.class, ScriptBytecodeAdapter.createPojoWrapper(ScriptBytecodeAdapter.castToType(arrayOfCallSite[121].callGroovyObjectGetProperty(this), LoggerContext.class), LoggerContext.class), platformMBeanServer, objectName), JMXConfigurator.class);
/*     */       
/* 239 */       try { arrayOfCallSite[122].call(platformMBeanServer, jmxConfigurator, objectName); } catch (Exception all)
/*     */       
/* 241 */       { arrayOfCallSite[123].callCurrent(this, "Failed to create mbean", all); }
/*     */       finally {}
/*     */     } 
/*     */   }
/*     */   
/*     */   public void scan() {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
/*     */       scan(null);
/*     */       null;
/*     */       return;
/*     */     } 
/*     */     scan(null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level, List<String> appenderNames) {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     logger(name, level, appenderNames, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void appender(String name, Class clazz) {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     appender(name, clazz, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void receiver(String name, Class aClass) {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     receiver(name, aClass, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void turboFilter(Class clazz) {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     turboFilter(clazz, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public String timestamp(String datePattern) {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     return (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) ? timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1))) : timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1)));
/*     */   }
/*     */   
/*     */   public void jmxConfigurator() {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
/*     */       jmxConfigurator(null);
/*     */       null;
/*     */       return;
/*     */     } 
/*     */     jmxConfigurator(null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public List<Appender> getAppenderList() {
/*     */     return this.appenderList;
/*     */   }
/*     */   
/*     */   public void setAppenderList(List<Appender> paramList) {
/*     */     this.appenderList = paramList;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/gaffer/ConfigurationDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */