package javolution.osgi.internal;

import javolution.context.LogContext;
import javolution.lang.Configurable;

public final class ConfigurableListenerImpl
        implements Configurable.Listener {
    public <T> void configurableInitialized(Configurable<T> configurable, T value) {
        LogContext.debug(new Object[]{configurable.getName(), "=", value});
    }

    public <T> void configurableReconfigured(Configurable<T> configurable, T oldValue, T newValue) {
        LogContext.debug(new Object[]{configurable.getName(), " reconfigured from ", oldValue, " to ", newValue});
    }
}

