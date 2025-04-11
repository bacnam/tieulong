package org.apache.mina.core.session;

public interface IoSessionConfig {
    int getReadBufferSize();

    void setReadBufferSize(int paramInt);

    int getMinReadBufferSize();

    void setMinReadBufferSize(int paramInt);

    int getMaxReadBufferSize();

    void setMaxReadBufferSize(int paramInt);

    int getThroughputCalculationInterval();

    void setThroughputCalculationInterval(int paramInt);

    long getThroughputCalculationIntervalInMillis();

    int getIdleTime(IdleStatus paramIdleStatus);

    long getIdleTimeInMillis(IdleStatus paramIdleStatus);

    void setIdleTime(IdleStatus paramIdleStatus, int paramInt);

    int getReaderIdleTime();

    void setReaderIdleTime(int paramInt);

    long getReaderIdleTimeInMillis();

    int getWriterIdleTime();

    void setWriterIdleTime(int paramInt);

    long getWriterIdleTimeInMillis();

    int getBothIdleTime();

    void setBothIdleTime(int paramInt);

    long getBothIdleTimeInMillis();

    int getWriteTimeout();

    void setWriteTimeout(int paramInt);

    long getWriteTimeoutInMillis();

    boolean isUseReadOperation();

    void setUseReadOperation(boolean paramBoolean);

    void setAll(IoSessionConfig paramIoSessionConfig);
}

