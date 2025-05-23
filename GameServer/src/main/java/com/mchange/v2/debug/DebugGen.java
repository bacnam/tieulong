package com.mchange.v2.debug;

import com.mchange.v1.io.WriterUtils;
import com.mchange.v1.lang.BooleanUtils;
import com.mchange.v1.util.SetUtils;
import com.mchange.v1.util.StringTokenizerUtils;
import com.mchange.v2.cmdline.CommandLineUtils;
import com.mchange.v2.cmdline.ParsedCommandLine;
import com.mchange.v2.io.DirectoryDescentUtils;
import com.mchange.v2.io.FileIterator;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public final class DebugGen
implements DebugConstants
{
static final String[] VALID = new String[] { "codebase", "packages", "trace", "debug", "recursive", "javac", "noclobber", "classname", "skipdirs", "outputbase" };

static final String[] REQUIRED = new String[] { "codebase", "packages", "trace", "debug" };

static final String[] ARGS = new String[] { "codebase", "packages", "trace", "debug", "classname", "outputbase" };

static final String EOL = System.getProperty("line.separator");

static int trace_level;

static boolean debug;

static boolean recursive;

static String classname;

static boolean clobber;

static Set skipDirs;

public static final synchronized void main(String[] paramArrayOfString) {
try {
ParsedCommandLine parsedCommandLine = CommandLineUtils.parse(paramArrayOfString, "--", VALID, REQUIRED, ARGS);

String str1 = parsedCommandLine.getSwitchArg("codebase");
str1 = platify(str1);
if (!str1.endsWith(File.separator)) {
str1 = str1 + File.separator;
}

String str2 = parsedCommandLine.getSwitchArg("outputbase");
if (str2 != null) {

str2 = platify(str2);
if (!str2.endsWith(File.separator)) {
str2 = str2 + File.separator;
}
} else {
str2 = str1;
} 
File file = new File(str2);

if (file.exists()) {

if (!file.isDirectory())
{

System.exit(-1);
}
else if (!file.canWrite())
{
System.err.println("Output Base '" + file.getPath() + "' is not writable!");
System.exit(-1);
}

} else if (!file.mkdirs()) {

System.err.println("Output Base directory '" + file.getPath() + "' does not exist, and could not be created!");
System.exit(-1);
} 

String[] arrayOfString = StringTokenizerUtils.tokenizeToArray(parsedCommandLine.getSwitchArg("packages"), ", \t");
File[] arrayOfFile = new File[arrayOfString.length]; int i;
for (byte b = 0; b < i; b++) {
arrayOfFile[b] = new File(str1 + sepify(arrayOfString[b]));
}

trace_level = Integer.parseInt(parsedCommandLine.getSwitchArg("trace"));

debug = BooleanUtils.parseBoolean(parsedCommandLine.getSwitchArg("debug"));

classname = parsedCommandLine.getSwitchArg("classname");
if (classname == null) {
classname = "Debug";
}

recursive = parsedCommandLine.includesSwitch("recursive");

clobber = !parsedCommandLine.includesSwitch("noclobber");

String str3 = parsedCommandLine.getSwitchArg("skipdirs");
if (str3 != null) {

String[] arrayOfString1 = StringTokenizerUtils.tokenizeToArray(str3, ", \t");
skipDirs = SetUtils.setFromArray((Object[])arrayOfString1);
}
else {

skipDirs = new HashSet();
skipDirs.add("CVS");
} 

if (parsedCommandLine.includesSwitch("javac"))
System.err.println("autorecompilation of packages not yet implemented."); 
int j;
for (i = 0, j = arrayOfFile.length; i < j; i++) {

if (recursive) {

if (!recursivePrecheckPackages(str1, arrayOfFile[i], str2, file))
{
System.err.println("One or more of the specifies packages could not be processed. Aborting. No files have been modified.");

System.exit(-1);

}

}
else if (!precheckPackage(str1, arrayOfFile[i], str2, file)) {

System.err.println("One or more of the specifies packages could not be processed. Aborting. No files have been modified.");

System.exit(-1);
} 
} 

for (i = 0, j = arrayOfFile.length; i < j; i++) {

if (recursive) {
recursiveWriteDebugFiles(str1, arrayOfFile[i], str2, file);
} else {
writeDebugFile(str2, arrayOfFile[i]);
} 
} 
} catch (Exception exception) {

exception.printStackTrace();
System.err.println();
usage();
} 
}

private static void usage() {
System.err.println("java " + DebugGen.class.getName() + " \\");
System.err.println("\t--codebase=<directory under which packages live> \\  (no default)");
System.err.println("\t--packages=<comma separated list of packages>    \\  (no default)");
System.err.println("\t--debug=<true|false>                             \\  (no default)");
System.err.println("\t--trace=<an int between 0 and 10>                \\  (no default)");
System.err.println("\t--outputdir=<directory under which to generate>  \\  (defaults to same dir as codebase)");
System.err.println("\t--recursive                                      \\  (no args)");
System.err.println("\t--noclobber                                      \\  (no args)");
System.err.println("\t--classname=<class to generate>                  \\  (defaults to Debug)");
System.err.println("\t--skipdirs=<directories that should be skipped>  \\  (defaults to CVS)");
}

private static String ify(String paramString, char paramChar1, char paramChar2) {
if (paramChar1 == paramChar2) return paramString;

StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
for (b = 0, i = stringBuffer.length(); b < i; b++) {
if (stringBuffer.charAt(b) == paramChar1)
stringBuffer.setCharAt(b, paramChar2); 
}  return stringBuffer.toString();
}

private static String platify(String paramString) {
String str = ify(paramString, '/', File.separatorChar);
str = ify(str, '\\', File.separatorChar);
str = ify(str, ':', File.separatorChar);
return str;
}

private static String dottify(String paramString) {
return ify(paramString, File.separatorChar, '.');
}
private static String sepify(String paramString) {
return ify(paramString, '.', File.separatorChar);
}

private static boolean recursivePrecheckPackages(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(paramFile1);
while (fileIterator.hasNext()) {

File file1 = fileIterator.nextFile();
if (!file1.isDirectory() || skipDirs.contains(file1.getName())) {
continue;
}
File file2 = outputDir(paramString1, file1, paramString2, paramFile2);
if (!file2.exists() && !file2.mkdirs()) {

System.err.println("Required output dir: '" + file2 + "' does not exist, and could not be created.");

return false;
} 
if (!precheckOutputPackageDir(file2))
return false; 
} 
return true;
}

private static File outputDir(String paramString1, File paramFile1, String paramString2, File paramFile2) {
if (paramString1.equals(paramString2)) {
return paramFile1;
}
String str = paramFile1.getPath();
if (!str.startsWith(paramString1)) {

System.err.println(DebugGen.class.getName() + ": program bug. Source package path '" + str + "' does not begin with codebase '" + paramString1 + "'.");

System.exit(-1);
} 
return new File(paramFile2, str.substring(paramString1.length()));
}

private static boolean precheckPackage(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
return precheckOutputPackageDir(outputDir(paramString1, paramFile1, paramString2, paramFile2));
}

private static boolean precheckOutputPackageDir(File paramFile) throws IOException {
File file = new File(paramFile, classname + ".java");
if (!paramFile.canWrite()) {

System.err.println("File '" + file.getPath() + "' is not writable.");
return false;
} 
if (!clobber && file.exists()) {

System.err.println("File '" + file.getPath() + "' exists, and we are in noclobber mode.");
return false;
} 

return true;
}

private static void recursiveWriteDebugFiles(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(outputDir(paramString1, paramFile1, paramString2, paramFile2));
while (fileIterator.hasNext()) {

File file = fileIterator.nextFile();

if (!file.isDirectory() || skipDirs.contains(file.getName())) {
continue;
}
writeDebugFile(paramString2, file);
} 
}

private static void writeDebugFile(String paramString, File paramFile) throws IOException {
File file = new File(paramFile, classname + ".java");
String str = dottify(paramFile.getPath().substring(paramString.length()));
System.err.println("Writing file: " + file.getPath());
OutputStreamWriter outputStreamWriter = null;

try {
outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF8");

outputStreamWriter.write("outputStreamWriter.write(" * This class generated by " + DebugGen.class.getName() + EOL);
outputStreamWriter.write(" * and will probably be overwritten by the same! Edit at" + EOL);
outputStreamWriter.write(" * YOUR PERIL!!! Hahahahaha." + EOL);
outputStreamWriter.write(" ********************************************************************/" + EOL);
outputStreamWriter.write(EOL);
outputStreamWriter.write("package " + str + ';' + EOL);
outputStreamWriter.write(EOL);
outputStreamWriter.write("import com.mchange.v2.debug.DebugConstants;" + EOL);
outputStreamWriter.write(EOL);
outputStreamWriter.write("final class " + classname + " implements DebugConstants" + EOL);
outputStreamWriter.write("{" + EOL);
outputStreamWriter.write("\tfinal static boolean DEBUG = " + debug + ';' + EOL);
outputStreamWriter.write("\tfinal static int     TRACE = " + traceStr(trace_level) + ';' + EOL);
outputStreamWriter.write(EOL);
outputStreamWriter.write("\tprivate " + classname + "()" + EOL);
outputStreamWriter.write("\t{}" + EOL);
outputStreamWriter.write("}" + EOL);
outputStreamWriter.write(EOL);
outputStreamWriter.flush();
} finally {

WriterUtils.attemptClose(outputStreamWriter);
} 
}

private static String traceStr(int paramInt) {
if (paramInt == 0)
return "TRACE_NONE"; 
if (paramInt == 5)
return "TRACE_MED"; 
if (paramInt == 10) {
return "TRACE_MAX";
}
return String.valueOf(paramInt);
}
}

