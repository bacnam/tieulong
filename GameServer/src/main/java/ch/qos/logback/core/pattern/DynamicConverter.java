package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.Status;

import java.util.List;

public abstract class DynamicConverter<E>
        extends FormattingConverter<E>
        implements LifeCycle, ContextAware {
    protected boolean started = false;
    ContextAwareBase cab = new ContextAwareBase(this);
    private List<String> optionList;

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }

    public boolean isStarted() {
        return this.started;
    }

    public String getFirstOption() {
        if (this.optionList == null || this.optionList.size() == 0) {
            return null;
        }
        return this.optionList.get(0);
    }

    protected List<String> getOptionList() {
        return this.optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    public Context getContext() {
        return this.cab.getContext();
    }

    public void setContext(Context context) {
        this.cab.setContext(context);
    }

    public void addStatus(Status status) {
        this.cab.addStatus(status);
    }

    public void addInfo(String msg) {
        this.cab.addInfo(msg);
    }

    public void addInfo(String msg, Throwable ex) {
        this.cab.addInfo(msg, ex);
    }

    public void addWarn(String msg) {
        this.cab.addWarn(msg);
    }

    public void addWarn(String msg, Throwable ex) {
        this.cab.addWarn(msg, ex);
    }

    public void addError(String msg) {
        this.cab.addError(msg);
    }

    public void addError(String msg, Throwable ex) {
        this.cab.addError(msg, ex);
    }
}

