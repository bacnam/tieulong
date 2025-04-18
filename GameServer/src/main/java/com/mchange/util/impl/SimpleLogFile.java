package com.mchange.util.impl;

import com.mchange.util.MessageLogger;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class SimpleLogFile
implements MessageLogger
{
PrintWriter logWriter;
DateFormat df = DateFormat.getDateTimeInstance(3, 3);

public SimpleLogFile(File paramFile, String paramString) throws UnsupportedEncodingException, IOException {
this.logWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile.getAbsolutePath(), true), paramString)), true);
}

public SimpleLogFile(File paramFile) throws IOException {
this.logWriter = new PrintWriter(new BufferedOutputStream(new FileOutputStream(paramFile.getAbsolutePath(), true)), true);
}

public synchronized void log(String paramString) throws IOException {
logMessage(paramString);
flush();
}

public synchronized void log(Throwable paramThrowable, String paramString) throws IOException {
logMessage(paramString);
paramThrowable.printStackTrace(this.logWriter);
flush();
}

private void logMessage(String paramString) {
this.logWriter.println(this.df.format(new Date()) + " -- " + paramString);
}
private void flush() {
this.logWriter.flush();
}
public synchronized void close() {
this.logWriter.close();
}
public void finalize() {
close();
}
}

