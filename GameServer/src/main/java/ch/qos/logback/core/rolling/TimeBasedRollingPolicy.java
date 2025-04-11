package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.AsynchronousCompressor;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.rolling.helper.RenameUtil;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeBasedRollingPolicy<E>
        extends RollingPolicyBase
        implements TriggeringPolicy<E> {
    static final String FNP_NOT_SET = "The FileNamePattern option must be set before using TimeBasedRollingPolicy. ";
    static final int INFINITE_HISTORY = 0;
    FileNamePattern fileNamePatternWCS;
    private Compressor compressor;
    private RenameUtil renameUtil = new RenameUtil();

    Future<?> future;
    private int maxHistory = 0;

    private ArchiveRemover archiveRemover;

    TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedFileNamingAndTriggeringPolicy;

    boolean cleanHistoryOnStart = false;

    public void start() {
        this.renameUtil.setContext(this.context);

        if (this.fileNamePatternStr != null) {
            this.fileNamePattern = new FileNamePattern(this.fileNamePatternStr, this.context);
            determineCompressionMode();
        } else {
            addWarn("The FileNamePattern option must be set before using TimeBasedRollingPolicy. ");
            addWarn("See also http:");
            throw new IllegalStateException("The FileNamePattern option must be set before using TimeBasedRollingPolicy. See also http:");
        }

        this.compressor = new Compressor(this.compressionMode);
        this.compressor.setContext(this.context);

        this.fileNamePatternWCS = new FileNamePattern(Compressor.computeFileNameStr_WCS(this.fileNamePatternStr, this.compressionMode), this.context);

        addInfo("Will use the pattern " + this.fileNamePatternWCS + " for the active file");

        if (this.compressionMode == CompressionMode.ZIP) {
            String zipEntryFileNamePatternStr = transformFileNamePattern2ZipEntry(this.fileNamePatternStr);
            this.zipEntryFileNamePattern = new FileNamePattern(zipEntryFileNamePatternStr, this.context);
        }

        if (this.timeBasedFileNamingAndTriggeringPolicy == null) {
            this.timeBasedFileNamingAndTriggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<E>();
        }
        this.timeBasedFileNamingAndTriggeringPolicy.setContext(this.context);
        this.timeBasedFileNamingAndTriggeringPolicy.setTimeBasedRollingPolicy(this);
        this.timeBasedFileNamingAndTriggeringPolicy.start();

        if (this.maxHistory != 0) {
            this.archiveRemover = this.timeBasedFileNamingAndTriggeringPolicy.getArchiveRemover();
            this.archiveRemover.setMaxHistory(this.maxHistory);
            if (this.cleanHistoryOnStart) {
                addInfo("Cleaning on start up");
                this.archiveRemover.clean(new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime()));
            }
        }

        super.start();
    }

    public void stop() {
        if (!isStarted())
            return;
        waitForAsynchronousJobToStop();
        super.stop();
    }

    private void waitForAsynchronousJobToStop() {
        if (this.future != null)
            try {
                this.future.get(30L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                addError("Timeout while waiting for compression job to finish", e);
            } catch (Exception e) {
                addError("Unexpected exception while waiting for compression job to finish", e);
            }
    }

    private String transformFileNamePattern2ZipEntry(String fileNamePatternStr) {
        String slashified = FileFilterUtil.slashify(fileNamePatternStr);
        return FileFilterUtil.afterLastSlash(slashified);
    }

    public void setTimeBasedFileNamingAndTriggeringPolicy(TimeBasedFileNamingAndTriggeringPolicy<E> timeBasedTriggering) {
        this.timeBasedFileNamingAndTriggeringPolicy = timeBasedTriggering;
    }

    public TimeBasedFileNamingAndTriggeringPolicy<E> getTimeBasedFileNamingAndTriggeringPolicy() {
        return this.timeBasedFileNamingAndTriggeringPolicy;
    }

    public void rollover() throws RolloverFailure {
        String elapsedPeriodsFileName = this.timeBasedFileNamingAndTriggeringPolicy.getElapsedPeriodsFileName();

        String elapsedPeriodStem = FileFilterUtil.afterLastSlash(elapsedPeriodsFileName);

        if (this.compressionMode == CompressionMode.NONE) {
            if (getParentsRawFileProperty() != null) {
                this.renameUtil.rename(getParentsRawFileProperty(), elapsedPeriodsFileName);
            }
        } else if (getParentsRawFileProperty() == null) {
            this.future = asyncCompress(elapsedPeriodsFileName, elapsedPeriodsFileName, elapsedPeriodStem);
        } else {
            this.future = renamedRawAndAsyncCompress(elapsedPeriodsFileName, elapsedPeriodStem);
        }

        if (this.archiveRemover != null) {
            this.archiveRemover.clean(new Date(this.timeBasedFileNamingAndTriggeringPolicy.getCurrentTime()));
        }
    }

    Future asyncCompress(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName) throws RolloverFailure {
        AsynchronousCompressor ac = new AsynchronousCompressor(this.compressor);
        return ac.compressAsynchronously(nameOfFile2Compress, nameOfCompressedFile, innerEntryName);
    }

    Future renamedRawAndAsyncCompress(String nameOfCompressedFile, String innerEntryName) throws RolloverFailure {
        String parentsRawFile = getParentsRawFileProperty();
        String tmpTarget = parentsRawFile + System.nanoTime() + ".tmp";
        this.renameUtil.rename(parentsRawFile, tmpTarget);
        return asyncCompress(tmpTarget, nameOfCompressedFile, innerEntryName);
    }

    public String getActiveFileName() {
        String parentsRawFileProperty = getParentsRawFileProperty();
        if (parentsRawFileProperty != null) {
            return parentsRawFileProperty;
        }
        return this.timeBasedFileNamingAndTriggeringPolicy.getCurrentPeriodsFileNameWithoutCompressionSuffix();
    }

    public boolean isTriggeringEvent(File activeFile, E event) {
        return this.timeBasedFileNamingAndTriggeringPolicy.isTriggeringEvent(activeFile, event);
    }

    public int getMaxHistory() {
        return this.maxHistory;
    }

    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }

    public boolean isCleanHistoryOnStart() {
        return this.cleanHistoryOnStart;
    }

    public void setCleanHistoryOnStart(boolean cleanHistoryOnStart) {
        this.cleanHistoryOnStart = cleanHistoryOnStart;
    }

    public String toString() {
        return "c.q.l.core.rolling.TimeBasedRollingPolicy";
    }
}

