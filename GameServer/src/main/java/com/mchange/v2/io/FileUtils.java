package com.mchange.v2.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class FileUtils
{
public static File findRelativeToParent(File paramFile1, File paramFile2) throws IOException {
String str1 = paramFile1.getPath();
String str2 = paramFile2.getPath();
if (!str2.startsWith(str1))
throw new IllegalArgumentException(str2 + " is not a child of " + str1 + " [no transformations or canonicalizations tried]"); 
String str3 = str2.substring(str1.length());
File file = new File(str3);
if (file.isAbsolute())
file = new File(file.getPath().substring(1)); 
return file;
}

public static long diskSpaceUsed(File paramFile) throws IOException {
long l = 0L;
for (FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(paramFile); fileIterator.hasNext(); ) {

File file = fileIterator.nextFile();

if (!file.isFile()) {
continue;
}
l += file.length();
} 
return l;
}

public static void touchExisting(File paramFile) throws IOException {
if (paramFile.exists()) {
unguardedTouch(paramFile);
}
}

public static void touch(File paramFile) throws IOException {
if (!paramFile.exists())
createEmpty(paramFile); 
unguardedTouch(paramFile);
}

public static void createEmpty(File paramFile) throws IOException {
RandomAccessFile randomAccessFile = null;

try {
randomAccessFile = new RandomAccessFile(paramFile, "rws");
randomAccessFile.setLength(0L);
} finally {

try {
if (randomAccessFile != null) randomAccessFile.close(); 
} catch (IOException iOException) {
iOException.printStackTrace();
} 
} 
}
private static void unguardedTouch(File paramFile) throws IOException {
paramFile.setLastModified(System.currentTimeMillis());
}
}

