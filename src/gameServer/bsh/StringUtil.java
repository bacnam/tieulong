package bsh;

import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtil
{
public static String[] split(String s, String delim) {
Vector<String> v = new Vector();
StringTokenizer st = new StringTokenizer(s, delim);
while (st.hasMoreTokens())
v.addElement(st.nextToken()); 
String[] sa = new String[v.size()];
v.copyInto((Object[])sa);
return sa;
}

public static String[] bubbleSort(String[] in) {
Vector<String> v = new Vector();
for (int i = 0; i < in.length; i++) {
v.addElement(in[i]);
}
int n = v.size();
boolean swap = true;
while (swap) {
swap = false;
for (int j = 0; j < n - 1; j++) {
if (((String)v.elementAt(j)).compareTo(v.elementAt(j + 1)) > 0) {

String tmp = v.elementAt(j + 1);
v.removeElementAt(j + 1);
v.insertElementAt(tmp, j);
swap = true;
} 
} 
} 
String[] out = new String[n];
v.copyInto((Object[])out);
return out;
}

public static String maxCommonPrefix(String one, String two) {
int i = 0;
while (one.regionMatches(0, two, 0, i))
i++; 
return one.substring(0, i - 1);
}

public static String methodString(String name, Class[] types) {
StringBuffer sb = new StringBuffer(name + "(");
if (types.length > 0)
sb.append(" "); 
for (int i = 0; i < types.length; i++) {

Class c = types[i];
sb.append(((c == null) ? "null" : c.getName()) + ((i < types.length - 1) ? ", " : " "));
} 

sb.append(")");
return sb.toString();
}

public static String normalizeClassName(Class type) {
return Reflect.normalizeClassName(type);
}
}

