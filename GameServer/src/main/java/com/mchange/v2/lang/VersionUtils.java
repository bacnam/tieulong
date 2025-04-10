package com.mchange.v2.lang;

import com.mchange.v1.util.StringTokenizerUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

public final class VersionUtils
{
private static final MLogger logger = MLog.getLogger(VersionUtils.class);

private static final int[] DFLT_VERSION_ARRAY = new int[] { 1, 1 };

private static final int[] JDK_VERSION_ARRAY;

private static final int JDK_VERSION;

static {
String str = System.getProperty("java.version");

if (str == null) {

if (logger.isLoggable(MLevel.WARNING))
logger.warning("Could not find java.version System property. Defaulting to JDK 1.1"); 
arrayOfInt = DFLT_VERSION_ARRAY;
} else {

try {
arrayOfInt = extractVersionNumberArray(str);
} catch (NumberFormatException numberFormatException) {

if (logger.isLoggable(MLevel.WARNING))
logger.warning("java.version ''" + str + "'' could not be parsed. Defaulting to JDK 1.1."); 
arrayOfInt = DFLT_VERSION_ARRAY;
} 
} 
int i = 0;
if (arrayOfInt.length > 0)
i += arrayOfInt[0] * 10; 
if (arrayOfInt.length > 1) {
i += arrayOfInt[1];
}
JDK_VERSION_ARRAY = arrayOfInt;
JDK_VERSION = i;

try {
String str1 = System.getProperty("sun.arch.data.model");
if (str1 == null) {
integer = null;
} else {
integer = new Integer(str1);
} 
} catch (Exception exception) {

integer = null;
} 

if (integer == null || integer.intValue() == 32 || integer.intValue() == 64) {
NUM_BITS = integer;
} else {

if (logger.isLoggable(MLevel.WARNING)) {
logger.warning("Determined a surprising jvmNumerOfBits: " + integer + ". Setting jvmNumberOfBits to unknown (null).");
}
NUM_BITS = null;
} 
}
private static final Integer NUM_BITS;
static {
int[] arrayOfInt;
Integer integer;
}

public static Integer jvmNumberOfBits() {
return NUM_BITS;
}
public static boolean isJavaVersion10() {
return (JDK_VERSION == 10);
}
public static boolean isJavaVersion11() {
return (JDK_VERSION == 11);
}
public static boolean isJavaVersion12() {
return (JDK_VERSION == 12);
}
public static boolean isJavaVersion13() {
return (JDK_VERSION == 13);
}
public static boolean isJavaVersion14() {
return (JDK_VERSION == 14);
}
public static boolean isJavaVersion15() {
return (JDK_VERSION == 15);
}
public static boolean isAtLeastJavaVersion10() {
return (JDK_VERSION >= 10);
}
public static boolean isAtLeastJavaVersion11() {
return (JDK_VERSION >= 11);
}
public static boolean isAtLeastJavaVersion12() {
return (JDK_VERSION >= 12);
}
public static boolean isAtLeastJavaVersion13() {
return (JDK_VERSION >= 13);
}
public static boolean isAtLeastJavaVersion14() {
return (JDK_VERSION >= 14);
}
public static boolean isAtLeastJavaVersion15() {
return (JDK_VERSION >= 15);
}
public static boolean isAtLeastJavaVersion16() {
return (JDK_VERSION >= 16);
}
public static boolean isAtLeastJavaVersion17() {
return (JDK_VERSION >= 17);
}

public static int[] extractVersionNumberArray(String paramString) throws NumberFormatException {
return extractVersionNumberArray(paramString, paramString.split("\\D+"));
}

public static int[] extractVersionNumberArray(String paramString1, String paramString2) throws NumberFormatException {
String[] arrayOfString = StringTokenizerUtils.tokenizeToArray(paramString1, paramString2, false);
return extractVersionNumberArray(paramString1, arrayOfString);
}

private static int[] extractVersionNumberArray(String paramString, String[] paramArrayOfString) throws NumberFormatException {
int i = paramArrayOfString.length;
int[] arrayOfInt = new int[i];
for (byte b = 0; b < i; b++) {

try {
arrayOfInt[b] = Integer.parseInt(paramArrayOfString[b]);
}
catch (NumberFormatException numberFormatException) {

if (b <= 1) {
throw numberFormatException;
}

if (logger.isLoggable(MLevel.INFO)) {
logger.log(MLevel.INFO, "JVM version string (" + paramString + ") contains non-integral component (" + paramArrayOfString[b] + "). Using precending components only to resolve JVM version.");
}

int[] arrayOfInt1 = new int[b];
System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, b);
arrayOfInt = arrayOfInt1;

break;
} 
} 
return arrayOfInt;
}

public boolean prefixMatches(int[] paramArrayOfint1, int[] paramArrayOfint2) {
if (paramArrayOfint1.length > paramArrayOfint2.length)
return false; 
byte b;
int i;
for (b = 0, i = paramArrayOfint1.length; b < i; b++) {
if (paramArrayOfint1[b] != paramArrayOfint2[b])
return false; 
}  return true;
}

public static int lexicalCompareVersionNumberArrays(int[] paramArrayOfint1, int[] paramArrayOfint2) {
int i = paramArrayOfint1.length;
int j = paramArrayOfint2.length;
for (byte b = 0; b < i; b++) {

if (b == j)
return 1; 
if (paramArrayOfint1[b] > paramArrayOfint2[b])
return 1; 
if (paramArrayOfint1[b] < paramArrayOfint2[b])
return -1; 
} 
if (j > i) {
return -1;
}
return 0;
}
}

