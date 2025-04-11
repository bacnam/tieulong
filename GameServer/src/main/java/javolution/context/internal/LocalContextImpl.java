package javolution.context.internal;

import javolution.context.LocalContext;
import javolution.util.FastMap;

public final class LocalContextImpl
        extends LocalContext {
    private FastMap<LocalContext.Parameter<?>, Object> localSettings = new FastMap();

    private LocalContextImpl parent;

    protected LocalContext inner() {
        LocalContextImpl ctx = new LocalContextImpl();
        ctx.parent = this;
        return ctx;
    }

    public <T> void supersede(LocalContext.Parameter<T> param, T localValue) {
        if (localValue == null) throw new NullPointerException();
        this.localSettings.put(param, localValue);
    }

    protected <T> T getValue(LocalContext.Parameter<T> param, T defaultValue) {
        Object value = this.localSettings.get(param);
        if (value != null) return (T) value;
        if (this.parent != null) return this.parent.getValue(param, defaultValue);
        return defaultValue;
    }
}

