package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.CheckReturnValue;

@Beta
@GwtCompatible
public abstract class CharMatcher
implements Predicate<Character>
{
private static final String BREAKING_WHITESPACE_CHARS = "\t\n\013\f\r     　";
private static final String NON_BREAKING_WHITESPACE_CHARS = " ᠎ ";
public static final CharMatcher WHITESPACE = anyOf("\t\n\013\f\r     　 ᠎ ").or(inRange(' ', ' ')).precomputed();

public static final CharMatcher BREAKING_WHITESPACE = anyOf("\t\n\013\f\r     　").or(inRange(' ', ' ')).or(inRange(' ', ' ')).precomputed();

public static final CharMatcher ASCII = inRange(false, '');

public static final CharMatcher DIGIT;

static {
CharMatcher digit = inRange('0', '9');
String zeroes = "٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";

for (char base : zeroes.toCharArray()) {
digit = digit.or(inRange(base, (char)(base + 9)));
}
DIGIT = digit.precomputed();
}

public static final CharMatcher JAVA_WHITESPACE = inRange('\t', '\r').or(inRange('\034', ' ')).or(is(' ')).or(is('᠎')).or(inRange(' ', ' ')).or(inRange(' ', '​')).or(inRange(' ', ' ')).or(is(' ')).or(is('　')).precomputed();

public static final CharMatcher JAVA_DIGIT = new CharMatcher() {
public boolean matches(char c) {
return Character.isDigit(c);
}
};

public static final CharMatcher JAVA_LETTER = new CharMatcher() {
public boolean matches(char c) {
return Character.isLetter(c);
}
};

public static final CharMatcher JAVA_LETTER_OR_DIGIT = new CharMatcher() {
public boolean matches(char c) {
return Character.isLetterOrDigit(c);
}
};

public static final CharMatcher JAVA_UPPER_CASE = new CharMatcher() {
public boolean matches(char c) {
return Character.isUpperCase(c);
}
};

public static final CharMatcher JAVA_LOWER_CASE = new CharMatcher() {
public boolean matches(char c) {
return Character.isLowerCase(c);
}
};

public static final CharMatcher JAVA_ISO_CONTROL = inRange(false, '\037').or(inRange('', ''));

public static final CharMatcher INVISIBLE = inRange(false, ' ').or(inRange('', ' ')).or(is('­')).or(inRange('؀', '؃')).or(anyOf("۝܏ ឴឵᠎")).or(inRange(' ', '‏')).or(inRange(' ', ' ')).or(inRange(' ', '⁤')).or(inRange('⁪', '⁯')).or(is('　')).or(inRange('?', '')).or(anyOf("﻿￹￺￻")).precomputed();

public static final CharMatcher SINGLE_WIDTH = inRange(false, 'ӹ').or(is('־')).or(inRange('א', 'ת')).or(is('׳')).or(is('״')).or(inRange('؀', 'ۿ')).or(inRange('ݐ', 'ݿ')).or(inRange('฀', '๿')).or(inRange('Ḁ', '₯')).or(inRange('℀', '℺')).or(inRange('ﭐ', '﷿')).or(inRange('ﹰ', '﻿')).or(inRange('｡', 'ￜ')).precomputed();

public static final CharMatcher ANY = new CharMatcher()
{
public boolean matches(char c) {
return true;
}

public int indexIn(CharSequence sequence) {
return (sequence.length() == 0) ? -1 : 0;
}

public int indexIn(CharSequence sequence, int start) {
int length = sequence.length();
Preconditions.checkPositionIndex(start, length);
return (start == length) ? -1 : start;
}

public int lastIndexIn(CharSequence sequence) {
return sequence.length() - 1;
}

public boolean matchesAllOf(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return true;
}

public boolean matchesNoneOf(CharSequence sequence) {
return (sequence.length() == 0);
}

public String removeFrom(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return "";
}

public String replaceFrom(CharSequence sequence, char replacement) {
char[] array = new char[sequence.length()];
Arrays.fill(array, replacement);
return new String(array);
}

public String replaceFrom(CharSequence sequence, CharSequence replacement) {
StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
for (int i = 0; i < sequence.length(); i++) {
retval.append(replacement);
}
return retval.toString();
}

public String collapseFrom(CharSequence sequence, char replacement) {
return (sequence.length() == 0) ? "" : String.valueOf(replacement);
}

public String trimFrom(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return "";
}

public int countIn(CharSequence sequence) {
return sequence.length();
}

public CharMatcher and(CharMatcher other) {
return Preconditions.<CharMatcher>checkNotNull(other);
}

public CharMatcher or(CharMatcher other) {
Preconditions.checkNotNull(other);
return this;
}

public CharMatcher negate() {
return NONE;
}

public CharMatcher precomputed() {
return this;
}
};

public static final CharMatcher NONE = new CharMatcher()
{
public boolean matches(char c) {
return false;
}

public int indexIn(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return -1;
}

public int indexIn(CharSequence sequence, int start) {
int length = sequence.length();
Preconditions.checkPositionIndex(start, length);
return -1;
}

public int lastIndexIn(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return -1;
}

public boolean matchesAllOf(CharSequence sequence) {
return (sequence.length() == 0);
}

public boolean matchesNoneOf(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return true;
}

public String removeFrom(CharSequence sequence) {
return sequence.toString();
}

public String replaceFrom(CharSequence sequence, char replacement) {
return sequence.toString();
}

public String replaceFrom(CharSequence sequence, CharSequence replacement) {
Preconditions.checkNotNull(replacement);
return sequence.toString();
}

public String collapseFrom(CharSequence sequence, char replacement) {
return sequence.toString();
}

public String trimFrom(CharSequence sequence) {
return sequence.toString();
}

public int countIn(CharSequence sequence) {
Preconditions.checkNotNull(sequence);
return 0;
}

public CharMatcher and(CharMatcher other) {
Preconditions.checkNotNull(other);
return this;
}

public CharMatcher or(CharMatcher other) {
return Preconditions.<CharMatcher>checkNotNull(other);
}

public CharMatcher negate() {
return ANY;
}

void setBits(CharMatcher.LookupTable table) {}

public CharMatcher precomputed() {
return this;
}
};

public static CharMatcher is(final char match) {
return new CharMatcher() {
public boolean matches(char c) {
return (c == match);
}

public String replaceFrom(CharSequence sequence, char replacement) {
return sequence.toString().replace(match, replacement);
}

public CharMatcher and(CharMatcher other) {
return other.matches(match) ? this : NONE;
}

public CharMatcher or(CharMatcher other) {
return other.matches(match) ? other : super.or(other);
}

public CharMatcher negate() {
return isNot(match);
}

void setBits(CharMatcher.LookupTable table) {
table.set(match);
}

public CharMatcher precomputed() {
return this;
}
};
}

public static CharMatcher isNot(final char match) {
return new CharMatcher() {
public boolean matches(char c) {
return (c != match);
}

public CharMatcher and(CharMatcher other) {
return other.matches(match) ? super.and(other) : other;
}

public CharMatcher or(CharMatcher other) {
return other.matches(match) ? ANY : this;
}

public CharMatcher negate() {
return is(match);
}
};
}

public static CharMatcher anyOf(CharSequence sequence) {
final char match1, match2;
switch (sequence.length()) {
case 0:
return NONE;
case 1:
return is(sequence.charAt(0));
case 2:
match1 = sequence.charAt(0);
match2 = sequence.charAt(1);
return new CharMatcher() {
public boolean matches(char c) {
return (c == match1 || c == match2);
}

void setBits(CharMatcher.LookupTable table) {
table.set(match1);
table.set(match2);
}

public CharMatcher precomputed() {
return this;
}
};
} 

final char[] chars = sequence.toString().toCharArray();
Arrays.sort(chars);

return new CharMatcher() {
public boolean matches(char c) {
return (Arrays.binarySearch(chars, c) >= 0);
}

void setBits(CharMatcher.LookupTable table) {
for (char c : chars) {
table.set(c);
}
}
};
}

public static CharMatcher noneOf(CharSequence sequence) {
return anyOf(sequence).negate();
}

public static CharMatcher inRange(final char startInclusive, final char endInclusive) {
Preconditions.checkArgument((endInclusive >= startInclusive));
return new CharMatcher() {
public boolean matches(char c) {
return (startInclusive <= c && c <= endInclusive);
}

void setBits(CharMatcher.LookupTable table) {
char c = startInclusive;

do { table.set(c);
c = (char)(c + 1); } while (c != endInclusive);
}

public CharMatcher precomputed() {
return this;
}
};
}

public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
Preconditions.checkNotNull(predicate);
if (predicate instanceof CharMatcher) {
return (CharMatcher)predicate;
}
return new CharMatcher() {
public boolean matches(char c) {
return predicate.apply(Character.valueOf(c));
}

public boolean apply(Character character) {
return predicate.apply(Preconditions.checkNotNull(character));
}
};
}

