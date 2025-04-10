package javolution.text;

import java.io.IOException;
import java.io.Serializable;
import javolution.lang.MathLib;

public class TextBuilder
implements Appendable, CharSequence, Serializable
{
private static final long serialVersionUID = 1536L;
private static final int B0 = 5;
private static final int C0 = 32;
private static final int B1 = 10;
private static final int C1 = 1024;
private static final int M1 = 1023;
private char[] _low = new char[32];

private char[][] _high = new char[1][];

private int _length;

private int _capacity = 32;

public TextBuilder() {
this._high[0] = this._low;
}

public TextBuilder(String str) {
this();
append(str);
}

public TextBuilder(int capacity) {
this();
while (capacity > this._capacity) {
increaseCapacity();
}
}

public final int length() {
return this._length;
}

public final char charAt(int index) {
if (index >= this._length)
throw new IndexOutOfBoundsException(); 
return (index < 1024) ? this._low[index] : this._high[index >> 10][index & 0x3FF];
}

public final void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
if (srcBegin < 0 || srcBegin > srcEnd || srcEnd > this._length)
throw new IndexOutOfBoundsException();  int j;
for (int i = srcBegin; i < srcEnd; ) {
char[] chars0 = this._high[i >> 10];
int i0 = i & 0x3FF;
int length = MathLib.min(1024 - i0, srcEnd - i);
System.arraycopy(chars0, i0, dst, j, length);
i += length;
j += length;
} 
}

public final void setCharAt(int index, char c) {
if (index < 0 || index >= this._length)
throw new IndexOutOfBoundsException(); 
this._high[index >> 10][index & 0x3FF] = c;
}

public final void setLength(int newLength) {
setLength(newLength, false);
}

public final void setLength(int newLength, char fillChar) {
if (newLength < 0)
throw new IndexOutOfBoundsException(); 
if (newLength <= this._length) {
this._length = newLength;
} else {
for (int i = this._length; i++ < newLength;) {
append(fillChar);
}
} 
}

public final CharSequence subSequence(int start, int end) {
if (start < 0 || end < 0 || start > end || end > this._length)
throw new IndexOutOfBoundsException(); 
return Text.valueOf(this, start, end);
}

public final TextBuilder append(char c) {
if (this._length >= this._capacity)
increaseCapacity(); 
this._high[this._length >> 10][this._length & 0x3FF] = c;
this._length++;
return this;
}

public final TextBuilder append(Object obj) {
if (obj == null) return append("null"); 
TextFormat<Object> textFormat = TextContext.getFormat(obj.getClass());
if (textFormat == null) return append(obj.toString()); 
return textFormat.format(obj, this);
}

public final TextBuilder append(CharSequence csq) {
return (csq == null) ? append("null") : append(csq, 0, csq.length());
}

public final TextBuilder append(CharSequence csq, int start, int end) {
if (csq == null)
return append("null"); 
if (start < 0 || end < 0 || start > end || end > csq.length())
throw new IndexOutOfBoundsException(); 
for (int i = start; i < end;) {
append(csq.charAt(i++));
}
return this;
}

public final TextBuilder append(String str) {
return (str == null) ? append("null") : append(str, 0, str.length());
}

public final TextBuilder append(String str, int start, int end) {
if (str == null)
return append("null"); 
if (start < 0 || end < 0 || start > end || end > str.length()) {
throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", str.length(): " + str.length());
}
int newLength = this._length + end - start;
while (this._capacity < newLength)
increaseCapacity(); 
int j;
for (int i = start; i < end; ) {
char[] chars = this._high[j >> 10];
int dstBegin = j & 0x3FF;
int inc = MathLib.min(1024 - dstBegin, end - i);
str.getChars(i, i += inc, chars, dstBegin);
j += inc;
} 
this._length = newLength;
return this;
}

public final TextBuilder append(Text txt) {
return (txt == null) ? append("null") : append(txt, 0, txt.length());
}

public final TextBuilder append(Text txt, int start, int end) {
if (txt == null)
return append("null"); 
if (start < 0 || end < 0 || start > end || end > txt.length())
throw new IndexOutOfBoundsException(); 
int newLength = this._length + end - start;
while (this._capacity < newLength)
increaseCapacity(); 
int j;
for (int i = start; i < end; ) {
char[] chars = this._high[j >> 10];
int dstBegin = j & 0x3FF;
int inc = MathLib.min(1024 - dstBegin, end - i);
txt.getChars(i, i += inc, chars, dstBegin);
j += inc;
} 
this._length = newLength;
return this;
}

public final TextBuilder append(char[] chars) {
append(chars, 0, chars.length);
return this;
}

