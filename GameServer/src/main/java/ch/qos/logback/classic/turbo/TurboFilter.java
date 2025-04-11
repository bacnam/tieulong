package ch.qos.logback.classic.turbo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.spi.LifeCycle;
import org.slf4j.Marker;

public abstract class TurboFilter
        extends ContextAwareBase
        implements LifeCycle {
    boolean start = false;
    private String name;

    public abstract FilterReply decide(Marker paramMarker, Logger paramLogger, Level paramLevel, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable);

    public void start() {
        this.start = true;
    }

    public boolean isStarted() {
        return this.start;
    }

    public void stop() {
        this.start = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

