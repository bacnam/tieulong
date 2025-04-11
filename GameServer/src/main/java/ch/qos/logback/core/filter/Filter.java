package ch.qos.logback.core.filter;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.spi.LifeCycle;

public abstract class Filter<E>
        extends ContextAwareBase
        implements LifeCycle {
    boolean start = false;
    private String name;

    public void start() {
        this.start = true;
    }

    public boolean isStarted() {
        return this.start;
    }

    public void stop() {
        this.start = false;
    }

    public abstract FilterReply decide(E paramE);

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

