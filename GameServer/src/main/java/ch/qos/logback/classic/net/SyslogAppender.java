package ch.qos.logback.classic.net;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.SyslogStartConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.net.SyslogAppenderBase;
import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SyslogAppender
        extends SyslogAppenderBase<ILoggingEvent> {
    public static final String DEFAULT_SUFFIX_PATTERN = "[%thread] %logger %msg";
    public static final String DEFAULT_STACKTRACE_PATTERN = "\t";
    PatternLayout stackTraceLayout = new PatternLayout();
    String stackTracePattern = "\t";

    boolean throwableExcluded = false;

    public void start() {
        super.start();
        setupStackTraceLayout();
    }

    String getPrefixPattern() {
        return "%syslogStart{" + getFacility() + "}%nopex{}";
    }

    public SyslogOutputStream createOutputStream() throws SocketException, UnknownHostException {
        return new SyslogOutputStream(getSyslogHost(), getPort());
    }

    public int getSeverityForEvent(Object eventObject) {
        ILoggingEvent event = (ILoggingEvent) eventObject;
        return LevelToSyslogSeverity.convert(event);
    }

    protected void postProcess(Object eventObject, OutputStream sw) {
        if (this.throwableExcluded) {
            return;
        }
        ILoggingEvent event = (ILoggingEvent) eventObject;
        IThrowableProxy tp = event.getThrowableProxy();

        if (tp == null) {
            return;
        }
        String stackTracePrefix = this.stackTraceLayout.doLayout(event);
        boolean isRootException = true;
        while (tp != null) {
            StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
            try {
                handleThrowableFirstLine(sw, tp, stackTracePrefix, isRootException);
                isRootException = false;
                for (StackTraceElementProxy step : stepArray) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(stackTracePrefix).append(step);
                    sw.write(sb.toString().getBytes());
                    sw.flush();
                }
            } catch (IOException e) {
                break;
            }
            tp = tp.getCause();
        }
    }

    private void handleThrowableFirstLine(OutputStream sw, IThrowableProxy tp, String stackTracePrefix, boolean isRootException) throws IOException {
        StringBuilder sb = (new StringBuilder()).append(stackTracePrefix);

        if (!isRootException) {
            sb.append("Caused by: ");
        }
        sb.append(tp.getClassName()).append(": ").append(tp.getMessage());
        sw.write(sb.toString().getBytes());
        sw.flush();
    }

    boolean stackTraceHeaderLine(StringBuilder sb, boolean topException) {
        return false;
    }

    public Layout<ILoggingEvent> buildLayout() {
        PatternLayout layout = new PatternLayout();
        layout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());

        if (this.suffixPattern == null) {
            this.suffixPattern = "[%thread] %logger %msg";
        }
        layout.setPattern(getPrefixPattern() + this.suffixPattern);
        layout.setContext(getContext());
        layout.start();
        return (Layout<ILoggingEvent>) layout;
    }

    private void setupStackTraceLayout() {
        this.stackTraceLayout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());

        this.stackTraceLayout.setPattern(getPrefixPattern() + this.stackTracePattern);
        this.stackTraceLayout.setContext(getContext());
        this.stackTraceLayout.start();
    }

    public boolean isThrowableExcluded() {
        return this.throwableExcluded;
    }

    public void setThrowableExcluded(boolean throwableExcluded) {
        this.throwableExcluded = throwableExcluded;
    }

    public String getStackTracePattern() {
        return this.stackTracePattern;
    }

    public void setStackTracePattern(String stackTracePattern) {
        this.stackTracePattern = stackTracePattern;
    }
}

