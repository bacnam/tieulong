package ch.qos.logback.core.status;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.StatusPrinter;

import java.io.PrintStream;
import java.util.List;

abstract class OnPrintStreamStatusListenerBase
        extends ContextAwareBase
        implements StatusListener, LifeCycle {
    static final long DEFAULT_RETROSPECTIVE = 300L;
    boolean isStarted = false;
    long retrospective = 300L;

    protected abstract PrintStream getPrintStream();

    private void print(Status status) {
        StringBuilder sb = new StringBuilder();
        StatusPrinter.buildStr(sb, "", status);
        getPrintStream().print(sb);
    }

    public void addStatusEvent(Status status) {
        if (!this.isStarted)
            return;
        print(status);
    }

    private void retrospectivePrint() {
        if (this.context == null)
            return;
        long now = System.currentTimeMillis();
        StatusManager sm = this.context.getStatusManager();
        List<Status> statusList = sm.getCopyOfStatusList();
        for (Status status : statusList) {
            long timestamp = status.getDate().longValue();
            if (now - timestamp < this.retrospective) {
                print(status);
            }
        }
    }

    public void start() {
        this.isStarted = true;
        if (this.retrospective > 0L) {
            retrospectivePrint();
        }
    }

    public long getRetrospective() {
        return this.retrospective;
    }

    public void setRetrospective(long retrospective) {
        this.retrospective = retrospective;
    }

    public void stop() {
        this.isStarted = false;
    }

    public boolean isStarted() {
        return this.isStarted;
    }
}

