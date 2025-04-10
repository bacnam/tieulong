package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public final class Strings
{
public static String nullToEmpty(@Nullable String string) {
return (string == null) ? "" : string;
}

@Nullable
public static String emptyToNull(@Nullable String string) {
return isNullOrEmpty(string) ? null : string;
}

public static boolean isNullOrEmpty(@Nullable String string) {
return (string == null || string.length() == 0);
}

public static String padStart(String string, int minLength, char padChar) {
Preconditions.checkNotNull(string);
if (string.length() >= minLength) {
return string;
}
StringBuilder sb = new StringBuilder(minLength);
for (int i = string.length(); i < minLength; i++) {
sb.append(padChar);
}
sb.append(string);
return sb.toString();
}

public static String padEnd(String string, int minLength, char padChar) {
Preconditions.checkNotNull(string);
if (string.length() >= minLength) {
return string;
}
StringBuilder sb = new StringBuilder(minLength);
sb.append(string);
for (int i = string.length(); i < minLength; i++) {
sb.append(padChar);
}
return sb.toString();
}

public static String repeat(String string, int count) {
Preconditions.checkNotNull(string);
Preconditions.checkArgument((count >= 0), "invalid count: %s", new Object[] { Integer.valueOf(count) });

StringBuilder builder = new StringBuilder(string.length() * count);
for (int i = 0; i < count; i++) {
builder.append(string);
}
return builder.toString();
}
}

