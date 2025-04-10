package ch.qos.logback.classic.gaffer;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachable;
import groovy.lang.Closure;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AppenderDelegate
extends ComponentDelegate
{
private Map<String, Appender<?>> appendersByName;

public AppenderDelegate(Appender appender) {
super(appender); Map<String, Appender<?>> map = ScriptBytecodeAdapter.createMap(new Object[0]);
}

public AppenderDelegate(Appender appender, List appenders) {
super(appender); Map<String, Appender<?>> map = ScriptBytecodeAdapter.createMap(new Object[0]);
Object object = arrayOfCallSite[0].call(appenders, new _closure1(this, this)); this.appendersByName = (Map<String, Appender<?>>)ScriptBytecodeAdapter.castToType(object, Map.class); } class _closure1 extends Closure implements GeneratedClosure { public Object doCall(Object it) { CallSite[] arrayOfCallSite = $getCallSiteArray(); return ScriptBytecodeAdapter.createMap(new Object[] { arrayOfCallSite[0].callGetProperty(it), it }); }
public _closure1(Object _outerInstance, Object _thisObject) { super(_outerInstance, _thisObject); } public Object doCall() { CallSite[] arrayOfCallSite = $getCallSiteArray();
return doCall(null); } }
public String getLabel() {
CallSite[] arrayOfCallSite = $getCallSiteArray(); return "appender";
}

public void appenderRef(String name) {
CallSite[] arrayOfCallSite = $getCallSiteArray(); if (!DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(AppenderAttachable.class, arrayOfCallSite[2].callGetProperty(arrayOfCallSite[3].callGroovyObjectGetProperty(this))))) {
Object errorMessage = arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].callGetProperty(arrayOfCallSite[8].callGetProperty(arrayOfCallSite[9].callGroovyObjectGetProperty(this))), " does not implement "), arrayOfCallSite[10].callGetProperty(AppenderAttachable.class)), ".");
throw (Throwable)arrayOfCallSite[11].callConstructor(IllegalArgumentException.class, errorMessage);
} 
arrayOfCallSite[12].call(arrayOfCallSite[13].callGroovyObjectGetProperty(this), arrayOfCallSite[14].call(this.appendersByName, name));
}

public Map<String, Appender<?>> getAppendersByName() {
return this.appendersByName;
}

public void setAppendersByName(Map<String, Appender<?>> paramMap) {
this.appendersByName = paramMap;
}
}

