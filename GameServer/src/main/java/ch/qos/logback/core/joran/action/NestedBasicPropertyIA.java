package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.util.PropertySetter;
import ch.qos.logback.core.util.AggregationType;
import org.xml.sax.Attributes;

import java.util.Stack;

public class NestedBasicPropertyIA
        extends ImplicitAction {
    Stack<IADataForBasicProperty> actionDataStack = new Stack<IADataForBasicProperty>();

    public boolean isApplicable(ElementPath elementPath, Attributes attributes, InterpretationContext ec) {
        IADataForBasicProperty ad;
        String nestedElementTagName = elementPath.peekLast();

        if (ec.isEmpty()) {
            return false;
        }

        Object o = ec.peekObject();
        PropertySetter parentBean = new PropertySetter(o);
        parentBean.setContext(this.context);

        AggregationType aggregationType = parentBean.computeAggregationType(nestedElementTagName);

        switch (aggregationType) {
            case NOT_FOUND:
            case AS_COMPLEX_PROPERTY:
            case AS_COMPLEX_PROPERTY_COLLECTION:
                return false;

            case AS_BASIC_PROPERTY:
            case AS_BASIC_PROPERTY_COLLECTION:
                ad = new IADataForBasicProperty(parentBean, aggregationType, nestedElementTagName);

                this.actionDataStack.push(ad);

                return true;
        }
        addError("PropertySetter.canContainComponent returned " + aggregationType);
        return false;
    }

    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
    }

    public void body(InterpretationContext ec, String body) {
        String finalBody = ec.subst(body);

        IADataForBasicProperty actionData = this.actionDataStack.peek();
        switch (actionData.aggregationType) {
            case AS_BASIC_PROPERTY:
                actionData.parentBean.setProperty(actionData.propertyName, finalBody);
                break;
            case AS_BASIC_PROPERTY_COLLECTION:
                actionData.parentBean.addBasicProperty(actionData.propertyName, finalBody);
                break;
        }
    }

    public void end(InterpretationContext ec, String tagName) {
        this.actionDataStack.pop();
    }
}