public CharMatcher negate() {
final CharMatcher original = this;
return new CharMatcher() {
public boolean matches(char c) {
return !original.matches(c);
}

public boolean matchesAllOf(CharSequence sequence) {
return original.matchesNoneOf(sequence);
}

public boolean matchesNoneOf(CharSequence sequence) {
return original.matchesAllOf(sequence);
}

public int countIn(CharSequence sequence) {
return sequence.length() - original.countIn(sequence);
}

public CharMatcher negate() {
return original;
}
};
}

public CharMatcher and(CharMatcher other) {
return new And(Arrays.asList(new CharMatcher[] { this, Preconditions.<CharMatcher>checkNotNull(other) }));
}

private static class And extends CharMatcher {
List<CharMatcher> components;

And(List<CharMatcher> components) {
this.components = components;
}

public boolean matches(char c) {
for (CharMatcher matcher : this.components) {
if (!matcher.matches(c)) {
return false;
}
} 
return true;
}

public CharMatcher and(CharMatcher other) {
List<CharMatcher> newComponents = new ArrayList<CharMatcher>(this.components);
newComponents.add(Preconditions.checkNotNull(other));
return new And(newComponents);
}
}

public CharMatcher or(CharMatcher other) {
return new Or(Arrays.asList(new CharMatcher[] { this, Preconditions.<CharMatcher>checkNotNull(other) }));
}

