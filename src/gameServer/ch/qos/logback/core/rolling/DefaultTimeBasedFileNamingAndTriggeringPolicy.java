package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
import java.io.File;
import java.util.Date;

public class DefaultTimeBasedFileNamingAndTriggeringPolicy<E>
extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
{
public void start() {
super.start();
this.archiveRemover = (ArchiveRemover)new TimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc);
this.archiveRemover.setContext(this.context);
this.started = true;
}

public boolean isTriggeringEvent(File activeFile, E event) {
long time = getCurrentTime();
if (time >= this.nextCheck) {
Date dateOfElapsedPeriod = this.dateInCurrentPeriod;
addInfo("Elapsed period: " + dateOfElapsedPeriod);
this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convert(dateOfElapsedPeriod);

setDateInCurrentPeriod(time);
computeNextCheck();
return true;
} 
return false;
}

public String toString() {
return "c.q.l.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy";
}
}

