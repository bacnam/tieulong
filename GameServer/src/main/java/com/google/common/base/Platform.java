package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
static char[] charBufferFromThreadLocal() {
return DEST_TL.get();
}

static long systemNanoTime() {
return System.nanoTime();
}

private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>()
{
protected char[] initialValue() {
return new char[1024];
}
};

static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
return matcher.precomputedInternal();
}
}

