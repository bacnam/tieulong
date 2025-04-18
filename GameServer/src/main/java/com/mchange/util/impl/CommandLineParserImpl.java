package com.mchange.util.impl;

import com.mchange.util.CommandLineParser;

public class CommandLineParserImpl
implements CommandLineParser
{
String[] argv;
String[] validSwitches;
String[] reqSwitches;
String[] argSwitches;
char switch_char;

public CommandLineParserImpl(String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, char paramChar) {
this.argv = paramArrayOfString1;
this.validSwitches = (paramArrayOfString2 == null) ? new String[0] : paramArrayOfString2;
this.reqSwitches = (paramArrayOfString3 == null) ? new String[0] : paramArrayOfString3;
this.argSwitches = (paramArrayOfString4 == null) ? new String[0] : paramArrayOfString4;
this.switch_char = paramChar;
}

public CommandLineParserImpl(String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4) {
this(paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramArrayOfString4, '-');
}

public boolean checkSwitch(String paramString) {
for (byte b = 0; b < this.argv.length; b++) {
if (this.argv[b].charAt(0) == this.switch_char && this.argv[b].equals(this.switch_char + paramString))
return true; 
}  return false;
}

public String findSwitchArg(String paramString) {
for (byte b = 0; b < this.argv.length - 1; b++) {
if (this.argv[b].charAt(0) == this.switch_char && this.argv[b].equals(this.switch_char + paramString))
return (this.argv[b + 1].charAt(0) == this.switch_char) ? null : this.argv[b + 1]; 
}  return null;
}

public boolean checkArgv() {
return (checkValidSwitches() && checkRequiredSwitches() && checkSwitchArgSyntax());
}

boolean checkValidSwitches() {
for (byte b = 0; b < this.argv.length; b++) {
if (this.argv[b].charAt(0) == this.switch_char) {

byte b1 = 0; while (true) { if (b1 < this.validSwitches.length)
{ if (this.argv[b].equals(this.switch_char + this.validSwitches[b1]))
break;  b1++; continue; }  return false; } 
} 
}  return true;
}

boolean checkRequiredSwitches() {
for (int i = this.reqSwitches.length; --i >= 0;) {
if (!checkSwitch(this.reqSwitches[i])) return false; 
}  return true;
}

boolean checkSwitchArgSyntax() {
for (int i = this.argSwitches.length; --i >= 0;) {

if (checkSwitch(this.argSwitches[i])) {

String str = findSwitchArg(this.argSwitches[i]);
if (str == null || str.charAt(0) == this.switch_char)
return false; 
} 
} 
return true;
}

public int findLastSwitched() {
for (int i = this.argv.length; --i >= 0;) {
if (this.argv[i].charAt(0) == this.switch_char)
return i; 
}  return -1;
}

public String[] findUnswitchedArgs() {
String[] arrayOfString1 = new String[this.argv.length];
byte b1 = 0;
for (byte b2 = 0; b2 < this.argv.length; b2++) {

if (this.argv[b2].charAt(0) == this.switch_char)
{ if (contains(this.argv[b2].substring(1), this.argSwitches)) b2++;  }
else { arrayOfString1[b1++] = this.argv[b2]; }

}  String[] arrayOfString2 = new String[b1];
System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, b1);
return arrayOfString2;
}

private static boolean contains(String paramString, String[] paramArrayOfString) {
for (int i = paramArrayOfString.length; --i >= 0;) {
if (paramArrayOfString[i].equals(paramString)) return true; 
}  return false;
}
}

