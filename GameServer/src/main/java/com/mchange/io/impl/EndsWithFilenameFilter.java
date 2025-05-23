package com.mchange.io.impl;

import java.io.File;
import java.io.FilenameFilter;

public class EndsWithFilenameFilter
implements FilenameFilter
{
public static final int ALWAYS = 0;
public static final int NEVER = 1;
public static final int MATCH = 2;
String[] endings = null;

int accept_dirs;

public EndsWithFilenameFilter(String[] paramArrayOfString, int paramInt) {
this.endings = paramArrayOfString;
this.accept_dirs = paramInt;
}

public EndsWithFilenameFilter(String paramString, int paramInt) {
this.endings = new String[] { paramString };
this.accept_dirs = paramInt;
}

public boolean accept(File paramFile, String paramString) {
if (this.accept_dirs != 2 && (new File(paramFile, paramString)).isDirectory()) return (this.accept_dirs == 0); 
for (int i = this.endings.length; --i >= 0;) {
if (paramString.endsWith(this.endings[i])) return true; 
}  return false;
}
}

