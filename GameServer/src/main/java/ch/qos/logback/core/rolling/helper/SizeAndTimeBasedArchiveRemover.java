package ch.qos.logback.core.rolling.helper;

import java.io.File;
import java.util.Date;

public class SizeAndTimeBasedArchiveRemover
        extends DefaultArchiveRemover {
    public SizeAndTimeBasedArchiveRemover(FileNamePattern fileNamePattern, RollingCalendar rc) {
        super(fileNamePattern, rc);
    }

    public void cleanByPeriodOffset(Date now, int periodOffset) {
        Date dateOfPeriodToClean = this.rc.getRelativeDate(now, periodOffset);

        String regex = this.fileNamePattern.toRegexForFixedDate(dateOfPeriodToClean);
        String stemRegex = FileFilterUtil.afterLastSlash(regex);
        File archive0 = new File(this.fileNamePattern.convertMultipleArguments(new Object[]{dateOfPeriodToClean, Integer.valueOf(0)}));

        archive0 = archive0.getAbsoluteFile();

        File parentDir = archive0.getAbsoluteFile().getParentFile();
        File[] matchingFileArray = FileFilterUtil.filesInFolderMatchingStemRegex(parentDir, stemRegex);

        for (File f : matchingFileArray) {
            Date fileLastModified = this.rc.getRelativeDate(new Date(f.lastModified()), -1);

            if (fileLastModified.compareTo(dateOfPeriodToClean) <= 0) {
                addInfo("deleting " + f);
                f.delete();
            }
        }

        if (this.parentClean)
            removeFolderIfEmpty(parentDir);
    }
}

