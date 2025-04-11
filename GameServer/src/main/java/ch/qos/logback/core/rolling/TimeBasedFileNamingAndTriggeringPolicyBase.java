package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import ch.qos.logback.core.rolling.helper.RollingCalendar;
import ch.qos.logback.core.spi.ContextAwareBase;

import java.io.File;
import java.util.Date;

public abstract class TimeBasedFileNamingAndTriggeringPolicyBase<E>
        extends ContextAwareBase
        implements TimeBasedFileNamingAndTriggeringPolicy<E> {
    protected TimeBasedRollingPolicy<E> tbrp;
    protected ArchiveRemover archiveRemover = null;

    protected String elapsedPeriodsFileName;
    protected RollingCalendar rc;
    protected long artificialCurrentTime = -1L;
    protected Date dateInCurrentPeriod = null;

    protected long nextCheck;
    protected boolean started = false;

    public boolean isStarted() {
        return this.started;
    }

    public void start() {
        DateTokenConverter dtc = this.tbrp.fileNamePattern.getPrimaryDateTokenConverter();
        if (dtc == null) {
            throw new IllegalStateException("FileNamePattern [" + this.tbrp.fileNamePattern.getPattern() + "] does not contain a valid DateToken");
        }

        this.rc = new RollingCalendar();
        this.rc.init(dtc.getDatePattern());
        addInfo("The date pattern is '" + dtc.getDatePattern() + "' from file name pattern '" + this.tbrp.fileNamePattern.getPattern() + "'.");

        this.rc.printPeriodicity(this);

        setDateInCurrentPeriod(new Date(getCurrentTime()));
        if (this.tbrp.getParentsRawFileProperty() != null) {
            File currentFile = new File(this.tbrp.getParentsRawFileProperty());
            if (currentFile.exists() && currentFile.canRead()) {
                setDateInCurrentPeriod(new Date(currentFile.lastModified()));
            }
        }

        addInfo("Setting initial period to " + this.dateInCurrentPeriod);
        computeNextCheck();
    }

    public void stop() {
        this.started = false;
    }

    protected void computeNextCheck() {
        this.nextCheck = this.rc.getNextTriggeringMillis(this.dateInCurrentPeriod);
    }

    protected void setDateInCurrentPeriod(long now) {
        this.dateInCurrentPeriod.setTime(now);
    }

    public void setDateInCurrentPeriod(Date _dateInCurrentPeriod) {
        this.dateInCurrentPeriod = _dateInCurrentPeriod;
    }

    public String getElapsedPeriodsFileName() {
        return this.elapsedPeriodsFileName;
    }

    public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
        return this.tbrp.fileNamePatternWCS.convert(this.dateInCurrentPeriod);
    }

    public long getCurrentTime() {
        if (this.artificialCurrentTime >= 0L) {
            return this.artificialCurrentTime;
        }
        return System.currentTimeMillis();
    }

    public void setCurrentTime(long timeInMillis) {
        this.artificialCurrentTime = timeInMillis;
    }

    public void setTimeBasedRollingPolicy(TimeBasedRollingPolicy<E> _tbrp) {
        this.tbrp = _tbrp;
    }

    public ArchiveRemover getArchiveRemover() {
        return this.archiveRemover;
    }
}

