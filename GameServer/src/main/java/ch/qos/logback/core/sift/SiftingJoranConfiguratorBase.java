package ch.qos.logback.core.sift;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.DefinePropertyAction;
import ch.qos.logback.core.joran.action.ImplicitAction;
import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
import ch.qos.logback.core.joran.action.PropertyAction;
import ch.qos.logback.core.joran.action.TimestampAction;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.joran.spi.RuleStore;
import java.util.List;
import java.util.Map;

public abstract class SiftingJoranConfiguratorBase<E>
extends GenericConfigurator
{
protected final String key;
protected final String value;
protected final Map<String, String> parentPropertyMap;
static final String ONE_AND_ONLY_ONE_URL = "http:
int errorEmmissionCount;

protected void addImplicitRules(Interpreter interpreter) {
NestedComplexPropertyIA nestedComplexIA = new NestedComplexPropertyIA();
nestedComplexIA.setContext(this.context);
interpreter.addImplicitAction((ImplicitAction)nestedComplexIA);
NestedBasicPropertyIA nestedSimpleIA = new NestedBasicPropertyIA();
nestedSimpleIA.setContext(this.context);
interpreter.addImplicitAction((ImplicitAction)nestedSimpleIA);
}

protected SiftingJoranConfiguratorBase(String key, String value, Map<String, String> parentPropertyMap) {
this.errorEmmissionCount = 0;
this.key = key;
this.value = value;
this.parentPropertyMap = parentPropertyMap; } protected void oneAndOnlyOneCheck(Map<?, ?> appenderMap) { String errMsg = null;
if (appenderMap.size() == 0) {
this.errorEmmissionCount++;
errMsg = "No nested appenders found within the <sift> element in SiftingAppender.";
} else if (appenderMap.size() > 1) {
this.errorEmmissionCount++;
errMsg = "Only and only one appender can be nested the <sift> element in SiftingAppender. See also http:
} 

if (errMsg != null && this.errorEmmissionCount < 4) {
addError(errMsg);
} }

public void doConfigure(List<SaxEvent> eventList) throws JoranException {
super.doConfigure(eventList); } protected void addInstanceRules(RuleStore rs) {
rs.addRule(new ElementSelector("configuration/property"), (Action)new PropertyAction());
rs.addRule(new ElementSelector("configuration/timestamp"), (Action)new TimestampAction());
rs.addRule(new ElementSelector("configuration/define"), (Action)new DefinePropertyAction());
} public abstract Appender<E> getAppender(); public String toString() {
return getClass().getName() + "{" + this.key + "=" + this.value + '}';
}
}

