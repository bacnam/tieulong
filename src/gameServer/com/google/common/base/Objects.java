package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import javax.annotation.Nullable;

@GwtCompatible
public final class Objects
{
public static boolean equal(@Nullable Object a, @Nullable Object b) {
return (a == b || (a != null && a.equals(b)));
}

public static int hashCode(@Nullable Object... objects) {
return Arrays.hashCode(objects);
}

public static ToStringHelper toStringHelper(Object self) {
return new ToStringHelper(simpleName(self.getClass()));
}

public static ToStringHelper toStringHelper(Class<?> clazz) {
return new ToStringHelper(simpleName(clazz));
}

public static ToStringHelper toStringHelper(String className) {
return new ToStringHelper(className);
}

private static String simpleName(Class<?> clazz) {
String name = clazz.getName();

name = name.replaceAll("\\$[0-9]+", "\\$");

int start = name.lastIndexOf('$');

if (start == -1) {
start = name.lastIndexOf('.');
}
return name.substring(start + 1);
}

public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
return (first != null) ? first : Preconditions.<T>checkNotNull(second);
}

public static final class ToStringHelper
{
private final StringBuilder builder;

private boolean needsSeparator = false;

private ToStringHelper(String className) {
Preconditions.checkNotNull(className);
this.builder = (new StringBuilder(32)).append(className).append('{');
}

public ToStringHelper add(String name, @Nullable Object value) {
Preconditions.checkNotNull(name);
maybeAppendSeparator().append(name).append('=').append(value);
return this;
}

public ToStringHelper addValue(@Nullable Object value) {
maybeAppendSeparator().append(value);
return this;
}

public String toString() {
try {
return this.builder.append('}').toString();
}
finally {

this.builder.setLength(this.builder.length() - 1);
} 
}

private StringBuilder maybeAppendSeparator() {
if (this.needsSeparator) {
return this.builder.append(", ");
}
this.needsSeparator = true;
return this.builder;
}
}
}

