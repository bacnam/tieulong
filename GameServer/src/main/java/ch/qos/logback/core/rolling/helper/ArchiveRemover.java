package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAware;

import java.util.Date;

public interface ArchiveRemover extends ContextAware {
    void clean(Date paramDate);

    void setMaxHistory(int paramInt);
}

