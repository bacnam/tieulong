package ch.qos.logback.core;

import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.WarnStatus;

import java.util.List;

public abstract class AppenderBase<E>
        extends ContextAwareBase
        implements Appender<E> {
    static final int ALLOWED_REPEATS = 5;
    protected volatile boolean started = false;
    protected String name;
    private boolean guard = false;
    private FilterAttachableImpl<E> fai = new FilterAttachableImpl();
    private int statusRepeatCount = 0;
    private int exceptionCount = 0;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized void doAppend(E eventObject) {
        if (this.guard) {
            return;
        }

        try {
            this.guard = true;

            if (!this.started) {
                if (this.statusRepeatCount++ < 5) {
                    addStatus((Status) new WarnStatus("Attempted to append to non started appender [" + this.name + "].", this));
                }

                return;
            }

            if (getFilterChainDecision(eventObject) == FilterReply.DENY) {
                return;
            }

            append(eventObject);
        } catch (Exception e) {
            if (this.exceptionCount++ < 5) {
                addError("Appender [" + this.name + "] failed to append.", e);
            }
        } finally {
            this.guard = false;
        }
    }

    protected abstract void append(E paramE);

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }

    public boolean isStarted() {
        return this.started;
    }

    public String toString() {
        return getClass().getName() + "[" + this.name + "]";
    }

    public void addFilter(Filter<E> newFilter) {
        this.fai.addFilter(newFilter);
    }

    public void clearAllFilters() {
        this.fai.clearAllFilters();
    }

    public List<Filter<E>> getCopyOfAttachedFiltersList() {
        return this.fai.getCopyOfAttachedFiltersList();
    }

    public FilterReply getFilterChainDecision(E event) {
        return this.fai.getFilterChainDecision(event);
    }
}

