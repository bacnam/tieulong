package com.mchange.v2.cmdline;

import java.util.HashMap;
import java.util.LinkedList;

class ParsedCommandLineImpl
implements ParsedCommandLine
{
String[] argv;
String switchPrefix;
String[] unswitchedArgs;
HashMap foundSwitches = new HashMap<Object, Object>();

ParsedCommandLineImpl(String[] paramArrayOfString1, String paramString, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4) throws BadCommandLineException {
this.argv = paramArrayOfString1;
this.switchPrefix = paramString;

LinkedList<String> linkedList = new LinkedList();
int i = paramString.length();
byte b;
for (b = 0; b < paramArrayOfString1.length; b++) {

if (paramArrayOfString1[b].startsWith(paramString)) {

String str1 = paramArrayOfString1[b].substring(i);
String str2 = null;

int j = str1.indexOf('=');
if (j >= 0) {

str2 = str1.substring(j + 1);
str1 = str1.substring(0, j);
}
else if (contains(str1, paramArrayOfString4)) {

if (b < paramArrayOfString1.length - 1 && !paramArrayOfString1[b + 1].startsWith(paramString)) {
str2 = paramArrayOfString1[++b];
}
} 
if (paramArrayOfString2 != null && !contains(str1, paramArrayOfString2))
throw new UnexpectedSwitchException("Unexpected Switch: " + str1, str1); 
if (paramArrayOfString4 != null && str2 != null && !contains(str1, paramArrayOfString4)) {
throw new UnexpectedSwitchArgumentException("Switch \"" + str1 + "\" should not have an " + "argument. Argument \"" + str2 + "\" found.", str1, str2);
}

this.foundSwitches.put(str1, str2);
} else {

linkedList.add(paramArrayOfString1[b]);
} 
} 
if (paramArrayOfString3 != null)
{
for (b = 0; b < paramArrayOfString3.length; b++) {
if (!this.foundSwitches.containsKey(paramArrayOfString3[b])) {
throw new MissingSwitchException("Required switch \"" + paramArrayOfString3[b] + "\" not found.", paramArrayOfString3[b]);
}
} 
}
this.unswitchedArgs = new String[linkedList.size()];
linkedList.toArray(this.unswitchedArgs);
}

public String getSwitchPrefix() {
return this.switchPrefix;
}
public String[] getRawArgs() {
return (String[])this.argv.clone();
}
public boolean includesSwitch(String paramString) {
return this.foundSwitches.containsKey(paramString);
}
public String getSwitchArg(String paramString) {
return (String)this.foundSwitches.get(paramString);
}
public String[] getUnswitchedArgs() {
return (String[])this.unswitchedArgs.clone();
}

private static boolean contains(String paramString, String[] paramArrayOfString) {
for (int i = paramArrayOfString.length; --i >= 0;) {
if (paramArrayOfString[i].equals(paramString)) return true; 
}  return false;
}
}

