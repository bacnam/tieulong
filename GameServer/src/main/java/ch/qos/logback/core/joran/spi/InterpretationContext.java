package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.util.OptionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import org.xml.sax.Locator;

public class InterpretationContext
extends ContextAwareBase
implements PropertyContainer
{
Stack<Object> objectStack;
Map<String, Object> objectMap;
Map<String, String> propertiesMap;
Interpreter joranInterpreter;
final List<InPlayListener> listenerList = new ArrayList<InPlayListener>();
DefaultNestedComponentRegistry defaultNestedComponentRegistry = new DefaultNestedComponentRegistry();

public InterpretationContext(Context context, Interpreter joranInterpreter) {
this.context = context;
this.joranInterpreter = joranInterpreter;
this.objectStack = new Stack();
this.objectMap = new HashMap<String, Object>(5);
this.propertiesMap = new HashMap<String, String>(5);
}

public DefaultNestedComponentRegistry getDefaultNestedComponentRegistry() {
return this.defaultNestedComponentRegistry;
}

public Map<String, String> getCopyOfPropertyMap() {
return new HashMap<String, String>(this.propertiesMap);
}

void setPropertiesMap(Map<String, String> propertiesMap) {
this.propertiesMap = propertiesMap;
}

String updateLocationInfo(String msg) {
Locator locator = this.joranInterpreter.getLocator();

if (locator != null) {
return msg + locator.getLineNumber() + ":" + locator.getColumnNumber();
}
return msg;
}

public Locator getLocator() {
return this.joranInterpreter.getLocator();
}

public Interpreter getJoranInterpreter() {
return this.joranInterpreter;
}

public Stack<Object> getObjectStack() {
return this.objectStack;
}

public boolean isEmpty() {
return this.objectStack.isEmpty();
}

public Object peekObject() {
return this.objectStack.peek();
}

public void pushObject(Object o) {
this.objectStack.push(o);
}

public Object popObject() {
return this.objectStack.pop();
}

public Object getObject(int i) {
return this.objectStack.get(i);
}

public Map<String, Object> getObjectMap() {
return this.objectMap;
}

public void addSubstitutionProperty(String key, String value) {
if (key == null || value == null) {
return;
}

value = value.trim();
this.propertiesMap.put(key, value);
}

public void addSubstitutionProperties(Properties props) {
if (props == null) {
return;
}
for (Object keyObject : props.keySet()) {
String key = (String)keyObject;
String val = props.getProperty(key);
addSubstitutionProperty(key, val);
} 
}

public String getProperty(String key) {
String v = this.propertiesMap.get(key);
if (v != null) {
return v;
}
return this.context.getProperty(key);
}

public String subst(String value) {
if (value == null) {
return null;
}
return OptionHelper.substVars(value, this, (PropertyContainer)this.context);
}

public boolean isListenerListEmpty() {
return this.listenerList.isEmpty();
}

public void addInPlayListener(InPlayListener ipl) {
if (this.listenerList.contains(ipl)) {
addWarn("InPlayListener " + ipl + " has been already registered");
} else {
this.listenerList.add(ipl);
} 
}

public boolean removeInPlayListener(InPlayListener ipl) {
return this.listenerList.remove(ipl);
}

void fireInPlay(SaxEvent event) {
for (InPlayListener ipl : this.listenerList)
ipl.inPlay(event); 
}
}

