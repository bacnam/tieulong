package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;

public class LoggerContextAwareBase
        extends ContextAwareBase
        implements LoggerContextAware {
    public void setContext(Context context) {
        if (context instanceof LoggerContext || context == null) {
            super.setContext(context);
        } else {
            throw new IllegalArgumentException("LoggerContextAwareBase only accepts contexts of type c.l.classic.LoggerContext");
        }
    }

    public LoggerContext getLoggerContext() {
        return (LoggerContext) this.context;
    }

    public void setLoggerContext(LoggerContext context) {
        super.setContext((Context) context);
    }
}

