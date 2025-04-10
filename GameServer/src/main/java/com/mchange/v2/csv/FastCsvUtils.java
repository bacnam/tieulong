package com.mchange.v2.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FastCsvUtils
{
private static final int ESCAPE_BIT = 16777216;
private static final int SHIFT_BIT = 33554432;
private static final int SHIFT_OFFSET = 8;

public static String csvReadLine(BufferedReader paramBufferedReader) throws IOException {
String str2, str1 = paramBufferedReader.readLine();

if (str1 != null) {

int i = countQuotes(str1);
if (i % 2 != 0) {

StringBuilder stringBuilder = new StringBuilder(str1);

while (true) {
str1 = paramBufferedReader.readLine();
stringBuilder.append(str1);
i += countQuotes(str1);

if (i % 2 == 0)
return stringBuilder.toString(); 
} 
} 
str2 = str1;
} else {

str2 = null;
} 
return str2;
}

private static int countQuotes(String paramString) {
char[] arrayOfChar = paramString.toCharArray();
byte b1 = 0; byte b2; int i;
for (b2 = 0, i = arrayOfChar.length; b2 < i; b2++) {

if (arrayOfChar[b2] == '"') b1++; 
} 
return b1;
}

public static String[] splitRecord(String paramString) throws MalformedCsvException {
int[] arrayOfInt = upshiftQuoteString(paramString);

List<int[]> list = splitShifted(arrayOfInt);
int i = list.size();
String[] arrayOfString = new String[i];
for (byte b = 0; b < i; b++)
arrayOfString[b] = downshift(list.get(b)); 
return arrayOfString;
}

private static void debugPrint(int[] paramArrayOfint) {
int i = paramArrayOfint.length;
char[] arrayOfChar = new char[i];
for (byte b = 0; b < i; b++)
arrayOfChar[b] = isShifted(paramArrayOfint[b]) ? '_' : (char)paramArrayOfint[b]; 
System.err.println(new String(arrayOfChar));
}

private static List splitShifted(int[] paramArrayOfint) {
ArrayList<int[]> arrayList = new ArrayList();

int i = 0; byte b; int j;
for (b = 0, j = paramArrayOfint.length; b <= j; b++) {

if (b == j || paramArrayOfint[b] == 44) {

int k = b - i;

int n = -1; int m;
for (m = i; m <= b; m++) {

if (m == b) {

n = 0;
break;
} 
if (paramArrayOfint[m] != 32 && paramArrayOfint[m] != 9)
break; 
} 
if (n < 0)
{
if (m == b - 1) {
n = 1;
} else {

for (n = b - m; n > 0; n--) {

int i1 = m + n - 1;
if (paramArrayOfint[i1] != 32 && paramArrayOfint[i1] != 9) {
break;
}
} 
} 
}

int[] arrayOfInt = new int[n];
if (n > 0)
System.arraycopy(paramArrayOfint, m, arrayOfInt, 0, n); 
arrayList.add(arrayOfInt);
i = b + 1;
} 
} 
return arrayList;
}

private static String downshift(int[] paramArrayOfint) {
int i = paramArrayOfint.length;
char[] arrayOfChar = new char[i];
for (byte b = 0; b < i; b++) {

int j = paramArrayOfint[b];
arrayOfChar[b] = (char)(isShifted(j) ? (j >>> 8) : j);
} 
return new String(arrayOfChar);
}

private static boolean isShifted(int paramInt) {
return ((paramInt & 0x2000000) != 0);
}

private static int[] upshiftQuoteString(String paramString) throws MalformedCsvException {
char[] arrayOfChar = paramString.toCharArray();
int[] arrayOfInt1 = new int[arrayOfChar.length];

EscapedCharReader escapedCharReader = new EscapedCharReader(arrayOfChar);
byte b = 0;
boolean bool = false;
int i;
for (i = escapedCharReader.read(bool); i >= 0; i = escapedCharReader.read(bool)) {

if (i == 34) {
bool = !bool ? true : false;
} else {
arrayOfInt1[b++] = findShiftyChar(i, bool);
} 
} 
int[] arrayOfInt2 = new int[b];
System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, b);
return arrayOfInt2;
}

private static int findShiftyChar(int paramInt, boolean paramBoolean) {
return paramBoolean ? (paramInt << 8 | 0x2000000) : paramInt;
}
private static int escape(int paramInt) {
return paramInt | 0x1000000;
}
private static boolean isEscaped(int paramInt) {
return ((paramInt & 0x1000000) != 0);
}

private static class EscapedCharReader
{
char[] chars;
int finger;

EscapedCharReader(char[] param1ArrayOfchar) {
this.chars = param1ArrayOfchar;
this.finger = 0;
}

int read(boolean param1Boolean) throws MalformedCsvException {
if (this.finger < this.chars.length) {

char c = this.chars[this.finger++];
if (c == '"' && param1Boolean) {

if (this.finger < this.chars.length) {

char c1 = this.chars[this.finger];
if (c1 == '"') {

this.finger++;

return FastCsvUtils.escape(c1);
} 
return c;
} 

return c;
} 

return c;
} 

return -1;
}
}
}