private static class Or extends CharMatcher {
List<CharMatcher> components;

Or(List<CharMatcher> components) {
this.components = components;
}

public boolean matches(char c) {
for (CharMatcher matcher : this.components) {
if (matcher.matches(c)) {
return true;
}
} 
return false;
}

public CharMatcher or(CharMatcher other) {
List<CharMatcher> newComponents = new ArrayList<CharMatcher>(this.components);
newComponents.add(Preconditions.checkNotNull(other));
return new Or(newComponents);
}

void setBits(CharMatcher.LookupTable table) {
for (CharMatcher matcher : this.components) {
matcher.setBits(table);
}
}
}

public CharMatcher precomputed() {
return Platform.precomputeCharMatcher(this);
}

CharMatcher precomputedInternal() {
final LookupTable table = new LookupTable();
setBits(table);

return new CharMatcher() {
public boolean matches(char c) {
return table.get(c);
}

public CharMatcher precomputed() {
return this;
}
};
}

void setBits(LookupTable table) {
char c = Character.MIN_VALUE;

do { if (!matches(c))
continue;  table.set(c);

c = (char)(c + 1); } while (c != Character.MAX_VALUE);
}

private static final class LookupTable
{
int[] data = new int[2048];

void set(char index) {
this.data[index >> 5] = this.data[index >> 5] | 1 << index;
}

boolean get(char index) {
return ((this.data[index >> 5] & 1 << index) != 0);
}

private LookupTable() {}
}

public boolean matchesAnyOf(CharSequence sequence) {
return !matchesNoneOf(sequence);
}

public boolean matchesAllOf(CharSequence sequence) {
for (int i = sequence.length() - 1; i >= 0; i--) {
if (!matches(sequence.charAt(i))) {
return false;
}
} 
return true;
}

public boolean matchesNoneOf(CharSequence sequence) {
return (indexIn(sequence) == -1);
}

public int indexIn(CharSequence sequence) {
int length = sequence.length();
for (int i = 0; i < length; i++) {
if (matches(sequence.charAt(i))) {
return i;
}
} 
return -1;
}

public int indexIn(CharSequence sequence, int start) {
int length = sequence.length();
Preconditions.checkPositionIndex(start, length);
for (int i = start; i < length; i++) {
if (matches(sequence.charAt(i))) {
return i;
}
} 
return -1;
}

