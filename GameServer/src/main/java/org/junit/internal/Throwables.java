package org.junit.internal;

public final class Throwables
{
public static Exception rethrowAsException(Throwable e) throws Exception {
rethrow(e);
return null;
}

private static <T extends Throwable> void rethrow(Throwable e) throws T {
throw (T)e;
}
}

