package javolution.text;

import java.io.IOException;
import javolution.lang.MathLib;
import javolution.lang.Realtime;

@Realtime
public final class TypeFormat
{
private static final int INT_MAX_DIV10 = 214748364;
private static final long LONG_MAX_DIV10 = 922337203685477580L;

public static boolean parseBoolean(CharSequence csq, Cursor cursor) {
int start = cursor.getIndex();
int end = csq.length();
if (end >= start + 5 && (csq.charAt(start) == 'f' || csq.charAt(start) == 'F')) {

if ((csq.charAt(++start) == 'a' || csq.charAt(start) == 'A') && (csq.charAt(++start) == 'l' || csq.charAt(start) == 'L') && (csq.charAt(++start) == 's' || csq.charAt(start) == 'S') && (csq.charAt(++start) == 'e' || csq.charAt(start) == 'E')) {

cursor.increment(5);
return false;
} 
} else if (end >= start + 4 && (csq.charAt(start) == 't' || csq.charAt(start) == 'T')) {

if ((csq.charAt(++start) == 'r' || csq.charAt(start) == 'R') && (csq.charAt(++start) == 'u' || csq.charAt(start) == 'U') && (csq.charAt(++start) == 'e' || csq.charAt(start) == 'E')) {

cursor.increment(4);
return true;
} 
}  throw new IllegalArgumentException("Invalid boolean representation");
}

public static boolean parseBoolean(CharSequence csq) {
Cursor cursor = new Cursor();
boolean result = parseBoolean(csq, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

public static byte parseByte(CharSequence csq, int radix, Cursor cursor) {
int i = parseInt(csq, radix, cursor);
if (i < -128 || i > 127)
throw new NumberFormatException("Overflow"); 
return (byte)i;
}

public static byte parseByte(CharSequence csq, int radix) {
Cursor cursor = new Cursor();
byte result = parseByte(csq, radix, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

public static byte parseByte(CharSequence csq, Cursor cursor) {
return parseByte(csq, 10, cursor);
}

public static byte parseByte(CharSequence csq) {
return parseByte(csq, 10);
}

public static short parseShort(CharSequence csq, int radix, Cursor cursor) {
int i = parseInt(csq, radix, cursor);
if (i < -32768 || i > 32767)
throw new NumberFormatException("Overflow"); 
return (short)i;
}

public static short parseShort(CharSequence csq, int radix) {
Cursor cursor = new Cursor();
short result = parseShort(csq, radix, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

public static short parseShort(CharSequence csq, Cursor cursor) {
return parseShort(csq, 10, cursor);
}

public static short parseShort(CharSequence csq) {
return parseShort(csq, 10);
}

public static int parseInt(CharSequence csq, int radix, Cursor cursor) {
int start = cursor.getIndex();
int end = csq.length();
boolean isNegative = false;
int result = 0;
int i = start;
for (; i < end; i++) {
char c = csq.charAt(i);
int digit = (c <= '9') ? (c - 48) : ((c <= 'Z' && c >= 'A') ? (c - 65 + 10) : ((c <= 'z' && c >= 'a') ? (c - 97 + 10) : -1));

if (digit >= 0 && digit < radix) {
int newResult = result * radix - digit;
if (newResult > result) {
throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
}
result = newResult; continue;
}  if (c == '-' && i == start) {
isNegative = true; continue;
}  if (c == '+' && i == start) {
continue;
}
} 

if (result == 0 && (end == 0 || csq.charAt(i - 1) != '0')) {
throw new NumberFormatException("Invalid integer representation for " + csq.subSequence(start, end));
}

if (result == Integer.MIN_VALUE && !isNegative) {
throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
}
cursor.increment(i - start);
return isNegative ? result : -result;
}

public static int parseInt(CharSequence csq, int radix) {
Cursor cursor = new Cursor();
int result = parseInt(csq, radix, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

public static int parseInt(CharSequence csq, Cursor cursor) {
return parseInt(csq, 10, cursor);
}

public static int parseInt(CharSequence csq) {
return parseInt(csq, 10);
}

public static long parseLong(CharSequence csq, int radix, Cursor cursor) {
int start = cursor.getIndex();
int end = csq.length();
boolean isNegative = false;
long result = 0L;
int i = start;
for (; i < end; i++) {
char c = csq.charAt(i);
int digit = (c <= '9') ? (c - 48) : ((c <= 'Z' && c >= 'A') ? (c - 65 + 10) : ((c <= 'z' && c >= 'a') ? (c - 97 + 10) : -1));

if (digit >= 0 && digit < radix) {
long newResult = result * radix - digit;
if (newResult > result) {
throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
}
result = newResult; continue;
}  if (c == '-' && i == start) {
isNegative = true; continue;
}  if (c == '+' && i == start) {
continue;
}
} 

if (result == 0L && (end == 0 || csq.charAt(i - 1) != '0')) {
throw new NumberFormatException("Invalid integer representation for " + csq.subSequence(start, end));
}

if (result == Long.MIN_VALUE && !isNegative) {
throw new NumberFormatException("Overflow parsing " + csq.subSequence(start, end));
}
cursor.increment(i - start);
return isNegative ? result : -result;
}

public static long parseLong(CharSequence csq, int radix) {
Cursor cursor = new Cursor();
long result = parseLong(csq, radix, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

public static long parseLong(CharSequence csq, Cursor cursor) {
return parseLong(csq, 10, cursor);
}

public static long parseLong(CharSequence csq) {
return parseLong(csq, 10);
}

public static float parseFloat(CharSequence csq, Cursor cursor) {
return (float)parseDouble(csq, cursor);
}

public static float parseFloat(CharSequence csq) {
return (float)parseDouble(csq);
}

public static double parseDouble(CharSequence csq, Cursor cursor) throws NumberFormatException {
int start = cursor.getIndex();
int end = csq.length();
int i = start;
char c = csq.charAt(i);

if (c == 'N' && match("NaN", csq, i, end)) {
cursor.increment(3);
return Double.NaN;
} 

boolean isNegative = (c == '-');
if ((isNegative || c == '+') && ++i < end) {
c = csq.charAt(i);
}

if (c == 'I' && match("Infinity", csq, i, end)) {
cursor.increment(i + 8 - start);
return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
} 

if ((c < '0' || c > '9') && c != '.') {
throw new NumberFormatException("Digit or '.' required");
}

long decimal = 0L;
int decimalPoint = -1;
while (true) {
int digit = c - 48;
if (digit >= 0 && digit < 10) {
long tmp = decimal * 10L + digit;
if (decimal > 922337203685477580L || tmp < decimal) {
throw new NumberFormatException("Too many digits - Overflow");
}
decimal = tmp;
} else if (c == '.' && decimalPoint < 0) {
decimalPoint = i;
} else {
break;
}  if (++i >= end)
break; 
c = csq.charAt(i);
} 
if (isNegative)
decimal = -decimal; 
int fractionLength = (decimalPoint >= 0) ? (i - decimalPoint - 1) : 0;

int exp = 0;
if (i < end && (c == 'E' || c == 'e')) {
c = csq.charAt(++i);
boolean isNegativeExp = (c == '-');
if ((isNegativeExp || c == '+') && ++i < end)
c = csq.charAt(i); 
if (c < '0' || c > '9')
throw new NumberFormatException("Invalid exponent"); 
while (true) {
int digit = c - 48;
if (digit >= 0 && digit < 10) {
int tmp = exp * 10 + digit;
if (exp > 214748364 || tmp < exp)
throw new NumberFormatException("Exponent Overflow"); 
exp = tmp;

if (++i >= end)
break; 
c = csq.charAt(i); continue;
}  break;
}  if (isNegativeExp)
exp = -exp; 
} 
cursor.increment(i - start);
return MathLib.toDoublePow10(decimal, exp - fractionLength);
}

public static double parseDouble(CharSequence csq) throws NumberFormatException {
Cursor cursor = new Cursor();
double result = parseDouble(csq, cursor);
if (!cursor.atEnd(csq)) {
throw new IllegalArgumentException("Extraneous characters \"" + cursor.tail(csq) + "\"");
}
return result;
}

static boolean match(String str, CharSequence csq, int start, int length) {
for (int i = 0; i < str.length(); i++) {
if (start + i >= length || csq.charAt(start + i) != str.charAt(i))
return false; 
} 
return true;
}

static boolean match(String str, String csq, int start, int length) {
for (int i = 0; i < str.length(); i++) {
if (start + i >= length || csq.charAt(start + i) != str.charAt(i))
return false; 
} 
return true;
}

public static Appendable format(boolean b, Appendable a) throws IOException {
return b ? a.append("true") : a.append("false");
}

public static Appendable format(int i, Appendable a) throws IOException {
if (a instanceof TextBuilder)
return ((TextBuilder)a).append(i); 
TextBuilder tb = new TextBuilder();
tb.append(i);
return a.append(tb);
}

public static Appendable format(int i, int radix, Appendable a) throws IOException {
if (a instanceof TextBuilder)
return ((TextBuilder)a).append(i, radix); 
TextBuilder tb = new TextBuilder();
tb.append(i, radix);
return a.append(tb);
}

public static Appendable format(long l, Appendable a) throws IOException {
if (a instanceof TextBuilder)
return ((TextBuilder)a).append(l); 
TextBuilder tb = new TextBuilder();
tb.append(l);
return a.append(tb);
}

public static Appendable format(long l, int radix, Appendable a) throws IOException {
if (a instanceof TextBuilder)
return ((TextBuilder)a).append(l, radix); 
TextBuilder tb = new TextBuilder();
tb.append(l, radix);
return a.append(tb);
}

public static Appendable format(float f, Appendable a) throws IOException {
return format(f, 10, (MathLib.abs(f) >= 1.0E7D || MathLib.abs(f) < 0.001D), false, a);
}

public static Appendable format(double d, Appendable a) throws IOException {
return format(d, -1, (MathLib.abs(d) >= 1.0E7D || MathLib.abs(d) < 0.001D), false, a);
}

public static Appendable format(double d, int digits, boolean scientific, boolean showZero, Appendable a) throws IOException {
if (a instanceof TextBuilder)
return ((TextBuilder)a).append(d, digits, scientific, showZero); 
TextBuilder tb = new TextBuilder();
tb.append(d, digits, scientific, showZero);
return a.append(tb);
}
}

