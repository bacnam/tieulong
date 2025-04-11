package javolution.lang;

import javolution.context.LogContext;
import javolution.context.SecurityContext;
import javolution.osgi.internal.OSGiServices;
import javolution.text.TextContext;

import java.lang.reflect.Field;

public abstract class Configurable<T> {
    public static SecurityContext.Permission<Configurable<?>> RECONFIGURE_PERMISSION = new SecurityContext.Permission(Configurable.class, "reconfigure");
    private final SecurityContext.Permission<Configurable<T>> reconfigurePermission = new SecurityContext.Permission(Configurable.class, "reconfigure", this);
    private String name;
    private volatile T value;

    public Configurable() {
        String name = getName();
        T defaultValue = getDefault();
        if (name != null) {
            try {
                String property = System.getProperty(name);
                if (property != null) {
                    defaultValue = parse(property);
                    LogContext.debug(new Object[]{name, ", System Properties Value: ", defaultValue});
                }

            } catch (SecurityException securityError) {
            }
        }

        this.name = name;
        this.value = initialized(defaultValue);
        Object[] listeners = OSGiServices.getConfigurableListeners();
        for (Object listener : listeners) {
            ((Listener) listener).configurableInitialized(this, this.value);
        }
    }

    public T get() {
        return this.value;
    }

    public String getName() {
        if (this.name != null)
            return this.name;
        Class<?> thisClass = getClass();
        Class<?> enclosingClass = thisClass.getEnclosingClass();
        String fieldName = null;
        for (Field field : enclosingClass.getFields()) {
            if (field.getType().isAssignableFrom(thisClass)) {
                if (fieldName != null) {
                    throw new UnsupportedOperationException("Multiple configurables static fields in the same classrequires the Configurable.getName() method to be overriden.");
                }

                fieldName = field.getName();
            }
        }
        return (fieldName != null) ? (enclosingClass.getName() + "#" + fieldName) : null;
    }

    public SecurityContext.Permission<Configurable<T>> getReconfigurePermission() {
        return this.reconfigurePermission;
    }

    public T reconfigure(T newValue) {
        SecurityContext.check(this.reconfigurePermission);
        synchronized (this) {
            T oldValue = this.value;
            this.value = reconfigured(oldValue, newValue);
            Object[] listeners = OSGiServices.getConfigurableListeners();
            for (Object listener : listeners) {
                ((Listener) listener).configurableReconfigured(this, oldValue, this.value);
            }

            return this.value;
        }
    }

    protected abstract T getDefault();

    protected T initialized(T value) {
        return value;
    }

    protected T parse(String str) {
        Class<? extends T> type = (Class) getDefault().getClass();
        return (T) TextContext.getFormat(type).parse(str);
    }

    protected T reconfigured(T oldValue, T newValue) {
        return newValue;
    }

    public static interface Listener {
        <T> void configurableInitialized(Configurable<T> param1Configurable, T param1T);

        <T> void configurableReconfigured(Configurable<T> param1Configurable, T param1T1, T param1T2);
    }
}

