package com.mchange.v1.util;

import com.mchange.util.AssertException;

public class DebugUtils
{
public static void myAssert(boolean paramBoolean) {
if (!paramBoolean) throw new AssertException(); 
}
public static void myAssert(boolean paramBoolean, String paramString) {
if (!paramBoolean) throw new AssertException(paramString); 
}
}

