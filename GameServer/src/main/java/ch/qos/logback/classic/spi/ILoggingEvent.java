package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import org.slf4j.Marker;

import java.util.Map;

public interface ILoggingEvent extends DeferredProcessingAware {
    String getThreadName();

    Level getLevel();

    String getMessage();

    Object[] getArgumentArray();

    String getFormattedMessage();

    String getLoggerName();

    LoggerContextVO getLoggerContextVO();

    IThrowableProxy getThrowableProxy();

    StackTraceElement[] getCallerData();

    boolean hasCallerData();

    Marker getMarker();

    Map<String, String> getMDCPropertyMap();

    Map<String, String> getMdc();

    long getTimeStamp();

    void prepareForDeferredProcessing();
}

