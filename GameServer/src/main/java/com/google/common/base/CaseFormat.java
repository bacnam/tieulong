package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum CaseFormat
{
LOWER_HYPHEN(CharMatcher.is('-'), "-"),

LOWER_UNDERSCORE(CharMatcher.is('_'), "_"),

LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), ""),

UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), ""),

UPPER_UNDERSCORE(CharMatcher.is('_'), "_");

private final CharMatcher wordBoundary;
private final String wordSeparator;

CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
this.wordBoundary = wordBoundary;
this.wordSeparator = wordSeparator;
}

public String to(CaseFormat format, String s) {
if (format == null) {
throw new NullPointerException();
}
if (s == null) {
throw new NullPointerException();
}

if (format == this) {
return s;
}

switch (this) {
case LOWER_HYPHEN:
switch (format) {
case LOWER_UNDERSCORE:
return s.replace('-', '_');
case UPPER_UNDERSCORE:
return Ascii.toUpperCase(s.replace('-', '_'));
} 
break;
case LOWER_UNDERSCORE:
switch (format) {
case LOWER_HYPHEN:
return s.replace('_', '-');
case UPPER_UNDERSCORE:
return Ascii.toUpperCase(s);
} 
break;
case UPPER_UNDERSCORE:
switch (format) {
case LOWER_HYPHEN:
return Ascii.toLowerCase(s.replace('_', '-'));
case LOWER_UNDERSCORE:
return Ascii.toLowerCase(s);
} 

break;
} 

StringBuilder out = null;
int i = 0;
int j = -1;
while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
if (i == 0) {

out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
out.append(format.normalizeFirstWord(s.substring(i, j)));
} else {
out.append(format.normalizeWord(s.substring(i, j)));
} 
out.append(format.wordSeparator);
i = j + this.wordSeparator.length();
} 
if (i == 0) {
return format.normalizeFirstWord(s);
}
out.append(format.normalizeWord(s.substring(i)));
return out.toString();
}

private String normalizeFirstWord(String word) {
switch (this) {
case LOWER_CAMEL:
return Ascii.toLowerCase(word);
} 
return normalizeWord(word);
}

private String normalizeWord(String word) {
switch (this) {
case LOWER_HYPHEN:
return Ascii.toLowerCase(word);
case LOWER_UNDERSCORE:
return Ascii.toLowerCase(word);
case LOWER_CAMEL:
return firstCharOnlyToUpper(word);
case UPPER_CAMEL:
return firstCharOnlyToUpper(word);
case UPPER_UNDERSCORE:
return Ascii.toUpperCase(word);
} 
throw new RuntimeException("unknown case: " + this);
}

private static String firstCharOnlyToUpper(String word) {
int length = word.length();
if (length == 0) {
return word;
}
return (new StringBuilder(length)).append(Ascii.toUpperCase(word.charAt(0))).append(Ascii.toLowerCase(word.substring(1))).toString();
}
}

