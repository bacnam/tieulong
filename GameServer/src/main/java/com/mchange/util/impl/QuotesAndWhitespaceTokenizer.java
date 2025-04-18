package com.mchange.util.impl;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class QuotesAndWhitespaceTokenizer
extends StringEnumerationHelperBase
{
Object current;
LinkedList list = new LinkedList();

public QuotesAndWhitespaceTokenizer(String paramString) throws IllegalArgumentException {
int i = 0;
int j = paramString.length();
while (i < j) {

int k = paramString.indexOf('"', i);
if (k >= 0) {

StringTokenizer stringTokenizer1 = new StringTokenizer(paramString.substring(i, k));
if (stringTokenizer1.hasMoreTokens()) this.list.add(stringTokenizer1); 
int m = paramString.indexOf('"', k + 1);
if (m == -1)
throw new IllegalArgumentException("Badly quoted string: " + paramString); 
this.list.add(paramString.substring(k + 1, m));
i = m + 1;

continue;
} 
StringTokenizer stringTokenizer = new StringTokenizer(paramString.substring(i));
if (stringTokenizer.hasMoreTokens()) this.list.add(stringTokenizer);

} 

advance();
}

public synchronized boolean hasMoreStrings() {
return (this.current != null);
}

public synchronized String nextString() {
if (this.current instanceof String) {

String str1 = (String)this.current;
advance();
return str1;
} 

StringTokenizer stringTokenizer = (StringTokenizer)this.current;
String str = stringTokenizer.nextToken();
if (!stringTokenizer.hasMoreTokens()) advance(); 
return str;
}

private void advance() {
if (this.list.isEmpty()) {
this.current = null;
} else {

this.current = this.list.getFirst();
this.list.removeFirst();
} 
}

public static void main(String[] paramArrayOfString) {
String str = "\t  \n\r";

for (QuotesAndWhitespaceTokenizer quotesAndWhitespaceTokenizer = new QuotesAndWhitespaceTokenizer(str); quotesAndWhitespaceTokenizer.hasMoreStrings();)
System.out.println(quotesAndWhitespaceTokenizer.nextString()); 
}
}

