package ch.qos.logback.classic.spi;

import ch.qos.logback.core.CoreConstants;

import java.util.List;

public class CallerData {
    public static final String NA = "?";
    public static final int LINE_NA = -1;
    public static final String CALLER_DATA_NA = "?#?:?" + CoreConstants.LINE_SEPARATOR;
    public static final StackTraceElement[] EMPTY_CALLER_DATA_ARRAY = new StackTraceElement[0];
    private static final String LOG4J_CATEGORY = "org.apache.log4j.Category";
    private static final String SLF4J_BOUNDARY = "org.slf4j.Logger";

    public static StackTraceElement[] extract(Throwable t, String fqnOfInvokingClass, int maxDepth, List<String> frameworkPackageList) {
        if (t == null) {
            return null;
        }

        StackTraceElement[] steArray = t.getStackTrace();

        int found = -1;
        for (int i = 0; i < steArray.length; i++) {
            if (isInFrameworkSpace(steArray[i].getClassName(), fqnOfInvokingClass, frameworkPackageList)) {

                found = i + 1;
            } else if (found != -1) {
                break;
            }
        }

        if (found == -1) {
            return EMPTY_CALLER_DATA_ARRAY;
        }

        int availableDepth = steArray.length - found;
        int desiredDepth = (maxDepth < availableDepth) ? maxDepth : availableDepth;

        StackTraceElement[] callerDataArray = new StackTraceElement[desiredDepth];
        for (int j = 0; j < desiredDepth; j++) {
            callerDataArray[j] = steArray[found + j];
        }
        return callerDataArray;
    }

    static boolean isInFrameworkSpace(String currentClass, String fqnOfInvokingClass, List<String> frameworkPackageList) {
        if (currentClass.equals(fqnOfInvokingClass) || currentClass.equals("org.apache.log4j.Category") || currentClass.startsWith("org.slf4j.Logger") || isInFrameworkSpaceList(currentClass, frameworkPackageList)) {
            return true;
        }
        return false;
    }

    private static boolean isInFrameworkSpaceList(String currentClass, List<String> frameworkPackageList) {
        if (frameworkPackageList == null) {
            return false;
        }
        for (String s : frameworkPackageList) {
            if (currentClass.startsWith(s))
                return true;
        }
        return false;
    }

    public static StackTraceElement naInstance() {
        return new StackTraceElement("?", "?", "?", -1);
    }
}

