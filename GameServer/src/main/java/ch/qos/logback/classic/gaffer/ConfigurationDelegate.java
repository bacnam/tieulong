package ch.qos.logback.classic.gaffer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jmx.MBeanUtil;
import ch.qos.logback.classic.net.ReceiverBase;
import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.util.CachingDateFormatter;
import ch.qos.logback.core.util.Duration;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;

import java.lang.management.ManagementFactory;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
import org.slf4j.Logger;

public class ConfigurationDelegate extends ContextAwareBase implements GroovyObject {
    private List<Appender> appenderList;

    public ConfigurationDelegate() {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        List<Appender> list = ScriptBytecodeAdapter.createList(new Object[0]);
        MetaClass metaClass = $getStaticMetaClass();
    }

    public Object getDeclaredOrigin() {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return this;
    }

    public void scan(String scanPeriodStr) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ReconfigureOnChangeFilter rocf = (ReconfigureOnChangeFilter) ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(ReconfigureOnChangeFilter.class), ReconfigureOnChangeFilter.class);
        arrayOfCallSite[1].call(rocf, arrayOfCallSite[2].callGroovyObjectGetProperty(this));
        if (DefaultTypeTransformation.booleanUnbox(scanPeriodStr)) {

            try {
                Duration duration = (Duration) ScriptBytecodeAdapter.castToType(arrayOfCallSite[3].call(Duration.class, scanPeriodStr), Duration.class);
                arrayOfCallSite[4].call(rocf, arrayOfCallSite[5].call(duration));
                arrayOfCallSite[6].callCurrent(this, arrayOfCallSite[7].call("Setting ReconfigureOnChangeFilter scanning period to ", duration));
            } catch (NumberFormatException nfe) {
                arrayOfCallSite[8].callCurrent(this, arrayOfCallSite[9].call(arrayOfCallSite[10].call("Error while converting [", arrayOfCallSite[11].callGroovyObjectGetProperty(this)), "] to long"), nfe);
            } finally {
            }
        }
        arrayOfCallSite[12].call(rocf);
        arrayOfCallSite[13].callCurrent(this, "Adding ReconfigureOnChangeFilter as a turbo filter");
        arrayOfCallSite[14].call(arrayOfCallSite[15].callGroovyObjectGetProperty(this), rocf);
    }

    public void statusListener(Class listenerClass) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        StatusListener statusListener = (StatusListener) ScriptBytecodeAdapter.castToType(arrayOfCallSite[16].call(listenerClass), StatusListener.class);
        arrayOfCallSite[17].call(arrayOfCallSite[18].callGetProperty(arrayOfCallSite[19].callGroovyObjectGetProperty(this)), statusListener);
        if (statusListener instanceof ContextAware) {
            arrayOfCallSite[20].call(ScriptBytecodeAdapter.castToType(statusListener, ContextAware.class), arrayOfCallSite[21].callGroovyObjectGetProperty(this));
        }
        if (statusListener instanceof LifeCycle) {
            arrayOfCallSite[22].call(ScriptBytecodeAdapter.castToType(statusListener, LifeCycle.class));
        }
        arrayOfCallSite[23].callCurrent(this, new GStringImpl(new Object[]{arrayOfCallSite[24].callGetProperty(listenerClass)}, new String[]{"Added status listener of type [", "]"}));
    }

    public void conversionRule(String conversionWord, Class converterClass) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        String converterClassName = ShortTypeHandling.castToString(arrayOfCallSite[25].call(converterClass));

        Map ruleRegistry = (Map) ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].call(arrayOfCallSite[27].callGroovyObjectGetProperty(this), arrayOfCallSite[28].callGetProperty(CoreConstants.class)), Map.class);
        if (ScriptBytecodeAdapter.compareEqual(ruleRegistry, null)) {
            Object object = arrayOfCallSite[29].callConstructor(HashMap.class);
            ruleRegistry = (Map) ScriptBytecodeAdapter.castToType(object, Map.class);
            arrayOfCallSite[30].call(arrayOfCallSite[31].callGroovyObjectGetProperty(this), arrayOfCallSite[32].callGetProperty(CoreConstants.class), ruleRegistry);
        }

        arrayOfCallSite[33].callCurrent(this, arrayOfCallSite[34].call(arrayOfCallSite[35].call(arrayOfCallSite[36].call(arrayOfCallSite[37].call("registering conversion word ", conversionWord), " with class ["), converterClassName), "]"));
        arrayOfCallSite[38].call(ruleRegistry, conversionWord, converterClassName);
    }

    public void root(Level level) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        root(level, ScriptBytecodeAdapter.createList(new Object[0]));
    }

    public void root(Level level, List appenderNames) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if (ScriptBytecodeAdapter.compareEqual(level, null)) {
            arrayOfCallSite[39].callCurrent(this, "Root logger cannot be set to level null");
        } else {
            arrayOfCallSite[40].callCurrent(this, arrayOfCallSite[41].callGetProperty(Logger.class), level, appenderNames);
        }
    }

    public void logger(String name, Level level) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        logger(name, level, ScriptBytecodeAdapter.createList(new Object[0]), null);
    }

    public void logger(String name, Level level, List appenderNames, Boolean additivity) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if (DefaultTypeTransformation.booleanUnbox(name)) {
            Logger logger = (Logger) ScriptBytecodeAdapter.castToType(arrayOfCallSite[42].call(ScriptBytecodeAdapter.castToType(arrayOfCallSite[43].callGroovyObjectGetProperty(this), LoggerContext.class), name), Logger.class);
            arrayOfCallSite[44].callCurrent(this, arrayOfCallSite[45].call(new GStringImpl(new Object[]{name}, new String[]{"Setting level of logger [", "] to "}), level));
            Level level1 = level;
            ScriptBytecodeAdapter.setProperty(level1, null, logger, "level");
            Reference aName;
            Iterator iterator;
            for (aName = new Reference(null), iterator = (Iterator) ScriptBytecodeAdapter.castToType(arrayOfCallSite[46].call(appenderNames), Iterator.class); iterator.hasNext(); ) {
                aName.set(iterator.next());
                Appender appender = (Appender) ScriptBytecodeAdapter.castToType(arrayOfCallSite[47].call(this.appenderList, new _logger_closure1(this, this, aName)), Appender.class);
                if (ScriptBytecodeAdapter.compareNotEqual(appender, null)) {
                    arrayOfCallSite[48].callCurrent(this, arrayOfCallSite[49].call(new GStringImpl(new Object[]{aName.get()}, new String[]{"Attaching appender named [", "] to "}), logger));
                    arrayOfCallSite[50].call(logger, appender);
                    continue;
                }
                arrayOfCallSite[51].callCurrent(this, new GStringImpl(new Object[]{aName.get()}, new String[]{"Failed to find appender named [", "]"}));
            }

            if (ScriptBytecodeAdapter.compareNotEqual(additivity, null)) {
                Boolean bool = additivity;
                ScriptBytecodeAdapter.setProperty(bool, null, logger, "additive");
            }
        } else {
            arrayOfCallSite[52].callCurrent(this, "No name attribute for logger");
        }
    }

    public void appender(String name, Class clazz, Closure closure) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[53].callCurrent(this, arrayOfCallSite[54].call(arrayOfCallSite[55].call("About to instantiate appender of type [", arrayOfCallSite[56].callGetProperty(clazz)), "]"));
        Appender appender = (Appender) ScriptBytecodeAdapter.castToType(arrayOfCallSite[57].call(clazz), Appender.class);
        arrayOfCallSite[58].callCurrent(this, arrayOfCallSite[59].call(arrayOfCallSite[60].call("Naming appender as [", name), "]"));
        String str = name;
        ScriptBytecodeAdapter.setProperty(str, null, appender, "name");
        Object object = arrayOfCallSite[61].callGroovyObjectGetProperty(this);
        ScriptBytecodeAdapter.setProperty(object, null, appender, "context");
        arrayOfCallSite[62].call(this.appenderList, appender);
        if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
            AppenderDelegate ad = (AppenderDelegate) ScriptBytecodeAdapter.castToType(arrayOfCallSite[63].callConstructor(AppenderDelegate.class, appender, this.appenderList), AppenderDelegate.class);
            arrayOfCallSite[64].callCurrent(this, ad, appender);
            Object object1 = arrayOfCallSite[65].callGroovyObjectGetProperty(this);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, ad, "context");
            AppenderDelegate appenderDelegate1 = ad;
            ScriptBytecodeAdapter.setGroovyObjectProperty(appenderDelegate1, ConfigurationDelegate.class, (GroovyObject) closure, "delegate");
            Object object2 = arrayOfCallSite[66].callGetProperty(Closure.class);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject) closure, "resolveStrategy");
            arrayOfCallSite[67].call(closure);
        }

        try {
            arrayOfCallSite[68].call(appender);
        } catch (RuntimeException e) {
            arrayOfCallSite[69].callCurrent(this, arrayOfCallSite[70].call(arrayOfCallSite[71].call("Failed to start apppender named [", name), "]"), e);
        } finally {
        }
    }

    class _logger_closure1 extends Closure implements GeneratedClosure {
        public _logger_closure1(Object _outerInstance, Object _thisObject, Reference aName) {
            super(_outerInstance, _thisObject);
            Reference reference = aName;
            this.aName = reference;
        }

        public Object doCall(Object it) {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[0].callGetProperty(it), this.aName.get()));
        }

        public Object getaName() {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return this.aName.get();
        }
    }

    public void receiver(String name, Class aClass, Closure closure) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[72].callCurrent(this, arrayOfCallSite[73].call(arrayOfCallSite[74].call("About to instantiate receiver of type [", arrayOfCallSite[75].callGetProperty(arrayOfCallSite[76].callGroovyObjectGetProperty(this))), "]"));
        ReceiverBase receiver = (ReceiverBase) ScriptBytecodeAdapter.castToType(arrayOfCallSite[77].call(aClass), ReceiverBase.class);
        Object object = arrayOfCallSite[78].callGroovyObjectGetProperty(this);
        ScriptBytecodeAdapter.setProperty(object, null, receiver, "context");
        if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
            ComponentDelegate componentDelegate = (ComponentDelegate) ScriptBytecodeAdapter.castToType(arrayOfCallSite[79].callConstructor(ComponentDelegate.class, receiver), ComponentDelegate.class);
            Object object1 = arrayOfCallSite[80].callGroovyObjectGetProperty(this);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, componentDelegate, "context");
            ComponentDelegate componentDelegate1 = componentDelegate;
            ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ConfigurationDelegate.class, (GroovyObject) closure, "delegate");
            Object object2 = arrayOfCallSite[81].callGetProperty(Closure.class);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject) closure, "resolveStrategy");
            arrayOfCallSite[82].call(closure);
        }

        try {
            arrayOfCallSite[83].call(receiver);
        } catch (RuntimeException e) {
            arrayOfCallSite[84].callCurrent(this, arrayOfCallSite[85].call(arrayOfCallSite[86].call("Failed to start receiver of type [", arrayOfCallSite[87].call(aClass)), "]"), e);
        } finally {
        }
    }

    private void copyContributions(AppenderDelegate appenderDelegate, Appender appender) {
        Reference reference1 = new Reference(appenderDelegate), reference2 = new Reference(appender);
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if ((Appender) reference2.get() instanceof ConfigurationContributor) {
            ConfigurationContributor cc = (ConfigurationContributor) ScriptBytecodeAdapter.castToType(reference2.get(), ConfigurationContributor.class);
            arrayOfCallSite[88].call(arrayOfCallSite[89].call(cc), new _copyContributions_closure2(this, this, reference1, reference2));
        }
    }

    class _copyContributions_closure2 extends Closure implements GeneratedClosure {
        public _copyContributions_closure2(Object _outerInstance, Object _thisObject, Reference appenderDelegate, Reference appender) {
            super(_outerInstance, _thisObject);
            Reference reference1 = appenderDelegate;
            this.appenderDelegate = reference1;
            Reference reference2 = appender;
            this.appender = reference2;
        }

        public Object doCall(Object oldName, Object newName) {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            Closure closure = ScriptBytecodeAdapter.getMethodPointer(this.appender.get(), ShortTypeHandling.castToString(new GStringImpl(new Object[]{oldName}, new String[]{"", ""})));
            ScriptBytecodeAdapter.setProperty(closure, null, arrayOfCallSite[0].callGroovyObjectGetProperty(this.appenderDelegate.get()), ShortTypeHandling.castToString(new GStringImpl(new Object[]{newName}, new String[]{"", ""})));
            return closure;
        }

        public Object call(Object oldName, Object newName) {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return arrayOfCallSite[1].callCurrent((GroovyObject) this, oldName, newName);
        }

        public AppenderDelegate getAppenderDelegate() {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return (AppenderDelegate) ScriptBytecodeAdapter.castToType(this.appenderDelegate.get(), AppenderDelegate.class);
        }

        public Appender getAppender() {
            CallSite[] arrayOfCallSite = $getCallSiteArray();
            return (Appender) ScriptBytecodeAdapter.castToType(this.appender.get(), Appender.class);
        }
    }

    public void turboFilter(Class clazz, Closure closure) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[90].callCurrent(this, arrayOfCallSite[91].call(arrayOfCallSite[92].call("About to instantiate turboFilter of type [", arrayOfCallSite[93].callGetProperty(clazz)), "]"));
        TurboFilter turboFilter = (TurboFilter) ScriptBytecodeAdapter.castToType(arrayOfCallSite[94].call(clazz), TurboFilter.class);
        Object object = arrayOfCallSite[95].callGroovyObjectGetProperty(this);
        ScriptBytecodeAdapter.setProperty(object, null, turboFilter, "context");

        if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
            ComponentDelegate componentDelegate = (ComponentDelegate) ScriptBytecodeAdapter.castToType(arrayOfCallSite[96].callConstructor(ComponentDelegate.class, turboFilter), ComponentDelegate.class);
            Object object1 = arrayOfCallSite[97].callGroovyObjectGetProperty(this);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ConfigurationDelegate.class, componentDelegate, "context");
            ComponentDelegate componentDelegate1 = componentDelegate;
            ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ConfigurationDelegate.class, (GroovyObject) closure, "delegate");
            Object object2 = arrayOfCallSite[98].callGetProperty(Closure.class);
            ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ConfigurationDelegate.class, (GroovyObject) closure, "resolveStrategy");
            arrayOfCallSite[99].call(closure);
        }
        arrayOfCallSite[100].call(turboFilter);
        arrayOfCallSite[101].callCurrent(this, "Adding aforementioned turbo filter to context");
        arrayOfCallSite[102].call(arrayOfCallSite[103].callGroovyObjectGetProperty(this), turboFilter);
    }

    public String timestamp(String datePattern, long timeReference) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        long now = DefaultTypeTransformation.longUnbox(Integer.valueOf(-1));

        if (ScriptBytecodeAdapter.compareEqual(Long.valueOf(timeReference), Integer.valueOf(-1))) {
            arrayOfCallSite[104].callCurrent(this, "Using current interpretation time, i.e. now, as time reference.");
            Object object = arrayOfCallSite[105].call(System.class);
            now = DefaultTypeTransformation.longUnbox(object);
        } else {
            long l = timeReference;
            arrayOfCallSite[106].callCurrent(this, arrayOfCallSite[107].call(arrayOfCallSite[108].call("Using ", Long.valueOf(now)), " as time reference."));
        }
        CachingDateFormatter sdf = (CachingDateFormatter) ScriptBytecodeAdapter.castToType(arrayOfCallSite[109].callConstructor(CachingDateFormatter.class, datePattern), CachingDateFormatter.class);
        return ShortTypeHandling.castToString(arrayOfCallSite[110].call(sdf, Long.valueOf(now)));
    }

    public void jmxConfigurator(String name) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Object objectName = null;
        Object contextName = arrayOfCallSite[111].callGetProperty(arrayOfCallSite[112].callGroovyObjectGetProperty(this));
        if (ScriptBytecodeAdapter.compareNotEqual(name, null)) {

            try {
                Object object = arrayOfCallSite[113].callConstructor(ObjectName.class, name);
            } catch (MalformedObjectNameException e) {
                String str = name;
            } finally {
            }
        }
        if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
            Object objectNameAsStr = arrayOfCallSite[114].call(MBeanUtil.class, contextName, JMXConfigurator.class);
            Object object1 = arrayOfCallSite[115].call(MBeanUtil.class, arrayOfCallSite[116].callGroovyObjectGetProperty(this), this, objectNameAsStr);
            if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
                arrayOfCallSite[117].callCurrent(this, new GStringImpl(new Object[]{objectNameAsStr}, new String[]{"Failed to construct ObjectName for [", "]"}));

                return;
            }
        }
        Object platformMBeanServer = arrayOfCallSite[118].callGetProperty(ManagementFactory.class);
        if (!DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[119].call(MBeanUtil.class, platformMBeanServer, objectName))) {
            JMXConfigurator jmxConfigurator = (JMXConfigurator) ScriptBytecodeAdapter.castToType(arrayOfCallSite[120].callConstructor(JMXConfigurator.class, ScriptBytecodeAdapter.createPojoWrapper(ScriptBytecodeAdapter.castToType(arrayOfCallSite[121].callGroovyObjectGetProperty(this), LoggerContext.class), LoggerContext.class), platformMBeanServer, objectName), JMXConfigurator.class);

            try {
                arrayOfCallSite[122].call(platformMBeanServer, jmxConfigurator, objectName);
            } catch (Exception all) {
                arrayOfCallSite[123].callCurrent(this, "Failed to create mbean", all);
            } finally {
            }
        }
    }

    public void scan() {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            scan(null);
            return;
        }
        scan(null);
    }

    public void logger(String name, Level level, List<String> appenderNames) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        logger(name, level, appenderNames, null);
    }

    public void appender(String name, Class clazz) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        appender(name, clazz, null);
    }

    public void receiver(String name, Class aClass) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        receiver(name, aClass, null);
    }

    public void turboFilter(Class clazz) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        turboFilter(clazz, null);
    }

    public String timestamp(String datePattern) {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) ? timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1))) : timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1)));
    }

    public void jmxConfigurator() {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) {
            jmxConfigurator(null);
            return;
        }
        jmxConfigurator(null);
    }

    public List<Appender> getAppenderList() {
        return this.appenderList;
    }

    public void setAppenderList(List<Appender> paramList) {
        this.appenderList = paramList;
    }
}