public int lastIndexIn(CharSequence sequence) {
for (int i = sequence.length() - 1; i >= 0; i--) {
if (matches(sequence.charAt(i))) {
return i;
}
} 
return -1;
}

public int countIn(CharSequence sequence) {
int count = 0;
for (int i = 0; i < sequence.length(); i++) {
if (matches(sequence.charAt(i))) {
count++;
}
} 
return count;
}

@CheckReturnValue
public String removeFrom(CharSequence sequence) {
String string = sequence.toString();
int pos = indexIn(string);
if (pos == -1) {
return string;
}

char[] chars = string.toCharArray();
int spread = 1;

while (true) {
pos++;

while (pos != chars.length) {

if (matches(chars[pos]))

{ 

spread++; continue; }  chars[pos - spread] = chars[pos]; pos++;
}  break;
}  return new String(chars, 0, pos - spread);
}

@CheckReturnValue
public String retainFrom(CharSequence sequence) {
return negate().removeFrom(sequence);
}

@CheckReturnValue
public String replaceFrom(CharSequence sequence, char replacement) {
String string = sequence.toString();
int pos = indexIn(string);
if (pos == -1) {
return string;
}
char[] chars = string.toCharArray();
chars[pos] = replacement;
for (int i = pos + 1; i < chars.length; i++) {
if (matches(chars[i])) {
chars[i] = replacement;
}
} 
return new String(chars);
}

@CheckReturnValue
public String replaceFrom(CharSequence sequence, CharSequence replacement) {
int replacementLen = replacement.length();
if (replacementLen == 0) {
return removeFrom(sequence);
}
if (replacementLen == 1) {
return replaceFrom(sequence, replacement.charAt(0));
}

String string = sequence.toString();
int pos = indexIn(string);
if (pos == -1) {
return string;
}

int len = string.length();
StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);

int oldpos = 0;
do {
buf.append(string, oldpos, pos);
buf.append(replacement);
oldpos = pos + 1;
pos = indexIn(string, oldpos);
} while (pos != -1);

buf.append(string, oldpos, len);
return buf.toString();
}

@CheckReturnValue
public String trimFrom(CharSequence sequence) {
int len = sequence.length();

int first;

for (first = 0; first < len && 
matches(sequence.charAt(first)); first++);

int last;

for (last = len - 1; last > first && 
matches(sequence.charAt(last)); last--);

return sequence.subSequence(first, last + 1).toString();
}

@CheckReturnValue
public String trimLeadingFrom(CharSequence sequence) {
int len = sequence.length();

int first;
for (first = 0; first < len && 
matches(sequence.charAt(first)); first++);

return sequence.subSequence(first, len).toString();
}

@CheckReturnValue
public String trimTrailingFrom(CharSequence sequence) {
int len = sequence.length();

int last;
for (last = len - 1; last >= 0 && 
matches(sequence.charAt(last)); last--);

return sequence.subSequence(0, last + 1).toString();
}

@CheckReturnValue
public String collapseFrom(CharSequence sequence, char replacement) {
int first = indexIn(sequence);
if (first == -1) {
return sequence.toString();
}

StringBuilder builder = (new StringBuilder(sequence.length())).append(sequence.subSequence(0, first)).append(replacement);

boolean in = true;
for (int i = first + 1; i < sequence.length(); i++) {
char c = sequence.charAt(i);
if (apply(Character.valueOf(c))) {
if (!in) {
builder.append(replacement);
in = true;
} 
} else {
builder.append(c);
in = false;
} 
} 
return builder.toString();
}

@CheckReturnValue
public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
int first = negate().indexIn(sequence);
if (first == -1) {
return "";
}
StringBuilder builder = new StringBuilder(sequence.length());
boolean inMatchingGroup = false;
for (int i = first; i < sequence.length(); i++) {
char c = sequence.charAt(i);
if (apply(Character.valueOf(c))) {
inMatchingGroup = true;
} else {
if (inMatchingGroup) {
builder.append(replacement);
inMatchingGroup = false;
} 
builder.append(c);
} 
} 
return builder.toString();
}

public boolean apply(Character character) {
return matches(character.charValue());
}

public abstract boolean matches(char paramChar);
}

