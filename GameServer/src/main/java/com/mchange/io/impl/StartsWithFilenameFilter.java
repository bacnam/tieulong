package com.mchange.io.impl;

import java.io.File;
import java.io.FilenameFilter;

public class StartsWithFilenameFilter
implements FilenameFilter
{
public static final int ALWAYS = 0;
public static final int NEVER = 1;
public static final int MATCH = 2;
String[] beginnings = null;

int accept_dirs;

public StartsWithFilenameFilter(String[] paramArrayOfString, int paramInt) {
this.beginnings = paramArrayOfString;
this.accept_dirs = paramInt;
}

public StartsWithFilenameFilter(String paramString, int paramInt) {
this.beginnings = new String[] { paramString };
this.accept_dirs = paramInt;
}

public boolean accept(File paramFile, String paramString) {
if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
for (int i = this.beginnings.length; --i >= 0;) {
if (paramString.startsWith(this.beginnings[i])) return true; 
}  return false;
}
}

