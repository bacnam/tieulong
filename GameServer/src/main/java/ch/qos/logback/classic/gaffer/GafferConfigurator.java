package ch.qos.logback.classic.gaffer;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.ContextUtil;
import ch.qos.logback.core.util.OptionHelper;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import java.net.URL;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GafferConfigurator implements GroovyObject {
private LoggerContext context;

public GafferConfigurator(LoggerContext context) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); MetaClass metaClass = $getStaticMetaClass(); Object object = SYNTHETIC_LOCAL_VARIABLE_1; this.context = (LoggerContext)ScriptBytecodeAdapter.castToType(object, LoggerContext.class);
}
private static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback.debug";
protected void informContextOfURLUsedForConfiguration(URL url) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); arrayOfCallSite[0].call(ConfigurationWatchListUtil.class, this.context, url);
}

public void run(URL url) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { informContextOfURLUsedForConfiguration(url); null; } else { arrayOfCallSite[1].callCurrent(this, url); }
arrayOfCallSite[2].callCurrent(this, arrayOfCallSite[3].callGetProperty(url));
}

public void run(File file) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); arrayOfCallSite[4].callCurrent(this, arrayOfCallSite[5].call(arrayOfCallSite[6].call(file)));
arrayOfCallSite[7].callCurrent(this, arrayOfCallSite[8].callGetProperty(file));
}

public void run(String dslText) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); Binding binding = (Binding)ScriptBytecodeAdapter.castToType(arrayOfCallSite[9].callConstructor(Binding.class), Binding.class);
arrayOfCallSite[10].call(binding, "hostname", arrayOfCallSite[11].callGetProperty(ContextUtil.class));

Object configuration = arrayOfCallSite[12].callConstructor(CompilerConfiguration.class);
if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { arrayOfCallSite[15].call(configuration, importCustomizer()); } else { arrayOfCallSite[13].call(configuration, arrayOfCallSite[14].callCurrent(this)); }

String debugAttrib = ShortTypeHandling.castToString(arrayOfCallSite[16].call(System.class, DEBUG_SYSTEM_PROPERTY_KEY));
if (!((((DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[17].call(OptionHelper.class, debugAttrib)) || DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[18].call(debugAttrib, "false")))) || 
DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[19].call(debugAttrib, "null"))) ? 1 : 0))
{

arrayOfCallSite[20].call(OnConsoleStatusListener.class, this.context);
}

arrayOfCallSite[21].call(arrayOfCallSite[22].callConstructor(ContextUtil.class, this.context), arrayOfCallSite[23].call(this.context));

Reference dslScript = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].call(arrayOfCallSite[25].callConstructor(GroovyShell.class, binding, configuration), dslText), Script.class));

arrayOfCallSite[26].call(arrayOfCallSite[27].callGroovyObjectGetProperty(dslScript.get()), ConfigurationDelegate.class);
arrayOfCallSite[28].call(dslScript.get(), this.context);
_run_closure1 _run_closure1 = new _run_closure1(this, this, dslScript); ScriptBytecodeAdapter.setProperty(_run_closure1, null, arrayOfCallSite[29].callGroovyObjectGetProperty(dslScript.get()), "getDeclaredOrigin");

arrayOfCallSite[30].call(dslScript.get());
}

protected ImportCustomizer importCustomizer() {
CallSite[] arrayOfCallSite = $getCallSiteArray(); Object customizer = arrayOfCallSite[31].callConstructor(ImportCustomizer.class);

Object core = "ch.qos.logback.core";
arrayOfCallSite[32].call(customizer, ArrayUtil.createArray(core, new GStringImpl(new Object[] { core }, new String[] { "", ".encoder" }), new GStringImpl(new Object[] { core }, new String[] { "", ".read" }), new GStringImpl(new Object[] { core }, new String[] { "", ".rolling" }), new GStringImpl(new Object[] { core }, new String[] { "", ".status" }), "ch.qos.logback.classic.net"));

arrayOfCallSite[33].call(customizer, arrayOfCallSite[34].callGetProperty(PatternLayoutEncoder.class));

arrayOfCallSite[35].call(customizer, arrayOfCallSite[36].callGetProperty(Level.class));

arrayOfCallSite[37].call(customizer, "off", arrayOfCallSite[38].callGetProperty(Level.class), "OFF");
arrayOfCallSite[39].call(customizer, "error", arrayOfCallSite[40].callGetProperty(Level.class), "ERROR");
arrayOfCallSite[41].call(customizer, "warn", arrayOfCallSite[42].callGetProperty(Level.class), "WARN");
arrayOfCallSite[43].call(customizer, "info", arrayOfCallSite[44].callGetProperty(Level.class), "INFO");
arrayOfCallSite[45].call(customizer, "debug", arrayOfCallSite[46].callGetProperty(Level.class), "DEBUG");
arrayOfCallSite[47].call(customizer, "trace", arrayOfCallSite[48].callGetProperty(Level.class), "TRACE");
arrayOfCallSite[49].call(customizer, "all", arrayOfCallSite[50].callGetProperty(Level.class), "ALL");

return (ImportCustomizer)ScriptBytecodeAdapter.castToType(customizer, ImportCustomizer.class);
}

class _run_closure1 extends Closure implements GeneratedClosure {
public _run_closure1(Object _outerInstance, Object _thisObject, Reference dslScript) {
super(_outerInstance, _thisObject);
Reference reference = dslScript;
this.dslScript = reference;
}

public Object doCall(Object it) {
CallSite[] arrayOfCallSite = $getCallSiteArray();
return this.dslScript.get();
}

public Script getDslScript() {
CallSite[] arrayOfCallSite = $getCallSiteArray();
return (Script)ScriptBytecodeAdapter.castToType(this.dslScript.get(), Script.class);
}

public Object doCall() {
CallSite[] arrayOfCallSite = $getCallSiteArray();
return doCall(null);
}
}

public LoggerContext getContext() {
return this.context;
}

public void setContext(LoggerContext paramLoggerContext) {
this.context = paramLoggerContext;
}

public static final String getDEBUG_SYSTEM_PROPERTY_KEY() {
return DEBUG_SYSTEM_PROPERTY_KEY;
}
}

