package javolution.xml;

import javolution.context.AbstractContext;
import javolution.context.FormatContext;
import javolution.osgi.internal.OSGiServices;

public abstract class XMLContext
        extends FormatContext {
    public static XMLContext enter() {
        return (XMLContext) currentXMLContext().enterInner();
    }

    public static <T> XMLFormat<T> getFormat(Class<? extends T> type) {
        return currentXMLContext().searchFormat(type);
    }

    private static XMLContext currentXMLContext() {
        XMLContext ctx = (XMLContext) AbstractContext.current(XMLContext.class);
        if (ctx != null)
            return ctx;
        return OSGiServices.getXMLContext();
    }

    public abstract <T> void setFormat(Class<? extends T> paramClass, XMLFormat<T> paramXMLFormat);

    protected abstract <T> XMLFormat<T> searchFormat(Class<? extends T> paramClass);
}

