package ch.qos.logback.core.rolling;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.FileFilterUtil;
import ch.qos.logback.core.rolling.helper.SizeAndTimeBasedArchiveRemover;
import ch.qos.logback.core.util.FileSize;
import java.io.File;
import java.util.Date;

@NoAutoStart
public class SizeAndTimeBasedFNATP<E>
extends TimeBasedFileNamingAndTriggeringPolicyBase<E>
{
int currentPeriodsCounter = 0;

FileSize maxFileSize;

String maxFileSizeAsString;
private int invocationCounter;

public void start() {
super.start();

this.archiveRemover = createArchiveRemover();
this.archiveRemover.setContext(this.context);

String regex = this.tbrp.fileNamePattern.toRegexForFixedDate(this.dateInCurrentPeriod);
String stemRegex = FileFilterUtil.afterLastSlash(regex);

computeCurrentPeriodsHighestCounterValue(stemRegex);

this.started = true;
}

protected ArchiveRemover createArchiveRemover() {
return (ArchiveRemover)new SizeAndTimeBasedArchiveRemover(this.tbrp.fileNamePattern, this.rc);
}

void computeCurrentPeriodsHighestCounterValue(String stemRegex) {
File file = new File(getCurrentPeriodsFileNameWithoutCompressionSuffix());
File parentDir = file.getParentFile();

File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);

if (matchingFileArray == null || matchingFileArray.length == 0) {
this.currentPeriodsCounter = 0;
return;
} 
this.currentPeriodsCounter = FileFilterUtil.findHighestCounter(matchingFileArray, stemRegex);

if (this.tbrp.getParentsRawFileProperty() != null || this.tbrp.compressionMode != CompressionMode.NONE)
{
this.currentPeriodsCounter++;
}
}

private int invocationMask = 1;

public boolean isTriggeringEvent(File activeFile, E event) {
long time = getCurrentTime();
if (time >= this.nextCheck) {
Date dateInElapsedPeriod = this.dateInCurrentPeriod;
this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { dateInElapsedPeriod, Integer.valueOf(this.currentPeriodsCounter) });

this.currentPeriodsCounter = 0;
setDateInCurrentPeriod(time);
computeNextCheck();
return true;
} 

if ((++this.invocationCounter & this.invocationMask) != this.invocationMask) {
return false;
}
if (this.invocationMask < 15) {
this.invocationMask = (this.invocationMask << 1) + 1;
}

if (activeFile.length() >= this.maxFileSize.getSize()) {
this.elapsedPeriodsFileName = this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter) });

this.currentPeriodsCounter++;
return true;
} 

return false;
}

private String getFileNameIncludingCompressionSuffix(Date date, int counter) {
return this.tbrp.fileNamePattern.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(counter) });
}

public String getCurrentPeriodsFileNameWithoutCompressionSuffix() {
return this.tbrp.fileNamePatternWCS.convertMultipleArguments(new Object[] { this.dateInCurrentPeriod, Integer.valueOf(this.currentPeriodsCounter) });
}

public String getMaxFileSize() {
return this.maxFileSizeAsString;
}

public void setMaxFileSize(String maxFileSize) {
this.maxFileSizeAsString = maxFileSize;
this.maxFileSize = FileSize.valueOf(maxFileSize);
}
}

