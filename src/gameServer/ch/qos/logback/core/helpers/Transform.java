package ch.qos.logback.core.helpers;

import java.util.regex.Pattern;

public class Transform
{
private static final String CDATA_START = "<![CDATA[";
private static final String CDATA_END = "]]>";
private static final String CDATA_PSEUDO_END = "]]&gt;";
private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
private static final int CDATA_END_LEN = "]]>".length();
private static final Pattern UNSAFE_XML_CHARS = Pattern.compile("[\000-\b\013\f\016-\037<>&'\"]");

public static String escapeTags(String input) {
if (input == null || input.length() == 0 || !UNSAFE_XML_CHARS.matcher(input).find()) {
return input;
}
StringBuffer buf = new StringBuffer(input);
return escapeTags(buf);
}

public static String escapeTags(StringBuffer buf) {
for (int i = 0; i < buf.length(); i++) {
char ch = buf.charAt(i);
switch (ch) {
case '\t':
case '\n':
case '\r':
break;

case '&':
buf.replace(i, i + 1, "&amp;");
break;
case '<':
buf.replace(i, i + 1, "&lt;");
break;
case '>':
buf.replace(i, i + 1, "&gt;");
break;
case '"':
buf.replace(i, i + 1, "&quot;");
break;
case '\'':
buf.replace(i, i + 1, "&#39;");
break;
default:
if (ch < ' ')
{

buf.replace(i, i + 1, "�");
}
break;
} 
} 
return buf.toString();
}

public static void appendEscapingCDATA(StringBuilder output, String str) {
if (str == null) {
return;
}

int end = str.indexOf("]]>");

if (end < 0) {
output.append(str);

return;
} 

int start = 0;

while (end > -1) {
output.append(str.substring(start, end));
output.append("]]>]]&gt;<![CDATA[");
start = end + CDATA_END_LEN;

if (start < str.length()) {
end = str.indexOf("]]>", start);

continue;
} 
return;
} 
output.append(str.substring(start));
}
}