public final TextBuilder append(char[] chars, int offset, int length) {
int end = offset + length;
if (offset < 0 || length < 0 || end > chars.length)
throw new IndexOutOfBoundsException(); 
int newLength = this._length + length;
while (this._capacity < newLength)
increaseCapacity(); 
int j;
for (int i = offset; i < end; ) {
char[] dstChars = this._high[j >> 10];
int dstBegin = j & 0x3FF;
int inc = MathLib.min(1024 - dstBegin, end - i);
System.arraycopy(chars, i, dstChars, dstBegin, inc);
i += inc;
j += inc;
} 
this._length = newLength;
return this;
}

public final TextBuilder append(boolean b) {
return b ? append("true") : append("false");
}

public final TextBuilder append(int i) {
if (i <= 0) {
if (i == 0)
return append("0"); 
if (i == Integer.MIN_VALUE)
return append("-2147483648"); 
append('-');
i = -i;
} 
int digits = MathLib.digitLength(i);
if (this._capacity < this._length + digits)
increaseCapacity(); 
this._length += digits;
for (int index = this._length - 1;; index--) {
int j = i / 10;
this._high[index >> 10][index & 0x3FF] = (char)(48 + i - j * 10);
if (j == 0)
return this; 
i = j;
} 
}

public final TextBuilder append(int i, int radix) {
if (radix == 10)
return append(i); 
if (radix < 2 || radix > 36)
throw new IllegalArgumentException("radix: " + radix); 
if (i < 0) {
append('-');
if (i == Integer.MIN_VALUE) {
appendPositive(-(i / radix), radix);
return append(DIGIT_TO_CHAR[-(i % radix)]);
} 
i = -i;
} 
appendPositive(i, radix);
return this;
}

private void appendPositive(int l1, int radix) {
if (l1 >= radix) {
int l2 = l1 / radix;

if (l2 >= radix) {
int l3 = l2 / radix;

if (l3 >= radix) {
int l4 = l3 / radix;
appendPositive(l4, radix);
append(DIGIT_TO_CHAR[l3 - l4 * radix]);
} else {
append(DIGIT_TO_CHAR[l3]);
}  append(DIGIT_TO_CHAR[l2 - l3 * radix]);
} else {
append(DIGIT_TO_CHAR[l2]);
}  append(DIGIT_TO_CHAR[l1 - l2 * radix]);
} else {
append(DIGIT_TO_CHAR[l1]);
} 
}
private static final char[] DIGIT_TO_CHAR = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

public final TextBuilder append(long l) {
if (l <= 0L) {
if (l == 0L)
return append("0"); 
if (l == Long.MIN_VALUE)
return append("-9223372036854775808"); 
append('-');
l = -l;
} 
if (l <= 2147483647L)
return append((int)l); 
append(l / 1000000000L);
int i = (int)(l % 1000000000L);
int digits = MathLib.digitLength(i);
append("000000000", 0, 9 - digits);
return append(i);
}

public final TextBuilder append(long l, int radix) {
if (radix == 10)
return append(l); 
if (radix < 2 || radix > 36)
throw new IllegalArgumentException("radix: " + radix); 
if (l < 0L) {
append('-');
if (l == Long.MIN_VALUE) {
appendPositive(-(l / radix), radix);
return append(DIGIT_TO_CHAR[(int)-(l % radix)]);
} 
l = -l;
} 
appendPositive(l, radix);
return this;
}

private void appendPositive(long l1, int radix) {
if (l1 >= radix) {
long l2 = l1 / radix;

if (l2 >= radix) {
long l3 = l2 / radix;

if (l3 >= radix) {
long l4 = l3 / radix;
appendPositive(l4, radix);
append(DIGIT_TO_CHAR[(int)(l3 - l4 * radix)]);
} else {
append(DIGIT_TO_CHAR[(int)l3]);
}  append(DIGIT_TO_CHAR[(int)(l2 - l3 * radix)]);
} else {
append(DIGIT_TO_CHAR[(int)l2]);
}  append(DIGIT_TO_CHAR[(int)(l1 - l2 * radix)]);
} else {
append(DIGIT_TO_CHAR[(int)l1]);
} 
}

public final TextBuilder append(float f) {
return append(f, 10, (MathLib.abs(f) >= 1.0E7D || MathLib.abs(f) < 0.001D), false);
}

public final TextBuilder append(double d) {
return append(d, -1, (MathLib.abs(d) >= 1.0E7D || MathLib.abs(d) < 0.001D), false);
}

