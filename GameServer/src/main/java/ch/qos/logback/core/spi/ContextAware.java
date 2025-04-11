package ch.qos.logback.core.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.status.Status;

public interface ContextAware {
    Context getContext();

    void setContext(Context paramContext);

    void addStatus(Status paramStatus);

    void addInfo(String paramString);

    void addInfo(String paramString, Throwable paramThrowable);

    void addWarn(String paramString);

    void addWarn(String paramString, Throwable paramThrowable);

    void addError(String paramString);

    void addError(String paramString, Throwable paramThrowable);
}

