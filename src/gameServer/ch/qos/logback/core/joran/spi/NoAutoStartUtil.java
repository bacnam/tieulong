package ch.qos.logback.core.joran.spi;

public class NoAutoStartUtil
{
public static boolean notMarkedWithNoAutoStart(Object o) {
if (o == null) {
return false;
}
Class<?> clazz = o.getClass();
NoAutoStart a = clazz.<NoAutoStart>getAnnotation(NoAutoStart.class);
return (a == null);
}
}