public final TextBuilder append(double d, int digits, boolean scientific, boolean showZero) {
long m;
if (digits > 19)
throw new IllegalArgumentException("digits: " + digits); 
if (d != d)
return append("NaN"); 
if (d == Double.POSITIVE_INFINITY)
return append("Infinity"); 
if (d == Double.NEGATIVE_INFINITY)
return append("-Infinity"); 
if (d == 0.0D) {
if (digits < 0)
return append("0.0"); 
append('0');
if (showZero) {
append('.');
for (int j = 1; j < digits; j++) {
append('0');
}
} 
return this;
} 
if (d < 0.0D) {
d = -d;
append('-');
} 

int e = MathLib.floorLog10(d);

if (digits < 0) {

long m17 = MathLib.toLongPow10(d, 16 - e);

long m16 = m17 / 10L;
double dd = MathLib.toDoublePow10(m16, e - 16 + 1);
if (dd == d) {
digits = 16;
m = m16;
} else {
digits = 17;
m = m17;
} 
} else {

m = MathLib.toLongPow10(d, digits - 1 - e);
} 

if (scientific || e >= digits) {

long pow10 = POW10_LONG[digits - 1];
int k = (int)(m / pow10);
append((char)(48 + k));
m -= pow10 * k;
appendFraction(m, digits - 1, showZero);
append('E');
append(e);
} else {
int exp = digits - e - 1;
if (exp < POW10_LONG.length) {
long pow10 = POW10_LONG[exp];
long l = m / pow10;
append(l);
m -= pow10 * l;
} else {
append('0');
}  appendFraction(m, exp, showZero);
} 
return this;
}

private void appendFraction(long l, int digits, boolean showZero) {
append('.');
if (l == 0L)
{ if (showZero) {
for (int i = 0; i < digits; i++) {
append('0');
}
} else {
append('0');
}  }
else { int length = MathLib.digitLength(l);
for (int j = length; j < digits; j++) {
append('0');
}
if (!showZero)
while (l % 10L == 0L) {
l /= 10L;
} 
append(l); }

}

private static final long[] POW10_LONG = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };

public final TextBuilder insert(int index, CharSequence csq) {
if (index < 0 || index > this._length)
throw new IndexOutOfBoundsException("index: " + index); 
int shift = csq.length();
int newLength = this._length + shift;
while (newLength >= this._capacity) {
increaseCapacity();
}
this._length = newLength; int i;
for (i = this._length - shift; --i >= index;) {
setCharAt(i + shift, charAt(i));
}
for (i = csq.length(); --i >= 0;) {
setCharAt(index + i, csq.charAt(i));
}
return this;
}

public final TextBuilder clear() {
this._length = 0;
return this;
}

public final TextBuilder delete(int start, int end) {
if (start < 0 || end < 0 || start > end || end > length())
throw new IndexOutOfBoundsException(); 
for (int i = end, j = start; i < this._length;) {
setCharAt(j++, charAt(i++));
}
this._length -= end - start;
return this;
}

public final TextBuilder reverse() {
int n = this._length - 1;
for (int j = n - 1 >> 1; j >= 0; ) {
char c = charAt(j);
setCharAt(j, charAt(n - j));
setCharAt(n - j--, c);
} 
return this;
}

public final Text toText() {
return Text.valueOf(this, 0, this._length);
}

public final String toString() {
return (this._length < 1024) ? new String(this._low, 0, this._length) : toLargeString();
}

private String toLargeString() {
char[] data = new char[this._length];
getChars(0, this._length, data, 0);
return new String(data, 0, this._length);
}

public final CharArray toCharArray() {
char[] data;
CharArray cArray = new CharArray();

if (this._length < 1024) {
data = this._low;
} else {
data = new char[this._length];
getChars(0, this._length, data, 0);
} 
cArray.setArray(data, 0, this._length);
return cArray;
}

public final int hashCode() {
int h = 0;
for (int i = 0; i < this._length;) {
h = 31 * h + charAt(i++);
}
return h;
}

public final boolean equals(Object obj) {
if (this == obj)
return true; 
if (!(obj instanceof TextBuilder))
return false; 
TextBuilder that = (TextBuilder)obj;
if (this._length != that._length)
return false; 
for (int i = 0; i < this._length;) {
if (charAt(i) != that.charAt(i++))
return false; 
} 
return true;
}

public final boolean contentEquals(CharSequence csq) {
if (csq.length() != this._length)
return false; 
for (int i = 0; i < this._length; ) {
char c = this._high[i >> 10][i & 0x3FF];
if (csq.charAt(i++) != c)
return false; 
} 
return true;
}

private void increaseCapacity() {
if (this._capacity < 1024) {
this._capacity <<= 1;
char[] tmp = new char[this._capacity];
System.arraycopy(this._low, 0, tmp, 0, this._length);
this._low = tmp;
this._high[0] = tmp;
} else {
int j = this._capacity >> 10;
if (j >= this._high.length) {
char[][] tmp = new char[this._high.length * 2][];
System.arraycopy(this._high, 0, tmp, 0, this._high.length);
this._high = tmp;
} 
this._high[j] = new char[1024];
this._capacity += 1024;
} 
}
}

