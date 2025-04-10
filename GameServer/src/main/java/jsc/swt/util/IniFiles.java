package jsc.swt.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Vector;

public class IniFiles
{
String inifile = "";
Vector INIFILE = new Vector();

public IniFiles(String paramString) {
this.inifile = paramString;
}

String bis(String paramString1, String paramString2) {
if (paramString1.indexOf(paramString2) != -1)
return paramString1.substring(0, paramString1.indexOf(paramString2)); 
return "";
}

public boolean getBoolValue(String paramString1, String paramString2) {
String str = getValue(paramString1, paramString2);
return str.equals("TRUE");
}

public int getIntValue(String paramString1, String paramString2) {
String str = getValue(paramString1, paramString2);
if (str.equals("")) str = "-1"; 
return (new Integer(str)).intValue();
}

public long getLongValue(String paramString1, String paramString2) {
String str = getValue(paramString1, paramString2);
if (str.equals("")) str = "-1"; 
return (new Long(str)).longValue();
}

public String getValue(String paramString1, String paramString2) {
for (byte b = 0; b < this.INIFILE.size(); b++) {
oberkey oberkey = this.INIFILE.elementAt(b);
if (oberkey.key.equals(paramString1)) {
for (byte b1 = 0; b1 < oberkey.UB.size(); b1++) {
unterkey unterkey = oberkey.UB.elementAt(b1);
if (unterkey.key.equals(paramString2)) {
return unterkey.value;
}
} 
return "";
} 
} 
return "";
}

String hinter(String paramString1, String paramString2) {
if (paramString1.indexOf(paramString2) != -1)
return paramString1.substring(paramString1.indexOf(paramString2) + paramString2.length()); 
return "";
}

public boolean loadIni() {
this.INIFILE = new Vector();
String str = "";

try { BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.inifile)));
while (true) {
str = bufferedReader.readLine();
if (str != null) {
str = str.trim();
if (!str.equals("") && !str.startsWith(";"))
{
if (str.startsWith("[")) {
str = hinter(str, "[");
str = bis(str, "]");
oberkey oberkey = new oberkey(this, str);
this.INIFILE.addElement(oberkey);
} else {
String str1 = bis(str, "=");
String str2 = hinter(str, "=");
unterkey unterkey = new unterkey(this, str1, str2);
((oberkey)this.INIFILE.lastElement()).UB.addElement(unterkey);
} 
}
} 
if (str == null) {
bufferedReader.close();
return true;
} 
}  }
catch (Exception exception) { return false; }

}

public boolean loadIni(String paramString) {
this.inifile = paramString;
return loadIni();
}

public void saveIni() {
try {
PrintStream printStream = new PrintStream(new FileOutputStream(this.inifile));

for (byte b = 0; b < this.INIFILE.size(); b++) {
oberkey oberkey = this.INIFILE.elementAt(b);
printStream.print("[" + oberkey.key + "]" + '\r' + '\n');
for (byte b1 = 0; b1 < oberkey.UB.size(); b1++) {
unterkey unterkey = oberkey.UB.elementAt(b1);
printStream.print(unterkey.key + "=" + unterkey.value + '\r' + '\n');
} 
printStream.print("\r\n");
} 
printStream.close();
} catch (IOException iOException) {}
}

public void saveIni(String paramString) {
this.inifile = paramString;
saveIni();
}

public void setValue(String paramString1, String paramString2, boolean paramBoolean) {
String str = "FALSE";
if (paramBoolean) str = "TRUE"; 
setValue(paramString1, paramString2, str);
}

public void setValue(String paramString1, String paramString2, int paramInt) {
setValue(paramString1, paramString2, "" + paramInt);
}

public void setValue(String paramString1, String paramString2, long paramLong) {
setValue(paramString1, paramString2, "" + paramLong);
}

public void setValue(String paramString1, String paramString2, String paramString3) {
for (byte b = 0; b < this.INIFILE.size(); b++) {
oberkey oberkey1 = this.INIFILE.elementAt(b);
if (oberkey1.key.equals(paramString1)) {
for (byte b1 = 0; b1 < oberkey1.UB.size(); b1++) {
unterkey unterkey2 = oberkey1.UB.elementAt(b1);
if (unterkey2.key.equals(paramString2)) {
unterkey2.value = paramString3;
return;
} 
} 
unterkey unterkey1 = new unterkey(this, paramString2, paramString3);
oberkey1.UB.addElement(unterkey1);
return;
} 
} 
oberkey oberkey = new oberkey(this, paramString1);
unterkey unterkey = new unterkey(this, paramString2, paramString3);
oberkey.UB.addElement(unterkey);
this.INIFILE.addElement(oberkey);
}

class unterkey { String key;

public unterkey(IniFiles this$0, String param1String1, String param1String2) {
this.this$0 = this$0; this.key = ""; this.value = "";
this.key = param1String1;
this.value = param1String2;
}
String value; private final IniFiles this$0; }
class oberkey { String key;
Vector UB;
private final IniFiles this$0;

public oberkey(IniFiles this$0, String param1String) {
this.this$0 = this$0; this.key = ""; this.UB = new Vector();
this.key = param1String;
} }

static class Test
{
public static void main(String[] param1ArrayOfString) {
IniFiles iniFiles = new IniFiles("C:\\WINDOWS\\SUSTATS.INI");
iniFiles.loadIni();
System.out.println(iniFiles.getValue("HELP", "Browser"));
System.out.println(iniFiles.getValue("HELP", "SUSPath"));
iniFiles.setValue("TEST", "Score", 42);
iniFiles.saveIni();
}
}
}

