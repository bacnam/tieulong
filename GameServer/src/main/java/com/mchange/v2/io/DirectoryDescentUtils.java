package com.mchange.v2.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public final class DirectoryDescentUtils
{
public static FileIterator depthFirstEagerDescent(File paramFile) throws IOException {
return depthFirstEagerDescent(paramFile, null, false);
}

public static FileIterator depthFirstEagerDescent(File paramFile, FileFilter paramFileFilter, boolean paramBoolean) throws IOException {
LinkedList linkedList = new LinkedList();
HashSet hashSet = new HashSet();
depthFirstEagerDescend(paramFile, paramFileFilter, paramBoolean, linkedList, hashSet);
return new IteratorFileIterator(linkedList.iterator());
}

public static void addSubtree(File paramFile, FileFilter paramFileFilter, boolean paramBoolean, Collection paramCollection) throws IOException {
HashSet hashSet = new HashSet();
depthFirstEagerDescend(paramFile, paramFileFilter, paramBoolean, paramCollection, hashSet);
}

private static void depthFirstEagerDescend(File paramFile, FileFilter paramFileFilter, boolean paramBoolean, Collection<File> paramCollection, Set<String> paramSet) throws IOException {
String str = paramFile.getCanonicalPath();
if (!paramSet.contains(str)) {

if (paramFileFilter == null || paramFileFilter.accept(paramFile))
paramCollection.add(paramBoolean ? new File(str) : paramFile); 
paramSet.add(str);
String[] arrayOfString = paramFile.list(); byte b; int i;
for (b = 0, i = arrayOfString.length; b < i; b++) {

File file = new File(paramFile, arrayOfString[b]);
if (file.isDirectory()) {
depthFirstEagerDescend(file, paramFileFilter, paramBoolean, paramCollection, paramSet);
}
else if (paramFileFilter == null || paramFileFilter.accept(file)) {
paramCollection.add(paramBoolean ? file.getCanonicalFile() : file);
} 
} 
} 
}

private static class IteratorFileIterator implements FileIterator {
Iterator ii;
Object last;

IteratorFileIterator(Iterator param1Iterator) {
this.ii = param1Iterator;
}
public File nextFile() throws IOException {
return (File)next();
}
public boolean hasNext() throws IOException {
return this.ii.hasNext();
}
public Object next() throws IOException {
return this.last = this.ii.next();
}

public void remove() throws IOException {
if (this.last != null) {

((File)this.last).delete();
this.last = null;
} else {

throw new IllegalStateException();
} 
}

public void close() throws IOException {}
}

public static void main(String[] paramArrayOfString) {
try {
FileIterator fileIterator = depthFirstEagerDescent(new File(paramArrayOfString[0]));
while (fileIterator.hasNext()) {
System.err.println(fileIterator.nextFile().getPath());
}
} catch (Exception exception) {
exception.printStackTrace();
} 
}
}

