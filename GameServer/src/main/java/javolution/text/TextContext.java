package javolution.text;

import javolution.context.AbstractContext;
import javolution.context.FormatContext;
import javolution.osgi.internal.OSGiServices;

public abstract class TextContext
        extends FormatContext {
    public static TextContext enter() {
        return (TextContext) currentTextContext().enterInner();
    }

    public static <T> TextFormat<T> getFormat(Class<? extends T> type) {
        return currentTextContext().searchFormat(type);
    }

    private static TextContext currentTextContext() {
        TextContext ctx = (TextContext) AbstractContext.current(TextContext.class);
        if (ctx != null)
            return ctx;
        return OSGiServices.getTextContext();
    }

    public abstract <T> void setFormat(Class<? extends T> paramClass, TextFormat<T> paramTextFormat);

    protected abstract <T> TextFormat<T> searchFormat(Class<? extends T> paramClass);
}

