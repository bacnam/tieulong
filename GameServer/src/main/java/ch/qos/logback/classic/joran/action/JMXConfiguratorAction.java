package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jmx.JMXConfigurator;
import ch.qos.logback.classic.jmx.MBeanUtil;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class JMXConfiguratorAction
        extends Action {
    static final String OBJECT_NAME_ATTRIBUTE_NAME = "objectName";
    static final String CONTEXT_NAME_ATTRIBUTE_NAME = "contextName";
    static final char JMX_NAME_SEPARATOR = ',';

    public void begin(InterpretationContext ec, String name, Attributes attributes) throws ActionException {
        String objectNameAsStr;
        addInfo("begin");

        String contextName = this.context.getName();
        String contextNameAttributeVal = attributes.getValue("contextName");

        if (!OptionHelper.isEmpty(contextNameAttributeVal)) {
            contextName = contextNameAttributeVal;
        }

        String objectNameAttributeVal = attributes.getValue("objectName");

        if (OptionHelper.isEmpty(objectNameAttributeVal)) {
            objectNameAsStr = MBeanUtil.getObjectNameFor(contextName, JMXConfigurator.class);
        } else {

            objectNameAsStr = objectNameAttributeVal;
        }

        ObjectName objectName = MBeanUtil.string2ObjectName(this.context, this, objectNameAsStr);

        if (objectName == null) {
            addError("Failed construct ObjectName for [" + objectNameAsStr + "]");

            return;
        }
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        if (!MBeanUtil.isRegistered(mbs, objectName)) {

            JMXConfigurator jmxConfigurator = new JMXConfigurator((LoggerContext) this.context, mbs, objectName);

            try {
                mbs.registerMBean(jmxConfigurator, objectName);
            } catch (Exception e) {
                addError("Failed to create mbean", e);
            }
        }
    }

    public void end(InterpretationContext ec, String name) throws ActionException {
    }
}

