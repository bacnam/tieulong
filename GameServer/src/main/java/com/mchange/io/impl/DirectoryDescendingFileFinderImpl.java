package com.mchange.io.impl;

import com.mchange.io.FileEnumeration;
import com.mchange.io.IOEnumeration;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Stack;

public class DirectoryDescendingFileFinderImpl
implements IOEnumeration, FileEnumeration
{
private static final Object dummy = new Object();

Hashtable markedDirex = new Hashtable<Object, Object>();

Stack direx = new Stack();
Stack files = new Stack();

FilenameFilter filter;

boolean canonical;

public DirectoryDescendingFileFinderImpl(File paramFile, FilenameFilter paramFilenameFilter, boolean paramBoolean) throws IOException {
if (!paramFile.isDirectory())
throw new IllegalArgumentException(paramFile.getName() + " is not a directory."); 
this.filter = paramFilenameFilter;
this.canonical = paramBoolean;
blossomDirectory(paramFile);
while (this.files.empty() && !this.direx.empty())
blossomDirectory(this.direx.pop()); 
}

public DirectoryDescendingFileFinderImpl(File paramFile) throws IOException {
this(paramFile, null, false);
}
public boolean hasMoreFiles() {
return !this.files.empty();
}

public File nextFile() throws IOException {
if (this.files.empty()) throw new NoSuchElementException(); 
File file = this.files.pop();
while (this.files.empty() && !this.direx.empty())
blossomDirectory(this.direx.pop()); 
return file;
}

public boolean hasMoreElements() {
return hasMoreFiles();
}
public Object nextElement() throws IOException {
return nextFile();
}

private void blossomDirectory(File paramFile) throws IOException {
String str = paramFile.getCanonicalPath();
String[] arrayOfString = (this.filter == null) ? paramFile.list() : paramFile.list(this.filter);
for (int i = arrayOfString.length; --i >= 0;) {

if (this.filter == null || this.filter.accept(paramFile, arrayOfString[i])) {

String str1 = (this.canonical ? str : paramFile.getPath()) + File.separator + arrayOfString[i];
File file = new File(str1);

if (file.isFile()) { this.files.push(file);
continue; }

if (!this.markedDirex.containsKey(file.getCanonicalPath())) {
this.direx.push(file);
}
} 
} 
this.markedDirex.put(str, dummy);
}

public static void main(String[] paramArrayOfString) {
try {
File file = new File(paramArrayOfString[0]);
DirectoryDescendingFileFinderImpl directoryDescendingFileFinderImpl = new DirectoryDescendingFileFinderImpl(file);
while (directoryDescendingFileFinderImpl.hasMoreFiles()) {
System.out.println(directoryDescendingFileFinderImpl.nextFile().getAbsolutePath());
}
} catch (Exception exception) {
exception.printStackTrace();
} 
}
}

