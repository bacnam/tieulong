package javolution.text;

import javolution.lang.Immutable;
import javolution.lang.MathLib;

public final class CharSet
implements Immutable<CharSet>
{
public static final CharSet EMPTY = new CharSet(new long[0]);

public static final CharSet WHITESPACES = valueOf(new char[] { '\t', '\n', '\013', '\f', '\r', '\034', '\035', '\036', '\037', ' ', ' ', '᠎', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '​', ' ', ' ', ' ', '　' });

public static final CharSet SPACES = valueOf(new char[] { ' ', ' ', ' ', '᠎', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '​', ' ', ' ', ' ', ' ', '　' });

public static final CharSet ISO_CONTROLS = valueOf(new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '' });

private final long[] _mapping;

private CharSet(long[] mapping) {
this._mapping = mapping;
}

public static CharSet valueOf(char... chars) {
int maxChar = 0;
for (int i = chars.length; --i >= 0;) {
if (chars[i] > maxChar) {
maxChar = chars[i];
}
} 
CharSet charSet = new CharSet(new long[(maxChar >> 6) + 1]);
for (int j = chars.length; --j >= 0; ) {
char c = chars[j];
charSet._mapping[c >> 6] = charSet._mapping[c >> 6] | 1L << (c & 0x3F);
} 
return charSet;
}

public static CharSet rangeOf(char first, char last) {
if (first > last) {
throw new IllegalArgumentException("first should be less or equal to last");
}
CharSet charSet = new CharSet(new long[(last >> 6) + 1]);
for (char c = first; c <= last; c = (char)(c + 1)) {
charSet._mapping[c >> 6] = charSet._mapping[c >> 6] | 1L << (c & 0x3F);
}
return charSet;
}

public boolean contains(char c) {
int i = c >> 6;
return (i < this._mapping.length) ? (((this._mapping[i] & 1L << (c & 0x3F)) != 0L)) : false;
}

public int indexIn(CharSequence csq) {
return indexIn(csq, 0);
}

public int indexIn(CharSequence csq, int fromIndex) {
for (int i = fromIndex, n = csq.length(); i < n; i++) {
if (contains(csq.charAt(i)))
return i; 
} 
return -1;
}

public int indexIn(char[] chars) {
return indexIn(chars, 0);
}

public int indexIn(char[] chars, int fromIndex) {
for (int i = fromIndex, n = chars.length; i < n; i++) {
if (contains(chars[i]))
return i; 
} 
return -1;
}

public int lastIndexIn(CharSequence csq) {
return lastIndexIn(csq, csq.length() - 1);
}

public int lastIndexIn(CharSequence csq, int fromIndex) {
for (int i = fromIndex; i >= 0; i--) {
if (contains(csq.charAt(i)))
return i; 
} 
return -1;
}

public int lastIndexIn(char[] chars) {
return lastIndexIn(chars, chars.length - 1);
}

public int lastIndexIn(char[] chars, int fromIndex) {
for (int i = fromIndex; i >= 0; i--) {
if (contains(chars[i]))
return i; 
} 
return -1;
}

public CharSet plus(CharSet that) {
if (that._mapping.length > this._mapping.length)
return that.plus(this); 
CharSet result = copy();
for (int i = that._mapping.length; --i >= 0;) {
result._mapping[i] = result._mapping[i] | that._mapping[i];
}
return result;
}

public CharSet minus(CharSet that) {
CharSet result = copy();
for (int i = MathLib.min(this._mapping.length, that._mapping.length); --i >= 0;) {
result._mapping[i] = result._mapping[i] & (that._mapping[i] ^ 0xFFFFFFFFFFFFFFFFL);
}
return result;
}

public String toString() {
TextBuilder tb = new TextBuilder();
tb.append('{');
int length = this._mapping.length << 6;
for (int i = 0; i < length; i++) {
if (contains((char)i)) {
if (tb.length() > 1) {
tb.append(',');
tb.append(' ');
} 
tb.append('\'');
tb.append((char)i);
tb.append('\'');
} 
} 
tb.append('}');
return tb.toString();
}

private CharSet copy() {
CharSet charSet = new CharSet(new long[this._mapping.length]);
for (int i = this._mapping.length; --i >= 0;) {
charSet._mapping[i] = this._mapping[i];
}
return charSet;
}

public CharSet value() {
return this;
}
}

