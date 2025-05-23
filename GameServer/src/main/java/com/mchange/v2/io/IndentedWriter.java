package com.mchange.v2.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class IndentedWriter
extends FilterWriter
{
static final String EOL;

static {
String str = System.getProperty("line.separator");
EOL = (str != null) ? str : "\r\n";
}

int indent_level = 0;
boolean at_line_start = true;

public IndentedWriter(Writer paramWriter) {
super(paramWriter);
}
private boolean isEol(char paramChar) {
return (paramChar == '\r' || paramChar == '\n');
}
public void upIndent() {
this.indent_level++;
}
public void downIndent() {
this.indent_level--;
}

public void write(int paramInt) throws IOException {
this.out.write(paramInt);
this.at_line_start = isEol((char)paramInt);
}

public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
this.out.write(paramArrayOfchar, paramInt1, paramInt2);
this.at_line_start = isEol(paramArrayOfchar[paramInt1 + paramInt2 - 1]);
}

public void write(String paramString, int paramInt1, int paramInt2) throws IOException {
if (paramInt2 > 0) {

this.out.write(paramString, paramInt1, paramInt2);
this.at_line_start = isEol(paramString.charAt(paramInt1 + paramInt2 - 1));
} 
}

private void printIndent() throws IOException {
for (byte b = 0; b < this.indent_level; b++) {
this.out.write(9);
}
}

public void print(String paramString) throws IOException {
if (this.at_line_start)
printIndent(); 
this.out.write(paramString);
char c = paramString.charAt(paramString.length() - 1);
this.at_line_start = isEol(c);
}

public void println(String paramString) throws IOException {
if (this.at_line_start)
printIndent(); 
this.out.write(paramString);
this.out.write(EOL);
this.at_line_start = true;
}

public void print(boolean paramBoolean) throws IOException {
print(String.valueOf(paramBoolean));
}
public void print(byte paramByte) throws IOException {
print(String.valueOf(paramByte));
}
public void print(char paramChar) throws IOException {
print(String.valueOf(paramChar));
}
public void print(short paramShort) throws IOException {
print(String.valueOf(paramShort));
}
public void print(int paramInt) throws IOException {
print(String.valueOf(paramInt));
}
public void print(long paramLong) throws IOException {
print(String.valueOf(paramLong));
}
public void print(float paramFloat) throws IOException {
print(String.valueOf(paramFloat));
}
public void print(double paramDouble) throws IOException {
print(String.valueOf(paramDouble));
}
public void print(Object paramObject) throws IOException {
print(String.valueOf(paramObject));
}
public void println(boolean paramBoolean) throws IOException {
println(String.valueOf(paramBoolean));
}
public void println(byte paramByte) throws IOException {
println(String.valueOf(paramByte));
}
public void println(char paramChar) throws IOException {
println(String.valueOf(paramChar));
}
public void println(short paramShort) throws IOException {
println(String.valueOf(paramShort));
}
public void println(int paramInt) throws IOException {
println(String.valueOf(paramInt));
}
public void println(long paramLong) throws IOException {
println(String.valueOf(paramLong));
}
public void println(float paramFloat) throws IOException {
println(String.valueOf(paramFloat));
}
public void println(double paramDouble) throws IOException {
println(String.valueOf(paramDouble));
}
public void println(Object paramObject) throws IOException {
println(String.valueOf(paramObject));
}
public void println() throws IOException {
println("");
}
}

