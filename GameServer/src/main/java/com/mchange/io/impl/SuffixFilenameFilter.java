package com.mchange.io.impl;

import java.io.File;
import java.io.FilenameFilter;

public class SuffixFilenameFilter
implements FilenameFilter
{
public static final int ALWAYS = 0;
public static final int NEVER = 1;
public static final int MATCH = 2;
String[] suffixes = null;

int accept_dirs;

public SuffixFilenameFilter(String[] paramArrayOfString, int paramInt) {
this.suffixes = paramArrayOfString;
this.accept_dirs = paramInt;
}

public SuffixFilenameFilter(String paramString, int paramInt) {
this.suffixes = new String[] { paramString };
this.accept_dirs = paramInt;
}

public boolean accept(File paramFile, String paramString) {
if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
for (int i = this.suffixes.length; --i >= 0;) {
if (paramString.endsWith(this.suffixes[i])) return true; 
}  return false;
}
}

