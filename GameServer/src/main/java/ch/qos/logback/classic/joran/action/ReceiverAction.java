package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.net.ReceiverBase;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

public class ReceiverAction
        extends Action {
    private ReceiverBase receiver;
    private boolean inError;

    public void begin(InterpretationContext ic, String name, Attributes attributes) throws ActionException {
        String className = attributes.getValue("class");
        if (OptionHelper.isEmpty(className)) {
            addError("Missing class name for receiver. Near [" + name + "] line " + getLineNumber(ic));

            this.inError = true;

            return;
        }
        try {
            addInfo("About to instantiate receiver of type [" + className + "]");

            this.receiver = (ReceiverBase) OptionHelper.instantiateByClassName(className, ReceiverBase.class, this.context);

            this.receiver.setContext(this.context);

            ic.pushObject(this.receiver);
        } catch (Exception ex) {
            this.inError = true;
            addError("Could not create a receiver of type [" + className + "].", ex);
            throw new ActionException(ex);
        }
    }

    public void end(InterpretationContext ic, String name) throws ActionException {
        if (this.inError)
            return;
        ic.getContext().register((LifeCycle) this.receiver);
        this.receiver.start();

        Object o = ic.peekObject();
        if (o != this.receiver) {
            addWarn("The object at the of the stack is not the remote pushed earlier.");
        } else {

            ic.popObject();
        }
    }
}

