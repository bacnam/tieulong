package ch.qos.logback.core.pattern.util;

public class RegularEscapeUtil
implements IEscapeUtil
{
public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
if (escapeChars.indexOf(next) >= 0) {
buf.append(next);
}
switch (next) {
case '_':
return;

case '\\':
buf.append(next);

case 't':
buf.append('\t');

case 'r':
buf.append('\r');

case 'n':
buf.append('\n');
} 

String commaSeperatedEscapeChars = formatEscapeCharsForListing(escapeChars);
throw new IllegalArgumentException("Illegal char '" + next + " at column " + pointer + ". Only \\\\, \\_" + commaSeperatedEscapeChars + ", \\t, \\n, \\r combinations are allowed as escape characters.");
}

String formatEscapeCharsForListing(String escapeChars) {
StringBuilder commaSeperatedEscapeChars = new StringBuilder();
for (int i = 0; i < escapeChars.length(); i++) {
commaSeperatedEscapeChars.append(", \\").append(escapeChars.charAt(i));
}
return commaSeperatedEscapeChars.toString();
}

public static String basicEscape(String s) {
int len = s.length();
StringBuilder sbuf = new StringBuilder(len);

int i = 0;
while (i < len) {
char c = s.charAt(i++);
if (c == '\\') {
c = s.charAt(i++);
if (c == 'n') {
c = '\n';
} else if (c == 'r') {
c = '\r';
} else if (c == 't') {
c = '\t';
} else if (c == 'f') {
c = '\f';
} else if (c == '\b') {
c = '\b';
} else if (c == '"') {
c = '"';
} else if (c == '\'') {
c = '\'';
} else if (c == '\\') {
c = '\\';
} 
} 
sbuf.append(c);
} 
return sbuf.toString();
}
}

