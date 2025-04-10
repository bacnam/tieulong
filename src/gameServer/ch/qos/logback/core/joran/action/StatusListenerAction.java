package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

public class StatusListenerAction
extends Action
{
boolean inError = false;
StatusListener statusListener = null;

public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
this.inError = false;
String className = attributes.getValue("class");
if (OptionHelper.isEmpty(className)) {
addError("Missing class name for statusListener. Near [" + name + "] line " + getLineNumber(ec));

this.inError = true;

return;
} 
try {
this.statusListener = (StatusListener)OptionHelper.instantiateByClassName(className, StatusListener.class, this.context);

ec.getContext().getStatusManager().add(this.statusListener);
if (this.statusListener instanceof ContextAware) {
((ContextAware)this.statusListener).setContext(this.context);
}
addInfo("Added status listener of type [" + className + "]");
ec.pushObject(this.statusListener);
} catch (Exception e) {
this.inError = true;
addError("Could not create an StatusListener of type [" + className + "].", e);

throw new ActionException(e);
} 
}

public void finish(InterpretationContext ec) {}

public void end(InterpretationContext ec, String e) {
if (this.inError) {
return;
}
if (this.statusListener instanceof LifeCycle) {
((LifeCycle)this.statusListener).start();
}
Object o = ec.peekObject();
if (o != this.statusListener) {
addWarn("The object at the of the stack is not the statusListener pushed earlier.");
} else {
ec.popObject();
} 
}
}

